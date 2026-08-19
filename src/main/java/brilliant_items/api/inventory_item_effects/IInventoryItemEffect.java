package brilliant_items.api.inventory_item_effects;

import brilliant_items.api.IEffect;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/// An effect that is applied to an item when it is viewed in any inventory
@SideOnly(Side.CLIENT)
public interface IInventoryItemEffect extends IEffect {
    /// Return a render mode. This decides whether the `renderPass` function should be called before or after the item
    /// has been rendered
    ///
    /// @return The Render Mode
    @Nonnull
    default RenderMode getRenderMode() {
        return RenderMode.IN_FRONT;
    }

    /// Whether this effect should be rendered while the item is not inside a slot.
    /// For example, when the user is holding an item with their mouse
    ///
    /// @return Whether this item should be rendered when not inside a slot
    default boolean shouldRenderWhenNotInSlot() {
        return true;
    }

    /// Runs each frame to render the effect
    ///
    /// @param tessellator The Tessellator Instance
    /// @param buffer      The Buffer Instance
    /// @param player      The current player
    /// @param stack       The stack the effect is applied to
    /// @param uvs         The uv coordinates of the texture in the framebuffer
    /// @param localPos    The local position of the item in the current gui container
    /// @param absolutePos The absolute position of the item on the entire screen
    void renderPass(
            @Nonnull Tessellator tessellator,
            @Nonnull BufferBuilder buffer,
            @Nonnull EntityLivingBase player,
            @Nonnull ItemStack stack,
            @Nonnull AbsoluteItemTextureUV uvs,
            @Nonnull LocalItemCoordinates localPos,
            @Nonnull AbsoluteItemCoordinates absolutePos
    );
}
