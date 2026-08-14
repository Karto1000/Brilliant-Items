package brilliant_items.capabilities;

import brilliant_items.api.effects.IBrilliantItemEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SideOnly(Side.CLIENT)
public class ItemEffects {
    @Nonnull
    private final List<IBrilliantItemEffect> effects = new ArrayList<>();

    /// Get a list of each effect that is held by this provider
    ///
    /// @return A list of effects
    @Nonnull
    public List<IBrilliantItemEffect> getEffects() {
        return this.effects;
    }

    /// Add a new effect to this provider
    ///
    /// @param effect The effect to add
    public void add(@Nonnull IBrilliantItemEffect effect) {
        this.effects.add(effect);
    }

    /// Remove a specific instance of an effect from this provider
    ///
    /// @param effect The effect to remove
    public void remove(@Nonnull IBrilliantItemEffect effect) {
        this.effects.remove(effect);
    }

    /// Returns a Stream of the internal effects list
    ///
    /// @return The Stream
    @Nonnull
    public Stream<IBrilliantItemEffect> stream() {
        return this.effects.stream();
    }

    /// Clear all effects for this provider
    public void clear() {
        this.effects.clear();
    }
}
