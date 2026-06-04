package org.xyp.todoapp.core.json.impl

import org.xyp.todoapp.core.json.OptField
import org.xyp.todoapp.core.json.impl.OptFieldJsonSerializer.Companion.logger
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class OptFieldJson3Serializer : ValueSerializer<OptField<*>>() {
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