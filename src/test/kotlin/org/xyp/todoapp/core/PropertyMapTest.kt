package org.xyp.todoapp.core

import com.fasterxml.jackson.annotation.JsonFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xyp.todoapp.core.json.JsonHelper
import org.xyp.todoapp.core.objtransfer.MapTransfer
import org.xyp.todoapp.core.objtransfer.PropertyUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.listOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


class PropertyMapTest {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(PropertyMapTest::class.java)
    }

    class T1(val x: Int)

    data class T2(val x: Int)

    data class T3(val x: Int, val inner: T2, var inner2: T1)

    data class T4(
        val x: Int,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        val birthDate: LocalDateTime
    )

    class Student(val name: String, val number: String, val age: Int)

    @Test
    fun `simplest property getter`() {
        logger.info("==>test")
        val t1 = T1(11)
        val t2 = T2(12)

        val v1 = PropertyUtil.getProperty(t1, "x")
        val v2 = PropertyUtil.getProperty(t2, "x")

        logger.info("==>test [{}]", v1)
        logger.info("==>test [{}]", v2)
        assertTrue(v1 is Int, "v1 should be an Int but was ${v1::class.java}")
        assertTrue(v2 is Int, "v2 should be an Int but was ${v2::class.java}")
        assertEquals(11, v1, "v1 should be 11")
        assertEquals(12, v2, "v2 should be 12")
    }

    @Test
    fun `property getter for inner`() {
        val t3 = T3(10, T2(10), T1(11))

        val v1 = PropertyUtil.getProperty(t3, "x")
        val v2 = PropertyUtil.getProperty(t3, "inner.x")
        val v3 = PropertyUtil.getProperty(t3, "inner2.x")
        val vnull = PropertyUtil.getProperty(t3, "inner2.xr")
        logger.info("[{}]", v1)
        logger.info("[{}]", v2)
        logger.info("[{}]", v3)
        logger.info("[{}]", vnull)

        assertEquals(10, v1)
        assertEquals(10, v2)
        assertEquals(11, v3)
        assertNull(vnull)

        assertTrue(v1 is Int, "v1 should be Int but was ${v1::class.java}")
        assertTrue(v2 is Int, "v2 should be Int but was ${v2::class.java}")
        assertTrue(v3 is Int, "v3 should be Int but was ${v3::class.java}")
    }

    @Test
    fun `static json mapper test`() {
        val now = LocalDateTime.now()
        val formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        val t4 = T4(10, now)

        val t4json = JsonHelper.objToJsonString(t4)
        logger.info(t4json)

        val t4Map = JsonHelper.jsonToMap(t4json)
        logger.info("{}", t4Map)
        assertTrue(t4Map is Map<*, *>, "t4Map should be a Map but was ${t4Map::class.java}")
        assertEquals(formatted, t4Map["birthDate"])
    }

    @Test
    fun `list filter`() {
        val data = mapOf<String, List<Map<String, Any>>>(
            "list" to listOf(
                mapOf("f" to "true", "u" to 1),
                mapOf("f" to "false", "u" to 2),
                mapOf("f" to true, "u" to 1),
            )
        )
        val config = mapOf(
            "array" to
                    mapOf(
                        $$$"""$$_ext_array""" to $$$"""${list}""",
                        $$$"""$$_ext_array_filter""" to $$$"""#{['item']['f']}""",
                    ),
        )

        val result = MapTransfer.transferJson(data, JsonHelper.objToJsonString(config))
        logger.info(result.toString())

        assertNotNull(result, "result should not be null")
        assertTrue(result is Map<*, *>, "result should be a List but was ${result::class.java}")
        val resultMap = result as Map<*, *>
        val resultList = resultMap["array"] as List<Map<String, Any>>
        assertTrue { resultList is List<*> }
        assertEquals(2, resultList.size)
        for (item in resultList) {
            logger.info("{}", item)
            val innerItem = item["item"] as Map<String, Any>
            assertEquals("true", "" + innerItem["f"])
            assertEquals(1, innerItem["u"])
        }
    }

    @Test
    fun `map transfer`() {
        val map = mapOf<String, Any>(
            "object" to Student("studentName", "sNum", 11),
            "name" to "name1",
            "number" to "number1",
            "age" to 10,
            "scores" to listOf<Map<String, Any>>(
                mapOf("course" to "c1", "score" to 1),
                mapOf("course" to "c2", "score" to 3),
                mapOf("course" to "c3", "score" to 5),
            ),
            "friends" to listOf<Student>(
                Student("studentName1", "sNum1", 12),
                Student("studentName2", "sNum2", 13),
            ),
            "addresses" to setOf("a", "b"),
            "prop1" to mapOf("prop2" to "value1-2")
        )

        val configMap = $$$"""
            {
               "studentNameField" : "${object.name}",
               "score1" :  "#{[\"scores\"][1][\"course\"]}",
               "friend1" : "#{[\"friends\"][1][\"number\"]}",
               "friend0" : "#{[\"friends\"][0].number}"
            }
        """.trimIndent()

        val result = MapTransfer.transferJson(map, configMap)
        logger.info("{}", result)
        assertTrue { result is Map<*, *> }
        val resultMap = result as Map<String, Any>
        assertEquals("studentName", resultMap["studentNameField"])
        assertEquals("sNum1", resultMap["friend0"])
        assertEquals("sNum2", resultMap["friend1"])
    }
}