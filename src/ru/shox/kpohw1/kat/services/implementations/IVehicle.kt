package ru.shox.kpohw1.kat.services.implementations

// транспортная единица парка: то, что можно выдать в аренду
// наследует IInventoryItem (стоит на балансе) и добавляет оценку
// 'простоты для новичка' - шкалу от 1 до 10
interface IVehicle : IInventoryItem {

    // простота освоения для новичка по шкале от 1 до 10
    // чем выше значение, тем проще техника для нового пользователя
    val beginnerScore: Int
}
