package com.example
import PSQL.*
import kotlin.random.Random

val requests = listOf(
    "Включить/Выключить датчик",
    "Узнать состояние датчика",
    "Узнать значение датчика",
    "Получить информацию о датчике",
    "Получить массив комнат в доме",
    "Получить массив датчиков в доме",
)

fun main() {
    PostgresFactory.init()

    val random = Random(System.currentTimeMillis())

    for (i in 0 until 3600){ // 1 час работы
        for (home in HomeDAO.getAllHomes()) {
          // Отправляем по одному запросу в секунду в течение 1 минуты
            val request = requests[random.nextInt(requests.size)]
            var result = ""
            when (request) {
                "Включить/Выключить датчик" -> {
                    val sensors = HomeDAO.getSensorsInHome(home.id)
                    val sensor = sensors[random.nextInt(sensors.size)]
                    val sensorState: Boolean = random.nextBoolean()
                    SensorDAO.updateSensor(sensor.id, sensorState, sensor.value)
                    result = "Состояние датчика ${sensor.id}: ${if (sensorState) "Включен" else "Выключен"}"
                }
                "Узнать состояние датчика" -> {
                    val sensors = HomeDAO.getSensorsInHome(home.id)
                    val sensor = sensors[random.nextInt(sensors.size)]
                    result = "Состояние датчика ${sensor.id}: ${if (sensor.state) "Включен" else "Выключен"}"
                }
                "Узнать значение датчика" -> {
                    val sensors = HomeDAO.getSensorsInHome(home.id)
                    val sensor = sensors[random.nextInt(sensors.size)]
                    result = "Значение датчика ${sensor.id}: ${if (sensor.state) "Включен" else "Выключен"}"
                }
                "Получить массив комнат в доме" -> {
                    result = "Комнаты в доме ${home.id}: ${HomeDAO.getRoomsInHome(home.id)}"
                }
                "Получить массив датчиков в доме" -> {
                    result = "Датчики в доме ${home.id}: ${HomeDAO.getSensorsInHome(home.id)}"
                }
                "Получить информацию о датчике" -> {
                    val sensor = HomeDAO.getSensorsInHome(home.id).random()
                    result = "Датчик ${sensor.name} №${sensor.id} ${if (sensor.state) "Включен" else "Выключен"}, значение ${sensor.state}"
                }
            }
            // print(result)
            // TODO: Отправка result
        }
        Thread.sleep(1000)
    }
}
