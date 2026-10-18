## 1. SOLID: где и как применён каждый принцип
Явно применён принцип инверсии зависимостей (DIP) - он виден в конструкторах и на нём держится подмена зависимостей в тестах. Дальше напишу обоснование по каждому из пяти принципов *("Каждый принцип обоснуйте в README")*.

##### S - Single Responsibility (единственная ответственность)
Каждый тип меняется ровно по одной причине:
- `Park` *(services/infrastructure/Park.kt)* - хранение единиц на балансе (add/all/findByInventoryNumber).
- `ServiceCenter` *(services/ServiceCenter.kt)* - решение техосмотра.
- `BeginnerFriendlyPolicy` *(services/policies/BeginnerFriendlyPolicy.kt)* - правило порога 'подходит новичку'.''
- `ParkService` *(services/ParkService.kt) - реализация сценариев парка (приёмка, учёт).
- `Vehicle`/`Item` *(models/vehicles/* или models/items/*)* - данные и инварианты, без поведения сценариев.
- `ParkScenarioRunners` *(runners/ParkScenarioRunners.kt)* - представление (консоль).
Смена порога для новичков правит *BeginnerFriendlyPolicy*, смена правила осмотра - *ServiceCenter*.

##### O - Open/Closed (открыт для расширения, закрыт для изменения)
Расширение новыми видами техники не требует исправлений в логике:
- *ParkService.totalDailyEnergyKwh()* считает по *filterIsInstance<IEnergyConsumer>()* - новый энергопотребитель учитывается автоматически.
- *ParkService.beginnerFriendlyVehicles()* фильтрует по *filterIsInstance<IVehicle>()*.
- *ServiceCenter.inspect(vehicle: IVehicle)* работает с любым *IVehicle*.

##### L - Liskov Substitution (подстановка)
- *Scooter*, *Bicycle*, *ElectricBicycle* - подтипы *Vehicle : IVehicle*, любой можно передать в *ServiceCenter.inspect(vehicle: IVehicle)* и *ParkService.admitVehicle(vehicle: IVehicle)* без изменения корректности.
- *Helmet*, *ChargeStation* - подтипы *Item : IThing*, любой подходит для *IParkService.registerItem(item: IThing)*.
- База *Vehicle* реализует инварианты(beginnerScore in 1..10, непустые номер/название), поэтому подтипы не сильно изменяют базовый тип.
- Подтипы добавляют возможности(IEnergyConsumer), а не ломают базовое поведение: *Bicycle* и *Scooter* просто не реализуют *IEnergyConsumer*, оставаясь полноценными *IVehicle*.

##### I - Interface Segregation (разделение интерфейсов)
Интерфейсы мелкие и строго по назначению:
- *IInventoryItem* - минимум: *inventoryNumber, title*.
- *IEnergyConsumer* - только *dailyEnergyKwh*, реализуют лишь *ElectricBicycle* и *ChargeStation*.
- *IVehicle* отделён от *IThing*.
- *ITechInspectionService* - единственный метод *inspect*.
Клиенты зависят только от нужного: *ParkService* использует *IVehicle*, *IThing*, *IEnergyConsumer*.

##### D - Dependency Inversion (инверсия зависимостей)
- *ParkService* принимает в конструктор абстракции *ITechInspectionService*, *BeginnerFriendlyPolicy* и *Park*, а не создаёт *ServiceCenter* сам.
- *ITechInspectionService.inspect* возвращает абстракцию *IInspectionResult*, а не конкретный *InspectionResult*.
- 'Раннер' *ParkScenarioRunners* зависит от абстракции *IParkService*.
- Единственное место связывания 'абстракция - реализация' - фабрика *ParkConfig*.




## 2. Какой принцип сознательно ограничен / 'почти нарушен'
DIP ограничен на границе *BeginnerFriendlyPolicy*: это конкретный класс, а не интерфейс, и *ParkService* зависит от конкретного типа политики.

Почему это приемлемо:
- это одно чистое стабильное правило;
- политика всё равно внедряется через конструктор и задаётся порогом, поэтому настраивается и подменяется в тестах без отдельного интерфейса;
- *IBeginnerFriendlyPolicy* был бы ненужной абстракцией (нарушение YAGNI *[You Aren’t Gonna Need It]*) ради принципа.





## 3. Что сломается (или не сломается) при расширении

#### 3.1 Новый вид транспорта - 'Monowheel'
Ничего не должно ломаться. Нужно будет сделать:
- добавить *models/vehicles/MonoWheel.kt : Vehicle(..., IEnergyConsumer)* (если оно потребляет энергию) - по образцу *ElectricBicycle*;
- *ParkService*, *ServiceCenter*, *Park*, *BeginnerFriendlyPolicy* - без изменений;
- опционально: пункт меню в *ParkScenarioRunners* и в демо(слой представления).
*ParkConfig* менять не нужно - новых зависимостей у типа нет.

#### 3.2 Новый сотрудник - 'Employee'
Ломается не прям логика, а нехватка классов в инфраструктуре:
- понятия 'сотрудник' в проекте нет, и *Park* типизирован как *LinkedHashMap<String, IInventoryItem>* - сотрудник на балансе не стоит и туда не влезает по смыслу;
- понадобится модель *Employee* (возможно, *IEmployee*), сервис работы с персоналом(например, назначение сотрудника на транспорт) и отдельное хранилище(*EmployeeRegistry*), либо обобщение *Park*;
Существующие типы (*ServiceCenter*, *ParkService* для транспорта) при этом не меняются - расширение дополняющее, но требует новой подсистемы.

#### 3.3 Новый склад запчастей - 'Warehouse'
Тоже дополнительно нужно будет сделать:
- ввести понятия *SparePart* (как *IThing*/*IInventoryItem*) и сервис склада(возможно, *Warehouse*);
- склад - это не баланс парка, поэтому корректнее завести отдельное хранилище, а не добавлять запчасти в *Park*;
Единственная точка возможной связанности - если запчасти должны попадать в общий отчёт *IParkService.inventory()*: тогда придётся собирать два хранилища.




