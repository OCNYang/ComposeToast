package com.yhz.composetoast

internal actual fun currentTimeMillis(): Long {
    return kotlin.js.Date.now().toLong()
}
