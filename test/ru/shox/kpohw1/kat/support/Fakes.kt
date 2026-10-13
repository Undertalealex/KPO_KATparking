package ru.shox.kpohw1.kat.support

import ru.shox.kpohw1.kat.models.items.ChargeStation
import ru.shox.kpohw1.kat.models.items.Helmet
import ru.shox.kpohw1.kat.models.vehicles.Bicycle
import ru.shox.kpohw1.kat.models.vehicles.ElectricBicycle
import ru.shox.kpohw1.kat.models.vehicles.Scooter
import ru.shox.kpohw1.kat.services.implementations.ITechInspectionService
import ru.shox.kpohw1.kat.services.implementations.IVehicle
import ru.shox.kpohw1.kat.services.infrastructure.IInspectionResult
import ru.shox.kpohw1.kat.services.infrastructure.InspectionResult

// заглушка службы техосмотра с предсказуемым решением
// подставляется вместо ServiceCenter в тестах, чтобы проверять логику ParkService
// независимо от правил реального осмотра
class FakeTechInspectionService(
    private val decision: (IVehicle) -> IInspectionResult,
) : ITechInspectionService {

    // все единицы, которые побывали на осмотре
    val inspected: MutableList<IVehicle> = mutableListOf()

    override fun inspect(vehicle: IVehicle): IInspectionResult {
        inspected += vehicle
        return decision(vehicle)
    }

    companion object {
        // всегда принимает
        fun alwaysAccepted(): FakeTechInspectionService =
            FakeTechInspectionService { InspectionResult.Accepted }

        // всегда отклоняет с указанной причиной
        fun alwaysRejected(reason: String = "отказ по заглушке"): FakeTechInspectionService =
            FakeTechInspectionService { InspectionResult.Rejected(reason) }
    }
}

fun scooter(
    inventoryNumber: String = "S-1",
    title: String = "Самокат",
    beginnerScore: Int = 8,
): Scooter = Scooter(inventoryNumber, title, beginnerScore)

fun bicycle(
    inventoryNumber: String = "B-1",
    title: String = "Велосипед",
    beginnerScore: Int = 8,
): Bicycle = Bicycle(inventoryNumber, title, beginnerScore)

fun electricBicycle(
    inventoryNumber: String = "E-1",
    title: String = "Электровелосипед",
    beginnerScore: Int = 8,
    dailyEnergyKwh: Double = 0.5,
): ElectricBicycle = ElectricBicycle(inventoryNumber, title, beginnerScore, dailyEnergyKwh)

fun helmet(
    inventoryNumber: String = "H-1",
    title: String = "Шлем",
    size: String = "M",
): Helmet = Helmet(inventoryNumber, title, size)

fun chargeStation(
    inventoryNumber: String = "C-1",
    title: String = "Зарядная станция",
    dailyEnergyKwh: Double = 0.5,
    ports: Int = 4,
): ChargeStation = ChargeStation(inventoryNumber, title, dailyEnergyKwh, ports)
