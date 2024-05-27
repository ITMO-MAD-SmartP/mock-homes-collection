import kotlin.random.Random
import java.util.*

class Sensor(val name: String, val sensorId: Int, val roomId: Int, var value: Int, var isOn: Boolean) {
    fun getInfo(): String {
        return "Датчик $name (ID: $sensorId) в комнате $roomId: Значение - $value, Состояние - ${if (isOn) "Включен" else "Выключен"}"
    }

    fun getState(): Boolean{
        return isOn
    }

    fun getVal(): Int{
        return value
    }

    fun changeState(newState: Boolean): Boolean {
        isOn = newState
        return isOn
    }

    fun equals(sensorId: Int): Boolean {
        return this.sensorId == sensorId
    }

    override fun hashCode(): Int {
        return sensorId
    }
}

class Room(val roomId: Int, val houseId: Int) {
    val sensors = mutableListOf<Int>()
    val connectedRooms = mutableListOf<Int>()

    fun addSensor(sensorId: Int): String {
        if (sensorId !in sensors) {
            sensors.add(sensorId)
            return "Успех"
        } else {
            return "Ошибка: Датчик с ID $sensorId уже существует в комнате $roomId"
        }
    }

    fun addConnectedRoom(roomId: Int) {
        if (roomId !in connectedRooms) {
            connectedRooms.add(roomId)
        }
    }

    fun equals(roomId: Int): Boolean {
        return this.roomId == roomId
    }

    override fun hashCode(): Int {
        return roomId
    }

}

class House(val Id: Int, val numRooms: Int, val ownerId: Int) {
    val houseId = Id
    val rooms = mutableListOf<Room>()
    val sensors = mutableListOf<Sensor>()

    fun addSensor(sensorId: Int, sensorName: String, roomId: Int): String {
        val room = rooms.find { it.equals(roomId) }
        if (room != null) {
            val sensor = Sensor(sensorName, sensorId, roomId, 0, false)
            sensors.add(sensor)
            room.addSensor(sensorId)
            return "Успех"
        } else {
            return "Ошибка: Комнаты с ID $roomId не существует в доме $houseId"
        }
    }

    fun getSensor(sensorId: Int): Sensor? {
        return sensors.find { it.equals(sensorId) }
    }

    fun removeSensor(sensorId: Int): String {
        var sensorIndex = sensors.indexOf(getSensor(sensorId))
        if (sensorIndex != -1) {
            sensors.removeAt(sensorIndex)
            return "Успех"
        } else {
            return "Ошибка: Датчика с ID $sensorId нет в доме $houseId"
        }
    }

    fun getRoom(roomId:Int): Room? {
        return rooms.find { it.equals(roomId) }
    }

    fun addRoom(roomId: Int, connectedRooms: List<Int>) {
        val room = Room(roomId, this.houseId)
        rooms.add(room)
        for (connectedRoomId in connectedRooms) {
            val connectedRoom = rooms.find { it.roomId == connectedRoomId }
            connectedRoom?.addConnectedRoom(roomId)
        }
    }

    fun removeRoom(roomId: Int): String {
        var roomIndex = rooms.indexOf(getRoom(roomId))
        if (roomIndex != -1) {
            sensors.removeAt(roomIndex)
            return "Успех"
        } else {
            return "Ошибка: Комнты с ID $roomId нет в доме $houseId"
        }
    }

    fun getRoomList(): List<Room> {
        return rooms
    }

    fun getSensorList(): List<Sensor> {
        return sensors
    }

    fun getSensorState(sensorId: Int): Boolean {
        return getSensor(sensorId)!!.getState()
    }

    fun getSensorValue(sensorId: Int): Int {
        return getSensor(sensorId)!!.getVal()
    }

    fun toggleSensorState(sensorId: Int, sensorState: Boolean): Boolean {
        return getSensor(sensorId)!!.changeState(sensorState)
    }
}

fun main() {


    val requests = listOf(
        "Включить/Выключить датчик",
        "Узнать состояние датчика",
        "Узнать значение датчика",
        "Получить информацию о датчике",
        "Получить массив комнат в доме",
        "Получить массив датчиков в доме",
        "Удалить датчик",
        "Добавить датчик"
    )

    val sensors = listOf(
        "Датчик температуры",
        "Датчик света",
        "Датчик уровня воды",
        "Выключатель света",
        "Дверной замок",
        "Кондиционер",
        "Телевизор",
        "Холодильник"
    )


    val random = Random(System.currentTimeMillis())
    val homes = mutableListOf<House>()
    for (i in 0 until 10000) {
        val house = House(i, Random.nextInt(1, 6), Random.nextInt(1, 101))
        for (roomId in 1 until house.numRooms + 1){
            var room = Room(roomId, house.houseId)
            var numSensors = Random.nextInt(1, 4)
            for (sensorId in 1 until numSensors + 1){
                house.addSensor(sensorId, sensors[random.nextInt(sensors.size)], roomId)
            }
            var connectedRooms = listOf<Int>()
            if (roomId != 1){
                val numConnectedRooms = Random.nextInt(1, roomId)
                connectedRooms = List(numConnectedRooms) { random.nextInt(roomId - 1) + 1 }
            }

            house.addRoom(roomId, connectedRooms)
        }

        homes.add(house)
    }


    for (i in 0 until 60){
        for (home in homes) {
          // Отправляем по одному запросу в секунду в течение 1 минуты
            val request = requests[random.nextInt(requests.size)]
            var result = ""
            when (request) {
                "Включить/Выключить датчик" -> {
                    val sensorId = home.getSensorList().random().sensorId
                    val sensorState: Boolean = Math.random() > 0.5f
                    result = if (home.toggleSensorState(sensorId, sensorState)) "Включен" else "Выключен"
                }
                "Узнать состояние датчика" -> {
                    val sensorId = home.getSensorList().random().sensorId
                    val state = home.getSensorState(sensorId)
                    result = "Состояние датчика $sensorId: ${if (state) "Включен" else "Выключен"}"
                }
                "Узнать значение датчика" -> {
                    val sensorId = home.getSensorList().random().sensorId
                    val value = home.getSensorValue(sensorId)
                    result = "Значение датчика $sensorId: $value"
                }
                "Получить массив комнат в доме" -> {
                    val rooms = home.getRoomList()
                    result = "Комнаты в доме ${home.houseId}: $rooms"
                }
                "Получить массив датчиков в доме" -> {
                    val sensors = home.getSensorList()
                    result = "Датчики в доме ${home.houseId}: $sensors"
                }
                "Удалить датчик" -> {
                    val sensorId = home.getSensorList().random().sensorId
                    result = home.removeSensor(sensorId)
                }
                "Добавить датчик" -> {
                    val sensorID = Random.nextInt(1, 500)
                    val sensorName = sensors[random.nextInt(sensors.size)]
                    val roomId = Random.nextInt(1, 101)
                    result = home.addSensor(sensorID, sensorName, roomId)
                }
                "Получить информацию о датчике" -> {
                    result = home.getSensorList().random().getInfo()
                }
            }
            // print(result)
            // TODO: Отправка result
        }
        Thread.sleep(1000)
    }
}
