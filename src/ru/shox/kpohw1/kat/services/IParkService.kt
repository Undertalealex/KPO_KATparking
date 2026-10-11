package ru.shox.kpohw1.kat.services

import ru.shox.kpohw1.kat.services.implementations.IInventoryItem
import ru.shox.kpohw1.kat.services.implementations.IThing
import ru.shox.kpohw1.kat.services.implementations.IVehicle
import ru.shox.kpohw1.kat.services.infrastructure.IInspectionResult

// сценарии работы парка: приёмка транспорта, учёт вещей, отчёты
// интерфейс отделён от реализации, чтобы user interface и сценарии
// зависели не от конкретного ParkService
interface IParkService {

    // провести техосмотр и, если он пройден, поставить транспорт на баланс
    fun admitVehicle(vehicle: IVehicle): IInspectionResult
    // поставить вещь на баланс парка
    fun registerItem(item: IThing)
    // суммарное суточное потребление энергии всех единиц на балансе, кВт*ч
    fun totalDailyEnergyKwh(): Double
    // транспорт, подходящий новичку, в порядке постановки на баланс
    fun beginnerFriendlyVehicles(): List<IVehicle>
    // полный инвентарь парка (транспорт+вещи)
    fun inventory(): List<IInventoryItem>
    // общее количество единиц на балансе
    fun unitCount(): Int
}
