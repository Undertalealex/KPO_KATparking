package ru.shox.kpohw1.kat.models.vehicles

import ru.shox.kpohw1.kat.services.implementations.IEnergyConsumer

// электровелосипед - потребляет энергию, поэтому реализует [IEnergyConsumer] и [Vehicle]
// dailyEnergyKwh среднесуточное потребление, кВт*ч (>= 0)
class ElectricBicycle(
    inventoryNumber: String,
    title: String,
    beginnerScore: Int,
    override val dailyEnergyKwh: Double,
) : Vehicle(inventoryNumber, title, beginnerScore), IEnergyConsumer {

    override val kind: String = "Электровелосипед"
    init {
        require(dailyEnergyKwh >= 0.0) { "Потребление энергии не может быть отрицательным" }
    }
}