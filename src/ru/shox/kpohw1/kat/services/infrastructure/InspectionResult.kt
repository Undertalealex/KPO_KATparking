package ru.shox.kpohw1.kat.services.infrastructure

// реализация IInspectionResult
// либо Accepted, либо Rejected с причиной
// новых вариантов результата не может появиться
sealed interface InspectionResult : IInspectionResult {

    // осмотр пройден
    data object Accepted : InspectionResult {
        override val isAccepted: Boolean = true
        override val reason: String? = null
    }
    // осмотр не пройден, указана причина
    data class Rejected(override val reason: String) : InspectionResult {
        override val isAccepted: Boolean = false
    }
}
