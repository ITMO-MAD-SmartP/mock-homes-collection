package com.example.PSQL

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

data class User(val id: Int, val name: String)

object UserDAO {
    fun createUser(name: String): Int = PostgresFactory.dbQuery {
        Users.insert {
            it[Users.name] = name
        } get Users.id
    }

    fun getUser(id: Int): User? = PostgresFactory.dbQuery {
        Users.select { Users.id eq id }
            .map { User(it[Users.id], it[Users.name]) }
            .singleOrNull()
    }

    fun getAllUsers(): List<User> = PostgresFactory.dbQuery {
        Users.selectAll().map { User(it[Users.id], it[Users.name]) }
    }

    fun updateUser(id: Int, name: String, age: Int) = PostgresFactory.dbQuery {
        Users.update({ Users.id eq id }) {
            it[Users.name] = name
        }
    }

    fun deleteUser(id: Int) = PostgresFactory.dbQuery {
        Users.deleteWhere { Users.id eq id }
    }
}