package com.yasand.jokerway.data

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class BoardCell(
    private val res: Resources,
    private val size: Int,
    val x: Int,
    val y: Int
) : Cell {

    private var status = CellStatus.Active
    private var state: CellState

    init {
        state = CellStateFactory.getCellState(
            status,
            res,
            size
        )
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        state.draw(canvas, x, y, paint)
    }

    override fun getCellCollisionRectangle() = Rect(x, y, x + size, y + size)

    override fun setCellStatus(status: CellStatus) {
        this.status = status
        updateState()
    }

    private fun updateState() {
        state = CellStateFactory.getCellState(
            status,
            res,
            size
        )
    }

    override fun getCellStatus() = status

    override fun changeStatus() {
        status = when (status) {
            CellStatus.Active, CellStatus.Inactive, CellStatus.Passed -> status
            CellStatus.AfterUse -> CellStatus.Passed
            CellStatus.Use -> CellStatus.AfterUse
        }
        updateState()
    }
}