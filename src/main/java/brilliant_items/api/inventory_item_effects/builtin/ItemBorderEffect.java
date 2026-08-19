package brilliant_items.api.inventory_item_effects.builtin;

import brilliant_items.BrilliantItems;
import brilliant_items.api.ReferencableEffect;
import brilliant_items.api.inventory_item_effects.*;
import brilliant_items.internal.config.HexColorAdapter;
import brilliant_items.internal.util.ColorUtil;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
@ReferencableEffect(identifier = "border", argumentsClass = ItemBorderEffect.Args.class)
public class ItemBorderEffect implements IInventoryItemEffect {
    private static final ResourceLocation BORDER_TEXTURE = new ResourceLocation(
            BrilliantItems.MODID,
            "textures/borders/border.png"
    );
    @Nonnull
    private final Args options;

    @AllArgsConstructor
    @Builder
    public static class Args {
        @JsonAdapter(HexColorAdapter.class)
        @Builder.Default
        public int color = 0xFFFFFFFF;
    }

    public ItemBorderEffect(@Nonnull Args options) {
        this.options = options;
    }

    @Override
    public boolean shouldRenderWhenNotInSlot() {
        return false;
    }

    @Nonnull
    @Override
    public RenderMode getRenderMode() {
        return RenderMode.BEHIND;
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
    @Override
    public void renderPass(
            @Nonnull Tessellator tessellator,
            @Nonnull BufferBuilder buffer,
            @Nonnull EntityLivingBase player,
            @Nonnull ItemStack stack,
            @Nonnull AbsoluteItemTextureUV uvs,
            @Nonnull LocalItemCoordinates localPos,
            @Nonnull AbsoluteItemCoordinates absolutePos
    ) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(BORDER_TEXTURE);

        float[] colors = ColorUtil.colorIntToNormFloat(this.options.color);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(localPos.left, localPos.top, 0.0D).tex(0, 0).color(colors[1], colors[2], colors[3], colors[0]).endVertex();
        buffer.pos(localPos.left, localPos.bottom, 0.0D).tex(0, 1).color(colors[1], colors[2], colors[3], colors[0]).endVertex();
        buffer.pos(localPos.right, localPos.bottom, 0.0D).tex(1, 1).color(colors[1], colors[2], colors[3], colors[0]).endVertex();
        buffer.pos(localPos.right, localPos.top, 0.0D).tex(1, 0).color(colors[1], colors[2], colors[3], colors[0]).endVertex();
        tessellator.draw();
    }
}
