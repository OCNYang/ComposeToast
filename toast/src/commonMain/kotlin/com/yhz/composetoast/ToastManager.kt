package com.yhz.composetoast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Toast 管理器
 *
 * 负责管理 Toast 的队列、显示和自动消失
 */
class ToastManager : ViewModel() {

    // Toast 队列
    private val _toastQueue = MutableStateFlow<List<ToastData>>(emptyList())

    // 当前显示的 Toast
    private val _currentToast = MutableStateFlow<ToastData?>(null)
    val currentToast: StateFlow<ToastData?> = _currentToast.asStateFlow()

    /**
     * 显示 Toast
     */
    fun showToast(
        message: String,
        type: ToastType = ToastType.INFO,
        duration: Long = 3000L,
        position: ToastPosition = ToastPosition.BOTTOM,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        val toast = ToastData(
            message = message,
            type = type,
            duration = duration,
            position = position,
            actionLabel = actionLabel,
            onAction = onAction
        )

        _toastQueue.update { it + toast }

        // 如果当前没有显示 Toast，立即显示
        if (_currentToast.value == null) {
            showNextToast()
        }
    }

    /**
     * 显示成功 Toast
     */
    fun showSuccess(message: String, duration: Long = 3000L) {
        showToast(message, ToastType.SUCCESS, duration)
    }

    /**
     * 显示错误 Toast
     */
    fun showError(message: String, duration: Long = 4000L) {
        showToast(message, ToastType.ERROR, duration)
    }

    /**
     * 显示警告 Toast
     */
    fun showWarning(message: String, duration: Long = 3500L) {
        showToast(message, ToastType.WARNING, duration)
    }

    /**
     * 显示信息 Toast
     */
    fun showInfo(message: String, duration: Long = 3000L) {
        showToast(message, ToastType.INFO, duration)
    }

    /**
     * 显示队列中的下一个 Toast
     */
    private fun showNextToast() {
        viewModelScope.launch {
            val queue = _toastQueue.value
            if (queue.isNotEmpty()) {
                val toast = queue.first()
                _currentToast.value = toast
                _toastQueue.update { it.drop(1) }

                // 自动消失
                delay(toast.duration)
                dismissCurrent()
            }
        }
    }

    /**
     * 关闭当前 Toast
     */
    fun dismissCurrent() {
        _currentToast.value = null
        showNextToast()
    }

    /**
     * 清除所有 Toast
     */
    fun clear() {
        _currentToast.value = null
        _toastQueue.value = emptyList()
    }
}

/**
 * 全局 Toast 对象
 *
 * 提供全局访问点，可在任何地方调用显示 Toast
 *
 * ## 使用示例
 *
 * ```kotlin
 * // 在 Composable 函数中
 * Toast.showSuccess("操作成功！")
 *
 * // 在 ViewModel 中
 * class MyViewModel : ViewModel() {
 *     fun doSomething() {
 *         Toast.showInfo("正在处理...")
 *     }
 * }
 *
 * // 在普通类中
 * fun handleError() {
 *     Toast.showError("发生错误！")
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
     * 显示自定义 Toast
     */
    fun show(
        message: String,
        type: ToastType = ToastType.INFO,
        duration: Long = 2000L,
        position: ToastPosition = ToastPosition.BOTTOM,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        get().showToast(message, type, duration, position, actionLabel, onAction)
    }

    /**
     * 显示信息 Toast
     */
    fun showInfo(message: String, duration: Long = 2000L) {
        get().showInfo(message, duration)
    }

    /**
     * 显示成功 Toast
     */
    fun showSuccess(message: String, duration: Long = 2000L) {
        get().showSuccess(message, duration)
    }

    /**
     * 显示警告 Toast
     */
    fun showWarning(message: String, duration: Long = 2500L) {
        get().showWarning(message, duration)
    }

    /**
     * 显示错误 Toast
     */
    fun showError(message: String, duration: Long = 3000L) {
        get().showError(message, duration)
    }

    /**
     * 清除所有 Toast
     */
    fun clear() {
        get().clear()
    }
}

/**
 * CompositionLocal for ToastManager
 *
 * 允许在 Compose 树中传递 ToastManager 实例
 */
val LocalToastManager = compositionLocalOf<ToastManager?> { null }

/**
 * 提供 ToastManager 的 Composable 包装器
 *
 * 这个函数会自动创建 ToastManager 并设置全局 Toast 访问点。
 * 应该在应用的根组件使用。
 *
 * ## 使用示例
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
 * @param content 应用内容
 */
@Composable
fun ProvideToastManager(
    content: @Composable () -> Unit
) {
    val toastManager = remember { ToastManager() }

    // 自动设置全局实例
    DisposableEffect(toastManager) {
        Toast.setInstance(toastManager)
        onDispose { }
    }

    CompositionLocalProvider(LocalToastManager provides toastManager) {
        ToastHost(toastManager = toastManager) {
            content()
        }
    }
}

/**
 * 获取当前的 ToastManager 实例
 *
 * 优先从 CompositionLocal 获取，如果不可用则使用全局实例。
 *
 * ## 使用示例
 *
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     val toastManager = rememberToastManager()
 *     Button(onClick = { toastManager.showSuccess("成功！") }) {
 *         Text("点击")
 *     }
 * }
 * ```
 */
@Composable
fun rememberToastManager(): ToastManager {
    val local = LocalToastManager.current
    return local ?: (Toast.getInstance() ?: error("Toast not initialized"))
}
