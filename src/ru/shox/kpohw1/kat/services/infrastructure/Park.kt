package ru.shox.kpohw1.kat.services.infrastructure

import ru.shox.kpohw1.kat.services.implementations.IInventoryItem

// хранилище единиц на балансе парка
// вся логика (осмотр, фильтры, подсчёты) живёт в сервисах, поэтому Park легко заменить на БД
// экземпляр один на приложение
class Park {

    private val items = LinkedHashMap<String, IInventoryItem>()
    // поставить единицу на баланс
    // бросает IllegalArgumentException, если единица с таким инвентарным номером уже на балансе
    fun add(item: IInventoryItem) {
        require(item.inventoryNumber.isNotBlank()) { "Инвентарный номер не может быть пустым" }
        require(!items.containsKey(item.inventoryNumber)) {
            "Единица с инвентарным номером ${item.inventoryNumber} уже стоит на балансе"
        }
        items[item.inventoryNumber] = item
    }
    // все единицы на балансе в порядке постановки
    fun all(): List<IInventoryItem> = items.values.toList()
    // поиск по инвентарному номеру
    fun findByInventoryNumber(inventoryNumber: String): IInventoryItem? = items[inventoryNumber]
}
