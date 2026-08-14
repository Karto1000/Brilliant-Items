package brilliant_items.internal.capabilities;

import brilliant_items.api.entity_item_effects.IBrilliantEntityItemEffect;
import brilliant_items.api.item_effects.IBrilliantInventoryEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SideOnly(Side.CLIENT)
public class ItemEffects {
    @Nonnull
    private final List<IBrilliantEntityItemEffect> entityEffects = new ArrayList<>();

    @Nonnull
    private final List<IBrilliantInventoryEffect> inventoryEffects = new ArrayList<>();

    /// Get a list of each effect entity effect that is held by this provider
    ///
    /// @return A list of entity effects
    @Nonnull
    public List<IBrilliantEntityItemEffect> getEntityEffects() {
        return this.entityEffects;
    }

    /// Get a list of each inventory effect that is held by this provider
    ///
    /// @return A list of inventory effects
    @Nonnull
    public List<IBrilliantInventoryEffect> getInventoryEffects() {
        return inventoryEffects;
    }

    /// Add a new entity effect to this provider
    ///
    /// @param effect The entity effect to add
    public void add(@Nonnull IBrilliantEntityItemEffect effect) {
        this.entityEffects.add(effect);
    }

    /// Add a new inventory effect to this provider
    ///
    /// @param effect The inventory effect to add
    public void add(@Nonnull IBrilliantInventoryEffect effect) {
        this.inventoryEffects.add(effect);
    }

    /// Remove a specific instance of an entity effect from this provider
    ///
    /// @param effect The entity effect to remove
    public void remove(@Nonnull IBrilliantEntityItemEffect effect) {
        this.entityEffects.remove(effect);
    }

    /// Remove a specific instance of an inventory effect from this provider
    ///
    /// @param effect The inventory effect to remove
    public void remove(@Nonnull IBrilliantInventoryEffect effect) {
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
