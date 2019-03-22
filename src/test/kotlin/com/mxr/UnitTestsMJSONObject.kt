package com.mxr

import org.junit.Assert
import org.junit.Test

class UnitTestsMJSONObject {

    @Test
    fun parseObjectTest_int() {
        val json = "{\"int_value\" : 123}"
        val parser = MJSONParser()
        try {
            val result = parser.parse(json)
            Assert.assertEquals(123, result.getInt("int_value"))
        }catch(e: Exception) {
            Assert.fail()
        }
    }

    @Test
    fun parseObjectTest_String() {
        val json = "{\"string_value\" : \"test_string\"}"
        val parser = MJSONParser()
        try {
            val result = parser.parse(json)
            Assert.assertEquals("test_string", result.getString("string_value"))
        }catch(e: Exception) {
            Assert.fail()
        }
    }

    @Test
    fun parseObjectTest_Boolean() {
        val json = "{\"bool_value\" : true}"
        val parser = MJSONParser()
        try {
            val result = parser.parse(json)
            Assert.assertTrue(result.getBoolean("bool_value")!!)
        }catch(e: Exception) {
            Assert.fail()
        }
    }

    @Test
    fun parseObjectTest_Array() {
        val json = "{\"array_value\" : [1,2,3,4]}"
        val parser = MJSONParser()
        try {
            val result = parser.parse(json)
            val array = result.getArray("array_value")

            Assert.assertEquals(array.getInt(0), 1)
        }catch(e: Exception) {
            Assert.fail()
        }
    }
}