package eu.matoosh.attendance.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> MutableLiveData<T>.asImmutable(): LiveData<T> {
    return ImmutableLiveData(this)
}

class ImmutableLiveData<T>(private val liveData: LiveData<T>) : LiveData<T>() {
    override fun getValue(): T? {
        return liveData.value
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        liveData.observe(owner, observer)
    }
}