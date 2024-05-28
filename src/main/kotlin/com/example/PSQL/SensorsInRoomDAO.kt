package com.example.PSQL

import org.jetbrains.exposed.sql.*

object SensorsInRoomDAO {
    fun addSensorToRoom(sensorId: Int, roomId: Int) = PostgresFactory.dbQuery {
        SensorsInRoom.insert {
            it[SensorsInRoom.sensorId] = sensorId
            it[SensorsInRoom.roomId] = roomId
        }
    }

    fun getSensorsInRoom(roomId: Int): List<Int> = PostgresFactory.dbQuery {
        SensorsInRoom.select { SensorsInRoom.roomId eq roomId }
            .map { it[SensorsInRoom.sensorId] }
    }
}
