package org.xyp.todoapp.core.json

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.xyp.todoapp.core.json.impl.OptFieldJson3Deserializer
import org.xyp.todoapp.core.json.impl.OptFieldJson3Serializer
import org.xyp.todoapp.core.json.impl.MaskedDataSerializer
import tools.jackson.databind.JacksonModule
import tools.jackson.databind.cfg.MapperConfig
import tools.jackson.databind.introspect.Annotated
import tools.jackson.databind.introspect.JacksonAnnotationIntrospector
import tools.jackson.databind.module.SimpleDeserializers
import tools.jackson.databind.module.SimpleModule
import tools.jackson.databind.module.SimpleSerializers

@Configuration
class ObjectMapperConfig(

) {
    companion object {
        var logger: Logger = LoggerFactory.getLogger(ObjectMapperConfig::class.java)
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
            builder.annotationIntrospector(object : JacksonAnnotationIntrospector() {
                override fun findSerializer(config: MapperConfig<*>,
                                            a: Annotated): Any? {
                    if (a.hasAnnotation(MaskedData::class.java)) {
                        val annotation = a.getAnnotation(MaskedData::class.java)
                        return MaskedDataSerializer(annotation)
                    }
                    return super.findSerializer(config, a)
                }
            })
//            builder.addMixIn(OptField::class.java, OptFieldMixin::class.java)
//            builder.changeDefaultPropertyInclusion { handler ->
//                handler.withContentInclusion(JsonInclude.Include.NON_EMPTY)
//                handler.withValueInclusion(JsonInclude.Include.NON_EMPTY)
//            }
        }
    }
}

//@JsonInclude(JsonInclude.Include.NON_EMPTY)
//abstract class OptFieldMixin

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
