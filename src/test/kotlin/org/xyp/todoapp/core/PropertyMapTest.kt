package org.xyp.todoapp.core

import com.fasterxml.jackson.annotation.JsonFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xyp.todoapp.core.json.JsonHelper
import org.xyp.todoapp.core.objtransfer.PropertyUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


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

    @Test
    fun `simplest property getter`() {
        logger.info("==>test")
        val t1 = T1(11)
        val t2 = T2(12)

        val v1 = PropertyUtil.getProperty(t1, "x")
        val v2 = PropertyUtil.getProperty(t2, "x")

        logger.info("==>test [{}]", v1)
        logger.info("==>test [{}]", v2)
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
        assertEquals(formatted, t4Map["birthDate"])
    }
}