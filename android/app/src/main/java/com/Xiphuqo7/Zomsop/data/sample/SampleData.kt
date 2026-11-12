package com.Xiphuqo7.Zomsop.data.sample

import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.config.GameConfig
import com.Xiphuqo7.Zomsop.domain.model.Balloon
import com.Xiphuqo7.Zomsop.domain.model.BalloonColor
import com.Xiphuqo7.Zomsop.domain.model.CollectionEntry
import com.Xiphuqo7.Zomsop.domain.model.LevelConfig
import com.Xiphuqo7.Zomsop.domain.model.StormCell
import com.Xiphuqo7.Zomsop.domain.model.Thermal
import com.Xiphuqo7.Zomsop.domain.model.Wave
import com.Xiphuqo7.Zomsop.domain.usecase.CollectionIds

object SampleData {

    val LEVELS: List<LevelConfig> = listOf(
        LevelConfig(0, R.string.level_1, 0.4f, 2, 2, 4, 60, 1200, 180f),
        LevelConfig(1, R.string.level_2, 0.6f, 2, 3, 5, 60, 1800, 205f),
        LevelConfig(2, R.string.level_3, 0.7f, 3, 3, 6, 65, 2400, 230f),
        LevelConfig(3, R.string.level_4, 0.9f, 3, 3, 7, 65, 3000, 255f),
        LevelConfig(4, R.string.level_5, 1.0f, 4, 4, 8, 70, 3600, 280f),
        LevelConfig(5, R.string.level_6, 1.2f, 4, 4, 9, 75, 4200, 300f)
    )

    val COLLECTION: List<CollectionEntry> = listOf(
        CollectionEntry(
            CollectionIds.CYAN,
            R.string.card_cyan_name,
            R.string.card_cyan_log,
            R.string.card_cyan_cond,
            R.drawable.sprite_2
        ),
        CollectionEntry(
            CollectionIds.AMBER,
            R.string.card_amber_name,
            R.string.card_amber_log,
            R.string.card_amber_cond,
            R.drawable.sprite_3
        ),
        CollectionEntry(
            CollectionIds.VIOLET,
            R.string.card_violet_name,
            R.string.card_violet_log,
            R.string.card_violet_cond,
            R.drawable.sprite_4
        ),
        CollectionEntry(
            CollectionIds.MK1,
            R.string.card_mk1_name,
            R.string.card_mk1_log,
            R.string.card_mk1_cond,
            R.drawable.sprite_1
        ),
        CollectionEntry(
            CollectionIds.MK2,
            R.string.card_mk2_name,
            R.string.card_mk2_log,
            R.string.card_mk2_cond,
            R.drawable.sprite_1
        ),
        CollectionEntry(
            CollectionIds.MK3,
            R.string.card_mk3_name,
            R.string.card_mk3_log,
            R.string.card_mk3_cond,
            R.drawable.logo
        ),
        CollectionEntry(
            CollectionIds.THERMAL,
            R.string.card_thermal_name,
            R.string.card_thermal_log,
            R.string.card_thermal_cond,
            R.drawable.icon_home
        ),
        CollectionEntry(
            CollectionIds.ANVIL,
            R.string.card_anvil_name,
            R.string.card_anvil_log,
            R.string.card_anvil_cond,
            R.drawable.sprite_5
        ),
        CollectionEntry(
            CollectionIds.CIRRUS,
            R.string.card_cirrus_name,
            R.string.card_cirrus_log,
            R.string.card_cirrus_cond,
            R.drawable.icon_pause
        )
    )

    val FALLBACK_WAVE: Wave = Wave(
        balloons = listOf(
            Balloon(0, BalloonColor.CYAN, 1, laneCenter(1), GameConfig.SPAWN_Y, 0f),
            Balloon(1, BalloonColor.AMBER, 2, laneCenter(2), GameConfig.SPAWN_Y - 0.09f, 1.6f),
            Balloon(2, BalloonColor.VIOLET, 3, laneCenter(3), GameConfig.SPAWN_Y - 0.18f, 3.1f)
        ),
        storms = listOf(
            StormCell(3, 0, laneCenter(0), GameConfig.SPAWN_Y - 0.05f, 0f)
        ),
        thermals = listOf(
            Thermal(4, 4, laneCenter(4), GameConfig.SPAWN_Y - 0.12f)
        )
    )

    fun entryName(id: String): Int =
        COLLECTION.firstOrNull { it.id == id }?.nameRes ?: 0

    private fun laneCenter(lane: Int): Float = (lane + 0.5f) / GameConfig.LANE_COUNT
}
