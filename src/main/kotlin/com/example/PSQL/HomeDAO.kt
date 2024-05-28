package com.example.PSQL

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

data class Home(val id: Int, val ownerId: Int)

object HomeDAO {

    fun createHome(ownerId: Int) = PostgresFactory.dbQuery {
        Homes.insert {
            it[Homes.ownerId] = ownerId
        } get Homes.id
    }

    fun getHome(id: Int): Home? = PostgresFactory.dbQuery {
        Homes.select { Homes.id eq id }
            .map { Home(it[Homes.id], it[Homes.ownerId]) }
            .singleOrNull()
    }

    fun getAllHomes(): List<Home> = PostgresFactory.dbQuery {
        Homes.selectAll().map { Home(it[Homes.id], it[Homes.ownerId]) }
    }

    fun addRoomToHome(homeId: Int, roomId: Int) = PostgresFactory.dbQuery {
        val roomId = Rooms.insert {
            it[id] = roomId
            it[Rooms.homeId] = homeId
        }
    }

    fun getRoomsInHome(homeId: Int): List<Room> = PostgresFactory.dbQuery {
        Rooms.select { Rooms.homeId eq homeId }
            .map { Room(it[Rooms.id], it[Rooms.homeId]) }
    }

    fun getSensorsInHome(homeId: Int): List<Sensor> = PostgresFactory.dbQuery {
        (Rooms innerJoin SensorsInRoom innerJoin Sensors)
            .select { Rooms.homeId eq homeId }
            .map { Sensor(it[Sensors.id], it[Sensors.name], it[Sensors.state], it[Sensors.value]) }
    }

    fun deleteHome(id: Int) = PostgresFactory.dbQuery {
        Homes.deleteWhere { Homes.id eq id }
    }
}

