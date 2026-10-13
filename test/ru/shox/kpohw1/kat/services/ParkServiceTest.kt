package ru.shox.kpohw1.kat.services

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.shox.kpohw1.kat.services.implementations.ITechInspectionService
import ru.shox.kpohw1.kat.services.infrastructure.Park
import ru.shox.kpohw1.kat.services.policies.BeginnerFriendlyPolicy
import ru.shox.kpohw1.kat.support.FakeTechInspectionService
import ru.shox.kpohw1.kat.support.bicycle
import ru.shox.kpohw1.kat.support.chargeStation
import ru.shox.kpohw1.kat.support.electricBicycle
import ru.shox.kpohw1.kat.support.helmet
import ru.shox.kpohw1.kat.support.scooter

class ParkServiceTest {

    private fun service(
        inspection: ITechInspectionService,
        policy: BeginnerFriendlyPolicy = BeginnerFriendlyPolicy(),
    ): ParkService = ParkService(Park(), inspection, policy)

    @Test
    fun `при успешном осмотре ставит транспорт на баланс`() {
        val service = service(FakeTechInspectionService.alwaysAccepted())

        val result = service.admitVehicle(scooter())

        assertTrue(result.isAccepted)
        assertEquals(1, service.unitCount())
    }

    @Test
    fun `при отказе не ставит транспорт на баланс`() {
        val service = service(FakeTechInspectionService.alwaysRejected())

        val result = service.admitVehicle(scooter())

        assertFalse(result.isAccepted)
        assertEquals(0, service.unitCount())
    }

    @Test
    fun `делегирует решение осмотра внедрённому сервису`() {
        val inspection = FakeTechInspectionService.alwaysRejected("сломан")
        val service = service(inspection)

        val result = service.admitVehicle(scooter())

        assertEquals("сломан", result.reason)
        assertEquals(1, inspection.inspected.size)
    }

    @Test
    fun `суммирует потребление только энергопотребителей`() {
        val service = service(FakeTechInspectionService.alwaysAccepted())
        service.admitVehicle(scooter())
        service.admitVehicle(bicycle())
        service.admitVehicle(electricBicycle(dailyEnergyKwh = 1.5))
        service.registerItem(chargeStation(dailyEnergyKwh = 0.5))
        service.registerItem(helmet())

        assertEquals(2.0, service.totalDailyEnergyKwh(), 1e-9)
    }

    @Test
    fun `фильтрует только подходящий новичкам транспорт`() {
        val service = service(FakeTechInspectionService.alwaysAccepted())
        service.admitVehicle(scooter(inventoryNumber = "S-1", beginnerScore = 8))
        service.admitVehicle(scooter(inventoryNumber = "S-2", beginnerScore = 5))
        service.admitVehicle(bicycle(inventoryNumber = "B-1", beginnerScore = 6))
        service.registerItem(helmet(inventoryNumber = "H-1"))

        val friendly = service.beginnerFriendlyVehicles()

        assertEquals(listOf("S-1", "B-1"), friendly.map { it.inventoryNumber })
    }

    @Test
    fun `учитывает внедрённую политику новичков`() {
        val service = service(FakeTechInspectionService.alwaysAccepted(), BeginnerFriendlyPolicy(threshold = 9))
        service.admitVehicle(scooter(inventoryNumber = "S-1", beginnerScore = 8))

        assertTrue(service.beginnerFriendlyVehicles().isEmpty())
    }

    @Test
    fun `инвентарь содержит и транспорт, и вещи`() {
        val service = service(FakeTechInspectionService.alwaysAccepted())
        service.admitVehicle(scooter(inventoryNumber = "S-1"))
        service.registerItem(helmet(inventoryNumber = "H-1"))

        assertEquals(listOf("S-1", "H-1"), service.inventory().map { it.inventoryNumber })
        assertEquals(2, service.unitCount())
    }
}
