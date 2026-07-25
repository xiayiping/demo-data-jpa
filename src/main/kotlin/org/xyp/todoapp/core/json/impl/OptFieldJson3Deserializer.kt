package org.xyp.todoapp.core.json.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.jackson.ObjectValueDeserializer
import org.xyp.todoapp.core.json.OptField
import tools.jackson.core.JsonParser
import tools.jackson.databind.BeanProperty
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JavaType
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer

class OptFieldJson3Deserializer() :
    ObjectValueDeserializer<OptField<*>>() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(OptFieldJson3Deserializer::class.java)
    }

    lateinit var valueType: JavaType

    constructor(vType: JavaType) : this() {
        this.valueType = vType
    }

    override fun deserializeObject(jsonParser: JsonParser,
                                   context: DeserializationContext,
                                   tree: JsonNode): OptField<*> {
        val value = context.readValue<Any>(jsonParser, valueType)
        return OptField.of(value)
    }

    override fun getAbsentValue(ctxt: DeserializationContext?): Any? {
        return OptField(null, false)
    }

    override fun getNullValue(ctxt: DeserializationContext?): Any? {
        return OptField(null, true)
    }

    override fun createContextual(
        ctxt: DeserializationContext,
        property: BeanProperty
    ): ValueDeserializer<*> {
        val wrapperType = property.type!!
        val valueType = wrapperType.containedType(0)
        return OptFieldJson3Deserializer(valueType!!)
    }
}
