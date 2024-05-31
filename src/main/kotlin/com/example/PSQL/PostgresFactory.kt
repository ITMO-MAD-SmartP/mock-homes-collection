package com.example.PSQL

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object PostgresFactory {
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/rmp",
            driver = "org.postgresql.Driver",
            user = "postgres"
        )
        transaction {
//            SchemaUtils.drop(Users, Homes, Rooms, Sensors, Switches, ConnectedRooms, SensorsInRoom, OwnedHomes)
//            SchemaUtils.create(Users, Homes, Rooms, Sensors, Switches, ConnectedRooms, SensorsInRoom, OwnedHomes)
        }
    }

    fun <T> dbQuery(block: () -> T): T =
        transaction { block() }
}
