package brilliant_items.capabilities;

import brilliant_items.api.effects.IBrilliantItemEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ItemEffects {
    private final List<IBrilliantItemEffect> effects = new ArrayList<>();

    /// Get a list of each effect that is held by this provider
    ///
    /// @return A list of effects
    public List<IBrilliantItemEffect> getEffects() {
        return this.effects;
    }

    /// Add a new effect to this provider
    ///
    /// @param effect The effect to add
    public void addEffect(IBrilliantItemEffect effect) {
        this.effects.add(effect);
    }

    /// Clear all effects for this provider
    public void clearEffects() {
        this.effects.clear();
    }
}
