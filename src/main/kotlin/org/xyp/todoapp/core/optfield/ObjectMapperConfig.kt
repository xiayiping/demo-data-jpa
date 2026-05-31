package org.xyp.todoapp.core.optfield

import com.fasterxml.jackson.annotation.JsonInclude
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.xyp.todoapp.core.optfield.impl.OptFieldJson3Deserializer
import org.xyp.todoapp.core.optfield.impl.OptFieldJson3Serializer
import tools.jackson.databind.JacksonModule
import tools.jackson.databind.module.SimpleDeserializers
import tools.jackson.databind.module.SimpleModule
import tools.jackson.databind.module.SimpleSerializers

@Configuration
class ObjectMapperConfig(

) {
    companion object {
        var logger: Logger = LoggerFactory.getLogger(ObjectMapperConfig::class.java)
    }

    init {

//        logger.info("ObjectMapper Config initialized ... ...")
//        val module = SimpleModule().apply {
//            addDeserializer(OptField::class.java, OptFieldJsonDeserializer())
//            addSerializer(OptField::class.java, OptFieldJsonSerializer())
//        }
//        objectMapper.apply {
//            setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
//            setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
//            registerModule(module)
//
//        }
    }

    private fun optFieldModule(): JacksonModule {
        return MyCustomModule()
    }

    // from JacksonAutoConfiguration
    @Bean
    fun customizer(): JsonMapperBuilderCustomizer {
        return JsonMapperBuilderCustomizer { builder ->
            logger.info("ObjectMapper Config initialized ... ...")
            builder.addModule(optFieldModule())
//            builder.addMixIn(OptField::class.java, OptFieldMixin::class.java)
//            builder.changeDefaultPropertyInclusion { handler ->
//                handler.withContentInclusion(JsonInclude.Include.NON_EMPTY)
//                handler.withValueInclusion(JsonInclude.Include.NON_EMPTY)
//            }
        }
    }
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
abstract class OptFieldMixin

class MyCustomModule : SimpleModule() {
    override fun setupModule(context: SetupContext) {
        val serializers: SimpleSerializers = SimpleSerializers()
        val deserializers: SimpleDeserializers = SimpleDeserializers()

        val serializer = OptFieldJson3Serializer()
        val deserializer = OptFieldJson3Deserializer()

        serializers.addSerializer(OptField::class.java, serializer)
        deserializers.addDeserializer(OptField::class.java, deserializer)

        context.addSerializers(serializers)
        context.addDeserializers(deserializers)

    }
}
