package ru.shox.kpohw1.kat.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import ru.shox.kpohw1.kat.services.IParkService
import ru.shox.kpohw1.kat.services.implementations.ITechInspectionService
import ru.shox.kpohw1.kat.support.FakeTechInspectionService
import ru.shox.kpohw1.kat.support.electricBicycle
import ru.shox.kpohw1.kat.support.scooter

// проверяет, что фабрика ParkConfig собирает рабочий граф объектов,
// а службу техосмотра можно подменить через DI-контейнер
class ParkConfigTest {

    @Test
    fun `контекст собирает сервис и применяет правило ServiceCenter`() {
        AnnotationConfigApplicationContext(ParkConfig::class.java).use { context ->
            val parkService = context.getBean(IParkService::class.java)

            assertTrue(parkService.admitVehicle(electricBicycle(inventoryNumber = "E-1", dailyEnergyKwh = 0.5)).isAccepted)
            assertFalse(parkService.admitVehicle(electricBicycle(inventoryNumber = "E-2", dailyEnergyKwh = 5.0)).isAccepted)
            assertEquals(1, parkService.unitCount())
        }
    }

    @Test
    fun `ServiceCenter подменяется заглушкой через DI`() {
        AnnotationConfigApplicationContext(ParkConfig::class.java, FakeInspectionConfig::class.java).use { context ->
            val parkService = context.getBean(IParkService::class.java)

            val result = parkService.admitVehicle(scooter())

            assertFalse(result.isAccepted)
            assertEquals("подмена из теста", result.reason)
            assertEquals(0, parkService.unitCount())
        }
    }

    // тестовая конфигурация: подменяет службу техосмотра
    @Configuration
    class FakeInspectionConfig {
        @Bean
        @Primary
        fun fakeTechInspectionService(): ITechInspectionService =
            FakeTechInspectionService.alwaysRejected("подмена из теста")
    }
}
