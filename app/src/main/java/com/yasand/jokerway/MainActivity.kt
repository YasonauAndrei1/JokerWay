package com.yasand.jokerway

import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yasand.jokerway.ext.addFragment
import com.yasand.jokerway.ext.replaceFragment
import com.yasand.jokerway.ext.visible
import com.yasand.jokerway.fragments.GameFragment
import com.yasand.jokerway.fragments.MenuFragment
import com.yasand.jokerway.fragments.StartHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity(),
    StartHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFullscreen()
        startGameFragment()
    }

    @Suppress("DEPRECATION")
    private fun setFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun startGameFragment() {
        supportFragmentManager.addFragment(
            R.id.containerView,
            MenuFragment.newInstance(),
            MenuFragment.TAG,
            true
        )
    }

    override fun onPlayClick() {
        supportFragmentManager.replaceFragment(
            R.id.containerView,
            GameFragment.newInstance(getScreenWidth().first, getScreenWidth().second),
            GameFragment.TAG,
            true
        )
    }

    private fun getScreenWidth(): Pair<Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            Pair(
                windowMetrics.bounds.width() - insets.left - insets.right,
                windowMetrics.bounds.height() - insets.top - insets.bottom
            )
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        }
        super.onBackPressed()
    }
}