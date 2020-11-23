package com.yasand.jokerway.data

import android.content.res.Resources
import android.graphics.*
import com.yasand.jokerway.R

class Meteor(private val res: Resources) :
    Player {

    private var state = PlayerState.Top
    private var icon: Bitmap

    var x: Float = 0f
    var y: Float = 0f

    private var size = 0

    private val matrix = Matrix()
    private var degree = 0f

    init {
        icon = BitmapFactory.decodeResource(res, R.drawable.ic_top)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        matrix.reset()
        matrix.postTranslate(-size / 2f, -size * 0.1f)
        matrix.postRotate(degree)
        matrix.postTranslate(x, y)
        canvas.drawBitmap(icon, matrix, paint)
    }

    override fun getPlayerCollisionRectangle() = when (state) {
        PlayerState.Top -> Rect(
            (x - size * 0.05f).toInt(),
            (y + size * 0.12f).toInt(),
            (x + size * 0.05f).toInt(),
            (y + size * 0.17f).toInt()
        )
        PlayerState.Left -> Rect(
            (x + size * 0.12f).toInt(),
            (y + size * 0.05f).toInt(),
            (x + size * 0.17f).toInt(),
            (y - size * 0.05f).toInt()
        )
        PlayerState.Right -> Rect(
            (x - size * 0.12f).toInt(),
            (y + size * 0.05f).toInt(),
            (x - size * 0.17f).toInt(),
            (y - size * 0.05f).toInt()
        )
        PlayerState.Bottom -> Rect(
            (x - size * 0.05f).toInt(),
            (y - size * 0.12f).toInt(),
            (x + size * 0.05f).toInt(),
            (y - size * 0.17f).toInt()
        )
    }

    override fun setPlayerState(state: PlayerState) {
        if (this.state == state) return
        recalculateDegree(state)
        this.state = state
    }

    private fun recalculateDegree(newState: PlayerState) {
        degree = when (newState) {
            PlayerState.Top -> 0f
            PlayerState.Left -> -90f
            PlayerState.Right -> 90f
            PlayerState.Bottom -> 180f
        }
    }

    override fun setSize(size: Int) {
        this.size = size
        scaleIcons()
    }

    override fun setPosition(position: Position) {
        x = position.x
        y = position.y
    }

    override fun setOffsetPosition(offsetX: Float, offsetY: Float) {
        x += offsetX
        y += offsetY
    }

    private fun scaleIcons() {
        icon = Bitmap.createScaledBitmap(icon, size, size, false)
    }
}