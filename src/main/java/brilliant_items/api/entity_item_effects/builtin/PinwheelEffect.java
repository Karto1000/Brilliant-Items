package brilliant_items.api.entity_item_effects.builtin;

import brilliant_items.BrilliantItems;
import brilliant_items.api.ReferencableEffect;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.internal.config.HexColorAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.validation.constraints.Min;

@Setter
@Getter
@SideOnly(Side.CLIENT)
@ReferencableEffect(identifier = "pinwheel", argumentsClass = PinwheelEffect.Args.class)
public class PinwheelEffect implements IEntityItemEffect {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Args {
        /// The color of the pinwheel
        @Builder.Default
        @JsonAdapter(HexColorAdapter.class)
        public int color = 0xFFFFFFFF;

        /// The width of the pinwheel in blocks
        @Builder.Default
        @Min(0)
        public float width = 0.75F;

        /// The height of the pinwheel in blocks
        @Builder.Default
        @Min(0)
        public float height = 0.75F;
    }

    private final static ResourceLocation PINWHEEL_TEXTURE = new ResourceLocation(
            BrilliantItems.MODID,
            "textures/pinwheel.png"
    );
    private final Args options;

    public PinwheelEffect(PinwheelEffect.Args args) {
        this.options = args;
    }

    /// Called each frame before the item is rendered
    ///
    /// @param entity                  The Entity item to be rendered
    /// @param manager                 The instance of the Render Manager
    /// @param vanillaRenderEntityItem The vanilla implementation for rendering the EntityItem
    /// @param vanillaRenderItem       The vanilla implementation for rendering an Item
    /// @param x                       The x offset of the item in the world from the player
    /// @param y                       The y offset of the item in the world from the player
    /// @param z                       The z offset of the item in the world from the player
    /// @param partialTicks            The partial Ticks
    @Override
    public void renderPass(
            @Nonnull EntityItem entity,
            @Nonnull RenderManager manager,
            @Nonnull RenderEntityItem vanillaRenderEntityItem,
            @Nonnull RenderItem vanillaRenderItem,
            double x,
            double y,
            double z,
            float partialTicks
    ) {
        float pinwheelA = (float) (this.options.color >> 24 & 255) / 255;
        float pinwheelR = (float) (this.options.color >> 16 & 255) / 255;
        float pinwheelG = (float) (this.options.color >> 8 & 255) / 255;
        float pinwheelB = (float) (this.options.color & 255) / 255;

        if (pinwheelA > 0) {
            ItemStack stack = entity.getItem();
            IBakedModel model = vanillaRenderItem.getItemModelWithOverrides(stack, entity.world, null);
            float groundOffset = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;

            float bobYValue = vanillaRenderEntityItem.shouldBob()
                    ? MathHelper.sin(((float) entity.getAge() + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F
                    : 0;

            float rotationAngle = (((float) entity.getAge() + partialTicks) / 20.0F + entity.hoverStart) * (180F / (float) Math.PI);

            EntityPlayerSP player = Minecraft.getMinecraft().player;
            double dotP = player.getLookVec()
                    .dotProduct(
                            entity
                                    .getPositionVector()
                                    .add(0, bobYValue + 0.2 * groundOffset, 0)
                                    .subtract(player.getPositionEyes(partialTicks)).normalize()
                    );
            float minProd = 0.99F;
            boolean isLookingAtItem = dotP > minProd;

            float pinwheelWidth = this.options.width;
            float pinwheelHeight = this.options.height;
            if (isLookingAtItem) pinwheelWidth = pinwheelWidth + ((float) dotP - minProd) * 20;
            if (isLookingAtItem) pinwheelHeight = pinwheelHeight + ((float) dotP - minProd) * 20;

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            GlStateManager.enableTexture2D();
            GlStateManager.disableCull();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);

            GlStateManager.pushMatrix();

            GlStateManager.translate(x, y + bobYValue + 0.2 * groundOffset + pinwheelHeight / 4, z);
            GlStateManager.rotate(-manager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(manager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(rotationAngle, 0, 0, 1);

            // Move it back a bit so the item is in front of the effect
            GlStateManager.translate(0.0F, 0.0F, 0.10F);

            // Draw the pinwheel
            Minecraft.getMinecraft().renderEngine.bindTexture(PINWHEEL_TEXTURE);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(-pinwheelWidth / 2, -pinwheelHeight / 2, 0).tex(0, 1).color(
                    pinwheelR,
                    pinwheelG,
                    pinwheelB,
                    pinwheelA
            ).endVertex();
            buffer.pos(pinwheelWidth / 2, -pinwheelHeight / 2, 0).tex(1, 1).color(
                    pinwheelR,
                    pinwheelG,
                    pinwheelB,
                    pinwheelA
            ).endVertex();
            buffer.pos(pinwheelWidth / 2, pinwheelHeight / 2, 0).tex(1, 0).color(
                    pinwheelR,
                    pinwheelG,
                    pinwheelB,
                    pinwheelA
            ).endVertex();
            buffer.pos(-pinwheelWidth / 2, pinwheelHeight / 2, 0).tex(0, 0).color(
                    pinwheelR,
                    pinwheelG,
                    pinwheelB,
                    pinwheelA
            ).endVertex();

            tessellator.draw();

            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
            GlStateManager.blendFunc(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
            );
            GlStateManager.enableCull();
            GlStateManager.enableLighting();

            GlStateManager.popMatrix();
        }
    }
}
