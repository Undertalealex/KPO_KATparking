package ru.shox.kpohw1.kat.models.items

// защитный шлем - энергию не потребляет, поэтому реализует только [Item]
class Helmet(
    inventoryNumber: String,
    title: String,
    val size: String,
) : Item(inventoryNumber, title) {

    init {
        require(size.isNotBlank()) { "Размер шлема не может быть пустым" }
    }
    override fun toString(): String = super.toString() + ", размер $size"
}