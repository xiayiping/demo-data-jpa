package org.xyp.todoapp.core.json.impl

import org.xyp.todoapp.core.json.MaskedData
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class MaskedDataSerializer(private val maskedData: MaskedData) : ValueSerializer<Any>() {
    override fun serialize(value: Any?,
                           gen: JsonGenerator,
                           ctxt: SerializationContext) {

        if (null == value) {
            gen.writeNull()
            return
        }
        gen.writeString("*****")
    }
}