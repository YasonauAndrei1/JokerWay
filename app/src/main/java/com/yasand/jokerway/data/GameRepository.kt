package com.yasand.jokerway.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class GameRepository : Repository {

    override suspend fun getLevelData(): LevelModel = withContext(Dispatchers.IO){
        delay(2000)
        LevelModel(data, Pair(2, 5), PlayerState.Top)
    }

    private val data: Array<Array<CellStatus>> = arrayOf(
        arrayOf(CellStatus.Active, CellStatus.Active, CellStatus.Inactive, CellStatus.Inactive, CellStatus.Inactive),
        arrayOf(CellStatus.Active, CellStatus.Active, CellStatus.Active, CellStatus.Active, CellStatus.Active),
        arrayOf(CellStatus.Active, CellStatus.Active, CellStatus.Active, CellStatus.Active, CellStatus.Inactive),
        arrayOf(CellStatus.Inactive, CellStatus.Inactive, CellStatus.Inactive, CellStatus.Active, CellStatus.Inactive),
        arrayOf(CellStatus.Inactive, CellStatus.Inactive, CellStatus.Active, CellStatus.Active, CellStatus.Inactive),
        arrayOf(CellStatus.Inactive, CellStatus.Inactive, CellStatus.Active, CellStatus.Inactive, CellStatus.Inactive),
    )
}