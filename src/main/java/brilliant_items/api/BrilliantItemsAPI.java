package brilliant_items.api;

import brilliant_items.BrilliantItems;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.internal.capabilities.ItemEffects;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class BrilliantItemsAPI {
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
