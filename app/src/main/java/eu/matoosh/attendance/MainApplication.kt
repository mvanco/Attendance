package eu.matoosh.attendance

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication() : Application(), ViewModelStoreOwner {
    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override val viewModelStore: ViewModelStore
        get() = TODO("Not yet implemented")
}