package com.yhz.composetoast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * Toast Host - Popup Implementation
 *
 * Uses Popup to display Toasts, solving two key issues:
 * 1. Only the Toast content area intercepts events, other areas remain interactive
 * 2. Toast can be displayed above Dialogs
 *
 * @param toastManager Toast manager instance
 * @param modifier Modifier for the host
 * @param toastContent Custom Toast layout function
 * @param content Main application content
 */
@Composable
fun ToastHost(
    toastManager: ToastManager,
    modifier: Modifier = Modifier,
    toastContent: @Composable() (ToastData, Dp, () -> Unit) -> Unit =
        { toastData, maxWidth, onDismiss ->
            ToastContent(toastData, maxWidth = maxWidth, onDismiss = onDismiss)
        },
    content: @Composable () -> Unit
) {
    // Main content
    Box(modifier = modifier.fillMaxSize()) {
        content()
    }

    // Toast displayed using Popup (independent window layer)
    val currentToast by toastManager.currentToast.collectAsState()

    currentToast?.let { toast ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            // Calculate Toast max width: screen width - 32dp (16dp padding on each side), capped at 400dp
            val maxToastWidth = remember(maxWidth) {
                minOf(maxWidth - 32.dp, 400.dp)
            }

            Popup(
                alignment = when (toast.position) {
                    ToastPosition.TOP -> Alignment.TopCenter
                    ToastPosition.CENTER -> Alignment.Center
                    ToastPosition.BOTTOM -> Alignment.BottomCenter
                },
                offset = IntOffset(
                    x = 0,
                    y = when (toast.position) {
                        ToastPosition.BOTTOM -> (-100).dp.value.toInt()  // Offset upward by 100dp to avoid bottom buttons
                        ToastPosition.TOP -> 16.dp.value.toInt()          // Offset downward by 16dp for top padding
                        ToastPosition.CENTER -> 0
                    }
                ),
                properties = PopupProperties(
                    focusable = false,  // Don't steal focus
                    dismissOnBackPress = false,  // Don't respond to back button
                    dismissOnClickOutside = false,  // Don't dismiss on outside click
                    clippingEnabled = false  // Allow content beyond bounds
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
                    ) + fadeOut(animationSpec = tween(300)),
                ) {
                    toastContent(
                        toast,
                        maxToastWidth,
                        { toastManager.dismissCurrent() }
                    )
                }
            }
        }
    }
}

/**
 * Toast content component
 *
 * Displays the default Toast UI with icon, message, and action buttons
 */
@Composable
internal fun ToastContent(
    toast: ToastData,
    maxWidth: Dp,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = toast.backgroundColor ?: MaterialTheme.colorScheme.surfaceVariant
    val textColor = toast.textColor ?: MaterialTheme.colorScheme.onSurfaceVariant
    val iconColor = toast.iconColor ?: MaterialTheme.colorScheme.primary
    val actionColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = modifier
            .wrapContentWidth()
            .widthIn(max = maxWidth)
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            toast.imageVector?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = "Toast icon",
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = toast.message,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )

            toast.actions.forEach { action ->
                TextButton(
                    onClick = {
                        action.onAction()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = action.label,
                        color = action.actionColor ?: actionColor
                    )
                }
            }
        }
    }
}


