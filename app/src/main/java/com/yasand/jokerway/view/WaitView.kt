package com.yasand.jokerway.view

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.yasand.jokerway.R

class WaitView {

    private var dialog: Dialog? = null

    fun showView(context: Context) {
        dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.wait_view)
        }
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}