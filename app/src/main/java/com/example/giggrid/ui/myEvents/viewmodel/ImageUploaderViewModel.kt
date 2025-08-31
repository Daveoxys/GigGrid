package com.example.giggrid.ui.myEvents.viewmodel

import android.content.Context
import android.net.Uri
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.giggrid.ui.UriTypeAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.giggrid.ui.myEvents.ImageItem
import com.google.gson.GsonBuilder

class ImageUploaderViewModel (private val context: Context): ViewModel() {
    
    private val _imageItems = MutableLiveData<List<ImageItem>>(emptyList())
    val imageItems: LiveData<List<ImageItem>> = _imageItems
    private val maxImages = 8
    private val prefsName = "image_uploader_prefs"
    private val keyImageItems = "image_items"

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
        .create()

    // This block runs when the ViewModel is first created
    init {
        loadImages()
    }

    private fun loadImages() {
        val sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(keyImageItems, null)

        if (jsonString != null) {
           val type = object : TypeToken<List<ImageItem>>() {}.type
            _imageItems.value = gson.fromJson(jsonString, type)
        }
    }

   // The most recently added image will appear first.
    fun addImage(imageItem: ImageItem) {
        val currentList = _imageItems.value.orEmpty().toMutableList()
        if (currentList.size < maxImages) {
            // Add the new URI to the beginning of the list
            currentList.add(0, imageItem)
            _imageItems.value = currentList
            saveImages(currentList)
        }
    }

    fun removeImage(imageItem: ImageItem) {
        val currentList = _imageItems.value.orEmpty().toMutableList()
        currentList.remove(imageItem)
        _imageItems.value = currentList
        saveImages(currentList) // Persist the updated list
    }

    private fun saveImages(items: MutableList<ImageItem>) {
        val sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

        val jsonString = gson.toJson(items)

        sharedPreferences.edit {
            putString(keyImageItems, jsonString).apply()
        }
    }

}