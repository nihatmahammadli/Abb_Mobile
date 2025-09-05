package com.nihatmahammadli.abbmobile.presentation.components.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihatmahammadli.abbmobile.databinding.DialogAiBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.ChatAdapter
import com.nihatmahammadli.abbmobile.presentation.model.ChatMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiChatDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAiBinding
    private lateinit var chatAdapter: ChatAdapter


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogAiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chatAdapter = ChatAdapter(mutableListOf())
        binding.responseAi.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply { stackFromEnd = true }
            adapter = chatAdapter
        }

        binding.sendBtn.setOnClickListener {
            val text = binding.aiInput.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            chatAdapter.addMessage(ChatMessage(text, true))
            chatAdapter.addMessage(ChatMessage("Salam, ABB Support sizi dinləyir!", false))

            binding.aiInput.text?.clear()
            binding.responseAi.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }
}
