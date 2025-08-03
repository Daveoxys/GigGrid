package com.example.GigGrid.ui.myEvents

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import androidx.core.net.toUri

class ImageUploaderViewModel (private val context: Context): ViewModel() {

    // LiveData to hold the list of image URIs
    private val _imageUris = MutableLiveData<List<Uri>>(emptyList())
    val imageUris: LiveData<List<Uri>> = _imageUris

    // Maximum number of images allowed
    private val MAX_IMAGES = 6
    private val PREFS_NAME = "image_uploader_prefs"
    private val KEY_IMAGE_URIS = "image_uris"

    // This block runs when the ViewModel is first created
    init {
        loadImages()
    }

    private fun loadImages() {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(KEY_IMAGE_URIS, null)

        if (jsonString != null) {
            val type = object : TypeToken<List<String>>() {}.type
            val stringList: List<String> = Gson().fromJson(jsonString, type)
            val uriList = stringList.map { it.toUri() }
            _imageUris.value = uriList
        }
    }

    /**
     * Adds a new image URI to the list.
     * The most recently added image will appear first.
     */
    fun addImage(uri: Uri) {
        val currentList = _imageUris.value.orEmpty().toMutableList()
        if (currentList.size < MAX_IMAGES) {
            // Add the new URI to the beginning of the list
            currentList.add(0, uri)
            _imageUris.value = currentList
            saveImages(currentList)
        }
    }

    fun removeImage(uri: Uri) {
        val currentList = _imageUris.value.orEmpty().toMutableList()
        currentList.remove(uri)
        _imageUris.value = currentList
        saveImages(currentList) // Persist the updated list
    }

    private fun saveImages(uris: List<Uri>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val uriStringList = uris.map { it.toString() }
        val jsonString = Gson().toJson(uriStringList)

        sharedPreferences.edit {
            putString(KEY_IMAGE_URIS, jsonString)
        }
    }

}