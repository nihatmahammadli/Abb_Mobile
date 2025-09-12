package com.nihatmahammadli.abbmobile.presentation.components.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihatmahammadli.abbmobile.databinding.DialogAiBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.ChatAdapter
import com.nihatmahammadli.abbmobile.presentation.components.dummyData.FakeResponses.fakeResponses
import com.nihatmahammadli.abbmobile.presentation.model.ChatMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogAiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUp()
        close()
    }

    fun close(){
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        binding.imageButton4.setOnClickListener {
            dismiss()
        }
    }

    fun setUp(){
        chatAdapter = ChatAdapter(mutableListOf())
        binding.responseAi.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply { stackFromEnd = true }
            adapter = chatAdapter
        }

        binding.sendBtn.setOnClickListener {
            val text = binding.aiInput.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            chatAdapter.addMessage(ChatMessage(text, true))

            binding.aiInput.text?.clear()
            binding.responseAi.scrollToPosition(chatAdapter.itemCount - 1)

            lifecycleScope.launch {
                delay(1200)
                val randomReply = fakeResponses.random()
                chatAdapter.addMessage(ChatMessage(randomReply, false))
                binding.responseAi.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }

    }
}
