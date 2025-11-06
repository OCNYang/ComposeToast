package com.yhz.composetoast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

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
fun DialogToastContent(
    toastManager: ToastManager = rememberToastManager(),
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

/**
 * 为 Composable 提供独立的 ToastManager，并自动管理其生命周期
 *
 * 当 [show] 为 true 时创建 ToastManager，为 false 时自动清理。
 * 适用于 Dialog、BottomSheet、ModalDrawer 等需要独立 Toast 显示的临时组件。
 *
 * ## 特性
 *
 * - **自动生命周期管理**：随 [show] 状态自动创建和销毁 ToastManager
 * - **资源自动清理**：组件消失时自动清空 Toast 队列
 * - **完全独立**：不与全局 Toast 冲突
 * - **通用性强**：适用于任何需要临时 ToastManager 的场景
 *
 * ## 使用示例
 *
 * ### 在 Dialog 中使用
 * ```kotlin
 * var showDialog by remember { mutableStateOf(false) }
 *
 * WithToastComposable(show = showDialog) { toastManager ->
 *     AlertDialog(
 *         onDismissRequest = { showDialog = false },
 *         title = { Text("Dialog Example") },
 *         text = {
 *             DialogToastContent(toastManager = toastManager) {
 *                 Text("Click the button to show a toast!")
 *             }
 *         },
 *         confirmButton = {
 *             Button(onClick = { toastManager.showSuccess("操作成功！") }) {
 *                 Text("确定")
 *             }
 *         },
 *         dismissButton = {
 *             TextButton(onClick = { showDialog = false }) {
 *                 Text("取消")
 *             }
 *         }
 *     )
 * }
 * ```
 *
 * ### 在 BottomSheet 中使用
 * ```kotlin
 * var showBottomSheet by remember { mutableStateOf(false) }
 *
 * WithToastComposable(show = showBottomSheet) { toastManager ->
 *     ModalBottomSheet(
 *         onDismissRequest = { showBottomSheet = false }
 *     ) {
 *         Column(modifier = Modifier.padding(16.dp)) {
 *             Text("Bottom Sheet Content")
 *             Button(onClick = { toastManager.showInfo("来自 BottomSheet 的 Toast") }) {
 *                 Text("显示 Toast")
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * ### 在自定义弹窗中使用
 * ```kotlin
 * var showCustomPopup by remember { mutableStateOf(false) }
 *
 * WithToastComposable(show = showCustomPopup) { toastManager ->
 *     Popup(
 *         alignment = Alignment.Center,
 *         onDismissRequest = { showCustomPopup = false }
 *     ) {
 *         Card(modifier = Modifier.padding(16.dp)) {
 *             Column(modifier = Modifier.padding(16.dp)) {
 *                 Text("Custom Popup")
 *                 Button(onClick = { toastManager.showWarning("警告信息") }) {
 *                     Text("显示警告")
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * ## 工作原理
 *
 * 1. 当 `show = true` 时，通过 `remember` 创建新的 ToastManager 实例
 * 2. 使用 `DisposableEffect` 监听组件生命周期
 * 3. 当 `show = false` 时，`onDispose` 被调用，清空 Toast 队列
 * 4. 每次从 false → true，都会创建全新的 ToastManager 实例
 *
 * ## 注意事项
 *
 * - **独立性**：此组件创建的 ToastManager 与全局 `Toast` 对象完全独立
 * - **作用域**：ToastManager 的生命周期严格绑定到 [show] 参数
 * - **配置变更**：屏幕旋转等配置变更会导致 ToastManager 重建（Toast 队列会丢失）
 * - **跨平台兼容**：在 Android/iOS 上使用 Dialog 时，需要配合 [DialogToastContent] 使用
 *
 * @param show 是否显示内容（同时决定 ToastManager 的生命周期）
 * @param content 内容 Composable，接收独立的 ToastManager 作为参数
 *
 * @see DialogToastContent 用于在 Dialog 内部正确显示 Toast
 * @see ToastManager Toast 管理器，负责队列管理和自动消失
 */
@Composable
fun WithToastComposable(
    show: Boolean,
    content: @Composable (ToastManager) -> Unit,
) {
    if (show) {
        if (needsDialogToastWrapper()) {
            val dialogToastManager = remember { ToastManager() }
            DisposableEffect(Unit) {
                onDispose {
                    dialogToastManager.clear()
                }
            }
            content(dialogToastManager)
        } else {
            // Desktop/Web/WASM: Popup 可以显示在 Dialog 之上
            // 不需要额外包装，使用全局的 ToastHost
            content(rememberToastManager())
        }
    }
}
