package ru.shox.kpohw1.kat.models.items

import ru.shox.kpohw1.kat.services.implementations.IEnergyConsumer

// зарядная станция - потребляет энергию, поэтому реализует [IEnergyConsumer]
// dailyEnergyKwh среднесуточное потребление, кВт*ч (>= 0)
class ChargeStation(
    inventoryNumber: String,
    title: String,
    override val dailyEnergyKwh: Double,
    val ports: Int,
) : Item(inventoryNumber, title), IEnergyConsumer {

    init {
        require(dailyEnergyKwh >= 0.0) { "Потребление энергии не может быть отрицательным" }
        require(ports > 0) { "Количество портов должно быть положительным" }
    }
    override fun toString(): String = super.toString() + ", портов $ports"
}
