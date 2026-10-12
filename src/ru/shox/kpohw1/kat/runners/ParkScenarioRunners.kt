package ru.shox.kpohw1.kat.runners

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import ru.shox.kpohw1.kat.models.items.ChargeStation
import ru.shox.kpohw1.kat.models.items.Helmet
import ru.shox.kpohw1.kat.models.vehicles.Bicycle
import ru.shox.kpohw1.kat.models.vehicles.ElectricBicycle
import ru.shox.kpohw1.kat.models.vehicles.Scooter
import ru.shox.kpohw1.kat.models.vehicles.Vehicle
import ru.shox.kpohw1.kat.services.IParkService
import ru.shox.kpohw1.kat.services.implementations.IInventoryItem
import ru.shox.kpohw1.kat.services.infrastructure.IInspectionResult

// сценарии работы с парком: готовый демонстрационный прогон и интерактивное меню
// ввод/вывод вынесены в параметры конструктора, поэтому сценарии проверяются
// юнит-тестами без реальной консоли
class ParkScenarioRunners(
    private val parkService: IParkService,
    private val input: BufferedReader = BufferedReader(InputStreamReader(System.`in`, Charsets.UTF_8)),
    private val out: PrintStream = System.out,
) {

    // точка входа сценариев: --demo запускает прогон, иначе - меню
    fun run(args: Array<String>) {
        if (args.contains(DEMO_FLAG)) runDemo() else runInteractive()
    }
    // демонстрация всех сценариев без ручного ввода
    fun runDemo() {
        out.println("=== Демонстрационный сценарий ===")
        out.println()

        out.println("1. Приёмка транспорта на баланс:")
        val vehicles: List<Vehicle> = listOf(
            Scooter("S-001", "Городской самокат", 8),
            Scooter("S-002", "Спортивный самокат", 5),
            Bicycle("B-001", "Городской велосипед", 9),
            ElectricBicycle("E-001", "Электровелосипед", 7, 1.2),
            ElectricBicycle("E-002", "Грузовой электровелосипед", 4, 3.5),
        )
        vehicles.forEach { vehicle ->
            val result = parkService.admitVehicle(vehicle)
            out.println("  + $vehicle — ${describe(result)}")
        }
        out.println()

        out.println("2. Постановка вещей на баланс:")
        val things = listOf(
            Helmet("H-001", "Шлем городской", "M"),
            Helmet("H-002", "Шлем спортивный", "L"),
            ChargeStation("C-001", "Зарядная станция", 0.6, 4),
        )
        things.forEach { item ->
            parkService.registerItem(item)
            out.println("  + $item — Поставлено на баланс")
        }
        out.println()

        out.println("3. Инвентарь парка:")
        printInventory()
        out.println()

        out.println("4. Суммарное суточное потребление энергии: ${format(parkService.totalDailyEnergyKwh())} кВт*ч")
        out.println()

        out.println("5. Транспорт, подходящий новичкам:")
        printBeginnerFriendly()
    }
    // интерактивное меню
    fun runInteractive() {
        out.println("=== Интерактивный режим ===")
        printMenu()
        while (true) {
            out.print("> ")
            out.flush()
            val command = input.readLine()?.trim() ?: return
            when (command) {
                "1" -> addScooter()
                "2" -> addBicycle()
                "3" -> addElectricBicycle()
                "4" -> addHelmet()
                "5" -> addChargeStation()
                "6" -> printInventory()
                "7" -> out.println("Суммарное суточное потребление энергии: ${format(parkService.totalDailyEnergyKwh())} кВт*ч")
                "8" -> printBeginnerFriendly()
                "0", "" -> {
                    out.println("До свидания!")
                    return
                }
                else -> out.println("Неизвестная команда: $command")
            }
            out.println()
        }
    }
    private fun printMenu() {
        out.println(
            """
            Меню:
              1 — принять самокат
              2 — принять велосипед
              3 — принять электровелосипед
              4 — добавить шлем
              5 — добавить зарядную станцию
              6 — показать инвентарь
              7 — показать суммарное потребление
              8 — показать транспорт для новичков
              0 — выход
            """.trimIndent(),
        )
        out.println()
    }
    private fun addScooter() = guarded {
        parkService.admitVehicle(
            Scooter(
                readText("Инвентарный номер"),
                readText("Название"),
                readInt("Простота для новичка (1-10)"),
            ),
        ).also { out.println(describe(it)) }
    }
    private fun addBicycle() = guarded {
        parkService.admitVehicle(
            Bicycle(
                readText("Инвентарный номер"),
                readText("Название"),
                readInt("Простота для новичка (1-10)"),
            ),
        ).also { out.println(describe(it)) }
    }
    private fun addElectricBicycle() = guarded {
        parkService.admitVehicle(
            ElectricBicycle(
                readText("Инвентарный номер"),
                readText("Название"),
                readInt("Простота для новичка (1-10)"),
                readDouble("Потребление, кВт*ч"),
            ),
        ).also { out.println(describe(it)) }
    }
    private fun addHelmet() = guarded {
        val item = Helmet(readText("Инвентарный номер"), readText("Название"), readText("Размер"))
        parkService.registerItem(item)
        out.println("$item — Поставлено на баланс")
    }
    private fun addChargeStation() = guarded {
        val item = ChargeStation(
            readText("Инвентарный номер"),
            readText("Название"),
            readDouble("Потребление, кВт*ч"),
            readInt("Количество портов"),
        )
        parkService.registerItem(item)
        out.println("$item — Поставлено на баланс")
    }
    private fun printInventory() {
        val items = parkService.inventory()
        if (items.isEmpty()) {
            out.println("  (пусто)")
            return
        }
        items.forEach { out.println("  • ${describeItem(it)}") }
        out.println("  Всего единиц на балансе: ${parkService.unitCount()}")
    }
    private fun printBeginnerFriendly() {
        val vehicles = parkService.beginnerFriendlyVehicles()
        if (vehicles.isEmpty()) {
            out.println("  (нет подходящего транспорта)")
            return
        }
        vehicles.forEach { out.println("  • $it") }
    }

    private fun describeItem(item: IInventoryItem): String =
        if (item is Vehicle) "$item [транспорт]" else "$item [вещь]"

    private fun describe(result: IInspectionResult): String =
        if (result.isAccepted) "Принято" else "Отклонено: ${result.reason}"

    private inline fun guarded(block: () -> Unit) {
        try {
            block()
        } catch (e: IllegalArgumentException) {
            out.println("Ошибка ввода: ${e.message}")
        }
    }

    private fun readText(label: String): String {
        val line = readLineOrThrow(label)
        require(line.isNotBlank()) { "$label не может быть пустым" }
        return line
    }
    private fun readInt(label: String): Int =
        readText(label).toIntOrNull() ?: throw IllegalArgumentException("$label должно быть целым числом")
    private fun readDouble(label: String): Double =
        readText(label).replace(',', '.').toDoubleOrNull()
            ?: throw IllegalArgumentException("$label должно быть числом")
    private fun readLineOrThrow(label: String): String {
        out.print("$label: ")
        out.flush()
        return input.readLine()?.trim() ?: throw IllegalArgumentException("нет данных для '$label'")
    }
    private fun format(value: Double): String = "%.2f".format(java.util.Locale.ROOT, value)

    companion object {
        // флаг запуска демонстрационного сценария
        const val DEMO_FLAG: String = "--demo"
    }
}
