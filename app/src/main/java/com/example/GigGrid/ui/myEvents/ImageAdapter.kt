package com.example.GigGrid.ui.myEvents
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.GigGrid.R
import com.example.GigGrid.databinding.ItemImageBinding

class ImageAdapter(
    private val images: List<Uri>,
    private val onRemoveClick: (Uri) -> Unit // The lambda function
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    // Use ViewBinding for the ViewHolder
    class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // The bind function also needs the click listener
        fun bind(imageUri: Uri, onRemoveClick: (Uri) -> Unit) {
            binding.imageView.load(imageUri) {
                crossfade(true)
            }
            // Set the click listener on the remove button
            binding.removeButton.setOnClickListener {
                onRemoveClick(imageUri)
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
        holder.bind(imageUri, onRemoveClick)
    }

    override fun getItemCount() = images.size
}