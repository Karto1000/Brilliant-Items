package brilliant_items.api.inventory_item_effects.builtin;

import brilliant_items.api.inventory_item_effects.*;
import brilliant_items.internal.rendering.ShaderNotFoundException;
import brilliant_items.internal.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.nio.FloatBuffer;
import java.time.Duration;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public class GlowEffect implements IInventoryItemEffect {
    public static final String GLOW_SHADER_DESIGNATION = "glow";
    private final NonNullList<Integer> colors;
    private final Duration transitionDuration;
    private static final FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(16);

    public GlowEffect(NonNullList<Integer> colors, Duration transitionDuration) {
        this.colors = colors;
        this.transitionDuration = transitionDuration;
    }

    public GlowEffect(int color) {
        this.transitionDuration = Duration.ZERO;
        this.colors = NonNullList.withSize(1, color);
    }

    /// Should return the OpenGL id referencing the shader program
    ///
    /// @return The shader program id
    @Override
    public int getShaderProgramId() throws ShaderNotFoundException {
        Optional<Integer> id = InventoryItemEffectShaderManager.getProgramId(GLOW_SHADER_DESIGNATION);
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
        IInventoryItemEffect.super.renderPass(tessellator, buffer, player, stack, uvs, localPos, absolutePos);

        int programId = this.getShaderProgramId();
        int colorUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_color");

        float[] color = ColorUtil.smoothInterpolate(
                this.transitionDuration,
                this.colors
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
