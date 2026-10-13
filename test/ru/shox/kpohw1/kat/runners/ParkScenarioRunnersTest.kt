package ru.shox.kpohw1.kat.runners

import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.io.StringReader
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.shox.kpohw1.kat.services.ParkService
import ru.shox.kpohw1.kat.services.ServiceCenter
import ru.shox.kpohw1.kat.services.infrastructure.Park
import ru.shox.kpohw1.kat.services.policies.BeginnerFriendlyPolicy

class ParkScenarioRunnersTest {

    private class Harness(val runners: ParkScenarioRunners, val out: ByteArrayOutputStream) {
        fun text(): String = out.toString(Charsets.UTF_8.name())
    }

    private fun harness(input: String): Harness {
        val out = ByteArrayOutputStream()
        val parkService = ParkService(Park(), ServiceCenter(), BeginnerFriendlyPolicy())
        val runners = ParkScenarioRunners(
            parkService,
            BufferedReader(StringReader(input)),
            PrintStream(out, true, Charsets.UTF_8),
        )
        return Harness(runners, out)
    }

    @Test
    fun `демо показывает приёмку, склад, энергию и транспорт для новичков`() {
        val harness = harness("")

        harness.runners.runDemo()

        val text = harness.text()
        assertTrue(text.contains("Принято"), text)
        assertTrue(text.contains("Отклонено"), text)
        assertTrue(text.contains("Поставлено на баланс"), text)
        assertTrue(text.contains("Всего единиц на балансе: 7"), text)
        assertTrue(text.contains("Суммарное суточное потребление энергии: 1.80 кВт*ч"), text)
        assertTrue(text.contains("S-001"), text)
        assertTrue(text.contains("B-001"), text)
    }

    @Test
    fun `флаг --demo запускает демонстрационный сценарий`() {
        val harness = harness("")

        harness.runners.run(arrayOf(ParkScenarioRunners.DEMO_FLAG))

        assertTrue(harness.text().contains("Демонстрационный сценарий"), harness.text())
    }

    @Test
    fun `без флага запускается интерактивное меню`() {
        val harness = harness("0\n")

        harness.runners.run(emptyArray())

        assertTrue(harness.text().contains("Интерактивный режим"), harness.text())
    }

    @Test
    fun `интерактив сообщает о неизвестной команде и завершается`() {
        val harness = harness("9\n0\n")

        harness.runners.runInteractive()

        val text = harness.text()
        assertTrue(text.contains("Неизвестная команда: 9"), text)
        assertTrue(text.contains("До свидания!"), text)
    }

    @Test
    fun `интерактив принимает самокат и печатает результат осмотра`() {
        val harness = harness("1\nS-100\nМой самокат\n7\n0\n")

        harness.runners.runInteractive()

        assertTrue(harness.text().contains("Принято"), harness.text())
    }

    @Test
    fun `интерактив сообщает об ошибке ввода на пустом поле`() {
        val harness = harness("1\n\n0\n")

        harness.runners.runInteractive()

        assertTrue(harness.text().contains("Ошибка ввода"), harness.text())
    }

    @Test
    fun `интерактив показывает суммарное потребление по команде`() {
        val harness = harness("3\nE-100\nЭлектровелосипед\n7\n1.5\n7\n0\n")

        harness.runners.runInteractive()

        assertTrue(harness.text().contains("Суммарное суточное потребление энергии: 1.50 кВт*ч"), harness.text())
    }
}
