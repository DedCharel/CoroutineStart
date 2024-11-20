package com.example.coroutinestart

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val parentJob = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d(LOG_TAG,"Exception caught $throwable")
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + exceptionHandler)

    fun method(){
        val child1Job = coroutineScope.launch {
            delay(3000)
            Log.d(LOG_TAG, "first coroutine finished")
        }

        val child2Job = coroutineScope.launch {
            delay(2000)
            Log.d(LOG_TAG, "second coroutine finished")
        }
        val child3Job = coroutineScope.launch {
            delay(1000)
            error()
            Log.d(LOG_TAG, "third coroutine finished")
        }
    }

    private fun error(){
        throw RuntimeException()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    companion object{
        const val LOG_TAG = "MainViewModel"
    }
}