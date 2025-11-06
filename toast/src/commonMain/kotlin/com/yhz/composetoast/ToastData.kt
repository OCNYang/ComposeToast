package com.yhz.composetoast

/**
 * Toast 类型
 */
enum class ToastType {
    INFO,
    SUCCESS,
    WARNING,
    ERROR
}

/**
 * Toast 显示位置
 */
enum class ToastPosition {
    TOP,
    CENTER,
    BOTTOM
}

/**
 * Toast 数据模型
 *
 * @param id Toast 唯一标识符
 * @param message 显示的消息内容
 * @param type Toast 类型（信息、成功、警告、错误）
 * @param duration 显示时长（毫秒），默认 2000ms
 * @param position 显示位置
 * @param actionLabel 操作按钮文本（可选）
 * @param onAction 操作按钮点击回调（可选）
 */
data class ToastData(
    val id: String = generateUUID(),
    val message: String,
    val type: ToastType = ToastType.INFO,
    val duration: Long = 2000L,  // 缩短为 2 秒，减少阻挡时间
    val position: ToastPosition = ToastPosition.BOTTOM,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)

/**
 * 生成简单的 UUID
 */
private fun generateUUID(): String {
    val timestamp = currentTimeMillis()
    val random = (0..999999).random()
    return "$timestamp-$random"
}

/**
 * 获取当前时间戳（跨平台）
 */
internal expect fun currentTimeMillis(): Long
