package com.example.GigGrid.ui.myEvents

import ImageUploaderViewModelFactory
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.GigGrid.databinding.FragmentMyEventsBinding

class MyEventsFragment : Fragment() {

    private var _binding: FragmentMyEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ImageUploaderViewModel
    private lateinit var imageAdapter: ImageAdapter

    // Activity Result Launcher to pick an image from the gallery
    private val getImageFromGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Persist read access permission to the URI
            requireContext().contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.addImage(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Initialize the ViewModel using the factory here
        val factory = ImageUploaderViewModelFactory(context)
        viewModel = ViewModelProvider(this, factory)[ImageUploaderViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val imageList = viewModel.imageUris.value.orEmpty()

        imageAdapter = ImageAdapter(imageList) { uri ->
            viewModel.removeImage(uri)
        }

        binding.recyclerView.apply {
            adapter = imageAdapter

            // CHANGE: Use GridLayoutManager with 2 columns
            layoutManager = GridLayoutManager(context, 2)
        }
    }


    private fun setupListeners() {
        binding.fabAddImage.setOnClickListener {
            // Launch the image picker
            getImageFromGallery.launch("image/*")
        }
    }

    private fun observeViewModel() {
        viewModel.imageUris.observe(viewLifecycleOwner) { uris ->
            imageAdapter = ImageAdapter(uris) { uri ->
                viewModel.removeImage(uri)
            }
            binding.recyclerView.adapter = imageAdapter

            // The FAB logic from before still works correctly with the new layout
            // if you adjust the layout of the RecyclerView above the FAB.
            if (uris.isNotEmpty()) {
                val params = binding.fabAddImage.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 16.dpToPx()
                binding.fabAddImage.layoutParams = params
            } else {
                val params = binding.fabAddImage.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 0
                binding.fabAddImage.layoutParams = params
            }
        }
    }

    // Extension function for converting dp to pixels
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}