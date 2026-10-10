package ru.shox.kpohw1.kat.services.implementations

import ru.shox.kpohw1.kat.services.infrastructure.IInspectionResult

// сервис технического осмотра: решает, можно ли принять единицу транспорта на баланс парка
// выделен в отдельный интерфейс, чтобы решение о приёмке можно было подменять
// (в тестах - на предсказуемую заглушку) и в случае расширения делать разный техосмотр
// для разного транспорта
interface ITechInspectionService {

    // провести техосмотр и вернуть результат: принято или отклонено (с причиной)
    fun inspect(vehicle: IVehicle): IInspectionResult
}
