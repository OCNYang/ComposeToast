package com.yhz.composetoast

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
