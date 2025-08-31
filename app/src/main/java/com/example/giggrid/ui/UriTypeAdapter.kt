package com.example.giggrid.ui

import android.net.Uri
import com.google.gson.*
import java.lang.reflect.Type
import androidx.core.net.toUri

class UriTypeAdapter : JsonSerializer<Uri>, JsonDeserializer<Uri> {

    // Convert Uri to String (for saving)
    override fun serialize(src: Uri, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }

    // Convert String to Uri (for loading)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Uri {
        return json.asString.toUri()
    }
}