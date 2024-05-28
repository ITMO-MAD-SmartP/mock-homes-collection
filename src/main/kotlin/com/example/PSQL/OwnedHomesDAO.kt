package com.example.PSQL

import org.jetbrains.exposed.sql.*

object OwnedHomesDAO {
    fun addOwnerToHome(userId: Int, homeId: Int) = PostgresFactory.dbQuery {
        OwnedHomes.insert {
            it[OwnedHomes.userId] = userId
            it[OwnedHomes.homeId] = homeId
        }
    }

    fun getOwnersForHome(homeId: Int): List<Int> = PostgresFactory.dbQuery {
        OwnedHomes.select { OwnedHomes.homeId eq homeId }
            .map { it[OwnedHomes.userId] }
    }

    fun getHomesForOwner(userId: Int): List<Int> = PostgresFactory.dbQuery {
        OwnedHomes.select { OwnedHomes.userId eq userId }
            .map { it[OwnedHomes.homeId] }
    }
}
