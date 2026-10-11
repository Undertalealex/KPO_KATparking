package ru.shox.kpohw1.kat.services

import ru.shox.kpohw1.kat.services.implementations.IEnergyConsumer
import ru.shox.kpohw1.kat.services.implementations.ITechInspectionService
import ru.shox.kpohw1.kat.services.implementations.IVehicle
import ru.shox.kpohw1.kat.services.infrastructure.IInspectionResult
import ru.shox.kpohw1.kat.services.infrastructure.InspectionResult

// служба технического осмотра парка
// правило приёмки: транспорт, потребляющий энергию, принимается, если его суточное
// потребление не превышает maxDailyEnergyKwh (транспорт без энергопотребления принимается всегда)
class ServiceCenter(
    private val maxDailyEnergyKwh: Double = DEFAULT_MAX_DAILY_ENERGY_KWH,
) : ITechInspectionService {

    init {
        require(maxDailyEnergyKwh >= 0.0) { "Порог потребления не может быть отрицательным" }
    }
    override fun inspect(vehicle: IVehicle): IInspectionResult {
        val energy = (vehicle as? IEnergyConsumer)?.dailyEnergyKwh
            ?: return InspectionResult.Accepted
        return if (energy <= maxDailyEnergyKwh) {
            InspectionResult.Accepted
        } else {
            InspectionResult.Rejected(
                "Суточное потребление ${format(energy)} кВт*ч превышает допустимые ${format(maxDailyEnergyKwh)} кВт*ч",
            )
        }
    }
    private fun format(value: Double): String = "%.2f".format(java.util.Locale.ROOT, value)

    companion object {
        // по умолчанию допускается не более 2.0 кВт*ч в сутки
        const val DEFAULT_MAX_DAILY_ENERGY_KWH: Double = 2.0
    }
}
