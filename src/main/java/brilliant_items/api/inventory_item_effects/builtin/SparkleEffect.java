package brilliant_items.api.inventory_item_effects.builtin;

import brilliant_items.BrilliantItems;
import brilliant_items.api.inventory_item_effects.AbsoluteItemCoordinates;
import brilliant_items.api.inventory_item_effects.AbsoluteItemTextureUV;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.api.inventory_item_effects.LocalItemCoordinates;
import brilliant_items.internal.rendering.ShaderNotFoundException;
import brilliant_items.internal.util.ColorUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Random;

public class SparkleEffect implements IInventoryItemEffect {
    private static final ResourceLocation DEFAULT_PARTICLE_TEXTURE = new ResourceLocation(
            BrilliantItems.MODID,
            "textures/particles/glow.png"
    );
    private final SparkleEffectOptions options;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class SparkleEffectOptions {
        /// The minimum lifetime in frames
        public int minLifetime = 800;
        /// The maximum lifetime in frames
        public int maxLifetime = 2000;
        public Vec2f velocity = Vec2f.ZERO;
        public int color = 0xFFFFFFFF;
        public int amountOfSparkles = 3;
        public float size = 2.5F;
        public ResourceLocation texture = DEFAULT_PARTICLE_TEXTURE;
    }

    public SparkleEffect(SparkleEffectOptions options) {
        this.options = options;
    }

    /// Should return the OpenGL id referencing the shader program
    ///
    /// @return The shader program id
    @Override
    public int getShaderProgramId() throws ShaderNotFoundException {
        // We don't need a shader here
        return -1;
    }

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
        long time = Minecraft.getSystemTime();

        long baseSeed = (long) (localPos.left * 137.0f) ^ (long) (localPos.top * 149.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.options.texture);

        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (int i = 0; i < this.options.amountOfSparkles; i++) {
            // Generate a set seed for each particle
            long sparkleSeed = baseSeed ^ (i * 31337L);
            Random rand = new Random(sparkleSeed);

            long lifetime = this.options.minLifetime + rand.nextInt(this.options.maxLifetime - this.options.minLifetime);
            long cycle = time / lifetime;

            rand.setSeed(sparkleSeed ^ cycle);

            // Value linearly going from 0-1 with 0 being the starting value
            float phase = (time % lifetime) / (float) lifetime;

            float offsetX = 1.0f + rand.nextFloat() * 14.0f;
            float offsetY = 1.0f + rand.nextFloat() * 14.0f;

            float centerX = localPos.left + offsetX + (this.options.velocity.x * phase);
            float centerY = localPos.top + offsetY + (this.options.velocity.y * phase);
            float intensity = (float) Math.sin(phase * Math.PI);

            float size = this.options.size * intensity;

            float[] color = ColorUtil.colorIntToNormFloat(this.options.color);
            float a = intensity * color[0];
            float r = color[1];
            float g = color[2];
            float b = color[3];

            buffer.pos(centerX - size, centerY - size, 0.0D).tex(0.0D, 0.0D).color(r, g, b, a).endVertex();
            buffer.pos(centerX - size, centerY + size, 0.0D).tex(0.0D, 1.0D).color(r, g, b, a).endVertex();
            buffer.pos(centerX + size, centerY + size, 0.0D).tex(1.0D, 1.0D).color(r, g, b, a).endVertex();
            buffer.pos(centerX + size, centerY - size, 0.0D).tex(1.0D, 0.0D).color(r, g, b, a).endVertex();
        }

        tessellator.draw();

        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
    }
}
