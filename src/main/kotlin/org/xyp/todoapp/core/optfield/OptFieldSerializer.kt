package org.xyp.todoapp.core.optfield
/*

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull
import org.slf4j.LoggerFactory

class OptFieldSerializer<T>(private val valueSerializer: KSerializer<T>)
    : KSerializer<OptionalField<T?>> {
    companion object {
        val logger = LoggerFactory.getLogger(OptFieldSerializer::class.java)!!
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("OptField") {}

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: OptionalField<T?>) {
        if (value.present) {
            if (value.value == null) encoder.encodeNull()
            else encoder.encodeSerializableValue(valueSerializer, value.value)
        }
    }

    override fun deserialize(decoder: Decoder): OptionalField<T?> {
        require(decoder is JsonDecoder)
        logger.trace("Decoding Json ...")
        val element = decoder.decodeJsonElement()

        return when {
            element is JsonNull -> OptionalField.of(null)
            else -> OptionalField.of(
                decoder.json.decodeFromJsonElement(valueSerializer, element)
            )
        }
    }
}
*/
