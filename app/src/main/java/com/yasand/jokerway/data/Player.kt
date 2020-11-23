package com.yasand.jokerway.data

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

enum class PlayerState {
    Top,
    Bottom,
    Left,
    Right
}

interface Player {

    fun draw(canvas: Canvas, paint: Paint)

    fun getPlayerCollisionRectangle() : Rect

    fun setPlayerState(state: PlayerState)

    fun setSize(size: Int)

    fun setPosition(position: Position)

    fun setOffsetPosition(offsetX: Float, offsetY: Float)
}