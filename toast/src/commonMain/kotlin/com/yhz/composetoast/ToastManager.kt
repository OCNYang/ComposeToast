package com.yhz.composetoast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

        // 如果当前没有显示 Toast，立即显示
        if (_currentToast.value == null) {
            showNextToast()
        }
    }

    /**
     * 显示成功 Toast
     */
    fun showSuccess(
        message: String,
        duration: Long = 2000L,
        imageVector: ImageVector = ToastIcons.Success,
        backgroundColor: Color = Color(0x554CAF50),
        textColor: Color = Color.White,
        iconColor: Color = Color.White
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
     * 显示错误 Toast
     */
    fun showError(
        message: String,
        duration: Long = 3000L,
        imageVector: ImageVector = ToastIcons.Error,
        backgroundColor: Color = Color(0xFFF44336),
        textColor: Color = Color.White,
        iconColor: Color = Color.White
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
     * 显示警告 Toast
     */
    fun showWarning(
        message: String,
        duration: Long = 2500L,
        imageVector: ImageVector = ToastIcons.Warning,
        backgroundColor: Color = Color(0xFFFF9800),
        textColor: Color = Color.White,
        iconColor: Color = Color.White
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
     * 显示信息 Toast
     */
    fun showInfo(
        message: String,
        duration: Long = 2000L,
        imageVector: ImageVector = ToastIcons.Info,
        backgroundColor: Color? = null,
        textColor: Color? = null,
        iconColor: Color? = null
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
     * 显示信息 Toast
     */
    fun showInfo(
        message: String,
        duration: Long = 2000L,
        imageVector: ImageVector = ToastIcons.Info,
        backgroundColor: Color? = null,
        textColor: Color? = null,
        iconColor: Color? = null
    ) {
        get().showInfo(message, duration, imageVector, backgroundColor, textColor, iconColor)
    }

    /**
     * 显示成功 Toast
     */
    fun showSuccess(
        message: String,
        duration: Long = 2000L,
        imageVector: ImageVector = ToastIcons.Success,
        backgroundColor: Color = Color(0xfc4CAF50),
        textColor: Color = Color.White,
        iconColor: Color = Color.White
    ) {
        get().showSuccess(message, duration, imageVector, backgroundColor, textColor, iconColor)
    }

    /**
     * 显示警告 Toast
     */
    fun showWarning(
        message: String,
        duration: Long = 2500L,
        imageVector: ImageVector = ToastIcons.Warning,
        backgroundColor: Color = Color(0xFFFF9800),
        textColor: Color = Color.White,
        iconColor: Color = Color.White
    ) {
        get().showWarning(message, duration, imageVector, backgroundColor, textColor, iconColor)
    }

    /**
     * 显示错误 Toast
     */
    fun showError(
        message: String,
        duration: Long = 3000L,
        imageVector: ImageVector = ToastIcons.Error,
        backgroundColor: Color = Color(0xFFF44336),
        textColor: Color = Color.White,
        iconColor: Color = Color.White
    ) {
        get().showError(message, duration, imageVector, backgroundColor, textColor, iconColor)
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
