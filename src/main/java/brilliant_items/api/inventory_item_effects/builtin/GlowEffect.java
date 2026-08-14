package brilliant_items.api.inventory_item_effects.builtin;

import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.api.inventory_item_effects.InventoryItemEffectShaderManager;
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
            int x,
            int y
    ) {
        IInventoryItemEffect.super.renderPass(tessellator, buffer, player, stack, x, y);
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        int programId = this.getShaderProgramId();
        int colorUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_color");

        float[] color = ColorUtil.smoothInterpolate(
                this.transitionDuration,
                this.colors
        );

        if (colorUniform != -1)
            ARBShaderObjects.glUniform4fARB(colorUniform, color[1], color[2], color[3], color[0]);

        MATRIX_BUFFER.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, MATRIX_BUFFER);

        // Extract 2D Affine Matrix components:
        // m0: X Scale / Rotation,  m4: X Shear
        // m1: Y Shear,             m5: Y Scale / Rotation
        // m12: X Translation,      m13: Y Translation
        float m0  = MATRIX_BUFFER.get(0);
        float m1  = MATRIX_BUFFER.get(1);
        float m4  = MATRIX_BUFFER.get(4);
        float m5  = MATRIX_BUFFER.get(5);
        float m12 = MATRIX_BUFFER.get(12);
        float m13 = MATRIX_BUFFER.get(13);

        float padding = 1;
        float minX = x - padding;
        float minY = y - padding;
        float maxX = x + 16 + padding;
        float maxY = y + 16 + padding;

        float width = res.getScaledWidth();
        float height = res.getScaledHeight();

        // Thank you gemini for this completely incomprehensible code

        // Top-Left (minX, minY)
        float uTL = (m0 * minX + m4 * minY + m12) / width;
        float vTL = 1.0f - ((m1 * minX + m5 * minY + m13) / height);

        // Bottom-Left (minX, maxY)
        float uBL = (m0 * minX + m4 * maxY + m12) / width;
        float vBL = 1.0f - ((m1 * minX + m5 * maxY + m13) / height);

        // Bottom-Right (maxX, maxY)
        float uBR = (m0 * maxX + m4 * maxY + m12) / width;
        float vBR = 1.0f - ((m1 * maxX + m5 * maxY + m13) / height);

        // Top-Right (maxX, minY)
        float uTR = (m0 * maxX + m4 * minY + m12) / width;
        float vTR = 1.0f - ((m1 * maxX + m5 * minY + m13) / height);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(minX, minY, 0.0D).tex(uTL, vTL).endVertex();
        buffer.pos(minX, maxY, 0.0D).tex(uBL, vBL).endVertex();
        buffer.pos(maxX, maxY, 0.0D).tex(uBR, vBR).endVertex();
        buffer.pos(maxX, minY, 0.0D).tex(uTR, vTR).endVertex();
        tessellator.draw();
    }
}
