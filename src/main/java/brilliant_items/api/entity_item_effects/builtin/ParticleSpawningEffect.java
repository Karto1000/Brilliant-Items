package brilliant_items.api.entity_item_effects.builtin;

import brilliant_items.api.ReferencableEffect;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.internal.config.HexColorAdapter;
import brilliant_items.internal.util.ColorUtil;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@SideOnly(Side.CLIENT)
@ReferencableEffect(identifier = "particle_spawner", argumentsClass = ParticleSpawningEffect.Args.class)
public class ParticleSpawningEffect implements IEntityItemEffect {
    @Nonnull
    private final Args options;

    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    @Builder
    public static class Args {
        /// The numeric id reference to a particle
        ///
        /// @see EnumParticleTypes
        @NotNull(message = "particleId is required")
        @Nonnull
        public final Integer particleId;

        /// A 1 / x chance for a new particle to spawn every frame
        @Builder.Default
        @Min(1)
        public final int rarity = 40;

        /// The velocity of the particle
        /// **!WARNING! Changes functionality based on the type of particle (i.E it doesn't always equal the velocity)**
        @Builder.Default
        @NotNull
        @Nonnull
        public final Vec3d velocity = new Vec3d(0, 0, 0);

        /// An offset from the bottom center of the item. If not specified, a random offset is picked
        @Nullable
        public final Vec3d offset;

        /// The maximum age of the particle in frames
        @Min(0)
        @Nullable
        public final Integer maxAge;

        /// The color of the particle
        @JsonAdapter(HexColorAdapter.class)
        @Nullable
        public final Integer color;
    }

    public ParticleSpawningEffect(@Nonnull Args options) {
        this.options = options;
    }

    /// Called at the end of each frame
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
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.isGamePaused()) return;

        if (entity.world.rand.nextInt(this.options.rarity) != 0) return;

        Vec3d offset = this.options.offset == null
                ? new Vec3d(
                (entity.world.rand.nextDouble() - 0.5) * 0.5,
                (entity.world.rand.nextDouble() - 0.5) * 0.5,
                (entity.world.rand.nextDouble() - 0.5) * 0.5
        )
                : this.options.offset;

        Particle particle = mc.effectRenderer.spawnEffectParticle(
                this.options.particleId,
                entity.posX + offset.x,
                entity.posY + offset.y,
                entity.posZ + offset.z,
                this.options.velocity.x,
                this.options.velocity.y,
                this.options.velocity.z
        );

        if (particle == null) return;
        if (this.options.maxAge != null) particle.setMaxAge(this.options.maxAge);
        if (this.options.color != null) {
            float[] color = ColorUtil.colorIntToNormFloat(this.options.color);
            particle.setAlphaF(color[0]);
            particle.setRBGColorF(color[1], color[2], color[3]);
        }
    }
}
