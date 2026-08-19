package brilliant_items.api.inventory_item_effects.builtin;

import brilliant_items.BrilliantItems;
import brilliant_items.api.ReferencableEffect;
import brilliant_items.api.inventory_item_effects.AbsoluteItemCoordinates;
import brilliant_items.api.inventory_item_effects.AbsoluteItemTextureUV;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.api.inventory_item_effects.LocalItemCoordinates;
import brilliant_items.internal.config.HexColorAdapter;
import brilliant_items.internal.util.ColorUtil;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import java.util.Random;

@SideOnly(Side.CLIENT)
@ReferencableEffect(identifier = "sparkle", argumentsClass = SparkleEffect.Args.class)
public class SparkleEffect implements IInventoryItemEffect {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Args {
        /// The minimum lifetime in frames
        @Min(0)
        @Builder.Default
        public int minLifetime = 800;

        /// The maximum lifetime in frames
        @Min(0)
        @Builder.Default
        public int maxLifetime = 2000;

        /// The velocity of the particle
        @Nonnull
        @Builder.Default
        public Vec2f velocity = Vec2f.ZERO;

        /// The color of the particle
        @JsonAdapter(HexColorAdapter.class)
        @Builder.Default
        public int color = 0xFFFFFFFF;

        /// The amount of particles always present over the item
        @Min(0)
        @Builder.Default
        public int amountOfSparkles = 3;

        /// The size of the particle
        @Min(0)
        @Builder.Default
        public float size = 2.5F;

        /// The texture of the particle
        @Nonnull
        @Builder.Default
        public ResourceLocation texture = DEFAULT_PARTICLE_TEXTURE;
    }

    private static final ResourceLocation DEFAULT_PARTICLE_TEXTURE = new ResourceLocation(
            BrilliantItems.MODID,
            "textures/particles/glow.png"
    );

    @Nonnull
    private final Args options;

    public SparkleEffect(@Nonnull Args options) {
        this.options = options;
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

        long baseSeed = (long) (stack.hashCode() * 137.0f) ^ (long) (stack.hashCode() * 149.0f);
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
