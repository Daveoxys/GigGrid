package com.example.GigGrid.ui.myEvents

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.GigGrid.R
import java.util.zip.Inflater
import com.example.GigGrid.databinding.DialogImageDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ImageDetailsDialogFragment (private val onDetailsAdded: (bandEvent: String, location: String, date: String)
-> Unit) : DialogFragment() {
    private var _binding: DialogImageDetailsBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogImageDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateDateText()

        binding.datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.addButton.setOnClickListener {
            val bandEvent = binding.bandEventEditText.text.toString()
            val location = binding.locationEditText.text.toString()
            val date = binding.dateEditText.text.toString()
            onDetailsAdded(bandEvent, location, date)
            dismiss()
        }
    }

    private fun showDatePickerDialog(){
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            {_, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                updateDateText()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateText() {
        val dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
        binding.dateEditText.setText(dateFormat.format(calendar.time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



