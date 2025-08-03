package com.example.GigGrid.ui.myEvents

import ImageUploaderViewModelFactory
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
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
        setupZoomOverlay()
    }

    private fun setupListeners() {
        binding.fabAddImage.setOnClickListener {
            // Launch the image picker
            getImageFromGallery.launch("image/*")
        }
    }

    private fun setupRecyclerView() {
        val imageList = viewModel.imageUris.value.orEmpty()

        imageAdapter = ImageAdapter(
            images = imageList,
            onRemoveClick = { uri -> viewModel.removeImage(uri) },
            onImageClick = { uri -> showZoomedImage(uri) }
        )

        binding.recyclerView.apply {
            adapter = imageAdapter

            // Use GridLayoutManager with 2 columns
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun setupZoomOverlay() {
        binding.zoomOverlay.setOnClickListener {
            hideZoomedImage()
        }

        // Handle the system back button to close the zoomed image
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.zoomOverlay.visibility == View.VISIBLE) {
                hideZoomedImage()
            } else {
                // If not zoomed, let the back button behave normally
                this.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
                this.isEnabled = true
            }
        }
    }

    // Function to show the zoomed image
    private fun showZoomedImage(uri: Uri) {
        binding.zoomedImageView.load(uri) {
            crossfade(true)
        }
        binding.zoomOverlay.visibility = View.VISIBLE
    }

    private fun hideZoomedImage() {
        binding.zoomOverlay.visibility = View.GONE
    }

    private fun observeViewModel() {
        viewModel.imageUris.observe(viewLifecycleOwner) { uris ->
            // Update the adapter with the new list and both click listeners
            imageAdapter = ImageAdapter(
                images = uris,
                onRemoveClick = { uri -> viewModel.removeImage(uri) },
                onImageClick = { uri -> showZoomedImage(uri) }
            )
            binding.recyclerView.adapter = imageAdapter

            if (uris.size >= 8){
                binding.fabAddImage.visibility = View.GONE
            } else {
                binding.fabAddImage.visibility = View.VISIBLE
            }

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