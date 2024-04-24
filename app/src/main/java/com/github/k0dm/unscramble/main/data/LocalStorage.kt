package com.github.k0dm.unscramble.main.data

import android.content.Context

interface LocalStorage {

    fun save(key: String, string: String)
    fun read(key: String): String
    fun save(key: String, boolean: Boolean)
    fun read(key: String, default: Boolean): Boolean

    class Base(context: Context) : LocalStorage {

        private val sharedPreferences =
            context.getSharedPreferences("UnscrambleData", Context.MODE_PRIVATE)

        override fun save(key: String, string: String) {
            sharedPreferences.edit().putString(key, string).apply()
        }

        override fun save(key: String, boolean: Boolean) {
            sharedPreferences.edit().putBoolean(key, boolean).apply()
        }

        override fun read(key: String): String {
            return sharedPreferences.getString(key, "") ?: ""
        }

        override fun read(key: String, default: Boolean): Boolean {
            return sharedPreferences.getBoolean(key, default)
        }

    }
}