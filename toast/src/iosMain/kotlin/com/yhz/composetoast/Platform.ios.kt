package com.yhz.composetoast

actual fun platform() = "iOS"

/**
 * iOS 平台采用保守策略，在 Dialog 内部使用 ToastHost
 *
 * 原因：iOS 的 AlertDialog 通过模态呈现（Modal Presentation）实现，
 * 创建新的 UIViewController 层级，Popup 可能无法显示在其之上
 */
actual fun needsDialogToastWrapper(): Boolean = true