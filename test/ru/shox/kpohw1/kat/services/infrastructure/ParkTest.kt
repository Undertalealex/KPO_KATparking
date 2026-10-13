package ru.shox.kpohw1.kat.services.infrastructure

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.shox.kpohw1.kat.support.bicycle
import ru.shox.kpohw1.kat.support.helmet
import ru.shox.kpohw1.kat.support.scooter

class ParkTest {

    @Test
    fun `добавляет и находит единицу по инвентарному номеру`() {
        val park = Park()
        val item = scooter("S-1")

        park.add(item)

        assertSame(item, park.findByInventoryNumber("S-1"))
        assertEquals(1, park.all().size)
    }

    @Test
    fun `возвращает null для неизвестного номера`() {
        assertNull(Park().findByInventoryNumber("нет-такого"))
    }

    @Test
    fun `сохраняет порядок добавления`() {
        val park = Park()
        park.add(scooter("S-1"))
        park.add(bicycle("B-1"))
        park.add(helmet("H-1"))

        assertEquals(listOf("S-1", "B-1", "H-1"), park.all().map { it.inventoryNumber })
    }

    @Test
    fun `запрещает дубликат инвентарного номера`() {
        val park = Park()
        park.add(scooter("S-1"))

        assertThrows<IllegalArgumentException> { park.add(bicycle("S-1")) }
    }

    @Test
    fun `запрещает единицу с пустым номером`() {
        assertThrows<IllegalArgumentException> { Park().add(scooter(" ")) }
    }
}
