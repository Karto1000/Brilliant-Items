package brilliant_items.rendering;

import net.minecraft.entity.item.EntityItem;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;

public interface IBrilliantItemEffect {
    /// Get the pinwheel color as ARGB
    ///
    /// @param entity       The item entity in the world
    /// @param x            The x offset from the player entity
    /// @param y            The y offset from the player entity
    /// @param z            The z offset from the player entity
    /// @param partialTicks The partial ticks
    /// @return The ARGB color
    int getPinwheelColor(
            @Nonnull EntityItem entity,
            double x,
            double y,
            double z,
            float partialTicks
    );

    /// Get the glow pillar color as ARGB
    ///
    /// @param entity       The item entity in the world
    /// @param x            The x offset from the player entity
    /// @param y            The y offset from the player entity
    /// @param z            The z offset from the player entity
    /// @param partialTicks The partial ticks
    /// @return The ARGB color
    int getGlowPillarColor(
            @Nonnull EntityItem entity,
            double x,
            double y,
            double z,
            float partialTicks
    );

    /// The pinwheel width in blocks
    ///
    /// @param entity       The item entity in the world
    /// @param x            The x offset from the player entity
    /// @param y            The y offset from the player entity
    /// @param z            The z offset from the player entity
    /// @param partialTicks The partial ticks
    /// @return The width
    default float getPinwheelWidth(
            @Nonnull EntityItem entity,
            double x,
            double y,
            double z,
            float partialTicks
    ) {
        return 0.6F;
    }

    /// The pinwheel height in blocks
    ///
    /// @param entity       The item entity in the world
    /// @param x            The x offset from the player entity
    /// @param y            The y offset from the player entity
    /// @param z            The z offset from the player entity
    /// @param partialTicks The partial ticks
    /// @return the height
    default float getPinwheelHeight(
            @Nonnull EntityItem entity,
            double x,
            double y,
            double z,
            float partialTicks
    ) {
        return 0.6F;
    }

    /// The glow pillar width in blocks
    ///
    /// @param entity       The item entity in the world
    /// @param x            The x offset from the player entity
    /// @param y            The y offset from the player entity
    /// @param z            The z offset from the player entity
    /// @param partialTicks The partial ticks
    /// @return The width
    default float getGlowPillarWidth(
            @Nonnull EntityItem entity,
            double x,
            double y,
            double z,
            float partialTicks
    ) {
        return 0.3F;
    }

    /// The glow pillar height in blocks
    ///
    /// @param entity       The item entity in the world
    /// @param x            The x offset from the player entity
    /// @param y            The y offset from the player entity
    /// @param z            The z offset from the player entity
    /// @param partialTicks The partial ticks
    /// @return the height
    default float getGlowPillarHeight(
            @Nonnull EntityItem entity,
            double x,
            double y,
            double z,
            float partialTicks
    ) {
        Random random = new Random( (long) entity.hashCode() * entity.hashCode());
        return random.nextFloat() / 2 + 1.5F;
    }
}
