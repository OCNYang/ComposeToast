package com.yhz.composetoast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ToastDataTest {

    @Test
    fun testToastDataCreation() {
        val toast = ToastData(
            message = "Test message",
            type = ToastType.INFO,
            duration = 3000L
        )

        assertEquals("Test message", toast.message)
        assertEquals(ToastType.INFO, toast.type)
        assertEquals(3000L, toast.duration)
        assertEquals(ToastPosition.BOTTOM, toast.position)
        assertNotNull(toast.id)
    }

    @Test
    fun testToastTypesExist() {
        val types = ToastType.entries
        assertTrue(types.contains(ToastType.INFO))
        assertTrue(types.contains(ToastType.SUCCESS))
        assertTrue(types.contains(ToastType.WARNING))
        assertTrue(types.contains(ToastType.ERROR))
    }

    @Test
    fun testToastPositionsExist() {
        val positions = ToastPosition.entries
        assertTrue(positions.contains(ToastPosition.TOP))
        assertTrue(positions.contains(ToastPosition.CENTER))
        assertTrue(positions.contains(ToastPosition.BOTTOM))
    }
}
