package com.example.PSQL

import org.jetbrains.exposed.sql.*

object ConnectedRoomsDAO {
    fun connectRooms(room1Id: Int, room2Id: Int) = PostgresFactory.dbQuery {
        ConnectedRooms.insert {
            it[ConnectedRooms.room1Id] = room1Id
            it[ConnectedRooms.room2Id] = room2Id
        }
    }

    fun getConnectedRooms(roomId: Int): List<Int> = PostgresFactory.dbQuery {
        ConnectedRooms.select { ConnectedRooms.room1Id eq roomId or (ConnectedRooms.room2Id eq roomId) }
            .map {
                if (it[ConnectedRooms.room1Id] == roomId) it[ConnectedRooms.room2Id]
                else it[ConnectedRooms.room1Id]
            }
    }
}
