package ru.shox.kpohw1.kat.services

import ru.shox.kpohw1.kat.services.implementations.IEnergyConsumer
import ru.shox.kpohw1.kat.services.implementations.IInventoryItem
import ru.shox.kpohw1.kat.services.implementations.IThing
import ru.shox.kpohw1.kat.services.implementations.ITechInspectionService
import ru.shox.kpohw1.kat.services.implementations.IVehicle
import ru.shox.kpohw1.kat.services.infrastructure.IInspectionResult
import ru.shox.kpohw1.kat.services.infrastructure.Park
import ru.shox.kpohw1.kat.services.policies.BeginnerFriendlyPolicy

// реализация сценариев парка поверх хранилища Park
// класс не создаёт зависимости сам - они приходят через конструктор (внедрение
// зависимостей), поэтому в тестах легко подменяются заглушками
class ParkService(
    private val park: Park,
    private val inspectionService: ITechInspectionService,
    private val beginnerFriendlyPolicy: BeginnerFriendlyPolicy,
) : IParkService {

    override fun admitVehicle(vehicle: IVehicle): IInspectionResult {
        val result = inspectionService.inspect(vehicle)
        if (result.isAccepted) {
            park.add(vehicle)
        }
        return result
    }
    override fun registerItem(item: IThing) {
        park.add(item)
    }
    override fun totalDailyEnergyKwh(): Double =
        park.all()
            .filterIsInstance<IEnergyConsumer>()
            .sumOf { it.dailyEnergyKwh }
    override fun beginnerFriendlyVehicles(): List<IVehicle> =
        park.all()
            .filterIsInstance<IVehicle>()
            .filter { beginnerFriendlyPolicy.isBeginnerFriendly(it) }
    override fun inventory(): List<IInventoryItem> = park.all()
    override fun unitCount(): Int = park.all().size
}
