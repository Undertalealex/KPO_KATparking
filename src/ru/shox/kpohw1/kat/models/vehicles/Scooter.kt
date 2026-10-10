package ru.shox.kpohw1.kat.models.vehicles

// обычный самокат - энергии не потребляет, поэтому наследует только Vehicle
// (электрический транспорт в проекте представлен электровелосипедом)
class Scooter(
    inventoryNumber: String,
    title: String,
    beginnerScore: Int,
) : Vehicle(inventoryNumber, title, beginnerScore) {

    override val kind: String = "Самокат"
}
