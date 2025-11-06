package com.yhz.composetoast

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Toast Manager
 *
 * Manages the Toast queue, display, and automatic dismissal
 */
class ToastManager : ViewModel() {

    // Toast queue
    private val _toastQueue = MutableStateFlow<List<ToastData>>(emptyList())

    // Currently displayed Toast
    private val _currentToast = MutableStateFlow<ToastData?>(null)
    val currentToast: StateFlow<ToastData?> = _currentToast.asStateFlow()

    /**
     * Shows a Toast with custom configuration
     */
    fun showToast(
        message: String,
        imageVector: ImageVector? = null,
        backgroundColor: Color? = null,
        textColor: Color? = null,
        iconColor: Color? = null,
        duration: Long = 2000L,
        position: ToastPosition = ToastPosition.BOTTOM,
        actions: List<ActionData> = emptyList()
    ) {
        val toast = ToastData(
            message = message,
            imageVector = imageVector,
            backgroundColor = backgroundColor,
            textColor = textColor,
            iconColor = iconColor,
            duration = duration,
            position = position,
            actions = actions
        )

        _toastQueue.update { it + toast }

        // Show immediately if no Toast is currently displayed
        if (_currentToast.value == null) {
            showNextToast()
        }
    }

    /**
     * Shows a success Toast
     */
    fun showSuccess(
        message: String,
        duration: Long = 2000L,
        imageVector: ImageVector = ToastIcons.Success,
        backgroundColor: Color = ToastDefaults.Success.backgroundColor,
        textColor: Color = ToastDefaults.Success.textColor,
        iconColor: Color = ToastDefaults.Success.iconColor
    ) {
        showToast(
            message = message,
            imageVector = imageVector,
            backgroundColor = backgroundColor,
            textColor = textColor,
            iconColor = iconColor,
            duration = duration
        )
    }

    /**
     * Shows an error Toast
     */
    fun showError(
        message: String,
        duration: Long = 3000L,
        imageVector: ImageVector = ToastIcons.Error,
        backgroundColor: Color = ToastDefaults.Error.backgroundColor,
        textColor: Color = ToastDefaults.Error.textColor,
        iconColor: Color = ToastDefaults.Error.iconColor
    ) {
        showToast(
            message = message,
            imageVector = imageVector,
            backgroundColor = backgroundColor,
            textColor = textColor,
            iconColor = iconColor,
            duration = duration
        )
    }

    /**
     * Shows a warning Toast
     */
    fun showWarning(
        message: String,
        duration: Long = 2500L,
        imageVector: ImageVector = ToastIcons.Warning,
        backgroundColor: Color = ToastDefaults.Warning.backgroundColor,
        textColor: Color = ToastDefaults.Warning.textColor,
        iconColor: Color = ToastDefaults.Warning.iconColor
    ) {
        showToast(
            message = message,
            imageVector = imageVector,
            backgroundColor = backgroundColor,
            textColor = textColor,
            iconColor = iconColor,
            duration = duration
        )
    }

    /**
     * Shows an info Toast
     */
    fun showInfo(
        message: String,
        duration: Long = 2000L,
        imageVector: ImageVector = ToastIcons.Info,
        backgroundColor: Color? = ToastDefaults.Info.backgroundColor,
        textColor: Color? = ToastDefaults.Info.textColor,
        iconColor: Color? = ToastDefaults.Info.iconColor
    ) {
        showToast(
            message = message,
            imageVector = imageVector,
            backgroundColor = backgroundColor,
            textColor = textColor,
            iconColor = iconColor,
            duration = duration
        )
    }

    /**
     * Shows the next Toast from the queue
     */
    private fun showNextToast() {
        viewModelScope.launch {
            val queue = _toastQueue.value
            if (queue.isNotEmpty()) {
                val toast = queue.first()
                _currentToast.value = toast
                _toastQueue.update { it.drop(1) }

                // Auto-dismiss after duration
                delay(toast.duration)
                dismissCurrent()
            }
        }
    }

    /**
     * Dismisses the current Toast
     */
    fun dismissCurrent() {
        _currentToast.value = null
        showNextToast()
    }

    /**
     * Clears all Toasts from queue and dismisses current Toast
     */
    fun clear() {
        _currentToast.value = null
        _toastQueue.value = emptyList()
    }
}

/**
 * Global Toast object
 *
 * Provides a global access point to show Toasts from anywhere in your app
 *
 * ## Usage Example
 *
 * ```kotlin
 * // In Composable functions
 * Toast.showSuccess("Operation successful!")
 *
 * // In ViewModels
 * class MyViewModel : ViewModel() {
 *     fun doSomething() {
 *         Toast.showInfo("Processing...")
 *     }
 * }
 *
 * // In regular classes
 * fun handleError() {
 *     Toast.showError("An error occurred!")
 * }
 * ```
 */
