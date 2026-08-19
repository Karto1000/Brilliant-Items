package brilliant_items.api.inventory_item_effects.builtin;

import brilliant_items.api.ReferencableEffect;
import brilliant_items.api.inventory_item_effects.*;
import brilliant_items.internal.config.HexListColorAdapter;
import brilliant_items.internal.rendering.ShaderNotFoundException;
import brilliant_items.internal.util.ColorUtil;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Optional;

@SideOnly(Side.CLIENT)
@ReferencableEffect(identifier = "radial_glow", argumentsClass = RadialGlowEffect.Args.class)
public class RadialGlowEffect implements IInventoryItemShaderEffect {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Args {
        /// A list of colors which will be smoothly interpolated between
        @Builder.Default
        @JsonAdapter(HexListColorAdapter.class)
        public NonNullList<Integer> colors = NonNullList.create();

        /// The amount of time it takes to interpolate from one color to the next.
        /// Doesn't have an effect if only one color is specified
        @Builder.Default
        public Duration duration = Duration.ZERO;
    }

    public static final String GLOW_SHADER_DESIGNATION = "radial_glow";

    @Nonnull
    private final Args options;

    public RadialGlowEffect(@Nonnull Args args) {
        this.options = args;
    }

    /// Should return the OpenGL id referencing the shader program
    ///
    /// @return The shader program id
    @Override
    public int getShaderProgramId() throws ShaderNotFoundException {
        Optional<Integer> id = ShaderManager.getProgramId(GLOW_SHADER_DESIGNATION);
        return id.orElseThrow(() -> new ShaderNotFoundException("No Shader program"));
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
        IInventoryItemShaderEffect.super.renderPass(tessellator, buffer, player, stack, uvs, localPos, absolutePos);

        int programId = this.getShaderProgramId();
        int colorUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_color");

        float[] color = ColorUtil.smoothInterpolate(
                this.options.duration,
                this.options.colors
        );

        if (colorUniform != -1)
            ARBShaderObjects.glUniform4fARB(colorUniform, color[1], color[2], color[3], color[0]);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(localPos.left, localPos.top, 0.0D).tex(uvs.left, uvs.top).endVertex();
        buffer.pos(localPos.left, localPos.bottom, 0.0D).tex(uvs.left, uvs.bottom).endVertex();
        buffer.pos(localPos.right, localPos.bottom, 0.0D).tex(uvs.right, uvs.bottom).endVertex();
        buffer.pos(localPos.right, localPos.top, 0.0D).tex(uvs.right, uvs.top).endVertex();
        tessellator.draw();
    }
}
