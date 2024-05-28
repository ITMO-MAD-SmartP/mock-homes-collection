package com.example.PSQL

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object PostgresFactory {
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://93.95.97.113:5432/blps",
            driver = "org.postgresql.Driver",
            user = "bob",
            password = "12345"
        )
        transaction {
            SchemaUtils.create(Users, Homes, Rooms, Sensors, Switches, ConnectedRooms, SensorsInRoom, OwnedHomes)
        }
    }

    fun <T> dbQuery(block: () -> T): T =
        transaction { block() }
}
