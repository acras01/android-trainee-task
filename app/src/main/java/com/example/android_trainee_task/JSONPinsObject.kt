package com.example.android_trainee_task

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser.parseString
import com.google.gson.reflect.TypeToken
import java.util.*


data class JSONPinsObject(val objects: String) {
    private var jelement: JsonElement = parseString(objects)
    private var jobject = jelement.asJsonObject

    val services: Array<String> = Gson().fromJson(
        jobject.getAsJsonArray("services").toString(),
        object : TypeToken<Array<String?>?>() {}.type)

    data class Pin(val id: Int, val service: String, val coordinates: HashMap<String, Double>)
    val pins: List<Pin> = Gson().fromJson(
        jobject.getAsJsonArray("pins").toString(),
        object : TypeToken<List<Pin>>() {}.type)
}