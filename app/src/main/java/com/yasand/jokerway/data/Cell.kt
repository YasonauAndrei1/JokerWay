package com.yasand.jokerway.data

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

enum class CellStatus {
    Inactive,
    Active,
    Use,
    AfterUse,
    Passed
}

interface Cell {

    fun draw(canvas: Canvas, paint: Paint)

    fun getCellCollisionRectangle(): Rect

    fun setCellStatus(status: CellStatus)

    fun getCellStatus(): CellStatus

    fun changeStatus()
}