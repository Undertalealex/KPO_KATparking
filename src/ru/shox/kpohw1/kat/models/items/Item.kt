package ru.shox.kpohw1.kat.models.items

import ru.shox.kpohw1.kat.services.implementations.IThing

// общая часть всех вещей: инвентарный номер и название
abstract class Item(
    final override val inventoryNumber: String,
    final override val title: String,
) : IThing {

    init {
        require(inventoryNumber.isNotBlank()) { "Инвентарный номер не может быть пустым" }
        require(title.isNotBlank()) { "Название не может быть пустым" }
    }
    override fun toString(): String = "$title ($inventoryNumber)"
}
