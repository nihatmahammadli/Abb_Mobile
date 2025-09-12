package com.nihatmahammadli.abbmobile.presentation.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.nihatmahammadli.abbmobile.presentation.model.BaseCardData
import java.lang.reflect.Type

class BaseCardDataAdapter : JsonDeserializer<BaseCardData>, JsonSerializer<BaseCardData> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BaseCardData {
        val jsonObject = json.asJsonObject
        val isCustom = jsonObject.get("isCustom").asBoolean

        return if (isCustom) {
            context.deserialize<BaseCardData.CustomCard>(
                jsonObject,
                BaseCardData.CustomCard::class.java
            )
        } else {
            context.deserialize<BaseCardData.DefaultCard>(
                jsonObject,
                BaseCardData.DefaultCard::class.java
            )
        }
    }

    override fun serialize(
        src: BaseCardData,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(src)
    }
}
