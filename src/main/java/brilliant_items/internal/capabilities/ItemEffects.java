package brilliant_items.internal.capabilities;

import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ItemEffects {
    @Nonnull
    private final List<IEntityItemEffect> entityEffects = new ArrayList<>();

    @Nonnull
    private final List<IInventoryItemEffect> inventoryEffects = new ArrayList<>();

    /// Get a list of each effect entity effect that is held by this provider
    ///
    /// @return A list of entity effects
    @Nonnull
    public List<IEntityItemEffect> getEntityEffects() {
        return this.entityEffects;
    }

    /// Get a list of each inventory effect that is held by this provider
    ///
    /// @return A list of inventory effects
    @Nonnull
    public List<IInventoryItemEffect> getInventoryEffects() {
        return inventoryEffects;
    }

    /// Add a new entity effect to this provider
    ///
    /// @param effect The entity effect to add
    public void add(@Nonnull IEntityItemEffect effect) {
        this.entityEffects.add(effect);
    }

    /// Add a new inventory effect to this provider
    ///
    /// @param effect The inventory effect to add
    public void add(@Nonnull IInventoryItemEffect effect) {
        this.inventoryEffects.add(effect);
        // We want to sort the list here to keep all the inventory effects with the BEHIND render mode first in the list
        this.inventoryEffects.sort(Comparator.comparing(IInventoryItemEffect::getRenderMode));
    }

    /// Remove a specific instance of an entity effect from this provider
    ///
    /// @param effect The entity effect to remove
    public void remove(@Nonnull IEntityItemEffect effect) {
        this.entityEffects.remove(effect);
    }

    /// Remove a specific instance of an inventory effect from this provider
    ///
    /// @param effect The inventory effect to remove
    public void remove(@Nonnull IInventoryItemEffect effect) {
        this.inventoryEffects.remove(effect);
    }

    /// Clear all entity effects for this provider
    public void clearEntityEffects() {
        this.entityEffects.clear();
    }

    /// Clear all inventory effects for this provider
    public void clearInventoryEffects() {
        this.inventoryEffects.clear();
    }
}
