package ru.shox.kpohw1.kat.models.vehicles

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.shox.kpohw1.kat.services.implementations.IEnergyConsumer

class VehicleTest {

    @Test
    fun `создаёт самокат с корректными полями`() {
        val scooter = Scooter("S-1", "Самокат", 8)

        assertEquals("S-1", scooter.inventoryNumber)
        assertEquals("Самокат", scooter.title)
        assertEquals(8, scooter.beginnerScore)
    }

    @Test
    fun `самокат не является потребителем энергии`() {
        val scooter: Any = Scooter("S-1", "Самокат", 8)

        assertEquals(false, scooter is IEnergyConsumer)
    }

    @Test
    fun `велосипед не является потребителем энергии`() {
        val bicycle: Any = Bicycle("B-1", "Велосипед", 9)

        assertEquals(false, bicycle is IEnergyConsumer)
    }

    @Test
    fun `электровелосипед потребляет энергию`() {
        val ebike = ElectricBicycle("E-1", "Электровелосипед", 7, 1.2)

        assertEquals(true, ebike is IEnergyConsumer)
        assertEquals(1.2, ebike.dailyEnergyKwh)
    }

    @Test
    fun `запрещает пустой инвентарный номер`() {
        assertThrows<IllegalArgumentException> { Bicycle("  ", "Велосипед", 5) }
    }

    @Test
    fun `запрещает пустое название`() {
        assertThrows<IllegalArgumentException> { Bicycle("B-1", "", 5) }
    }

    @Test
    fun `запрещает оценку простоты вне диапазона 1-10`() {
        assertThrows<IllegalArgumentException> { Bicycle("B-1", "Велосипед", 0) }
        assertThrows<IllegalArgumentException> { Bicycle("B-1", "Велосипед", 11) }
    }

    @Test
    fun `запрещает отрицательное потребление энергии`() {
        assertThrows<IllegalArgumentException> { ElectricBicycle("E-1", "Электровелосипед", 8, -1.0) }
    }

    @Test
    fun `описание содержит вид, название и номер`() {
        assertEquals("Велосипед 'Городской' (B-1)", Bicycle("B-1", "Городской", 5).toString())
    }
}
