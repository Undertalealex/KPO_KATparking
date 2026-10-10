package ru.shox.kpohw1.kat.models.vehicles

import ru.shox.kpohw1.kat.services.implementations.IVehicle

// общая часть всех транспортных единиц: инвентарный номер, название и оценка простоты для новичка
// базовый класс избавляет наследников от дублирования и централизует проверку инвариантов
abstract class Vehicle(
    final override val inventoryNumber: String,
    final override val title: String,
    final override val beginnerScore: Int,
) : IVehicle {

    init {
        require(inventoryNumber.isNotBlank()) { "Инвентарный номер не может быть пустым" }
        require(title.isNotBlank()) { "Название не может быть пустым" }
        require(beginnerScore in SCORE_RANGE) {
            "Простота для новичка должна быть в диапазоне $SCORE_RANGE, получено $beginnerScore"
        }
    }
    // описание вида транспорта — используется в выводе сценариев
    abstract val kind: String
    override fun toString(): String = "$kind '$title' ($inventoryNumber)"

    companion object {
        // Допустимый диапазон оценки простоты для новичка
        val SCORE_RANGE: IntRange = 1..10
    }
}
