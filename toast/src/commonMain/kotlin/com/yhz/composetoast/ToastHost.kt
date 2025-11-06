package com.yhz.composetoast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * Toast Host - Popup 方案
 *
 * 使用 Popup 方式显示 Toast，解决两个关键问题：
 * 1. 只有 Toast 内容区域会拦截事件，其他区域可以正常交互
 * 2. Toast 可以显示在 Dialog 之上
 *
 * @param toastManager Toast 管理器
 * @param content 主内容
 */
@Composable
fun ToastHost(
    toastManager: ToastManager,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // 主内容
    Box(modifier = modifier.fillMaxSize()) {
        content()
    }

    // Toast 使用 Popup 显示（独立的窗口层级）
    val currentToast by toastManager.currentToast.collectAsState()

    currentToast?.let { toast ->
        Popup(
            alignment = when (toast.position) {
                ToastPosition.TOP -> Alignment.TopCenter
                ToastPosition.CENTER -> Alignment.Center
                ToastPosition.BOTTOM -> Alignment.BottomCenter
            },
            properties = PopupProperties(
                focusable = false,  // 不抢夺焦点
                dismissOnBackPress = false,  // 不响应返回键
                dismissOnClickOutside = false,  // 点击外部不消失
                clippingEnabled = false  // 不裁剪，允许超出边界
            )
        ) {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = {
                        if (toast.position == ToastPosition.TOP) -it else it
                    },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(
                    targetOffsetY = {
                        if (toast.position == ToastPosition.TOP) -it else it
                    },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            ) {
                // 添加智能 padding，避免覆盖重要交互区域
                Box(
                    modifier = Modifier.padding(
                        top = when (toast.position) {
                            ToastPosition.TOP -> 16.dp
                            else -> 0.dp
                        },
                        bottom = when (toast.position) {
                            ToastPosition.BOTTOM -> 100.dp  // 底部留更多空间，避免覆盖按钮
                            else -> 0.dp
                        },
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
                    ToastContent(
                        toast = toast,
                        onDismiss = { toastManager.dismissCurrent() }
                    )
                }
            }
        }
    }
}

/**
 * Toast 内容组件
 */
@Composable
private fun ToastContent(
    toast: ToastData,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = getToastColors(toast.type)

    Card(
        modifier = modifier
            .widthIn(max = 400.dp)  // 减小最大宽度，减少阻挡面积
            .wrapContentHeight(),   // 高度自适应内容
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 图标
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(colors.icon, CircleShape)
            )

            // 消息
            Text(
                text = toast.message,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )

            // 操作按钮
            toast.actionLabel?.let { label ->
                TextButton(
                    onClick = {
                        toast.onAction?.invoke()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = label,
                        color = colors.action
                    )
                }
            }
        }
    }
}

/**
 * Toast 颜色配置
 */
private data class ToastColors(
    val background: Color,
    val icon: Color,
    val text: Color,
    val action: Color
)

/**
 * 获取 Toast 颜色
 */
@Composable
private fun getToastColors(type: ToastType): ToastColors {
    return when (type) {
        ToastType.INFO -> ToastColors(
            background = MaterialTheme.colorScheme.surfaceVariant,
            icon = MaterialTheme.colorScheme.primary,
            text = MaterialTheme.colorScheme.onSurfaceVariant,
            action = MaterialTheme.colorScheme.primary
        )
        ToastType.SUCCESS -> ToastColors(
            background = Color(0xFF4CAF50).copy(alpha = 0.95f),
            icon = Color.White,
            text = Color.White,
            action = Color.White
        )
        ToastType.WARNING -> ToastColors(
            background = Color(0xFFFF9800).copy(alpha = 0.95f),
            icon = Color.White,
            text = Color.White,
            action = Color.White
        )
        ToastType.ERROR -> ToastColors(
            background = Color(0xFFF44336).copy(alpha = 0.95f),
            icon = Color.White,
            text = Color.White,
            action = Color.White
        )
    }
}

