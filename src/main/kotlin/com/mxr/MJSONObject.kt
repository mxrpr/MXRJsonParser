package com.mxr

open class MJSONObject(private val result: Any?) {

    fun getInt(path: String): Int? {
        return (this.getObject(path, String()))?.toInt()
    }

    fun getString(path :String): String? {
        return this.getObject(path, String())
    }

    fun getArray(path: String): MJSONArray {
        val result: Any? = this.getObject(path, Any())

        return MJSONArray(result)
    }

    fun getBoolean(path: String): Boolean? {
        return (this.getObject(path, String()))?.toBoolean()
    }

    /**
     * Get an object at a specific [path]
     * @return Any?
     */
    private fun <T> getObject(path: String, classType: T): T? {
        val elements = path.split("/")
        var value : Any? = this.result as Map<*,*>
        for (element in elements) {
            when(value) {
                is Map<*,*> -> value = value[element]
            }
        }

        return value as T
    }
}