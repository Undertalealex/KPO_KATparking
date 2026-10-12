package ru.shox.kpohw1.kat.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.shox.kpohw1.kat.runners.ParkScenarioRunners
import ru.shox.kpohw1.kat.services.IParkService
import ru.shox.kpohw1.kat.services.ParkService
import ru.shox.kpohw1.kat.services.ServiceCenter
import ru.shox.kpohw1.kat.services.implementations.ITechInspectionService
import ru.shox.kpohw1.kat.services.infrastructure.Park
import ru.shox.kpohw1.kat.services.policies.BeginnerFriendlyPolicy

// Фабрика объектов приложения
@Configuration
class ParkConfig {
    
    // хранилище единиц на балансе: один общий экземпляр на всё приложение
    @Bean
    fun park(): Park = Park()
    // служба техосмотра с правилом приёмки по потреблению
    @Bean
    fun techInspectionService(): ITechInspectionService = ServiceCenter()
    // политика 'подходит новичку' (порог 6)
    @Bean
    fun beginnerFriendlyPolicy(): BeginnerFriendlyPolicy = BeginnerFriendlyPolicy()
    // сервис сценариев парка
    @Bean
    fun parkService(
        park: Park,
        techInspectionService: ITechInspectionService,
        beginnerFriendlyPolicy: BeginnerFriendlyPolicy,
    ): IParkService = ParkService(park, techInspectionService, beginnerFriendlyPolicy)
    // демонстрация и интерактивный режим
    @Bean
    fun parkScenarioRunners(parkService: IParkService): ParkScenarioRunners =
        ParkScenarioRunners(parkService)
}
