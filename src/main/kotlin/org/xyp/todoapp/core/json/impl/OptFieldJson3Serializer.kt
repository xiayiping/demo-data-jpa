package org.xyp.todoapp.core.json.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xyp.todoapp.core.json.OptField
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class OptFieldJson3Serializer : ValueSerializer<OptField<*>>() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(OptFieldJson3Serializer::class.java)
    }
    override fun serialize(value: OptField<*>?,
                           gen: JsonGenerator,
                           ctxt: SerializationContext) {
        if (value?.present ?: false) {
            gen.writePOJO(value.value)
        } else {
            logger.warn("serializer hit absent field, which is an impossible case!!")
            gen.writeNull()
        }
    }

    override fun isEmpty(ctxt: SerializationContext?,
                         value: OptField<*>?): Boolean {
        return !(value?.present ?: true)
    }
}