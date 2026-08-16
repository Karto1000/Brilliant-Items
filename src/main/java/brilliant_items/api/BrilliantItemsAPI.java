package brilliant_items.api;

import brilliant_items.BrilliantItems;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.internal.capabilities.ItemEffects;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import brilliant_items.internal.registry.EffectRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class BrilliantItemsAPI {
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T extends Class<? extends IEffect>> void registerForJSON(T... effect) {
        for (Class<?> clazz : effect) {
            ReferencableEffect referencableEffect = clazz.getAnnotation(ReferencableEffect.class);
            if (referencableEffect == null) {
                BrilliantItems.LOGGER.error(
                        "Cannot register JSON effect because it is not annotated with @ReferencableEffect"
                );
                return;
            }

            if (IInventoryItemEffect.class.isAssignableFrom(clazz)) {
                BrilliantItems.LOGGER.info("Registered '{}' effect as inventory effect", referencableEffect.identifier());
                EffectRegistry.registerInventoryEffect(
                        referencableEffect.identifier(),
                        // This cast is safe since we just checked it
                        (Class<? extends IInventoryItemEffect>) clazz,
                        referencableEffect.argumentsClass()
                );
            } else if (IEntityItemEffect.class.isAssignableFrom(clazz)) {
                BrilliantItems.LOGGER.info("Registered '{}' effect as entity effect", referencableEffect.identifier());
                EffectRegistry.registerEntityEffect(
                        referencableEffect.identifier(),
                        // This cast is safe since we just checked it
                        (Class<? extends IEntityItemEffect>) clazz,
                        referencableEffect.argumentsClass()
                );
            }
        }
    }

    /// Dynamically adds an entity item effect to a specific ItemStack at runtime
    ///
    /// @param stack  The ItemStack
    /// @param effect The Instance of the entity effect
    public static void addEffectTo(@Nonnull ItemStack stack, @Nonnull IEntityItemEffect effect) {
        if (stack.isEmpty()) return;

        ItemEffects effects = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (effects == null) {
            BrilliantItems.LOGGER.error(
                    "Cannot attach entity effect to ItemStack since it does not have the ITEM_EFFECTS_CAPABILITY"
            );
            return;
        }

        effects.add(effect);
    }

    /// Dynamically adds an inventory effect to a specific ItemStack at runtime
    ///
    /// @param stack  The ItemStack
    /// @param effect The Instance of the inventory effect
    public static void addEffectTo(@Nonnull ItemStack stack, @Nonnull IInventoryItemEffect effect) {
        if (stack.isEmpty()) return;

        ItemEffects effects = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (effects == null) {
            BrilliantItems.LOGGER.error(
                    "Cannot attach inventory effect to ItemStack since it does not have the ITEM_EFFECTS_CAPABILITY"
            );
            return;
        }

        effects.add(effect);
    }

    /// Dynamically removes an entity effect to a specific ItemStack at runtime
    ///
    /// @param stack  The ItemStack
    /// @param effect The Instance of the entity effect
    public static void removeEffectFrom(@Nonnull ItemStack stack, @Nonnull IEntityItemEffect effect) {
        if (stack.isEmpty()) return;

        ItemEffects effects = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (effects == null) {
            BrilliantItems.LOGGER.error(
                    "Cannot remove entity effect from ItemStack since it does not have the ITEM_EFFECTS_CAPABILITY"
            );
            return;
        }

        effects.remove(effect);
    }

    /// Dynamically removes an inventory effect to a specific ItemStack at runtime
    ///
    /// @param stack  The ItemStack
    /// @param effect The Instance of the inventory effect
    public static void removeEffectFrom(@Nonnull ItemStack stack, @Nonnull IInventoryItemEffect effect) {
        if (stack.isEmpty()) return;

        ItemEffects effects = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (effects == null) {
            BrilliantItems.LOGGER.error(
                    "Cannot remove inventory effect from ItemStack since it does not have the ITEM_EFFECTS_CAPABILITY"
            );
            return;
        }

        effects.remove(effect);
    }
}
