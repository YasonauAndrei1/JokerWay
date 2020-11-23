package com.yasand.jokerway.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.yasand.jokerway.data.CellStatus

class CellState(private val bitmap: Bitmap?, val status: CellStatus) {

    fun draw(canvas: Canvas, x: Int, y: Int, paint: Paint) {
        bitmap?.let {
            canvas.drawBitmap(bitmap, x.toFloat(), y.toFloat(), paint)
        }
    }
}