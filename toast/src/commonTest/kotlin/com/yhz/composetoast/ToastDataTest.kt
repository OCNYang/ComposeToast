package com.yhz.composetoast

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ToastDataTest {

    @Test
    fun testToastDataCreation() {
        val toast = ToastData(
            message = "Test message",
            backgroundColor = Color.Blue,
            textColor = Color.White,
            duration = 3000L
        )

        assertEquals("Test message", toast.message)
        assertEquals(Color.Blue, toast.backgroundColor)
        assertEquals(Color.White, toast.textColor)
        assertEquals(3000L, toast.duration)
        assertEquals(ToastPosition.BOTTOM, toast.position)
        assertNotNull(toast.id)
    }

    @Test
    fun testToastPositionsExist() {
        val positions = ToastPosition.entries
        assertTrue(positions.contains(ToastPosition.TOP))
        assertTrue(positions.contains(ToastPosition.CENTER))
        assertTrue(positions.contains(ToastPosition.BOTTOM))
    }

    @Test
    fun testActionDataCreation() {
        val action = ActionData(
            label = "Retry",
            actionColor = Color.Red,
            onAction = { }
        )

        assertEquals("Retry", action.label)
        assertEquals(Color.Red, action.actionColor)
    }

    @Test
    fun testToastDataWithActions() {
        val action1 = ActionData("OK") { }
        val action2 = ActionData("Cancel") { }

        val toast = ToastData(
            message = "Test with actions",
            actions = listOf(action1, action2)
        )

        assertEquals(2, toast.actions.size)
        assertEquals("OK", toast.actions[0].label)
        assertEquals("Cancel", toast.actions[1].label)
    }
}
