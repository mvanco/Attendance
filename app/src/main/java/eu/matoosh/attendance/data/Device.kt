package eu.matoosh.attendance.data

import android.annotation.SuppressLint
import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class Device @Inject constructor(@ApplicationContext context: Context) {

    @SuppressLint("HardwareIds")
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}