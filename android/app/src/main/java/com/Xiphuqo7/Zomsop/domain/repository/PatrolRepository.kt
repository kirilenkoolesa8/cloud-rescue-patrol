package com.Xiphuqo7.Zomsop.domain.repository

import com.Xiphuqo7.Zomsop.domain.model.PatrolProgress
import com.Xiphuqo7.Zomsop.domain.model.RunOutcome
import com.Xiphuqo7.Zomsop.domain.model.RunTally

interface PatrolRepository {
    fun progress(): PatrolProgress

    fun recordRun(
        levelIndex: Int,
        outcome: RunOutcome,
        score: Int,
        stars: Int,
        tally: RunTally,
        unlocked: Set<String>
    ): Boolean

    fun reset()
}
