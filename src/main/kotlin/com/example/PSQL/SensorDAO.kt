package com.example.PSQL

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

data class Sensor(val id: Int, val name: String, val state: Boolean, val value: Double)

object SensorDAO {
    fun createSensor(name: String, state: Boolean, value: Double): Int = PostgresFactory.dbQuery {
        Sensors.insert {
            it[Sensors.name] = name
            it[Sensors.state] = state
            it[Sensors.value] = value
        } get Sensors.id
    }
    fun addSensorToRoom(sensorId: Int, roomId: Int) = PostgresFactory.dbQuery {
        SensorsInRoom.insert {
            it[SensorsInRoom.sensorId] = sensorId
            it[SensorsInRoom.roomId] = roomId
        }
    }

    fun getSensor(id: Int): Sensor? = PostgresFactory.dbQuery {
        Sensors.select { Sensors.id eq id }
            .map { Sensor(it[Sensors.id], it[Sensors.name], it[Sensors.state], it[Sensors.value]) }
            .singleOrNull()
    }

    fun getAllSensors(): List<Sensor> = PostgresFactory.dbQuery {
        Sensors.selectAll().map { Sensor(it[Sensors.id], it[Sensors.name], it[Sensors.state], it[Sensors.value]) }
    }

    fun updateSensor(id: Int, state: Boolean, value: Double) = PostgresFactory.dbQuery {
        Sensors.update({ Sensors.id eq id }) {
            it[Sensors.state] = state
            it[Sensors.value] = value
        }
    }

    fun deleteSensor(id: Int) = PostgresFactory.dbQuery {
        Sensors.deleteWhere { Sensors.id eq id }
    }
}