package org.xyp.todoapp.core.json.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xyp.todoapp.core.json.OptField

class OptFieldJsonSerializer : JsonSerializer<OptField<*>>() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(OptFieldJsonSerializer::class.java)
    }

    override fun serialize(
        value: OptField<*>?,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        if (value?.present ?: false) {
            gen.writeObject(value.value)
        } else {
            logger.warn("serializer hit absent field, which is an impossible case!!")
            gen.writeNull()
        }
    }

    override fun isEmpty(provider: SerializerProvider?, value: OptField<*>?): Boolean {
        return value?.present ?: false
    }
}