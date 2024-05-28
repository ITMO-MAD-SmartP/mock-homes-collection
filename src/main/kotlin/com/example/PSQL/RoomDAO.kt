package com.example.PSQL

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

data class Room(val id: Int, val homeId: Int)

object RoomDAO {
    fun createRoom(homeId: Int, connectedRoomIds: List<Int>? = null): Int = PostgresFactory.dbQuery {
        val roomId = Rooms.insert {
            it[Rooms.homeId] = homeId
        } get Rooms.id

        connectedRoomIds?.forEach { connectedRoomId ->
            ConnectedRoomsDAO.connectRooms(roomId, connectedRoomId)
        }

        roomId
    }

    fun getRoom(id: Int): Room? = PostgresFactory.dbQuery {
        Rooms.select { Rooms.id eq id }
            .map { Room(it[Rooms.id], it[Rooms.homeId]) }
            .singleOrNull()
    }

    fun getAllRooms(): List<Room> = PostgresFactory.dbQuery {
        Rooms.selectAll().map { Room(it[Rooms.id], it[Rooms.homeId]) }
    }

    fun getSensorsInRoom(roomId: Int): List<Sensor> = PostgresFactory.dbQuery {
        (SensorsInRoom innerJoin Sensors).select { SensorsInRoom.roomId eq roomId }
            .map { Sensor(it[Sensors.id], it[Sensors.name], it[Sensors.state], it[Sensors.value]) }
    }

    fun deleteRoom(id: Int) = PostgresFactory.dbQuery {
        Rooms.deleteWhere { Rooms.id eq id }
    }
}
