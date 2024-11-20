package iut.nantes.exo20

import iut.nantes.exo20.config.PropertiesConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
// @Import --> Spring n'arrive pas à
// scan le fichier de configuration (?)
@Import(PropertiesConfig::class)
class Exo20Application

fun main(args: Array<String>) {
    runApplication<Exo20Application>(*args)
}
