package brilliant_items.api;

import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/// An Interface that can be implemented for any class that implements the Item class
@SideOnly(Side.CLIENT)
public interface IHasEffects {
    /// Get a list of effects to be applied to the given stack when it is present in the world.
    /// This is run once when the ItemStack is constructed
    ///
    /// @param stack The ItemStack
    /// @return A list of effects
    @Nonnull
    default NonNullList<IEntityItemEffect> getEntityEffects(ItemStack stack) {
        return NonNullList.create();
    }

    /// Get a list of effects to be applied to the given stack when it is in an inventory
    /// This is run once when the ItemStack is constructed
    ///
    /// @param stack The ItemStack
    /// @return A list of effects
    @Nonnull
    default NonNullList<IInventoryItemEffect> getInventoryEffects(ItemStack stack) {
        return NonNullList.create();
    }
}
