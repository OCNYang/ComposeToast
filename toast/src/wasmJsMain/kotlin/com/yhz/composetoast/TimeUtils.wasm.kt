package com.yhz.composetoast

internal actual fun currentTimeMillis(): Long {
    return kotlinx.browser.window.performance.now().toLong()
}
