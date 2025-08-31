package com.example.giggrid.ui.eventDiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventDiaryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Replace this"
    }
    val text: LiveData<String> = _text
}