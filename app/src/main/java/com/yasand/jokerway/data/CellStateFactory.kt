package com.yasand.jokerway.data

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.yasand.jokerway.R

class CellStateFactory {
    companion object {
        private val cellStateList = arrayListOf<CellState>()

        fun getCellState(status: CellStatus, res: Resources, size: Int): CellState {
            var state = cellStateList.firstOrNull { it.status == status }
            if (state == null) {
                val bitmap = when (status) {
                    CellStatus.Active -> getBitmapById(
                        res,
                        R.drawable.ic_board_active,
                        size
                    )
                    CellStatus.Use -> getBitmapById(
                        res,
                        R.drawable.ic_board_active,
                        size
                    )
                    CellStatus.AfterUse -> getBitmapById(
                        res,
                        R.drawable.ic_board_after_use,
                        size
                    )
                    CellStatus.Passed -> getBitmapById(
                        res,
                        R.drawable.ic_board_passed,
                        size
                    )
                    CellStatus.Inactive -> null
                }
                state = CellState(bitmap, status)
                cellStateList.add(state)
            }
            return state
        }

        private fun getBitmapById(res: Resources, id: Int, size: Int) : Bitmap {
            val bitmap = BitmapFactory.decodeResource(
                res,
                id
            )
            return Bitmap.createScaledBitmap(bitmap,size, size, false)
        }
    }
}