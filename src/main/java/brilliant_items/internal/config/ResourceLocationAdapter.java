package brilliant_items.internal.config;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class ResourceLocationAdapter implements JsonDeserializer<ResourceLocation>, JsonSerializer<ResourceLocation> {
    @Override
    public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public ResourceLocation deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context
    ) throws JsonParseException {
        return new ResourceLocation(json.getAsString());
    }
}
