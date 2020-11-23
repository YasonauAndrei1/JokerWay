package com.yasand.jokerway.data

interface Repository {

    suspend fun getLevelData() : LevelModel
}