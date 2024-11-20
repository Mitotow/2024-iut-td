package iut.nantes.exo20.config

import iut.nantes.exo20.controller.InfoProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(InfoProperties::class)
class PropertiesConfig