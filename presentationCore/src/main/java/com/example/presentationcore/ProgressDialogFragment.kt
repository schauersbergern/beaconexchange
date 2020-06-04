package com.example.presentationcore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment

class ProgressDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val loadingHint = view.findViewById<View>(R.id.loading_hint)
        progressBar.setOnLongClickListener {
            loadingHint.isVisible = true
            it.isInvisible = true
            true
        }
        loadingHint.setOnClickListener {
            progressBar.isVisible = true
            it.isInvisible = true
        }

        var cancelTries = 0
        dialog?.setOnKeyListener { _, _, _ ->
            // if true it will cancel on the current try
            isCancelable = cancelTries++ > CANCELABLE_THRESHOLD
            false
        }
    }

    companion object {
        const val CANCELABLE_THRESHOLD = 2
    }
}
