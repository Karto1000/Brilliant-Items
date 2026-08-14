package brilliant_items.internal.mixin.vanilla;

import brilliant_items.api.item_effects.IBrilliantInventoryEffect;
import brilliant_items.internal.rendering.InventoryEffectFramebuffer;
import brilliant_items.internal.capabilities.ItemEffects;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Unique
    private Framebuffer brilliantItems$currentTarget;

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

        for (IBrilliantInventoryEffect effect : effects.getInventoryEffects()) {
            effect.renderPass(tessellator, buffer, player, stack, x, y);
            ARBShaderObjects.glUseProgramObjectARB(0);
        }

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