package brilliant_items.rendering;

import brilliant_items.BrilliantItems;
import brilliant_items.handlers.ForgeConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class BrilliantItemRenderer extends Render<EntityItem> {
    private final static ResourceLocation PINWHEEL_TEXTURE = new ResourceLocation(
            BrilliantItems.MODID,
            "textures/pinwheel.png"
    );
    private final static ResourceLocation GLOW_PILLAR_TEXTURE = new ResourceLocation(
            BrilliantItems.MODID,
            "textures/glow_pillar.png"
    );

    IBrilliantItemEffect defaultEffectImpl = new IBrilliantItemEffect() {
        @Override
        public int getPinwheelColor(
                @Nonnull EntityItem entity,
                double x,
                double y,
                double z,
                float partialTicks
        ) {
            if (!ForgeConfigHandler.client.SHOULD_RARITY_INFLUENCE_COLOR) return 0x00000000;
            ItemStack stack = entity.getItem();
            char color = stack.getItem().getForgeRarity(stack).getColor().toString().charAt(1);
            return Minecraft.getMinecraft().fontRenderer.getColorCode(color) | 0x55000000;
        }

        @Override
        public int getGlowPillarColor(
                @Nonnull EntityItem entity,
                double x,
                double y,
                double z,
                float partialTicks
        ) {
            if (!ForgeConfigHandler.client.SHOULD_RARITY_INFLUENCE_COLOR) return 0x00000000;
            ItemStack stack = entity.getItem();
            char color = stack.getItem().getForgeRarity(stack).getColor().toString().charAt(1);
            return Minecraft.getMinecraft().fontRenderer.getColorCode(color) | 0x55000000;
        }
    };

    @Nonnull
    private final RenderEntityItem vanillaRenderEntityItem;

    @Nonnull
    private final RenderItem vanillaRenderItem;

    public BrilliantItemRenderer(
            @Nonnull RenderManager manager,
            @Nonnull RenderEntityItem vanillaRenderEntityItem,
            @Nonnull RenderItem vanillaRenderItem
    ) {
        super(manager);

        this.vanillaRenderItem = vanillaRenderItem;
        this.vanillaRenderEntityItem = vanillaRenderEntityItem;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityItem entity) {
        return null;
    }

    private void renderShine(
            @Nonnull EntityItem entity,
            @Nonnull IBrilliantItemEffect biEffect,
            double x,
            double y,
            double z,
            float partialTicks
    ) {
        float bobYValue = this.vanillaRenderEntityItem.shouldBob()
                ? MathHelper.sin(((float) entity.getAge() + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F
                : 0;

        float rotationAngle = (((float) entity.getAge() + partialTicks) / 20.0F + entity.hoverStart) * (180F / (float) Math.PI);

        ItemStack stack = entity.getItem();
        IBakedModel model = this.vanillaRenderItem.getItemModelWithOverrides(stack, entity.world, null);
        float groundOffset = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.enableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);

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

        int pinwheelColor = biEffect.getPinwheelColor(entity, x, y, z, partialTicks);
        float pinwheelA = (float) (pinwheelColor >> 24 & 255) / 255;

        int glowPillarColor = biEffect.getGlowPillarColor(entity, x, y, z, partialTicks);
        float glowPillarA = (float) (glowPillarColor >> 24 & 255) / 255;

        if (glowPillarA > 0) {
            float glowPillarWidth = biEffect.getGlowPillarWidth(entity, x, y, z, partialTicks);
            float glowPillarHeight = biEffect.getGlowPillarHeight(entity, x, y, z, partialTicks);
            if (isLookingAtItem) glowPillarWidth = glowPillarWidth + ((float) dotP - minProd) * 20;

            float glowPillarR = (float) (glowPillarColor >> 16 & 255) / 255;
            float glowPillarG = (float) (glowPillarColor >> 8 & 255) / 255;
            float glowPillarB = (float) (glowPillarColor & 255) / 255;

            GlStateManager.pushMatrix();

            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

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

            GlStateManager.popMatrix();
        }

        if (pinwheelA > 0) {
            float pinwheelHeight = biEffect.getPinwheelHeight(entity, x, y, z, partialTicks);
            float pinwheelWidth = biEffect.getPinwheelWidth(entity, x, y, z, partialTicks);
            if (isLookingAtItem) pinwheelWidth = pinwheelWidth + ((float) dotP - minProd) * 20;
            if (isLookingAtItem) pinwheelHeight = pinwheelHeight + ((float) dotP - minProd) * 20;

            float pinwheelR = (float) (pinwheelColor >> 16 & 255) / 255;
            float pinwheelG = (float) (pinwheelColor >> 8 & 255) / 255;
            float pinwheelB = (float) (pinwheelColor & 255) / 255;

            GlStateManager.pushMatrix();

            GlStateManager.translate(x, y + bobYValue + 0.2 * groundOffset + pinwheelHeight / 4, z);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(rotationAngle, 0, 0, 1);

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

            GlStateManager.popMatrix();
        }

        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
    }

    @Override
    public void doRender(
            @Nonnull EntityItem entity,
            double x,
            double y,
            double z,
            float entityYaw,
            float partialTicks
    ) {
        boolean shouldShineForRareItems = ForgeConfigHandler.client.SHOULD_RARITY_INFLUENCE_COLOR &&
                entity.getItem().getItem().getForgeRarity(entity.getItem()) != EnumRarity.COMMON;

        if (entity.getItem().getItem() instanceof IBrilliantItemEffect)
            this.renderShine(entity, (IBrilliantItemEffect) entity.getItem().getItem(), x, y, z, partialTicks);
        else if (shouldShineForRareItems) this.renderShine(entity, defaultEffectImpl, x, y, z, partialTicks);

        vanillaRenderEntityItem.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
