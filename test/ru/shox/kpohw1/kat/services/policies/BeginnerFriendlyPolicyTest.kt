package ru.shox.kpohw1.kat.services.policies

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.shox.kpohw1.kat.support.bicycle

class BeginnerFriendlyPolicyTest {

    @Test
    fun `порог по умолчанию равен 6`() {
        val policy = BeginnerFriendlyPolicy()

        assertEquals(6, policy.threshold())
    }

    @Test
    fun `подходит ровно на пороге и выше`() {
        val policy = BeginnerFriendlyPolicy()

        assertTrue(policy.isBeginnerFriendly(bicycle(beginnerScore = 6)))
        assertTrue(policy.isBeginnerFriendly(bicycle(beginnerScore = 10)))
    }

    @Test
    fun `не подходит ниже порога`() {
        val policy = BeginnerFriendlyPolicy()

        assertFalse(policy.isBeginnerFriendly(bicycle(beginnerScore = 5)))
    }

    @Test
    fun `поддерживает пользовательский порог`() {
        val policy = BeginnerFriendlyPolicy(threshold = 9)

        assertTrue(policy.isBeginnerFriendly(bicycle(beginnerScore = 9)))
        assertFalse(policy.isBeginnerFriendly(bicycle(beginnerScore = 8)))
    }

    @Test
    fun `запрещает порог вне диапазона`() {
        assertThrows<IllegalArgumentException> { BeginnerFriendlyPolicy(threshold = 0) }
        assertThrows<IllegalArgumentException> { BeginnerFriendlyPolicy(threshold = 11) }
    }
}
