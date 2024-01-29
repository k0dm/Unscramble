package com.github.k0dm.unscramble.creategame.data

import com.github.k0dm.unscramble.creategame.domain.GameSession
import com.github.k0dm.unscramble.main.data.LocalStorage
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


interface GameRepository {

    fun saveGameSession(gameSession: GameSession)

    fun restoreGame(): GameSession

    class Base(
        private val gson: Gson,
        private val localStorage: LocalStorage
    ) : GameRepository {

        override fun saveGameSession(gameSession: GameSession) {
            val gameSessionJson = gson.toJson(gameSession)
            localStorage.save(KEY, gameSessionJson)
        }

        override fun restoreGame(): GameSession {
            val gameSessionJson = localStorage.read(KEY)
            return gson.fromJson(gameSessionJson, GameSession.Base::class.java)
        }

        companion object {
            private const val KEY = "GameSessionJson"
        }
    }
}

class TypeAdapter<T> : JsonSerializer<T>, JsonDeserializer<T> {

    companion object {
        private const val CLASSNAME = "CLASSNAME"
        private const val DATA = "DATA"
    }

    override fun serialize(
        src: T?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty(CLASSNAME, src!!.javaClass.getName())
        jsonObject.add(DATA, context!!.serialize(src))
        return jsonObject
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): T {
        val jsonObject: JsonObject = json!!.getAsJsonObject()
        val prim = jsonObject[CLASSNAME] as JsonPrimitive
        val className = prim.asString
        var klass: Class<*>? = null
        try {
            klass = Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            // TODO: handle somehow
        }
        return context!!.deserialize(jsonObject[DATA], klass)
    }

}
