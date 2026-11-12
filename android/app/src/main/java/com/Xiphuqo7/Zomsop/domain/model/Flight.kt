package com.Xiphuqo7.Zomsop.domain.model

enum class BalloonColor { CYAN, AMBER, VIOLET }

enum class MarkerShape { CIRCLE, TRIANGLE, SQUARE }

val BalloonColor.marker: MarkerShape
    get() = when (this) {
        BalloonColor.CYAN -> MarkerShape.CIRCLE
        BalloonColor.AMBER -> MarkerShape.TRIANGLE
        BalloonColor.VIOLET -> MarkerShape.SQUARE
    }

data class Balloon(
    val id: Int,
    val color: BalloonColor,
    val lane: Int,
    val x: Float,
    val y: Float,
    val sway: Float
)

data class StormCell(
    val id: Int,
    val lane: Int,
    val x: Float,
    val y: Float,
    val spin: Float,
    val calm: Boolean = false,
    val telegraph: Boolean = true
)

data class Thermal(
    val id: Int,
    val lane: Int,
    val x: Float,
    val y: Float
)

data class Wave(
    val balloons: List<Balloon>,
    val storms: List<StormCell>,
    val thermals: List<Thermal>
)

data class MissionCard(
    val sequence: List<BalloonColor>,
    val progress: Int = 0
) {
    val next: BalloonColor?
        get() = sequence.getOrNull(progress)

    val completed: Boolean
        get() = progress >= sequence.size
}
