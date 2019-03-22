package com.mxr

/**
 * Simple JSONParser example
 */
class MJSONParser {

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
     *
     * @return Map of string keys and elements
     */
    fun parse(json: String): MJSONObject {
        val lexList = this.lex(json)
        val result: Pair<Any, List<Any>> = internalParse(lexList)

        return MJSONObject(result.first)
    }

    /**
     * Lexical analysis
     *
     * Lexical analysis breaks source input into the simplest decomposable elements
     * of a language. These are the "tokens".
     */
    private fun lex(json: String): List<Any> {
        val tokens = mutableListOf<Any>()
        var str = json
        var value = ""

        while (!str.isEmpty()) {
            when(str[0]) {
                in JSONSYNTAX -> {
                    if (!value.isEmpty()) {
                        tokens.add(value)
                        value = ""
                    }
                    tokens.add(str[0])
                    str = str.substring(1)
                }
                JSONQUOTE -> {
                    val result = getString(str.substring(1))
                    str = result.second //remaining string
                    tokens.add(result.first)
                }
                in JSONWHITESPACE -> {
                    str = str.substring(1)
                }
                else -> {
                    val result = this.getValue(str)
                    str = result.second //remaining string
                    tokens.add(result.first)
                }
            }
        }
        println(tokens)
        return tokens
    }

    private fun getValue(str: String) : Pair<String, String> {
        var result: String = ""
        for (c in str) {
            //TODO check for '\{"'
            if (c in JSONSYNTAX) {
                break
            } else
                result += c
        }
        return Pair(result, str.substring(result.length))
    }

    /**
     * Get the string till the next quote
     *
     * @return a pair of the found string and the rest of the parsed string
     */
    private fun getString(str: String): Pair<String, String> {
        var result = ""
        for (c in str) {
            //TODO check for '\{"'
            if (c == JSONQUOTE) {
                break
            } else
                result += c
        }

        // return with the string and the remaining string
        return Pair(result, str.substring(result.length + 1))
    }

    /**
     * Parse the result of the lex for array or object
     */
    private fun internalParse(tokens: List<Any>): Pair<Any, List<Any>> {
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

    /**
     * Parse for array
     */
    private fun parseArray(ptokens: List<Any>): Pair<Any, List<Any>> {
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
    }

    private fun parseObject(ptokens: List<Any>): Pair<Any, List<Any>> {
        var tokens: List<Any> = ptokens
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
            }
            else if (t != JSONCOMMA)
                throw Exception("Expected comma after element: '$t'")
            tokens = tokens.subList(1, tokens.size)
        }
    }
}
