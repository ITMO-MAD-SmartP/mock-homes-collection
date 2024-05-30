package com.exampleimport

import redis.clients.jedis.Jedis

object RedisClient {
    private val jedis = Jedis("93.95.97.113", 5432)

    fun pushToQueue(queueName: String, message: String) {
        jedis.rpush(queueName, message)
    }

    fun popFromQueue(queueName: String): String? {
        return jedis.lpop(queueName)
    }
}