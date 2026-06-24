package org.xyp.todoapp.core.json.impl

import org.slf4j.LoggerFactory
import org.xyp.todoapp.core.json.MaskedData
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class MaskedDataSerializer(private val maskedData: MaskedData) : ValueSerializer<Any>() {
    companion object {
        var logger = LoggerFactory.getLogger(MaskedDataSerializer::class.java)
    }
    override fun serialize(value: Any?,
                           gen: JsonGenerator,
                           ctxt: SerializationContext) {

        if (null == value) {
            gen.writeNull()
            return
        }
//        logger.info("{}", MaskedDataSerializer::class.java.simpleName)
        gen.writeString("*****")
    }
}