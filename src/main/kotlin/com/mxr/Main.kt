@file:JvmName("JSONParserExample")
package com.mxr

import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


fun main(str: Array<String>) {

    val fileName = "test2.json"

    if (!File(fileName).exists()) {
        print("JSON file does not exists!!")
        exitProcess(-1)
    }

    val content = File(fileName).readText()

    val jsonParser = JSONParser()
    var result = jsonParser.parse(content)

    when(result) {
        is Map<*,*> -> produceMapResult(jsonParser, (result as Map<String, Any>))
        is Array<*> -> produceArrayResult(jsonParser, result as ArrayList<*>)
    }
}

private fun produceArrayResult(parser: JSONParser, result: ArrayList<*>) {
            val size = result.size
            println("Result is array and size: $size")
            println(result.first().javaClass.getName())
            val picture = parser.getObject("tags", result.first() as Map<String, Any>, String)
            println("\nPicture: $picture")
}

private fun produceMapResult(parser: JSONParser, result: Map<String, Any>) {
    val size = result.size
    println("Result is map and size: $size")
    val variable = parser.getObject("tags", result, HashMap<String, String>())
    println("Name: $variable")
}