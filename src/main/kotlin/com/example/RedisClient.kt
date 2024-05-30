package com.exampleimport

import redis.clients.jedis.Jedis

object RedisClient {
    private val jedis = Jedis("localhost", 6379)

    fun pushToQueue(queueName: String, message: String) {
        jedis.lpush(queueName, message)
    }

    fun popFromQueue(queueName: String): String? {
        return jedis.rpop(queueName)
    }
}