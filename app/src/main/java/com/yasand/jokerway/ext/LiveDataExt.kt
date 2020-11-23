package com.yasand.jokerway.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.nonNull() : MediatorLiveData<T> {
    val mediatorLiveData = MediatorLiveData<T>()
    mediatorLiveData.addSource(this) {
        it.let { mediatorLiveData.value = it }
    }
    return mediatorLiveData
}