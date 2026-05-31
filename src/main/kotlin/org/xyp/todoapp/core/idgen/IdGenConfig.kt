package org.xyp.todoapp.core.idgen

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [
    IdGenProperties::class
])
class IdGenConfig {
}