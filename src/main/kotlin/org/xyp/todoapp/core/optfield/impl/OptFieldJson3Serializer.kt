package org.xyp.todoapp.core.optfield.impl

import org.xyp.todoapp.core.optfield.OptField
import org.xyp.todoapp.core.optfield.impl.OptFieldJsonSerializer.Companion.logger
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