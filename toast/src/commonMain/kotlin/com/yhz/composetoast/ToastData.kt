package com.yhz.composetoast

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Toast 显示位置
 */
enum class ToastPosition {
    TOP,
    CENTER,
    BOTTOM
}

/**
 * Toast 操作按钮数据
 *
 * @param label 按钮文本
 * @param actionColor 按钮文本颜色（可选）
 * @param onAction 点击回调
 */
data class ActionData(
    val label: String,
    val actionColor: Color? = null,
    val onAction: () -> Unit
)

/**
 * Toast 数据模型
 *
 * @param id Toast 唯一标识符
 * @param message 显示的消息内容
 * @param imageVector Toast 图标（可选），如果为 null 则不显示图标
 * @param backgroundColor 背景颜色（可选）
 * @param textColor 文本颜色（可选）
 * @param iconColor 图标颜色（可选）
 * @param duration 显示时长（毫秒），默认 2000ms
 * @param position 显示位置
 * @param actions 操作按钮列表
 */
data class ToastData(
    val id: String = generateUUID(),
    val message: String,
    val imageVector: ImageVector? = null,
    val backgroundColor: Color? = null,
    val textColor: Color? = null,
    val iconColor: Color? = null,
    val duration: Long = 2000L,
    val position: ToastPosition = ToastPosition.BOTTOM,
    val actions: List<ActionData> = emptyList()
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
