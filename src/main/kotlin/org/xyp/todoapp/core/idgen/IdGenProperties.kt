package org.xyp.todoapp.core.idgen

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.id-gen")
data class IdGenProperties(
    var defaultBatchSize: Long = 10,
    var defaultIdStartsFrom: Long = 100000,
) {
}