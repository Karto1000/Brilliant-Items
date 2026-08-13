package brilliant_items.registry;

import brilliant_items.BrilliantItems;
import brilliant_items.rendering.IBrilliantItemEffect;
import brilliant_items.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TestItem extends Item implements IBrilliantItemEffect {
    public TestItem() {
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName(BrilliantItems.MODID, "test_item");
        this.setTranslationKey("brilliant_items.test_item");
    }

    /// Get the pinwheel color as ARGB
    ///
    /// @param entity       The item entity in the world
    /// @param x            The x offset from the player entity
    /// @param y            The y offset from the player entity
    /// @param z            The z offset from the player entity
    /// @param partialTicks The partial ticks
    /// @return The ARGB color
    @Override
    public int getPinwheelColor(@Nonnull EntityItem entity, double x, double y, double z, float partialTicks) {
        return 0xFFFFFFFF;
    }

    /// Get the glow pillar color as ARGB
    ///
    /// @param entity       The item entity in the world
    /// @param x            The x offset from the player entity
    /// @param y            The y offset from the player entity
    /// @param z            The z offset from the player entity
    /// @param partialTicks The partial ticks
    /// @return The ARGB color
    @Override
    public int getGlowPillarColor(@Nonnull EntityItem entity, double x, double y, double z, float partialTicks) {
        return 0xFFFFFFFF;
    }

    @Override
    public float getPinwheelWidth(@Nonnull EntityItem entity, double x, double y, double z, float partialTicks) {
        return 0.75F;
    }

    @Override
    public float getPinwheelHeight(@Nonnull EntityItem entity, double x, double y, double z, float partialTicks) {
        return 0.75F;
    }
}
