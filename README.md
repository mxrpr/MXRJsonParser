# MXRJsonParser
Lexical analysis
Lexical analysis breaks down an input string into tokens. Comments and whitespace are discarded during lexical analysis.

In a simple lexical analyzer, you iterate over all the characters in an input string and break them apart into fundemental language constructs such as integers, strings,etc. In particular, strings must be part of the lexical analysis because you cannot throw away whitespace without knowing that it is not part of a string.
Let's take this json example:
{
  "array" : [1,2,3],
  "other": "this is a test string",
  "integer": 1
}
The lexer produces the following array:
[{, array, :, [, 1, ,, 2, ,, 3, ], ,, other, :, this is a test string, ,, integer, :, 1, }]

Special characters (' ', '\t', '\b', '\n', '\r') are excluded.

Parsing
 After the lexer produces this simple array, comes the interesting part, the parsing. A key structural difference between this lexer and parser is that the lexer returns a one-dimensional array of tokens. Parsers are often defined recursively and returns a recursive, tree-like object.
Parsing objects is a matter of parsing a key-value pair internally separated by a colon and externally separated by a comma until you reach the end of the object.
[CODE]
Parsing arrays is a matter of parsing array members and expecting a comma token between them or a right bracket indicating the end of the array.
[CODE]

Getting the results
Result can be a String, Integer, Map and other objects at specific path. let's take this simple json:
{
  "array" : [1,2,3],
  "other": "this is a test string",
  "integer": 1,
  "object" : {
    "object_string": "o_string",
    "object_int": 1234,
    "array" : [1,2,3],
    "other": "this is a test string",
    "integer": 1,
    "object" : {
      "object_string": "o_string",
      "object_int": 1234
    }
  }
}

At path "object/object/object_string" there is a string. How we can get it? This simpe method shows how to get an Object, a Map or array:

fun getObject(name: String, objectHierarchy: Map<String, Any>): Any?
fun <T> getMap(name: String, objectHierarchy: Map<String, Any>): LinkedHashMap<String, T>?
fun <T> getAsArray(path: String, objectHierarchy: Map<String, Any>): ArrayList<T>?

