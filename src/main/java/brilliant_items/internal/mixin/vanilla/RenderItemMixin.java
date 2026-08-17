package brilliant_items.internal.mixin.vanilla;

import brilliant_items.api.inventory_item_effects.*;
import brilliant_items.internal.capabilities.ItemEffects;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import brilliant_items.internal.rendering.InventoryEffectFramebuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.nio.FloatBuffer;

@SideOnly(Side.CLIENT)
@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Shadow
    @Final
    private TextureManager textureManager;
    @Unique
    private Framebuffer brilliantItems$currentTarget;
    @Unique
    private final FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(16);

    @Inject(method = "renderItemAndEffectIntoGUI(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"))
    public void brilliant_items_renderItemAndEffectIntoGUI_bindFramebuffer(
            EntityLivingBase player,
            ItemStack stack,
            int x,
            int y,
            CallbackInfo ci
    ) {
        ItemEffects effects = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (effects == null) return;
        if (effects.getInventoryEffects().isEmpty()) return;

        Minecraft mc = Minecraft.getMinecraft();
        brilliantItems$currentTarget = InventoryEffectFramebuffer.getTarget(mc.displayWidth, mc.displayHeight);
        brilliantItems$currentTarget.framebufferClear();
        brilliantItems$currentTarget.bindFramebuffer(true);
    }

    @Unique
    private AbsoluteItemCoordinates brilliantItems$getAbsoluteItemCoordinates(LocalItemCoordinates pos) {
        MATRIX_BUFFER.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, MATRIX_BUFFER);

        // Extract 2D Affine Matrix components:
        // m0: X Scale / Rotation,  m4: X Shear
        // m1: Y Shear,             m5: Y Scale / Rotation
        // m12: X Translation,      m13: Y Translation
        float m0 = MATRIX_BUFFER.get(0);
        float m1 = MATRIX_BUFFER.get(1);
        float m4 = MATRIX_BUFFER.get(4);
        float m5 = MATRIX_BUFFER.get(5);
        float m12 = MATRIX_BUFFER.get(12);
        float m13 = MATRIX_BUFFER.get(13);

        float top = m1 * pos.left + m5 * pos.top + m13;
        float right = m0 * pos.right + m4 * pos.bottom + m12;
        float bottom = m1 * pos.right + m5 * pos.bottom + m13;
        float left = m0 * pos.left + m4 * pos.top + m12;

        return new AbsoluteItemCoordinates(
                top,
                right,
                bottom,
                left
        );
    }

    @Unique
    private AbsoluteItemTextureUV brilliantItems$getAbsoluteItemTextureUV(LocalItemCoordinates pos) {
        MATRIX_BUFFER.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, MATRIX_BUFFER);

        // Extract 2D Affine Matrix components:
        // m0: X Scale / Rotation,  m4: X Shear
        // m1: Y Shear,             m5: Y Scale / Rotation
        // m12: X Translation,      m13: Y Translation
        float m0 = MATRIX_BUFFER.get(0);
        float m1 = MATRIX_BUFFER.get(1);
        float m4 = MATRIX_BUFFER.get(4);
        float m5 = MATRIX_BUFFER.get(5);
        float m12 = MATRIX_BUFFER.get(12);
        float m13 = MATRIX_BUFFER.get(13);

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();

        float top = 1 - (m1 * pos.left + m5 * pos.top + m13) / height;
        float right = (m0 * pos.right + m4 * pos.bottom + m12) / width;
        float bottom = 1 - (m1 * pos.right + m5 * pos.bottom + m13) / height;
        float left = (m0 * pos.left + m4 * pos.top + m12) / width;

        return new AbsoluteItemTextureUV(
                top,
                right,
                bottom,
                left
        );
    }

    @Unique
    private void brilliantItems$drawItemTexture(
            @Nonnull Tessellator tessellator,
            @Nonnull BufferBuilder buffer,
            @Nonnull AbsoluteItemTextureUV uvs,
            @Nonnull LocalItemCoordinates pos
    ) {
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(pos.left, pos.top, 0.0D).tex(uvs.left, uvs.top).endVertex();
        buffer.pos(pos.left, pos.bottom, 0.0D).tex(uvs.left, uvs.bottom).endVertex();
        buffer.pos(pos.right, pos.bottom, 0.0D).tex(uvs.right, uvs.bottom).endVertex();
        buffer.pos(pos.right, pos.top, 0.0D).tex(uvs.right, uvs.top).endVertex();
        tessellator.draw();
    }

    @Inject(method = "renderItemAndEffectIntoGUI(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", at = @At("TAIL"))
    public void brilliant_items_renderItemAndEffectIntoGUI_runShaders(
            EntityLivingBase player,
            ItemStack stack,
            int x,
            int y,
            CallbackInfo ci
    ) {
        if (brilliantItems$currentTarget == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        mc.getFramebuffer().bindFramebuffer(true);

        ItemEffects effects = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (effects == null) {
            brilliantItems$currentTarget = null;
            return;
        }
        if (effects.getInventoryEffects().isEmpty()) {
            brilliantItems$currentTarget = null;
            return;
        }

        brilliantItems$currentTarget.bindFramebufferTexture();

        ScaledResolution res = new ScaledResolution(mc);

        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, res.getScaledWidth(), res.getScaledHeight(), 0.0D, 0D, 3000.0D);

        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();

        GlStateManager.disableDepth();
        GlStateManager.disableLighting();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float padding = 0;
        float minX = x - padding;
        float minY = y - padding;
        float maxX = x + 16 + padding;
        float maxY = y + 16 + padding;

        LocalItemCoordinates localItemCoordinates = new LocalItemCoordinates(minY, maxX, maxY, minX);
        AbsoluteItemCoordinates absoluteItemCoordinates = brilliantItems$getAbsoluteItemCoordinates(localItemCoordinates);
        AbsoluteItemTextureUV texturePosition = brilliantItems$getAbsoluteItemTextureUV(localItemCoordinates);

        boolean behind = true;
        for (IInventoryItemEffect effect : effects.getInventoryEffects()) {
            if (effect.getRenderMode() == RenderMode.IN_FRONT && behind) {
                // Here we render the item when the render mode switches from behind to in_front
                this.brilliantItems$drawItemTexture(tessellator, buffer, texturePosition, localItemCoordinates);
                behind = false;
            }

            effect.renderPass(
                    tessellator,
                    buffer,
                    player,
                    stack,
                    texturePosition,
                    localItemCoordinates,
                    absoluteItemCoordinates
            );

            ARBShaderObjects.glUseProgramObjectARB(0);
        }

        // Render the item if we haven't already
        if (behind) this.brilliantItems$drawItemTexture(tessellator, buffer, texturePosition, localItemCoordinates);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.popMatrix();

        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        brilliantItems$currentTarget = null;
    }
}