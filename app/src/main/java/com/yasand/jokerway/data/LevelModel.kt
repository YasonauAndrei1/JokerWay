package com.yasand.jokerway.data

data class LevelModel(val matrix: Array<Array<CellStatus>>, val point: Pair<Int, Int>, val startState: PlayerState)