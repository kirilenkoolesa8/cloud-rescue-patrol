package com.Xiphuqo7.Zomsop.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val FRAME_MS = 16L

    const val CORRIDOR_REF_HEIGHT_DP = 620f
    const val CORRIDOR_REF_WIDTH_DP = 360f
    const val LANE_COUNT = 5
    const val GLIDER_Y = 0.78f
    const val SPAWN_Y = -0.18f
    const val DESPAWN_Y = 1.2f

    const val STEER_GAIN_DP_PER_S = 420f
    const val WIND_MAX_DP_PER_S = 160f
    const val AUTO_TRIM_STRENGTH = 0.85f
    const val AUTO_TRIM_EASE = 2.2f
    const val MAX_BANK_DEGREES = 18f
    const val BANK_RESPONSE = 7f

    const val LIFT_MAX = 100f
    const val LIFT_CHARGE_PER_S = 26f
    const val FLARE_COST = 40f
    const val FLARE_CALM_MS = 3000L
    const val FLARE_RADIUS = 0.34f
    const val THERMAL_SPEED_BONUS = 0.12f

    const val WIND_PHASE_MS = 12000L
    const val WIND_BANNER_MS = 1500L
    const val HINT_VISIBLE_MS = 6000L
    const val HINT_RESUME_MS = 3000L
    const val SCRAMBLE_FLASH_MS = 1200L
    const val CRASH_DURATION_MS = 1500L
    const val WAVE_INTERVAL_MS = 1500L
    const val MAX_WAVE_ATTEMPTS = 20
    const val REACHABLE_LANES = 3

    const val PICKUP_RADIUS_X = 0.09f
    const val PICKUP_RADIUS_Y = 0.055f
    const val STORM_RADIUS_X = 0.105f
    const val STORM_RADIUS_Y = 0.075f
    const val THERMAL_HALF_WIDTH = 0.09f
    const val THERMAL_HALF_HEIGHT = 0.12f

    const val BALLOON_VALUE = 120
    const val WRONG_PICK_PENALTY = 40
    const val TIME_BONUS_PER_SECOND = 15
    const val LIFT_BONUS_PER_UNIT = 2
    const val MULTIPLIER_STEP = 0.25f
    const val MULTIPLIER_MAX = 3f
    const val TOTAL_STARS = 18
    const val SEED_LEVEL_PRIME = 7919
    const val SEED_ATTEMPT_PRIME = 104729
}
