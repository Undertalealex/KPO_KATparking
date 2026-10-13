package ru.shox.kpohw1.kat

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.shox.kpohw1.kat.runners.ParkScenarioRunners
import ru.shox.kpohw1.kat.services.IParkService

// проверяет, что Spring Boot-контекст приложения поднимается и отдаёт готовые бины
@SpringBootTest
class ProgramTest {

    @Autowired
    private lateinit var parkService: IParkService

    @Autowired
    private lateinit var runners: ParkScenarioRunners

    @Test
    fun `контекст Spring Boot поднимается и собирает бины`() {
        assertNotNull(runners)
        assertEquals(0, parkService.unitCount())
    }
}
