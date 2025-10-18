package citas_service_nuevo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
// Spring Boot escaneará este paquete y todos sus sub-paquetes automáticamente
class CitasServiceNuevoApplication

fun main(args: Array<String>) {
    runApplication<CitasServiceNuevoApplication>(*args)
}