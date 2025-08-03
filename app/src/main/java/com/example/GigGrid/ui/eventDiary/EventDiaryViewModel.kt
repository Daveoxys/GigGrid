package com.example.GigGrid.ui.eventDiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventDiaryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Replace this with media and text for each event"
    }
    val text: LiveData<String> = _text
}