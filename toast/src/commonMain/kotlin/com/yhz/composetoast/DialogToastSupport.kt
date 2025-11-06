package com.yhz.composetoast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

/**
 * Dialog content wrapper that automatically handles cross-platform Toast display
 *
 * On Android and iOS platforms, AlertDialog's window layer is higher than Popup,
 * preventing Toast from appearing above Dialogs. This function handles it automatically:
 *
 * - **Android/iOS**: Adds ToastHost inside Dialog, Toast displays within Dialog
 * - **Desktop/Web**: Uses global Popup ToastHost, Toast can appear above Dialog
 *
 * ## Usage Example
 *
 * ```kotlin
 * val toastManager = remember { ToastManager() }
 *
 * // Use ToastHost at app root
 * ToastHost(toastManager = toastManager) {
 *     YourAppContent()
 * }
 *
 * // Use dialogToastContent in Dialog
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
 * @param toastManager Toast manager instance
 * @param content Dialog content
 */
@Composable
fun DialogToastContent(
    toastManager: ToastManager = rememberToastManager(),
    content: @Composable () -> Unit
) {
    if (needsDialogToastWrapper()) {
        // Android/iOS: Add ToastHost inside Dialog
        // Toast displays inside Dialog but remains visible
        ToastHost(toastManager = toastManager) {
            content()
        }
    } else {
        // Desktop/Web/WASM: Popup can appear above Dialog
        // No wrapper needed, use global ToastHost
        content()
    }
}

/**
 * Provides an independent ToastManager for a Composable and automatically manages its lifecycle
 *
 * Creates ToastManager when [show] is true, clears automatically when false.
 * Suitable for temporary components like Dialog, BottomSheet, ModalDrawer that need independent Toast display.
 *
 * ## Features
 *
 * - **Automatic lifecycle management**: Creates/destroys ToastManager based on [show] state
 * - **Automatic resource cleanup**: Clears Toast queue when component disappears
 * - **Complete independence**: Doesn't conflict with global Toast
 * - **Highly versatile**: Works with any temporary ToastManager scenarios
 *
 * ## Usage Examples
 *
 * ### In Dialog
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
 *             Button(onClick = { toastManager.showSuccess("Success!") }) {
 *                 Text("Confirm")
 *             }
 *         },
 *         dismissButton = {
 *             TextButton(onClick = { showDialog = false }) {
 *                 Text("Cancel")
 *             }
 *         }
 *     )
 * }
 * ```
 *
 * ### In BottomSheet
 * ```kotlin
 * var showBottomSheet by remember { mutableStateOf(false) }
 *
 * WithToastComposable(show = showBottomSheet) { toastManager ->
 *     ModalBottomSheet(
 *         onDismissRequest = { showBottomSheet = false }
 *     ) {
 *         Column(modifier = Modifier.padding(16.dp)) {
 *             Text("Bottom Sheet Content")
 *             Button(onClick = { toastManager.showInfo("Toast from BottomSheet") }) {
 *                 Text("Show Toast")
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * ### In Custom Popup
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
 *                 Button(onClick = { toastManager.showWarning("Warning message") }) {
 *                     Text("Show Warning")
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * ## How It Works
 *
 * 1. When `show = true`, creates new ToastManager instance via `remember`
 * 2. Uses `DisposableEffect` to monitor component lifecycle
 * 3. When `show = false`, `onDispose` is called, clearing Toast queue
 * 4. Each false → true transition creates a brand new ToastManager instance
 *
 * ## Notes
 *
 * - **Independence**: Created ToastManager is completely independent from global `Toast` object
 * - **Scope**: ToastManager lifecycle is strictly bound to [show] parameter
 * - **Configuration changes**: Screen rotation may cause ToastManager rebuild (Toast queue will be lost)
 * - **Cross-platform**: When using Dialog on Android/iOS, must use [DialogToastContent]
 *
 * @param show Whether to show content (also determines ToastManager lifecycle)
 * @param content Content Composable receiving independent ToastManager as parameter
 *
 * @see DialogToastContent For correctly displaying Toast inside Dialog
 * @see ToastManager Toast manager responsible for queue management and auto-dismissal
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
            // Desktop/Web/WASM: Popup can appear above Dialog
            // No wrapper needed, use global ToastHost
            content(rememberToastManager())
        }
    }
}
