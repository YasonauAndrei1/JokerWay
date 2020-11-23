package com.yasand.jokerway.view

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.yasand.jokerway.R
import com.yasand.jokerway.Result
import kotlinx.android.synthetic.main.dialog.*

class DialogView {

    var dialog: Dialog? = null

    fun showDialog(
        context: Context,
        result: Result,
        restartBlock: () -> Unit,
        shareBlock: () -> Unit
    ) {
        dialog = Dialog(context)
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
            setContentView(R.layout.dialog)
            if (result == Result.WON) {
                dialogImage.setImageResource(R.drawable.ic_win)
            } else {
                dialogImage.setImageResource(R.drawable.ic_lose)
            }
        }
        dialog?.restart?.setOnClickListener {
            dialog?.dismiss()
            restartBlock()
        }
        dialog?.share?.setOnClickListener {
            dialog?.dismiss()
            shareBlock()
        }
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}