## 4. Какие тесты фиксируют решения и как подменяется зависимость

#### Решение техосмотра
- *test/.../services/ServiceCenterTest.kt* - само правило: принимает транспорт без энергопотребления, смотрит ровно на границе *2.0* кВт·ч, выкидывает, если сверх лимита с причиной, поддерживает настраиваемый лимит.
- *test/.../services/ParkServiceTest.kt* - что попадание на баланс зависит от результата осмотра и что решение поручается внедрённому сервису(*inspection.inspected*).
- *test/.../config/ParkConfigTest.kt* - что через Spring подменяется именно *ServiceCenter*.

#### Правило для новичков
- *test/.../services/policies/BeginnerFriendlyPolicyTest.kt* - порог 6 и выше подходит, 5 - нет, поддержка пользовательского порога, запрет порога вне 1..10.
- *test/.../services/ParkServiceTest.kt* - фильтрация списка *beginnerFriendlyVehicles()* и учёт сделанной политики (порог 9 - пусто).

#### Как подменяется зависимость
1. Юнит-уровень. В конструктор *ParkService* передаётся *FakeTechInspectionService* (*test/.../support/Fakes.kt*) с готовыми решениями *alwaysAccepted()* / *alwaysRejected(reason)*.
2. Уровень контейнера. *ParkConfigTest.FakeInspectionConfig* объявляет *@Bean @Primary* типа *ITechInspectionService*. Контейнер(*AnnotationConfigApplicationContext* с *ParkConfig*) внедряет заглушку вместо *ServiceCenter*, и *admitVehicle* начинает всегда отклонять.

## 5. Что сделал ИИ, а что изменили вы
ИИ помог переработать код для работы DI контейнера SpringBoot, код писал и изменял(исправлял) я. Также спрашивал у ИИ, какие юнит-тесты стоит сделать, чтобы проверить поведение. Архитектуру придумал сам (все файлы и названия), кроме ParkScenarioRunners (эта идея от ИИ мне показалась хорошей). Сами общие названия пакетов я взял с примеров с семинара и лекций по КПО (мне показалось это правильным и логичным). Также ИИ помог мне понять, какие фишки можно использовать на языке Kotlin для упрощения кода, а также какие функции использовать для создания юнит-тестов. Также предполагал возможное расширение программы, далее будет написано.



## 6*. Идеи расширения

#### 6.1 Новые модели транспорта и вещей
Это уже проверили раньше. Достаточно добавить класс-наследник *Vehicle* или *Item*. *ParkService*, *ServiceCenter*, *Park*, *BeginnerFriendlyPolicy* не меняются, потому что работают через *IVehicle* или *IThing* или *IEnergyConsumer*.

#### 6.2 Разные техцентры и разные их ответы
- *ITechInspectionService* - интерфейс подразумевался для возможного расширения на большее количество техцентров, *ServiceCenter* - лишь одна из реализаций. Новый центр (возможно, *StrictServiceCenter* или *RegionalServiceCenter*) - это новый класс. Если реализаций станет несколько, выбрать нужную можно через стратегию выбора(например, по региону парка).
- *IInspectionResult* - интерфейс подразумевался для возможного расширения для разных техцентров. Причина отказа - обычная строка. Но новый вид
  результата (возможно, *AcceptedWithConditions*) потребует расширить иерархию *InspectionResult*.

#### 6.3 Новые правила
Сделал отдельный пакет под правила: новое правило(например, *PricingPolicy*) добавляется как новый класс. Но *ParkService* сейчас зависит от конкретного *BeginnerFriendlyPolicy*, поэтому:
- добавить *другое* правило рядом довольно просто;
- сделать сам порог для новичков взаимозаменяемой стратегией - потребует ввести *IBeginnerFriendlyPolicy* и заменить тип в конструкторе *ParkService*.

#### 6.4 Новые источники ввода: консоль - XML / JSON
Сейчас ввода-вывода уже введён: *ParkScenarioRunners* принимает *BufferedReader* и *PrintStream*, поэтому источник потока заменить просто. Но формат (меню-команды) зашит в *ParkScenarioRunners*. Чтобы наполнять парк из XML/JSON, нужна новая абстракция-загрузчик, которой пока нет.
