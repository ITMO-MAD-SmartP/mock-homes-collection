package com.example
import com.example.PSQL.*
import com.exampleimport.RedisClient
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary.stringToInt
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
data class Info(val sensorId: Int, val type: String, val value: Double, val time: String)

@Serializable
data class SensorRequest(
    @SerialName("SESSION") val session: String,
    @SerialName("HOME_ID") val home_id: Int,
    @SerialName("GADGET_ID") val id: Int,
    @SerialName("GADGET_TYPE") val name: String,
    @SerialName("ACTION") val action: String,
    @SerialName("VALUE") val value: Double,
)

@Serializable
data class HomeRequest(
    val userId: Int,
    val newHomeId: Int
)

val requests = listOf(
    "Включить/Выключить датчик",
    "Узнать состояние датчика",
    "Узнать значение датчика",
    "Получить информацию о датчике",
    "Получить массив комнат в доме",
    "Получить массив датчиков в доме",
)

fun sendInfo(sensor: Sensor){
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedTime = currentTime.toJavaLocalDateTime().format(formatter)
    RedisClient.pushToQueue("queue:tlog", Json.encodeToString(Info(sensor.id, sensor.name, sensor.value, formattedTime)))
}

fun changeInfo(sensor: Sensor){
    val random = Random(System.currentTimeMillis())
    when (sensor!!.name){
        "t" -> {
            SensorDAO.updateSensor(sensor.id, sensor.state, sensor.value * 0.9 + (random.nextDouble(-20.0, 40.0) - sensor.value) * 0.1)
        }
        "w" -> {
            SensorDAO.updateSensor(sensor.id, sensor.state, sensor.value * 0.9 + (random.nextDouble(0.0, 100.0) - sensor.value) * 0.1)
        }
    }
}

fun main() {
    PostgresFactory.init()
    FillDB.initDB()

    val random = Random(System.currentTimeMillis())

    for (i in 0 until 3600){ // 1 час работы
        if (i % 5 == 0){
            for (home in UserDAO.getHomesForUser(UserDAO.getUserByName("Алексей Коротков")[0].id)){
                for (sensor in HomeDAO.getSensorsInHome(home.id)) {
                    changeInfo(sensor)
                    sendInfo(sensor)
                }
            }
        }
        var requestJson = RedisClient.popFromQueue("changeGadgetState")
        while( requestJson != null){
            val sensorRequest = Json.decodeFromString<SensorRequest>(requestJson)
            when(sensorRequest.action){
                "SWITCH_ON" -> {
                    SensorDAO.updateSensor(sensorRequest.id, true, SensorDAO.getSensor(sensorRequest.id)!!.value)
                }
                "SWITCH_OFF" -> {
                    SensorDAO.updateSensor(sensorRequest.id, false, SensorDAO.getSensor(sensorRequest.id)!!.value)
                }
                "CHANGE_LIGHT_LEVEL" -> {
                    SensorDAO.updateSensor(sensorRequest.id, SensorDAO.getSensor(sensorRequest.id)!!.state, sensorRequest.value)
                }
            }
            requestJson = RedisClient.popFromQueue("changeGadgetState")
        }

        requestJson = RedisClient.popFromQueue("changeHomeOwnership")
        while( requestJson != null){
            val homeRequest = Json.decodeFromString<HomeRequest>(requestJson)

            requestJson = RedisClient.popFromQueue("changeHomeOwnership")
        }
        /*for (home in HomeDAO.getAllHomes()) {
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
        }*/
        Thread.sleep(1000)
    }
}
