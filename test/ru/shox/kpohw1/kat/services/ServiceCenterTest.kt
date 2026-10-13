package ru.shox.kpohw1.kat.services

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.shox.kpohw1.kat.support.bicycle
import ru.shox.kpohw1.kat.support.electricBicycle

class ServiceCenterTest {

    @Test
    fun `принимает транспорт без энергопотребления`() {
        assertTrue(ServiceCenter().inspect(bicycle()).isAccepted)
    }

    @Test
    fun `принимает энергопотребителя ровно на границе лимита`() {
        assertTrue(ServiceCenter(maxDailyEnergyKwh = 2.0).inspect(electricBicycle(dailyEnergyKwh = 2.0)).isAccepted)
    }

    @Test
    fun `отклоняет энергопотребителя сверх лимита и указывает причину`() {
        val result = ServiceCenter().inspect(electricBicycle(dailyEnergyKwh = 3.5))

        assertFalse(result.isAccepted)
        assertNotNull(result.reason)
    }

    @Test
    fun `поддерживает настраиваемый лимит`() {
        assertTrue(ServiceCenter(maxDailyEnergyKwh = 5.0).inspect(electricBicycle(dailyEnergyKwh = 3.5)).isAccepted)
    }

    @Test
    fun `запрещает отрицательный лимит`() {
        assertThrows<IllegalArgumentException> { ServiceCenter(maxDailyEnergyKwh = -1.0) }
    }
}
