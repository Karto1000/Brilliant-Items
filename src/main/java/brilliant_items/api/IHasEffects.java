package brilliant_items.api;

import brilliant_items.api.effects.IBrilliantItemEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public interface IHasEffects {
    /// Get a list of effects to be applied to the given stack. This is run once when the ItemStack is constructed
    ///
    /// @param stack The ItemStack
    /// @return A list of effects
    @Nonnull
    NonNullList<IBrilliantItemEffect> getEffects(ItemStack stack);
}
