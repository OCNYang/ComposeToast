package com.yhz.composetoast

actual fun platform() = "Android"

/**
 * Android 平台的 Popup 无法显示在 AlertDialog 之上，
 * 因为 AlertDialog 使用的窗口类型（TYPE_APPLICATION_ATTACHED_DIALOG）
 * 高于 Popup 的窗口类型（TYPE_APPLICATION_PANEL）
 */
actual fun needsDialogToastWrapper(): Boolean = true