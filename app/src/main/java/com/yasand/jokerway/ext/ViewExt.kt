package com.yasand.jokerway.ext

import android.view.View

@Suppress("NOTHING_TO_INLINE")
fun View.visible() {
    visibility = View.VISIBLE
}

@Suppress("NOTHING_TO_INLINE")
fun View.gone() {
    visibility = View.GONE
}