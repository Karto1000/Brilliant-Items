package brilliant_items.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Optional;

@FunctionalInterface
public interface EffectFactory<T> {
    /// A function to create a new instance of this effect from the provided arguments
    ///
    /// @param arguments The arguments
    /// @return The effect, or an empty optional if creation failed
    Optional<T> createFromConfig(HashMap<String, JsonPrimitive> arguments);
}
