package com.yhz.composetoast

import androidx.compose.runtime.Composable

/**
 * Dialog 内容包装器，自动处理跨平台的 Toast 显示
 *
 * 在 Android 和 iOS 平台上，AlertDialog 的窗口层级高于 Popup，
 * 导致 Toast 无法显示在 Dialog 之上。此函数会根据平台自动处理：
 *
 * - **Android/iOS**: 在 Dialog 内部添加 ToastHost，Toast 显示在 Dialog 内部
 * - **Desktop/Web**: 使用全局的 Popup ToastHost，Toast 可以显示在 Dialog 之上
 *
 * ## 使用示例
 *
 * ```kotlin
 * val toastManager = remember { ToastManager() }
 *
 * // 在应用根部使用 ToastHost
 * ToastHost(toastManager = toastManager) {
 *     YourAppContent()
 * }
 *
 * // 在 Dialog 中使用 dialogToastContent
 * AlertDialog(
 *     onDismissRequest = { ... },
 *     title = { Text("Dialog Title") },
 *     text = {
 *         dialogToastContent(toastManager) {
 *             Text("Dialog content here")
 *         }
 *     },
 *     confirmButton = {
 *         Button(onClick = {
 *             toastManager.showSuccess("Operation completed!")
 *         }) {
 *             Text("Show Toast")
 *         }
 *     }
 * )
 * ```
 *
 * @param toastManager Toast 管理器实例
 * @param content Dialog 的内容
 */
@Composable
fun dialogToastContent(
    toastManager: ToastManager,
    content: @Composable () -> Unit
) {
    if (needsDialogToastWrapper()) {
        // Android/iOS: 在 Dialog 内部添加 ToastHost
        // Toast 会显示在 Dialog 内部，但保证可见
        ToastHost(toastManager = toastManager) {
            content()
        }
    } else {
        // Desktop/Web/WASM: Popup 可以显示在 Dialog 之上
        // 不需要额外包装，使用全局的 ToastHost
        content()
    }
}
