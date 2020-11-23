package com.yasand.jokerway.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import com.yasand.jokerway.R
import com.yasand.jokerway.data.Cell
import com.yasand.jokerway.data.Player
import com.yasand.jokerway.data.PlayerState

class GameView(
    context: Context,
    attr: AttributeSet
) : SurfaceView(context, attr) {

    interface DrawCallback {
        fun draw(canvas: Canvas, paint: Paint)
    }

    private val background: Bitmap = BitmapFactory.decodeResource(
        resources,
        R.drawable.background_game
    )
    private var icon: Bitmap
    private val paint = Paint()

    private val cellList = arrayListOf<Cell>()

    private var drawCallback: DrawCallback? = null

    private var widthView = 0
    private var heightView = 0

    private var widthIcon = 0
    private var heightIcon = 0

    init {
        paint.color = Color.WHITE
        icon = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_joker_game
        )
    }

    fun setScreenSize(width: Int, height: Int) {
        widthView = width
        heightView = height
        calculateIconSize()
        icon = Bitmap.createScaledBitmap(icon, widthIcon, heightIcon, false)
    }

    private fun calculateIconSize() {
        val ratio = icon.width / icon.height
        widthIcon = widthView
        heightIcon = widthIcon * ratio
    }

    fun drawView(pair: Pair<List<Cell>, Player>) {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(background, 0f, 0f, paint)
            canvas.drawBitmap(icon, 0f, 0f, paint)
            pair.first.forEach {
                it.draw(canvas, paint)
            }
            pair.second.draw(canvas, paint)
            drawCallback?.draw(canvas, paint)
            cellList.forEach { it.draw(canvas, paint) }
            holder.unlockCanvasAndPost(canvas)
        }
    }
}