@file:JvmName("JSONParserExample")

package com.mxr

import java.io.File
import kotlin.system.measureTimeMillis


fun main(str: Array<String>) {
    val jsonParser = JSONParser()
    val fileName = "test2.json"

    if (!File(fileName).exists()) {
        throw Exception ("JSON file does not exists!!")
    }

    val content = File(fileName).readText()
    
    
    lateinit var result: Any
    val time = measureTimeMillis {
         result = jsonParser.parse(content)
    }

    when(result) {
        is ArrayList<*> -> produceArrayResult(jsonParser, result as ArrayList<*>)
        is Map<*,*> -> produceMapResult(jsonParser, (result as Map<String, Any>))
    }

    println("\nJSON parse time: $time milliseconds")
}

private fun produceArrayResult(parser: JSONParser, result: ArrayList<*>) {
            val size = result.size
            println("Result is array and size: $size")
            println(result.first().javaClass.getName())
            val picture = parser.getObject("picture", result.first()  as Map<String, Any>)
            if (picture is String)
                println("\nPicture: $picture")
}

private fun produceMapResult(parser: JSONParser, result: Map<String, Any>) {
            val size = result.size
            println("Result is map and size: $size")
            var name = parser.getObject("object1/name", result)
            println("Name: $name")
            name = parser.getObject("object4/object5/name", result)
            println("Name: $name")
    
}