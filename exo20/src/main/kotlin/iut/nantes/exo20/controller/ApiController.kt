package iut.nantes.exo20.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class GitProperties(
    val branch: String,
    val commit: String,
)

@ConfigurationProperties("custom")
data class InfoProperties(
    val appName: String,
    val appVersion: String,
    val git: GitProperties,
)

@RestController
@RequestMapping("/api/v1")
class ApiController {
    @Autowired
    private lateinit var properties: InfoProperties

    @GetMapping("/info")
    fun info(): ResponseEntity<InfoProperties> = ResponseEntity.ok(properties)
}