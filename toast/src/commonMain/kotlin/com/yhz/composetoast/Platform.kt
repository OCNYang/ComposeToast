package com.yhz.composetoast

expect fun platform(): String

/**
 * 判断当前平台是否需要在 Dialog 内部包裹 ToastHost
 *
 * Android 和 iOS 平台的 Popup 无法显示在 Dialog 之上，
 * 需要在 Dialog 内部单独添加 ToastHost
 *
 * @return true 表示需要在 Dialog 内包裹 ToastHost
 */
expect fun needsDialogToastWrapper(): Boolean