package com.mxr

import java.io.File

fun main(str: Array<String>) {
    val jsonParser = JSONParser()
    if (!File("test.json").exists()) {
        throw Exception ("JSON file does not exists!!")
    }
    val content = File("test.json").readText()
    val result = jsonParser.parse(content)

    println("${jsonParser.getAsArray<Int>("array", result)}")
    println(result["object"]!!.javaClass.name)
    println("${jsonParser.getObject("object/object_string", result)}")
    println("${jsonParser.getObject("object/object/object_string", result)}")
}