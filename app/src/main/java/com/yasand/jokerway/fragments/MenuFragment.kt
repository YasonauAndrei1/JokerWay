package com.yasand.jokerway.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yasand.jokerway.R
import com.yasand.jokerway.Result
import com.yasand.jokerway.view.DialogView
import com.yasand.jokerway.view.WaitView
import kotlinx.android.synthetic.main.fragment_menu.*

interface StartHandler {
    fun onPlayClick()
}

class MenuFragment : Fragment() {

    companion object {

        const val TAG = "MenuFragment"

        fun newInstance() = MenuFragment()
    }

    private var startHandler: StartHandler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playButton.setOnClickListener {
            startHandler?.onPlayClick()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is StartHandler)
            startHandler = context
    }

    override fun onDetach() {
        super.onDetach()
        startHandler = null
    }
}