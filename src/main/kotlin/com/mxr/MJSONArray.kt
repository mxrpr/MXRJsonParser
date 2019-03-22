package com.mxr

class MJSONArray(private val result: Any?) : MJSONObject(result) {

    fun getInt(index: Int): Int {
        return ((this.result as ArrayList<*>)[index] as String).toInt()
    }

    fun getString(index: Int): String {
        return (this.result as ArrayList<*>)[index] as String
    }

    fun getBoolean(index: Int): Boolean {
        return ((this.result as ArrayList<*>)[index] as String).toBoolean()
    }

    fun getArray(index: Int): MJSONArray {
        return MJSONArray((this.result as ArrayList<*>)[index])
    }
}