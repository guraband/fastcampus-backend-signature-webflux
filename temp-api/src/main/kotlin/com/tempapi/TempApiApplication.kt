package com.tempapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TempApiApplication

fun main(args: Array<String>) {
    runApplication<TempApiApplication>(*args)
}
