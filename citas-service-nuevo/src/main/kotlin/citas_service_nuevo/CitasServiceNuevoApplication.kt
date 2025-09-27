package com.clinica.api.citas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.clinica.api.citas"])
class CitasServiceApplication

fun main(args: Array<String>) {
    runApplication<CitasServiceApplication>(*args)
}