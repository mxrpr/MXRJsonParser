package com.mxr

/**
Simple JSONParser example
TODO: Fix for unchecked casts
 */

class JSONParser {

    private val JSONQUOTE = '"'
    private val JSONWHITESPACE = arrayListOf(' ', '\t', '\b', '\n', '\r')
    private val JSONCOMMA = ','
    private val JSONCOLON = ':'
    private val JSONLEFTBRACKET = '['
    private val JSONRIGHTBRACKET = ']'
    private val JSONLEFTBRACE = '{'
    private val JSONRIGHTBRACE = '}'
    private val JSONSYNTAX = arrayListOf(JSONCOMMA, JSONCOLON, JSONLEFTBRACKET, JSONRIGHTBRACKET,
            JSONLEFTBRACE, JSONRIGHTBRACE)

    /**
     * Parses the [json] string
     * @return Map of string keys and elements
     */
    fun parse(json: String): Map<String, Any> {
        val lexList = this.lex(json)
        val result: Pair<Any, MutableList<Any>> = internalParse(lexList)
        @Suppress("UNCHECKED_CAST")
        return result.first as Map<String, Any>
    }

    /**
     * Get value at a specific [path]
     * @return ArrayList<T>?
     */
    fun <T> getAsArray(path: String, objectHierarchy: Map<String, Any>): ArrayList<T>? {
        if (path.contains('/')) {
            val path = path.split("/")
            var root = objectHierarchy
            for (element in path) {
                if (path.last() == element)
                    return root[element] as ArrayList<T>
                root = root[element] as LinkedHashMap<String, Any>
            }
        }
        @Suppress("UNCHECKED_CAST")
        return objectHierarchy[path] as ArrayList<T>
    }

    /**
     * Get an object at a specific [path]
     * @return Any?
     */
    fun getObject(name: String, objectHierarchy: Map<String, Any>): Any? {
        if (name.contains('/')) {
            val path = name.split("/")
            var root = objectHierarchy
            for (element in path) {
                if (path.last() == element)
                    return root[element]
                @Suppress("UNCHECKED_CAST")
                root = root[element] as LinkedHashMap<String, Any>
            }
        }
        return objectHierarchy[name]
    }

    /**
     * Get Map at a specific [path]
     * @return LinkedHashMap<String, T>
     */
    fun <T> getMap(name: String, objectHierarchy: Map<String, Any>): LinkedHashMap<String, T>? {
        if (name.contains('/')) {
            val path = name.split("/")
            var root = objectHierarchy
            for (element in path) {
                if (path.last() == element)
                    return root[element] as? LinkedHashMap<String, T>
                    @Suppress("UNCHECKED_CAST")
                @Suppress("UNCHECKED_CAST")
                root = root[element] as LinkedHashMap<String, Any>
            }
        }
        @Suppress("UNCHECKED_CAST")
        return objectHierarchy[name] as? LinkedHashMap<String, T>
    }

    /**
     * Lexical analysis
     * Lexical analysis breaks source input into the simplest decomposable elements
     * of a language. These are the "tokens".
     */
    private fun lex(json: String): MutableList<Any> {
        val tokens = mutableListOf<Any>()
        var str = json
        var value = ""

        while (!str.isEmpty()) {
            if (str[0] in JSONSYNTAX) {
                if (!value.isEmpty()) {
                    tokens.add(value)
                    value = ""
                }
                tokens.add(str[0])
                str = str.substring(1)
                continue
            }
            if (str[0] == JSONQUOTE) {
                val result = getString(str.substring(1))
                str = result.second
                tokens.add(result.first)
                continue
            }
            if (str[0] in JSONWHITESPACE) {
                str = str.substring(1)
                continue
            }

            value += str[0]
            str = str.substring(1)
        }
        println(tokens)
        return tokens
    }


    private fun getString(str: String): Pair<String, String> {
        var result = ""
        for (c in str) {
            if (c == JSONQUOTE) {
                break
            } else
                result += c
        }

        return Pair(result, str.substring(result.length + 1))
    }

    /**
     *
     */
    private fun internalParse(tokens: MutableList<Any>): Pair<Any, MutableList<Any>> {
        val token: Any = tokens[0]
        when (token) {
            JSONLEFTBRACKET -> return parseArray(tokens.subList(1, tokens.size))
            JSONLEFTBRACE -> return parseObject(tokens.subList(1, tokens.size))
            else -> {
                if (!JSONSYNTAX.contains(token))
                    return Pair(token, tokens.subList(1, tokens.size))
            }
        }

        return Pair(token, tokens.subList(1, tokens.size))
    }

    private fun parseArray(ptokens: MutableList<Any>): Pair<Any, MutableList<Any>> {
        val result = mutableListOf<Any>()
        var tokens = ptokens
        var t = tokens[0]
        if (t == JSONRIGHTBRACKET) {
            return Pair(result, tokens.subList(1, tokens.size))
        }
        while (true) {
            val jsonTokens = internalParse(tokens)
            tokens = jsonTokens.second
            result.add(jsonTokens.first)
            t = tokens[0]
            if (t == JSONRIGHTBRACKET)
                return Pair(result, tokens.subList(1, tokens.size))
            else
                tokens = tokens.subList(1, tokens.size)
        }

        throw Exception("Expected end of array")
    }

    private fun parseObject(ptokens: MutableList<Any>): Pair<Any, MutableList<Any>> {
        var tokens: MutableList<Any> = ptokens
        val resultObject = mutableMapOf<Any, Any>()
        var t = tokens[0]
        if (t == JSONRIGHTBRACE) {
            return Pair(resultObject, tokens.subList(1, tokens.size))
        }
        while (true) {
            val jsonKey = tokens[0]
            if (jsonKey is String)
                tokens = tokens.subList(1, tokens.size)
            else
                throw Exception("Expected String key: '$jsonKey'")
            if (tokens[0] != JSONCOLON)
                throw Exception("Expected colon after key in object $tokens[0]")
            val jsonTokens = internalParse(tokens.subList(1, tokens.size))
            tokens = jsonTokens.second
            resultObject[jsonKey] = jsonTokens.first
            t = tokens[0]
            if (t == JSONRIGHTBRACE) {
                return Pair(resultObject, tokens.subList(1, tokens.size))
            } else if (t != JSONCOMMA)
                throw Exception("Expected comma after element: '$t'")
            tokens = tokens.subList(1, tokens.size)
        }
    }
}