package brilliant_items.internal.config;

import com.google.gson.JsonObject;
import lombok.NoArgsConstructor;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonConfig {
    public HashMap<ResourceLocation, ItemMapping> mappings = new HashMap<>();

    @NoArgsConstructor
    public static class ItemMapping {
        public List<EffectMapping> inventoryEffects = new ArrayList<>();
        public List<EffectMapping> entityEffects = new ArrayList<>();
    }

    @NoArgsConstructor
    public static class EffectMapping {
        public String identifier;
        public JsonObject arguments;
    }
}
