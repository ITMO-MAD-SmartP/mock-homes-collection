package com.example.PSQL

import kotlin.random.Random

object FillDB {
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
        "Анатолий",
        "Виталий",
        "Евгения",
        "Евгений",
        "Иосиф",
        "Борис",
        "Кристофор",
        "Кристина",
        "Александра",
        "Анжелика"
    )

    val secondNames = listOf(
        "Алентьев",
        "Брайан",
        "Симсон",
        "Кудлай",
        "Платонов",
        "Павлов",
        "Девяткин",
        "Восьмеркин",
        "Семеркин",
        "Иванов",
        "Васильев",
        "Лобанов",
        "Быков",
        "Королев",
        "Авксентьев",
        "Романов",
        "Клименков",
        "Борисов",
        "Юсупов",
        "Андреев",
        "Щербаков",
        "Шептунов",
        "Шевченко",
        "Путин"
    )

    val sensors = listOf("t", "w", "l")

    fun initDB() {
        for (i in 0 until names.size){
            for (j in 0 until secondNames.size) {
                UserDAO.createUser(names[i] + " " + secondNames[j])
            }
        }
        val random = Random(System.currentTimeMillis())
        for (i in 0 until 10000) {
            val userId = UserDAO.getAllUsers()[random.nextInt(names.size)].id
            val homeId = HomeDAO.createHome(userId)
            OwnedHomesDAO.addOwnerToHome(userId, homeId)
            val numRooms = random.nextInt(0, 4) + 2
            for (roomNumber: Int in 0 until numRooms) {
                var connectedRooms: List<Int>? = null
                if (roomNumber != 1) {
                    val numConnectedRooms = Random.nextInt(1, roomNumber + 2)
                    connectedRooms = HomeDAO.getRoomsInHome(homeId).shuffled().take(numConnectedRooms).map{it.id}
                }
                val roomId = RoomDAO.createRoom(homeId, connectedRooms)
                for (sensorId: Int in 0 until 3) {
                    SensorDAO.addSensorToRoom(
                        SensorDAO.createSensor(
                            sensors[sensorId],
                            random.nextBoolean(),
                            if (sensorId == 2) random.nextDouble(-20.0, 40.0) else random.nextDouble(0.0, 100.0)
                        ), roomId
                    )
                }
                HomeDAO.addRoomToHome(homeId, roomId)
            }
        }

        val userId = UserDAO.createUser("Алексей Коротков")
        for (i in 0 until 2){
            val homeId = HomeDAO.createHome(userId)
            OwnedHomesDAO.addOwnerToHome(userId, homeId)
            val numRooms = 6
            for (roomNumber: Int in 0 until numRooms) {
                var connectedRooms: List<Int>? = null
                if (roomNumber != 1) {
                    val numConnectedRooms = Random.nextInt(1, roomNumber + 1)
                    connectedRooms = HomeDAO.getRoomsInHome(homeId).shuffled().take(numConnectedRooms).map { it.id }
                }
                val roomId = RoomDAO.createRoom(homeId, connectedRooms)
                for (sensorId: Int in 0 until 3) {
                    SensorDAO.addSensorToRoom(
                        SensorDAO.createSensor(
                            sensors[sensorId],
                            random.nextBoolean(),
                            if (sensorId == 2) random.nextDouble(-20.0, 40.0) else random.nextDouble(0.0, 100.0)
                        ), roomId
                    )
                }
                HomeDAO.addRoomToHome(homeId, roomId)
            }
        }

    }
}