object Toast {
    private var instance: ToastManager? = null

    internal fun setInstance(toastManager: ToastManager) {
        instance = toastManager
    }

    internal fun getInstance(): ToastManager? = instance

    private fun get(): ToastManager {
        return instance ?: throw IllegalStateException(
            "Toast not initialized. Make sure to use ProvideToastManager at the root of your app."
        )
    }

    /**
     * Shows a custom Toast
     */
    fun show(
        message: String,
        imageVector: ImageVector? = null,
        backgroundColor: Color? = null,
        textColor: Color? = null,
        iconColor: Color? = null,
        duration: Long = 2000L,
        position: ToastPosition = ToastPosition.BOTTOM,
        vararg actions: ActionData
    ) {
        get().showToast(
            message = message,
            imageVector = imageVector,
            backgroundColor = backgroundColor,
            textColor = textColor,
            iconColor = iconColor,
            duration = duration,
            position = position,
            actions = actions.toList()
        )
    }

    /**
     * Shows an info Toast
     */
    fun showInfo(
        message: String,
        duration: Long = 2000L,
        imageVector: ImageVector = ToastIcons.Info,
        backgroundColor: Color? = ToastDefaults.Info.backgroundColor,
        textColor: Color? = ToastDefaults.Info.textColor,
        iconColor: Color? = ToastDefaults.Info.iconColor
    ) {
        get().showInfo(message, duration, imageVector, backgroundColor, textColor, iconColor)
    }

    /**
     * Shows a success Toast
     */
    fun showSuccess(
        message: String,
        duration: Long = 2000L,
        imageVector: ImageVector = ToastIcons.Success,
        backgroundColor: Color = ToastDefaults.Success.backgroundColor,
        textColor: Color = ToastDefaults.Success.textColor,
        iconColor: Color = ToastDefaults.Success.iconColor
    ) {
        get().showSuccess(message, duration, imageVector, backgroundColor, textColor, iconColor)
    }

    /**
     * Shows a warning Toast
     */
    fun showWarning(
        message: String,
        duration: Long = 2500L,
        imageVector: ImageVector = ToastIcons.Warning,
        backgroundColor: Color = ToastDefaults.Warning.backgroundColor,
        textColor: Color = ToastDefaults.Warning.textColor,
        iconColor: Color = ToastDefaults.Warning.iconColor
    ) {
        get().showWarning(message, duration, imageVector, backgroundColor, textColor, iconColor)
    }

    /**
     * Shows an error Toast
     */
    fun showError(
        message: String,
        duration: Long = 3000L,
        imageVector: ImageVector = ToastIcons.Error,
        backgroundColor: Color = ToastDefaults.Error.backgroundColor,
        textColor: Color = ToastDefaults.Error.textColor,
        iconColor: Color = ToastDefaults.Error.iconColor
    ) {
        get().showError(message, duration, imageVector, backgroundColor, textColor, iconColor)
    }

    /**
     * Clears all Toasts
     */
    fun clear() {
        get().clear()
    }
}

/**
 * CompositionLocal for ToastManager
 *
 * Allows passing ToastManager instance through the Compose tree
 */
val LocalToastManager = compositionLocalOf<ToastManager?> { null }

/**
 * Composable wrapper that provides ToastManager
 *
 * This function automatically creates a ToastManager and sets up the global Toast access point.
 * Should be used at the root of your application.
 *
 * ## Usage Example
 *
 * ```kotlin
 * @Composable
 * fun App() {
 *     MaterialTheme {
 *         ProvideToastManager {
 *             YourAppContent()
 *         }
 *     }
 * }
 * ```
 *
 * @param toastContent Custom Toast layout (optional, uses default if null)
 * @param content Application content
 */
@Composable
fun ProvideToastManager(
    toastContent: (@Composable() (ToastData, Dp, () -> Unit) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val toastManager = remember { ToastManager() }

    // Automatically set global instance
    DisposableEffect(toastManager) {
        Toast.setInstance(toastManager)
        onDispose { }
    }

    CompositionLocalProvider(LocalToastManager provides toastManager) {
        ToastHost(
            toastManager = toastManager,
            toastContent = toastContent ?: { toastData, maxWidth, onDismiss ->
                ToastContent(toastData, maxWidth = maxWidth, onDismiss = onDismiss)
            }
        ) {
            content()
        }
    }
}

/**
 * Gets the current ToastManager instance
 *
 * Retrieves from CompositionLocal first, falls back to global instance if unavailable.
 *
 * ## Usage Example
 *
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     val toastManager = rememberToastManager()
 *     Button(onClick = { toastManager.showSuccess("Success!") }) {
 *         Text("Click")
 *     }
 * }
 * ```
 */
@Composable
fun rememberToastManager(): ToastManager {
    val local = LocalToastManager.current
    return local ?: (Toast.getInstance() ?: error("Toast not initialized"))
}
