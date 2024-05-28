package com.example.PSQL

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    override val primaryKey = PrimaryKey(id)
}

object Homes : Table() {
    val id = integer("id").autoIncrement()
    val ownerId = integer("owner_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(id)
}

object Rooms : Table() {
    val id = integer("id").autoIncrement()
    val homeId = integer("home_id").references(Homes.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(id)
}

object Sensors : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val state = bool("state")
    val value = double("value")
    override val primaryKey = PrimaryKey(id)
}

object Switches : Table() {
    val id = integer("id").autoIncrement()
    val state = bool("state")
    val value = double("value")
    override val primaryKey = PrimaryKey(id)
}

object ConnectedRooms : Table() {
    val room1Id = integer("room1_id").references(Rooms.id, onDelete = ReferenceOption.CASCADE)
    val room2Id = integer("room2_id").references(Rooms.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(room1Id, room2Id)
}

object SensorsInRoom : Table() {
    val sensorId = integer("sensor_id").references(Sensors.id, onDelete = ReferenceOption.CASCADE)
    val roomId = integer("room_id").references(Rooms.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(sensorId, roomId)
}

object OwnedHomes : Table() {
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val homeId = integer("home_id").references(Homes.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(userId, homeId)
}