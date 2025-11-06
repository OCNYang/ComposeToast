package com.yhz.composetoast

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Position where the Toast should appear on screen
 */
enum class ToastPosition {
    TOP,
    CENTER,
    BOTTOM
}

/**
 * Default color configurations for different Toast types
 *
 * Modify these values to customize the global Toast appearance across your app
 */
object ToastDefaults {
    /**
     * Default colors for success Toast
     */
    object Success {
        val backgroundColor = Color(0xf64CAF50)
        val textColor = Color.White
        val iconColor = Color.White
    }

    /**
     * Default colors for error Toast
     */
    object Error {
        val backgroundColor = Color(0xF6F44336)
        val textColor = Color.White
        val iconColor = Color.White
    }

    /**
     * Default colors for warning Toast
     */
    object Warning {
        val backgroundColor = Color(0xF6FF9800)
        val textColor = Color.White
        val iconColor = Color.White
    }

    /**
     * Default colors for info Toast
     *
     * Note: Info Toast uses null by default, which falls back to Material Theme colors
     */
    object Info {
        val backgroundColor: Color? = null
        val textColor: Color? = null
        val iconColor: Color? = null
    }
}

/**
 * Action button data for Toast
 *
 * @param label Button text
 * @param actionColor Button text color (optional, defaults to Material Theme primary color)
 * @param onAction Click callback
 */
data class ActionData(
    val label: String,
    val actionColor: Color? = null,
    val onAction: () -> Unit
)

/**
 * Toast data model
 *
 * @param id Unique identifier for the Toast
 * @param message Message content to display
 * @param imageVector Icon to display (optional, no icon shown if null)
 * @param backgroundColor Background color (optional, uses Material Theme if null)
 * @param textColor Text color (optional, uses Material Theme if null)
 * @param iconColor Icon color (optional, uses Material Theme if null)
 * @param duration Display duration in milliseconds (default: 2000ms)
 * @param position Display position on screen
 * @param actions List of action buttons
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
 * Generates a simple UUID for Toast identification
 */
private fun generateUUID(): String {
    val timestamp = currentTimeMillis()
    val random = (0..999999).random()
    return "$timestamp-$random"
}

/**
 * Platform-specific implementation to get current timestamp in milliseconds
 */
internal expect fun currentTimeMillis(): Long
