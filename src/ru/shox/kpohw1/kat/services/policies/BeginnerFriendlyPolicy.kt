package ru.shox.kpohw1.kat.services.policies

import ru.shox.kpohw1.kat.services.implementations.IVehicle

// политика 'подходит ли транспорт новичку"
// единственное место, где зашит порог; вынесено из ParkService, чтобы правило
// можно было менять и тестировать отдельно, не трогая сервис парка
class BeginnerFriendlyPolicy(
    private val threshold: Int = DEFAULT_THRESHOLD,
) {
    init {
        require(threshold in VehicleScoreRange) {
            "Порог должен быть в диапазоне $VehicleScoreRange, получено $threshold"
        }
    }
    // true - транспорт подходит новичку
    fun isBeginnerFriendly(vehicle: IVehicle): Boolean = vehicle.beginnerScore >= threshold
    // порог, с которым создана политика (для вывода в сценариях)
    fun threshold(): Int = threshold

    companion object {
        // по умолчанию новичку подходит транспорт с оценкой 6 и выше
        const val DEFAULT_THRESHOLD: Int = 6
        // диапазон от 1 до 10 удобным способом :)
        private val VehicleScoreRange = 1..10
    }
}
