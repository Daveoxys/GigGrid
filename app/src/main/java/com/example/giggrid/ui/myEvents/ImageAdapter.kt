package com.example.giggrid.ui.myEvents
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.giggrid.databinding.ItemImageBinding

class ImageAdapter(
    private val images: List<ImageItem>,
    private val onRemoveClick: (ImageItem) -> Unit,
    private val onImageClick: (ImageItem) -> Unit
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var selectedPosition: Int = -1

    // Use ViewBinding for the ViewHolder
    class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            imageItem: ImageItem,
            onRemoveClick: (ImageItem) -> Unit,
            onImageClick: (ImageItem) -> Unit
        )
        {
            binding.imageView.load(imageItem) {
                crossfade(true)
            }
            // Set the click listener on the remove button
            binding.removeButton.setOnClickListener {
                onRemoveClick(imageItem)
            }
            // Set the click listener for the entire image
            binding.root.setOnClickListener {
                onImageClick(imageItem)
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
        val imageItem = images[position]

        // Load the image
        holder.binding.imageView.load(imageItem.uri) {
            crossfade(true)
        }

        // Show/hide the remove button based on the selected position
        holder.binding.removeButton.visibility =
            if (position == selectedPosition) View.VISIBLE else View.GONE

        // Set the click listener for the remove button
        holder.binding.removeButton.setOnClickListener {
            // Call the remove function from the ViewModel
            onRemoveClick(imageItem)
            // Reset the selected position after removing
            val previousSelectedPosition = selectedPosition
            selectedPosition = -1
            notifyItemChanged(previousSelectedPosition)
        }

        // Set the long-press listener on the entire item view
        holder.binding.root.setOnLongClickListener {
            val previousSelectedPosition = selectedPosition
            // Update the selected position to the current item
            selectedPosition = holder.bindingAdapterPosition

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
                onImageClick(imageItem)
            }
        }
    }

    override fun getItemCount() = images.size
}