package brilliant_items.api;

import brilliant_items.BrilliantItems;
import brilliant_items.api.effects.IBrilliantItemEffect;
import brilliant_items.capabilities.ItemEffects;
import brilliant_items.capabilities.ItemEffectsCapability;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class BrilliantItemsAPI {
    /// Dynamically adds an effect to a specific ItemStack at runtime
    ///
    /// @param stack  The ItemStack
    /// @param effect The Instance of the effect
    public static void addEffectTo(@Nonnull ItemStack stack, @Nonnull IBrilliantItemEffect effect) {
        if (stack.isEmpty()) return;

        ItemEffects effects = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (effects == null) {
            BrilliantItems.LOGGER.error(
                    "Cannot attach effect to ItemStack since it does not have the ITEM_EFFECTS_CAPABILITY"
            );
            return;
        }

        effects.add(effect);
    }

    /// Dynamically removes an effect to a specific ItemStack at runtime
    ///
    /// @param stack  The ItemStack
    /// @param effect The Instance of the effect
    public static void removeEffectFrom(@Nonnull ItemStack stack, @Nonnull IBrilliantItemEffect effect) {
        if (stack.isEmpty()) return;

        ItemEffects effects = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (effects == null) {
            BrilliantItems.LOGGER.error(
                    "Cannot remove effect from ItemStack since it does not have the ITEM_EFFECTS_CAPABILITY"
            );
            return;
        }

        effects.remove(effect);
    }
}
