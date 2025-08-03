package com.example.GigGrid.ui.myEvents
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.GigGrid.R
import com.example.GigGrid.databinding.ItemImageBinding

class ImageAdapter(
    private val images: List<Uri>,
    private val onRemoveClick: (Uri) -> Unit,
    private val onImageClick: (Uri) -> Unit
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var selectedPosition: Int = -1

    // Use ViewBinding for the ViewHolder
    class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            imageUri: Uri,
            onRemoveClick: (Uri) -> Unit,
            onImageClick: (Uri) -> Unit
        )
        {
            binding.imageView.load(imageUri) {
                crossfade(true)
            }
            // Set the click listener on the remove button
            binding.removeButton.setOnClickListener {
                onRemoveClick(imageUri)
            }
            // Set the click listener for the entire image
            binding.root.setOnClickListener {
                onImageClick(imageUri)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        // Correctly inflate the layout and pass the binding object to the ViewHolder
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri = images[position]

        // Load the image
        holder.binding.imageView.load(imageUri) {
            crossfade(true)
        }

        // Show/hide the remove button based on the selected position
        holder.binding.removeButton.visibility =
            if (position == selectedPosition) View.VISIBLE else View.GONE

        // Set the click listener for the remove button
        holder.binding.removeButton.setOnClickListener {
            // Call the remove function from the ViewModel
            onRemoveClick(imageUri)
            // Reset the selected position after removing
            val previousSelectedPosition = selectedPosition
            selectedPosition = -1
            notifyItemChanged(previousSelectedPosition)
        }

        // Set the long-press listener on the entire item view
        holder.binding.root.setOnLongClickListener {
            val previousSelectedPosition = selectedPosition
            // Update the selected position to the current item
            selectedPosition = holder.adapterPosition

            // Notify the adapter to refresh the old and new positions
            if (previousSelectedPosition != -1) {
                notifyItemChanged(previousSelectedPosition)
            }
            notifyItemChanged(selectedPosition)
            // Return true to consume the long-press event
            true
        }

        // Set the tap listener for zooming
        holder.binding.root.setOnClickListener {
            // If the remove button is visible (meaning it was long-pressed), hide it instead of zooming
            if (position == selectedPosition) {
                val previousSelectedPosition = selectedPosition
                selectedPosition = -1
                notifyItemChanged(previousSelectedPosition)
            } else if (selectedPosition != -1){
                val previousSelectedPosition = selectedPosition
                selectedPosition = -1
                notifyItemChanged(previousSelectedPosition)
            } else {
                // Otherwise, perform the zoom action
                onImageClick(imageUri)
            }
        }
    }

    override fun getItemCount() = images.size
}