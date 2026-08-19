package brilliant_items.api.inventory_item_effects.builtin;

import brilliant_items.api.ReferencableEffect;
import brilliant_items.api.inventory_item_effects.*;
import brilliant_items.internal.config.HexColorAdapter;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.hibernate.validator.constraints.Range;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Optional;

@SideOnly(Side.CLIENT)
@ReferencableEffect(identifier = "glow_outline", argumentsClass = GlowOutlineEffect.Args.class)
public class GlowOutlineEffect implements IInventoryItemShaderEffect {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Args {
        @Builder.Default
        @JsonAdapter(HexColorAdapter.class)
        public int color = 0xFFFFFFFF;

        @Builder.Default
        @Range(min = 0, max = 10)
        public int blurRadius = 2;

        @Builder.Default
        @Range(min = 0, max = 3)
        public float sigma = 1.2F;
    }

    @Nonnull
    private final Args options;
    public static final String OUTLINE_SHADER_DEFINITION = "glow_outline";

    public GlowOutlineEffect(@Nonnull Args args) {
        this.options = args;
    }

    @Nonnull
    @Override
    public RenderMode getRenderMode() {
        return RenderMode.BEHIND;
    }

    /// Should return the OpenGL id referencing the shader program.
    ///
    /// Return `-1` if you don't need a shader. Make sure, however, that you don't call a
    /// `IInventoryItemEffect.super.renderPass;` inside your implementation if it is -1
    ///
    /// @return The shader program id
    @Override
    public int getShaderProgramId() throws ShaderNotFoundException {
        Optional<Integer> id = ShaderManager.getProgramId(OUTLINE_SHADER_DEFINITION);
        return id.orElseThrow(() -> new ShaderNotFoundException("No Shader program called " + OUTLINE_SHADER_DEFINITION));
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
        int radiusUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_radius");
        int sigmaUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_sigma");

        float[] normColors = ColorUtil.colorIntToNormFloat(this.options.color);
        if (colorUniform != -1)
            ARBShaderObjects.glUniform4fARB(colorUniform, normColors[1], normColors[2], normColors[3], normColors[0]);

        if (radiusUniform != -1) ARBShaderObjects.glUniform1iARB(radiusUniform, this.options.blurRadius);
        if (sigmaUniform != -1) ARBShaderObjects.glUniform1fARB(sigmaUniform, this.options.sigma);


        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(localPos.left - 1, localPos.top - 1, 0.0D).tex(uvs.left, uvs.top).endVertex();
        buffer.pos(localPos.left - 1, localPos.bottom + 1, 0.0D).tex(uvs.left, uvs.bottom).endVertex();
        buffer.pos(localPos.right + 1, localPos.bottom + 1, 0.0D).tex(uvs.right, uvs.bottom).endVertex();
        buffer.pos(localPos.right + 1, localPos.top - 1, 0.0D).tex(uvs.right, uvs.top).endVertex();
        tessellator.draw();
    }
}
