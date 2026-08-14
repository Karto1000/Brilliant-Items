package brilliant_items.api.item_effects;

import brilliant_items.internal.rendering.ShaderNotFoundException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.nio.FloatBuffer;

@SideOnly(Side.CLIENT)
public interface IBrilliantInventoryEffect {
    /// Should return the OpenGL id referencing the shader program
    ///
    /// @return The shader program id
    int getShaderProgramId() throws ShaderNotFoundException;

    /// Runs each frame to render the item in the inventory, including the effect
    ///
    /// @param tessellator The Tessellator Instance
    /// @param buffer      The Buffer Instance
    /// @param player      The current player
    /// @param stack       The stack the effect is applied to
    /// @param x           The local gui x position
    /// @param y           The local gui y position
    default void renderPass(
            @Nonnull Tessellator tessellator,
            @Nonnull BufferBuilder buffer,
            @Nonnull EntityLivingBase player,
            @Nonnull ItemStack stack,
            int x,
            int y
    ) {
        int programId = this.getShaderProgramId();
        ARBShaderObjects.glUseProgramObjectARB(programId);

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);

        int itemPosUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_itemPosition");
        int textureUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_texture");
        int scaledScreenSizeUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_scaledScreenSize");

        if (itemPosUniform != -1) {
            FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(16);
            GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, MATRIX_BUFFER);
            float translateX = MATRIX_BUFFER.get(12);
            float translateY = MATRIX_BUFFER.get(13);
            ARBShaderObjects.glUniform2fARB(itemPosUniform, x + translateX, y + translateY);
        }
        if (textureUniform != -1) ARBShaderObjects.glUniform1iARB(textureUniform, 0);
        if (scaledScreenSizeUniform != -1)
            ARBShaderObjects.glUniform2fARB(scaledScreenSizeUniform, res.getScaledWidth(), res.getScaledHeight());

    }
}
