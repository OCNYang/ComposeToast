package com.yhz.composetoast

import android.os.Build
import android.widget.Toast

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform {
    return AndroidPlatform()
}