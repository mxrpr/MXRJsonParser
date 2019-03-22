package com.mxr

import org.junit.Assert
import org.junit.Test

class UnitTestsMJSONArray {

    @Test
    fun parseArrayTest_int() {
        val json = "{\"int_value\" : [1,2,4,5]}"
        val parser = MJSONParser()
        try {
            val result = parser.parse(json)
            val array = result.getArray("int_value")
            Assert.assertEquals(1, array.getInt(0))
            Assert.assertEquals(5, array.getInt(3))
        }catch(e: Exception) {
            Assert.fail()
        }
    }

    @Test
    fun parseArrayTest_String() {
        val json = "{\"string_value\" : [\"test_string\", \"test_string2\", \"test_string3\"]}"
        val parser = MJSONParser()
        try {
            val result = parser.parse(json)
            val array = result.getArray("string_value")
            Assert.assertEquals("test_string", array.getString(0))
            Assert.assertEquals("test_string3", array.getString(2))
        }catch(e: Exception) {
            e.printStackTrace()
            Assert.fail()
        }
    }

    @Test
    fun parseObjectTest_Boolean() {
        val json = "{\"bool_value\" : [false, true, false]}"
        val parser = MJSONParser()
        try {
            val result = parser.parse(json)
            val array = result.getArray("bool_value")

            Assert.assertFalse(array.getBoolean(0))
            Assert.assertTrue(array.getBoolean(1))

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

    @Test
    fun parseObjectTest_Array_Of_Array() {
        val json = "{\"array_value\" : [[1,9,8],2,3,4]}"
        val parser = MJSONParser()
        try {
            val result = parser.parse(json)
            val array = result.getArray("array_value")
            val subArray = array.getArray(0)
            Assert.assertEquals(subArray.getInt(1), 9)
        }catch(e: Exception) {
            Assert.fail()
        }
    }
}