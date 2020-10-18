package ru.romanow.inst.services.common.utils

import com.google.gson.GsonBuilder

object JsonSerializer {
    private val gson = GsonBuilder().create()

    fun toJson(str: Any) = gson.toJson(str)

    fun <T> fromJson(json: String, cls: Class<T>) = gson.fromJson<T>(json, cls)
}