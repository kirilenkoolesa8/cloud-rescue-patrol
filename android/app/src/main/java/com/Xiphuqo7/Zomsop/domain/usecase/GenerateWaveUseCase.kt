package com.Xiphuqo7.Zomsop.domain.usecase

import com.Xiphuqo7.Zomsop.core.config.GameConfig
import com.Xiphuqo7.Zomsop.domain.model.Balloon
import com.Xiphuqo7.Zomsop.domain.model.BalloonColor
import com.Xiphuqo7.Zomsop.domain.model.LevelConfig
import com.Xiphuqo7.Zomsop.domain.model.StormCell
import com.Xiphuqo7.Zomsop.domain.model.Thermal
import com.Xiphuqo7.Zomsop.domain.model.Wave
import kotlin.math.abs
import kotlin.random.Random

class GenerateWaveUseCase {

    operator fun invoke(
        level: LevelConfig,
        random: Random,
        required: BalloonColor,
        palette: List<BalloonColor>,
        gliderLane: Int,
        firstId: Int,
        fallback: Wave
    ): Wave {
        var attempt = 0
        while (attempt < GameConfig.MAX_WAVE_ATTEMPTS) {
            attempt++
            val candidate = build(level, random, required, palette, firstId)
            if (solvable(candidate, required, gliderLane)) return candidate
        }
        return shift(fallback, firstId)
    }

    private fun build(
        level: LevelConfig,
        random: Random,
        required: BalloonColor,
        palette: List<BalloonColor>,
        firstId: Int
    ): Wave {
        val lanes = (0 until GameConfig.LANE_COUNT).toMutableList()
        lanes.shuffle(random)
        val stormCount = if (random.nextFloat() < 0.75f) {
            1 + random.nextInt(maxOf(1, level.stormLanes - 1))
        } else {
            0
        }
        val stormLanes = lanes.take(stormCount.coerceAtMost(GameConfig.LANE_COUNT - 2))
        val freeLanes = lanes.filter { it !in stormLanes }.toMutableList()
        var id = firstId

        val storms = stormLanes.mapIndexed { i, lane ->
            StormCell(
                id = id++,
                lane = lane,
                x = laneCenter(lane) + jitter(random, 0.03f),
                y = GameConfig.SPAWN_Y - i * 0.07f,
                spin = random.nextFloat() * 6.28f
            )
        }

        val balloonCount = (2 + random.nextInt(2)).coerceAtMost(freeLanes.size)
        val balloons = ArrayList<Balloon>(balloonCount)
        for (i in 0 until balloonCount) {
            val lane = freeLanes.removeAt(random.nextInt(freeLanes.size))
            val color = if (i == 0) required else palette[random.nextInt(palette.size)]
            balloons.add(
                Balloon(
                    id = id++,
                    color = color,
                    lane = lane,
                    x = laneCenter(lane) + jitter(random, 0.02f),
                    y = GameConfig.SPAWN_Y - 0.04f - i * 0.1f,
                    sway = random.nextFloat() * 6.28f
                )
            )
        }

        val thermals = if (freeLanes.isNotEmpty() && random.nextFloat() < 0.45f) {
            val lane = freeLanes[random.nextInt(freeLanes.size)]
            listOf(
                Thermal(
                    id = id,
                    lane = lane,
                    x = laneCenter(lane),
                    y = GameConfig.SPAWN_Y - 0.14f
                )
            )
        } else {
            emptyList()
        }

        return Wave(balloons, storms, thermals)
    }

    private fun solvable(wave: Wave, required: BalloonColor, gliderLane: Int): Boolean {
        val blocked = wave.storms.map { it.lane }.toSet()
        if (blocked.size >= GameConfig.LANE_COUNT) return false
        val target = wave.balloons.firstOrNull { it.color == required } ?: return false
        if (target.lane in blocked) return false
        return abs(target.lane - gliderLane) <= GameConfig.REACHABLE_LANES
    }

    private fun shift(fallback: Wave, firstId: Int): Wave {
        var id = firstId
        return Wave(
            balloons = fallback.balloons.map { it.copy(id = id++) },
            storms = fallback.storms.map { it.copy(id = id++) },
            thermals = fallback.thermals.map { it.copy(id = id++) }
        )
    }

    private fun laneCenter(lane: Int): Float =
        (lane + 0.5f) / GameConfig.LANE_COUNT

    private fun jitter(random: Random, span: Float): Float =
        (random.nextFloat() - 0.5f) * 2f * span
}
