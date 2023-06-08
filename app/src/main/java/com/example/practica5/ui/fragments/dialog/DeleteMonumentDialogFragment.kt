package com.example.practica5.ui.fragments.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.practica5.R
import com.example.practica5.databinding.CustomDialogDeleteMonumentsBinding
import com.example.practica5.domain.model.vo.MonumentVO

class DeleteMonumentDialogFragment(private val monument: MonumentVO) : DialogFragment() {
    private val binding by lazy { CustomDialogDeleteMonumentsBinding.inflate(layoutInflater) }

    interface DeleteMonumentDialogListener {
        fun onDialogAccept(monument: MonumentVO)
    }

    var listener: DeleteMonumentDialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogView()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupDialogView() = with(binding) {
        deleteDialogBtnAccept.setOnClickListener {
            listener?.onDialogAccept(monument)
            dismiss()
        }

        dialogBtnClose.setOnClickListener {
            dismiss()
        }

        deleteDialogLabelMssg.text = getString(R.string.monuments__delete_dialog_message, monument.name)
    }
}