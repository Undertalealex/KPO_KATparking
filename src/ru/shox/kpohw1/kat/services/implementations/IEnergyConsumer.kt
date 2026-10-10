package ru.shox.kpohw1.kat.services.implementations

// устройство, которое потребляет электроэнергию - реализуется только теми
// единицами техники, которым это свойственно (электровелосипед, зарядная станция)
interface IEnergyConsumer {

    // среднесуточное потребление энергии, кВт*ч
    val dailyEnergyKwh: Double
}
