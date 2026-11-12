package com.Xiphuqo7.Zomsop.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xiphuqo7.Zomsop.core.config.GameConfig
import com.Xiphuqo7.Zomsop.data.sample.SampleData
import com.Xiphuqo7.Zomsop.domain.model.Balloon
import com.Xiphuqo7.Zomsop.domain.model.BalloonColor
import com.Xiphuqo7.Zomsop.domain.model.LevelConfig
import com.Xiphuqo7.Zomsop.domain.model.MissionCard
import com.Xiphuqo7.Zomsop.domain.model.PatrolSettings
import com.Xiphuqo7.Zomsop.domain.model.RunOutcome
import com.Xiphuqo7.Zomsop.domain.model.RunResult
import com.Xiphuqo7.Zomsop.domain.model.RunTally
import com.Xiphuqo7.Zomsop.domain.model.StormCell
import com.Xiphuqo7.Zomsop.domain.repository.PatrolRepository
import com.Xiphuqo7.Zomsop.domain.repository.SettingsRepository
import com.Xiphuqo7.Zomsop.domain.usecase.GenerateWaveUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ResolvePickupUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ResolveUnlocksUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ScoreRunUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

class GameViewModel(
    private val patrol: PatrolRepository,
    private val settingsRepository: SettingsRepository,
    private val generateWave: GenerateWaveUseCase,
    private val resolvePickup: ResolvePickupUseCase,
    private val scoreRun: ScoreRunUseCase,
    private val resolveUnlocks: ResolveUnlocksUseCase
) : ViewModel() {

    private val palette = listOf(BalloonColor.CYAN, BalloonColor.AMBER, BalloonColor.VIOLET)

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val resultChannel = Channel<RunResult>(Channel.BUFFERED)
    val results: Flow<RunResult> = resultChannel.receiveAsFlow()

    private var loop: Job? = null
    private var level: LevelConfig = SampleData.LEVELS.first()
    private var random: Random = Random(1)
    private var attempt = 0
    private var nextId = 0
    private var elapsedMs = 0L
    private var lastWaveMs = 0L
    private var windPhaseMs = 0L
    private var bannerUntil = 0L
    private var hintUntil = 0L
    private var scrambleUntil = 0L
    private var flareUntil = 0L
    private var crashMs = 0L
    private var touching = false
    private var targetX = 0.5f
    private var settings = PatrolSettings()
    private var cardsCompleted = 0
    private var longCards = 0
    private var bestStreak = 0
    private var flaresFired = 0
    private var liftFullCount = 0
    private var liftWasFull = false
    private var bestMultiplier = 100
    private var correctPicks = 0
    private var totalPicks = 0

    fun prepare(levelIndex: Int) {
        loop?.cancel()
        loop = null
        settings = settingsRepository.settings()
        level = SampleData.LEVELS.getOrElse(levelIndex) { SampleData.LEVELS.first() }
        attempt += 1
        random = Random(
            level.index * GameConfig.SEED_LEVEL_PRIME xor attempt * GameConfig.SEED_ATTEMPT_PRIME
        )
        nextId = 0
        elapsedMs = 0L
        lastWaveMs = -GameConfig.WAVE_INTERVAL_MS
        windPhaseMs = 0L
        bannerUntil = 0L
        hintUntil = GameConfig.HINT_VISIBLE_MS
        scrambleUntil = 0L
        flareUntil = 0L
        crashMs = 0L
        touching = false
        targetX = 0.5f
        cardsCompleted = 0
        longCards = 0
        bestStreak = 0
        flaresFired = 0
        liftFullCount = 0
        liftWasFull = false
        bestMultiplier = 100
        correctPicks = 0
        totalPicks = 0

        _state.value = GameUiState(
            levelIndex = level.index,
            levelNameRes = level.nameRes,
            phase = GamePhase.Ready,
            card = newCard(),
            targetRescues = level.targetRescues,
            secondsLeft = level.durationSeconds,
            totalSeconds = level.durationSeconds,
            windLanes = newWind(),
            showHint = true,
            showMarkers = settings.colorBlindMarkers,
            flareOnRight = settings.flareOnRight
        )
    }

    fun start() {
        if (_state.value.phase != GamePhase.Ready) return
        _state.value = _state.value.copy(phase = GamePhase.Playing, paused = false)
        hintUntil = elapsedMs + GameConfig.HINT_VISIBLE_MS
        runLoop()
    }

    fun pause() {
        if (_state.value.phase != GamePhase.Playing || _state.value.paused) return
        loop?.cancel()
        loop = null
        _state.value = _state.value.copy(paused = true)
    }

    fun resume() {
        if (!_state.value.paused) return
        hintUntil = elapsedMs + GameConfig.HINT_RESUME_MS
        _state.value = _state.value.copy(paused = false, showHint = true)
        runLoop()
    }

    fun restart() {
        prepare(level.index)
    }

    fun steer(fraction: Float) {
        touching = true
        targetX = fraction.coerceIn(0.04f, 0.96f)
    }

    fun releaseSteer() {
        touching = false
    }

    fun fireFlare() {
        val current = _state.value
        if (current.phase != GamePhase.Playing || current.paused) return
        if (current.lift < GameConfig.FLARE_COST) return
        flaresFired += 1
        flareUntil = elapsedMs + GameConfig.FLARE_CALM_MS
        _state.value = current.copy(
            lift = current.lift - GameConfig.FLARE_COST,
            flareActive = true,
            flareReady = false
        )
    }

    private fun runLoop() {
        loop?.cancel()
        loop = viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.FRAME_MS)
                tick()
            }
        }
    }

    private fun tick() {
        val current = _state.value
        elapsedMs += GameConfig.FRAME_MS
        val dt = GameConfig.FRAME_MS / 1000f

        if (current.phase == GamePhase.Crashing) {
            crashMs += GameConfig.FRAME_MS
            val progress = (crashMs.toFloat() / GameConfig.CRASH_DURATION_MS).coerceIn(0f, 1f)
            _state.value = current.copy(crashProgress = progress)
            if (crashMs >= GameConfig.CRASH_DURATION_MS) finish(RunOutcome.GROUNDED)
            return
        }
        if (current.phase != GamePhase.Playing) return

        val scrollStep = level.scrollDpPerS / GameConfig.CORRIDOR_REF_HEIGHT_DP * dt
        val lane = laneOf(current.gliderX)
        val wind = current.windLanes.getOrElse(lane) { 0f }
        val windStep = wind * GameConfig.WIND_MAX_DP_PER_S /
            GameConfig.CORRIDOR_REF_WIDTH_DP * level.windScale * dt

        var x = current.gliderX
        if (touching) {
            val gain = GameConfig.STEER_GAIN_DP_PER_S / GameConfig.CORRIDOR_REF_WIDTH_DP *
                settings.sensitivity.factor * dt
            x += (targetX - x).coerceIn(-gain, gain)
            x += windStep
        } else {
            x += windStep * (1f - GameConfig.AUTO_TRIM_STRENGTH)
            val safe = safeLaneCenter(current.storms, x)
            x += (safe - x) * GameConfig.AUTO_TRIM_EASE * dt
        }
        x = x.coerceIn(0.04f, 0.96f)
        val bankTarget = ((x - current.gliderX) * 900f)
            .coerceIn(-GameConfig.MAX_BANK_DEGREES, GameConfig.MAX_BANK_DEGREES)
        val bank = current.bank + (bankTarget - current.bank) * GameConfig.BANK_RESPONSE * dt

        val calm = elapsedMs < flareUntil
        val movedBalloons = current.balloons.mapNotNull { balloon ->
            val moved = balloon.copy(y = balloon.y + scrollStep)
            if (moved.y > GameConfig.DESPAWN_Y) null else moved
        }
        val movedStorms = current.storms.mapNotNull { storm ->
            val quiet = calm && abs(storm.x - x) < GameConfig.FLARE_RADIUS
            val moved = storm.copy(
                y = storm.y + scrollStep,
                spin = storm.spin + dt * 0.08f,
                calm = quiet,
                telegraph = storm.y < 0f
            )
            if (moved.y > GameConfig.DESPAWN_Y) null else moved
        }
        val movedThermals = current.thermals.mapNotNull { thermal ->
            val moved = thermal.copy(y = thermal.y + scrollStep)
            if (moved.y > GameConfig.DESPAWN_Y) null else moved
        }

        val gliderY = GameConfig.GLIDER_Y
        val hitStorm = movedStorms.firstOrNull { storm ->
            !storm.calm &&
                abs(storm.x - x) < GameConfig.STORM_RADIUS_X &&
                abs(storm.y - gliderY) < GameConfig.STORM_RADIUS_Y
        }
        if (hitStorm != null) {
            crashMs = 0L
            _state.value = current.copy(
                phase = GamePhase.Crashing,
                balloons = movedBalloons,
                storms = movedStorms,
                thermals = movedThermals,
                gliderX = x,
                bank = bank,
                crashProgress = 0f
            )
            return
        }

        var card = current.card
        var score = current.score
        var multiplier = current.multiplier
        var streak = current.streak
        var rescues = current.rescues
        var scrambled = elapsedMs < scrambleUntil
        val remaining = ArrayList<Balloon>(movedBalloons.size)
        for (balloon in movedBalloons) {
            val caught = abs(balloon.x - x) < GameConfig.PICKUP_RADIUS_X &&
                abs(balloon.y - gliderY) < GameConfig.PICKUP_RADIUS_Y
            if (!caught) {
                remaining.add(balloon)
                continue
            }
            totalPicks += 1
            val outcome = resolvePickup(card, balloon.color, multiplier, streak)
            card = outcome.card
            score = (score + outcome.scoreDelta).coerceAtLeast(0)
            multiplier = outcome.multiplier
            streak = outcome.streak
            if (outcome.correct) {
                correctPicks += 1
                bestStreak = maxOf(bestStreak, streak)
                bestMultiplier = maxOf(bestMultiplier, (multiplier * 100).toInt())
            } else {
                scrambled = true
                scrambleUntil = elapsedMs + GameConfig.SCRAMBLE_FLASH_MS
            }
            if (outcome.cardCompleted) {
                rescues += 1
                cardsCompleted += 1
                if (card.sequence.size >= 3) longCards += 1
                card = newCard()
            }
        }

        val inThermal = movedThermals.any { thermal ->
            abs(thermal.x - x) < GameConfig.THERMAL_HALF_WIDTH &&
                abs(thermal.y - gliderY) < GameConfig.THERMAL_HALF_HEIGHT
        }
        var lift = current.lift
        if (inThermal) {
            lift = (lift + GameConfig.LIFT_CHARGE_PER_S * dt).coerceAtMost(GameConfig.LIFT_MAX)
        }
        if (lift >= GameConfig.LIFT_MAX) {
            if (!liftWasFull) {
                liftFullCount += 1
                liftWasFull = true
            }
        } else {
            liftWasFull = false
        }

        var windLanes = current.windLanes
        var banner = elapsedMs < bannerUntil
        windPhaseMs += GameConfig.FRAME_MS
        if (windPhaseMs >= GameConfig.WIND_PHASE_MS) {
            windPhaseMs = 0L
            windLanes = newWind()
            bannerUntil = elapsedMs + GameConfig.WIND_BANNER_MS
            banner = true
        }

        var balloons = remaining.toList()
        var storms = movedStorms
        var thermals = movedThermals
        if (elapsedMs - lastWaveMs >= GameConfig.WAVE_INTERVAL_MS) {
            lastWaveMs = elapsedMs
            val required = card.next ?: palette[random.nextInt(palette.size)]
            val wave = generateWave(
                level = level,
                random = random,
                required = required,
                palette = palette,
                gliderLane = laneOf(x),
                firstId = nextId,
                fallback = SampleData.FALLBACK_WAVE
            )
            nextId += wave.balloons.size + wave.storms.size + wave.thermals.size + 1
            balloons = balloons + wave.balloons
            storms = storms + wave.storms
            thermals = thermals + wave.thermals
        }

        val secondsLeft = (level.durationSeconds - (elapsedMs / 1000L).toInt())
            .coerceAtLeast(0)
        val showHint = elapsedMs < hintUntil

        _state.value = current.copy(
            card = card,
            scrambled = scrambled,
            balloons = balloons,
            storms = storms,
            thermals = thermals,
            gliderX = x,
            bank = bank,
            lift = lift,
            flareActive = calm,
            inThermal = inThermal,
            score = score,
            multiplier = multiplier,
            streak = streak,
            rescues = rescues,
            secondsLeft = secondsLeft,
            windLanes = windLanes,
            windBanner = banner,
            showHint = showHint,
            scroll = current.scroll + scrollStep,
            flareReady = lift >= GameConfig.FLARE_COST
        )

        if (rescues >= level.targetRescues) {
            finish(RunOutcome.CLEARED)
        } else if (secondsLeft <= 0) {
            finish(RunOutcome.TIMEOUT)
        }
    }

    private fun finish(outcome: RunOutcome) {
        val running = loop
        loop = null
        running?.cancel()
        val current = _state.value
        val scored = scoreRun(level, outcome, current.score, current.secondsLeft, current.lift)
        val tally = RunTally(
            rescues = current.rescues,
            cardsCompleted = cardsCompleted,
            longCardsCompleted = longCards,
            bestStreak = bestStreak,
            flaresFired = flaresFired,
            liftFullCount = liftFullCount,
            bestMultiplierX100 = bestMultiplier,
            correctPicks = correctPicks,
            totalPicks = totalPicks
        )
        val before = patrol.progress()
        val unlocked = resolveUnlocks(before, level.index, outcome, tally)
        val newBest = patrol.recordRun(
            levelIndex = level.index,
            outcome = outcome,
            score = scored.score,
            stars = scored.stars,
            tally = tally,
            unlocked = unlocked
        )
        _state.value = current.copy(phase = GamePhase.Finished)
        resultChannel.trySend(
            RunResult(
                levelIndex = level.index,
                outcome = outcome,
                score = scored.score,
                rescues = current.rescues,
                bestStreak = bestStreak,
                secondsLeft = current.secondsLeft,
                stars = scored.stars,
                newBest = newBest,
                unlockedNameRes = SampleData.entryName(unlocked.firstOrNull().orEmpty())
            )
        )
    }

    private fun newCard(): MissionCard = MissionCard(
        sequence = List(level.sequenceLength) { palette[random.nextInt(palette.size)] }
    )

    private fun newWind(): List<Float> =
        List(GameConfig.LANE_COUNT) { random.nextFloat() * 2f - 1f }

    private fun laneOf(x: Float): Int =
        (x * GameConfig.LANE_COUNT).toInt().coerceIn(0, GameConfig.LANE_COUNT - 1)

    private fun safeLaneCenter(storms: List<StormCell>, x: Float): Float {
        val blocked = storms
            .filter { it.y > -0.4f && it.y < GameConfig.GLIDER_Y + 0.1f && !it.calm }
            .map { laneOf(it.x) }
            .toSet()
        val current = laneOf(x)
        if (current !in blocked) return x
        val free = (0 until GameConfig.LANE_COUNT).filter { it !in blocked }
        val target = free.minByOrNull { abs(it - current) } ?: current
        return (target + 0.5f) / GameConfig.LANE_COUNT
    }

    override fun onCleared() {
        loop?.cancel()
        loop = null
        super.onCleared()
    }
}

