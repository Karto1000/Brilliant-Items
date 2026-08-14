package brilliant_items.api.entity_item_effects.builtin;

import brilliant_items.BrilliantItems;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import lombok.Getter;
import lombok.Setter;
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

@Setter
@Getter
@SideOnly(Side.CLIENT)
public class GlowPillarEffect implements IEntityItemEffect {
    private static final ResourceLocation GLOW_PILLAR_TEXTURE = new ResourceLocation(
            BrilliantItems.MODID,
            "textures/glow_pillar.png"
    );

    private int color;
    private float width;
    private float height;

    public GlowPillarEffect(float width, float height, int color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

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
        float glowPillarA = (float) (this.getColor() >> 24 & 255) / 255;
        float glowPillarR = (float) (this.getColor() >> 16 & 255) / 255;
        float glowPillarG = (float) (this.getColor() >> 8 & 255) / 255;
        float glowPillarB = (float) (this.getColor() & 255) / 255;

        if (glowPillarA > 0) {
            float glowPillarWidth = this.width;
            float glowPillarHeight = this.height;
            float bobYValue = vanillaRenderEntityItem.shouldBob()
                    ? MathHelper.sin(((float) entity.getAge() + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F
                    : 0;

            ItemStack stack = entity.getItem();
            IBakedModel model = vanillaRenderItem.getItemModelWithOverrides(stack, entity.world, null);
            float groundOffset = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;

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

            if (isLookingAtItem) glowPillarWidth = glowPillarWidth + ((float) dotP - minProd) * 20;

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            GlStateManager.enableTexture2D();
            GlStateManager.disableCull();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);

            GlStateManager.pushMatrix();

            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-manager.playerViewY, 0.0F, 1.0F, 0.0F);

            // Move it back a bit so the item is in front of the effect
            GlStateManager.translate(0.0F, 0.0F, 0.10F);

            // Draw the glow pillar
            Minecraft.getMinecraft().renderEngine.bindTexture(GLOW_PILLAR_TEXTURE);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(-glowPillarWidth / 2, 0, 0)
                    .tex(0, 1)
                    .color(
                            glowPillarR,
                            glowPillarG,
                            glowPillarB,
                            glowPillarA
                    )
                    .endVertex();
            buffer.pos(glowPillarWidth / 2, 0, 0)
                    .tex(1, 1)
                    .color(
                            glowPillarR,
                            glowPillarG,
                            glowPillarB,
                            glowPillarA
                    )
                    .endVertex();
            buffer.pos(glowPillarWidth / 2, glowPillarHeight, 0)
                    .tex(1, 0)
                    .color(
                            glowPillarR,
                            glowPillarG,
                            glowPillarB,
                            glowPillarA
                    )
                    .endVertex();
            buffer.pos(-glowPillarWidth / 2, glowPillarHeight, 0)
                    .tex(0, 0)
                    .color(
                            glowPillarR,
                            glowPillarG,
                            glowPillarB,
                            glowPillarA
                    )
                    .endVertex();

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
