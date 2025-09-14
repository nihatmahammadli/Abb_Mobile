package com.nihatmahammadli.abbmobile.presentation.components.sheet

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FabBottomSheetBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.FabBtnAdapter
import com.nihatmahammadli.abbmobile.presentation.components.dummyData.FabBtnDummyData

class FabDialogFragment : DialogFragment() {
    private lateinit var binding: FabBottomSheetBinding

    private lateinit var adapter: FabBtnAdapter


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fab_bottom_sheet)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FabBottomSheetBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    fun setUpRecyclerView() {
        val list = FabBtnDummyData.list
        adapter = FabBtnAdapter(list) { pos, clickedItem ->
            when (pos) {
                0 -> {
                    setupQrScan()
                }

                1 -> {
                    findNavController().navigate(R.id.payments)
                }

                2 -> {
                    findNavController().navigate(R.id.transfer)
                }
            }
            dismiss()
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter = adapter
    }

    private fun setupQrScan() {
        val options = ScanOptions().apply {
            setPrompt("QR kodu oxudun")
            setBeepEnabled(true)
            setOrientationLocked(true)
            setCaptureActivity(CaptureActivity::class.java)
        }
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        val msg = result.contents ?: "Ləğv edildi"
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}
