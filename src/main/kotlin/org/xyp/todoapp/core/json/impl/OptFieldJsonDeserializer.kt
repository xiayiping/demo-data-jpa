package org.xyp.todoapp.core.json.impl

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import org.xyp.todoapp.core.json.OptField

class OptFieldJsonDeserializer() : JsonDeserializer<OptField<*>>(), ContextualDeserializer {
    lateinit var valueType: JavaType

    constructor(vtype: JavaType) : this() {
        this.valueType = vtype
    }

    override fun createContextual(
        ctxt: DeserializationContext?,
        property: BeanProperty?
    ): JsonDeserializer<*> {
        val wrapperType = property!!.type
        val valueType = wrapperType.containedType(0)
        return OptFieldJsonDeserializer(valueType)
    }

    override fun deserialize(
        p: JsonParser?,
        ctxt: DeserializationContext?
    ): OptField<*> {
        val value = ctxt!!.readValue<Any>(p, valueType)
        return OptField.Companion.of(value)
    }

    override fun getNullValue(ctxt: DeserializationContext?): OptField<*> {
        return OptField(null, true)
    }

    override fun getAbsentValue(ctxt: DeserializationContext?): OptField<*> {
        return OptField(null, false)
    }

}