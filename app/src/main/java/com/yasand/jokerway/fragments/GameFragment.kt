package com.yasand.jokerway.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.yasand.jokerway.GameViewModel
import com.yasand.jokerway.R
import com.yasand.jokerway.Result
import com.yasand.jokerway.data.PlayerState
import com.yasand.jokerway.ext.gone
import com.yasand.jokerway.ext.nonNull
import com.yasand.jokerway.ext.visible
import com.yasand.jokerway.view.DialogView
import com.yasand.jokerway.view.WaitView
import kotlinx.android.synthetic.main.fragment_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

private const val ARG_PARAM_WIDTH = "param1"
private const val ARG_PARAM_HEIGHT = "param2"

class GameFragment : Fragment() {

    companion object {
        const val TAG = "GameFragment"

        fun newInstance(width: Int, height: Int) = GameFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM_WIDTH, width)
                    putInt(ARG_PARAM_HEIGHT, height)
                }
            }
    }

    private var width: Int? = null
    private var height: Int? = null

    private val gameViewModel by viewModel<GameViewModel>()

    private var xPos = 0f
    private var yPos = 0f

    private val waitView = WaitView()
    private val dialogView = DialogView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            width = it.getInt(ARG_PARAM_WIDTH)
            height = it.getInt(ARG_PARAM_HEIGHT)
        }
        gameView?.post {
            if (width != null) gameView.setScreenSize(width ?: 0, height ?: 0)
        }
        startGame()
    }

    private fun startGame() {
        waitView.showView(requireContext())
        gameViewModel.getLevelData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setTouchListener()
    }

    private fun observeData() {
        gameViewModel.boardData.nonNull().observe(viewLifecycleOwner, Observer { data ->
            if (width == null || height == null) {
                showErrorToast()
            } else {
                gameViewModel.initializeGame(data, width ?: 0, height ?: 0)
                waitView.dismiss()
                gameViewModel.startGame()
            }
        })
        gameViewModel.gameData.nonNull().observe(viewLifecycleOwner, Observer {
            gameView?.drawView(it)
        })
        gameViewModel.gameStatus.nonNull().observe(viewLifecycleOwner, Observer {
            showDialog(it)
            gameViewModel.stopGame()
        })
    }

    private fun showErrorToast() {
        Toast.makeText(context, "Something went wrong. Please reload", Toast.LENGTH_LONG)
            .show()
    }

    override fun onResume() {
        super.onResume()
        gameView.visible()
        gameViewModel.startGame()
    }

    override fun onPause() {
        super.onPause()
        gameView.gone()
        gameViewModel.stopGame()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener() {
        gameView?.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    xPos = event.x
                    yPos = event.y
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val deltaX = xPos - event.x
                    val deltaY = yPos - event.y
                    if (abs(deltaX) > abs(deltaY)) {
                        if (deltaX > 0) {
                            gameViewModel.setPlayerState(PlayerState.Left)
                        } else {
                            gameViewModel.setPlayerState(PlayerState.Right)
                        }
                    } else {
                        if (deltaY > 0) {
                            gameViewModel.setPlayerState(PlayerState.Top)
                        } else {
                            gameViewModel.setPlayerState(PlayerState.Bottom)
                        }
                    }
                }
                else -> {
                }
            }
            true
        }
    }

    private fun showDialog(result: Result) {
        dialogView.dismiss()
        dialogView.showDialog(requireContext(), result, {
            gameViewModel.startNewGame()
        }, {
            Toast.makeText(context, "Your result was shared", Toast.LENGTH_LONG)
                .show()
            gameViewModel.startNewGame()
        })
    }
}