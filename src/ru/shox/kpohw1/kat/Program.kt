package ru.shox.kpohw1.kat

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import ru.shox.kpohw1.kat.runners.ParkScenarioRunners

// точка входа приложения 'Кат'
// @SpringBootApplication поднимает DI-контейнер Spring, который собирает граф
// объектов по ParkConfig; после старта контекста достаём сценарии и запускаем их
// баннер и шумные логи отключены, чтобы консольный вывод оставался чистым
@SpringBootApplication
class Program

fun main(args: Array<String>) {
    val context = SpringApplication(Program::class.java).apply {
        setBannerMode(Banner.Mode.OFF)
        setLogStartupInfo(false)
    }.run(*args)

    context.getBean(ParkScenarioRunners::class.java).run(args)
}
