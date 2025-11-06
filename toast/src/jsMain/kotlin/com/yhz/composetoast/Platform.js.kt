package com.yhz.composetoast

actual fun platform(): String = "Web (JavaScript)"

/**
 * Web (JS) 平台的 Popup 可以正常显示在 Dialog 之上
 */
actual fun needsDialogToastWrapper(): Boolean = false
