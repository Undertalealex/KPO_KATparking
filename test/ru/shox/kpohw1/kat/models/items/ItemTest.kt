package ru.shox.kpohw1.kat.models.items

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.shox.kpohw1.kat.services.implementations.IEnergyConsumer

class ItemTest {

    @Test
    fun `шлем хранит размер и не потребляет энергию`() {
        val helmet: Any = Helmet("H-1", "Шлем", "L")

        assertEquals("L", (helmet as Helmet).size)
        assertEquals(false, helmet is IEnergyConsumer)
        assertTrue(helmet.toString().contains("размер L"))
    }

    @Test
    fun `зарядная станция потребляет энергию`() {
        val station = ChargeStation("C-1", "Станция", 0.6, 4)

        assertTrue(station is IEnergyConsumer)
        assertEquals(0.6, station.dailyEnergyKwh)
        assertEquals(4, station.ports)
    }

    @Test
    fun `запрещает пустой размер шлема`() {
        assertThrows<IllegalArgumentException> { Helmet("H-1", "Шлем", " ") }
    }

    @Test
    fun `запрещает неположительное число портов`() {
        assertThrows<IllegalArgumentException> { ChargeStation("C-1", "Станция", 0.6, 0) }
    }

    @Test
    fun `запрещает пустой инвентарный номер`() {
        assertThrows<IllegalArgumentException> { Helmet("", "Шлем", "M") }
    }
}
