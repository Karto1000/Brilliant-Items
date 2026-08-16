package brilliant_items.internal.registry;

import brilliant_items.BrilliantItems;
import brilliant_items.api.IEffect;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.internal.config.JsonConfigManager;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import static brilliant_items.internal.config.JsonConfigManager.GSON;

public class EffectRegistry {
    private static final HashMap<String, RegisteredEffect<? extends IInventoryItemEffect>> inventoryEffects = new HashMap<>();
    private static final HashMap<String, RegisteredEffect<? extends IEntityItemEffect>> entityEffects = new HashMap<>();

    private static class RegisteredEffect<T extends IEffect> {
        final Class<T> effectClass;
        final Class<?> argsClass;

        RegisteredEffect(Class<T> effectClass, Class<?> argsClass) {
            this.effectClass = effectClass;
            this.argsClass = argsClass;
        }

        @Nonnull
        Optional<T> createInstance(@Nullable JsonObject arguments) {
            try {
                Object argsInstance = GSON.fromJson(arguments, argsClass);

                if (argsInstance == null) {
                    try {
                        // Try to set a default instance of args
                        argsInstance = argsClass.newInstance();
                    } catch (IllegalAccessException e) {
                        BrilliantItems.LOGGER.error(
                                "Could not create an instance of args class '{}' in effect '{}' due to it not having a default constructor",
                                this.argsClass,
                                this.effectClass,
                                e
                        );
                        return Optional.empty();
                    }
                }

                Set<ConstraintViolation<Object>> violations = JsonConfigManager.VALIDATOR.validate(argsInstance);
                if (!violations.isEmpty()) {
                    BrilliantItems.LOGGER.error("Configuration validation failed for effect arguments of '{}'!", this.effectClass.getSimpleName());
                    for (ConstraintViolation<Object> violation : violations) {
                        BrilliantItems.LOGGER.error(" -> Field '{}' {}. Current value: {}",
                                violation.getPropertyPath(),
                                violation.getMessage(),
                                violation.getInvalidValue());
                    }

                    return Optional.empty();
                }

                Constructor<T> constructor = effectClass.getDeclaredConstructor(argsClass);
                constructor.setAccessible(true);


                return Optional.of(constructor.newInstance(argsInstance));
            } catch (Exception e) {
                BrilliantItems.LOGGER.error(e);
                return Optional.empty();
            }
        }
    }

    public static void registerInventoryEffect(
            @Nonnull String identifier,
            @Nonnull Class<? extends IInventoryItemEffect> effectClass,
            @Nonnull Class<?> effectArgumentsClass
    ) {
        inventoryEffects.put(
                identifier,
                new RegisteredEffect<>(effectClass, effectArgumentsClass)
        );
    }

    public static void registerEntityEffect(
            @Nonnull String identifier,
            @Nonnull Class<? extends IEntityItemEffect> effectClass,
            @Nonnull Class<?> effectArgumentsClass
    ) {
        entityEffects.put(
                identifier,
                new RegisteredEffect<>(effectClass, effectArgumentsClass)
        );
    }

    @Nonnull
    public static Optional<? extends IInventoryItemEffect> createInventoryItemEffect(
            @Nonnull String identifier,
            @Nonnull JsonObject arguments
    ) {
        RegisteredEffect<? extends IInventoryItemEffect> regEffect = inventoryEffects.get(identifier);
        if (regEffect == null) return Optional.empty();
        return regEffect.createInstance(arguments);
    }

    @Nonnull
    public static Optional<? extends IEntityItemEffect> createEntityItemEffect(
            @Nonnull String identifier,
            @Nonnull JsonObject arguments
    ) {
        RegisteredEffect<? extends IEntityItemEffect> regEffect = entityEffects.get(identifier);
        if (regEffect == null) return Optional.empty();
        return regEffect.createInstance(arguments);
    }
}
