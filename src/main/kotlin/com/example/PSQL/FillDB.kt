package com.example.PSQL

import kotlin.random.Random

class FillDB {
    val names = listOf(
        "Боб",
        "Сэм",
        "Николсон",
        "Никита",
        "Олег",
        "Александр",
        "Анастасия",
        "Владимир",
        "Арсений",
        "Эдуард",
        "Райан",
        "Брайан",
        "Николай",
        "Анатолий"
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

    fun initDB() {
        val random = Random(System.currentTimeMillis())
        for (i in 0 until 10000) {
            val userId = UserDAO.createUser(names[random.nextInt(names.size)])
            val homeId = HomeDAO.createHome(userId)
            val numRooms = random.nextInt(2, 12)
            for (roomNumber in 0 until numRooms) {
                var connectedRooms: List<Int>? = null
                if (roomNumber != 1) {
                    val numConnectedRooms = Random.nextInt(1, roomNumber + 1)
                    connectedRooms = HomeDAO.getRoomsInHome(homeId).shuffled().take(numConnectedRooms).map{it.id}
                }
                val roomId = RoomDAO.createRoom(homeId, connectedRooms)
                val numSensors = Random.nextInt(0, 5)
                for (sensorId in 0 until numSensors) {
                    SensorDAO.addSensorToRoom(
                        SensorDAO.createSensor(
                            sensors[random.nextInt(sensors.size)],
                            random.nextBoolean(),
                            random.nextDouble()
                        ),
                    )
                }
                HomeDAO.addRoomToHome(homeId, roomId)
            }
        }
    }
}