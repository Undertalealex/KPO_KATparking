package ru.shox.kpohw1.kat.models.vehicles

// обычный велосипед - энергии не потребляет, поэтому реализует [Vehicle]
class Bicycle(
    inventoryNumber: String,
    title: String,
    beginnerScore: Int,
) : Vehicle(inventoryNumber, title, beginnerScore) {

    override val kind: String = "Велосипед"
}