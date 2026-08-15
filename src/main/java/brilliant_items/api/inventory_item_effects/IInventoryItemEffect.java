package brilliant_items.api.inventory_item_effects;

import brilliant_items.internal.rendering.ShaderNotFoundException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.ARBShaderObjects;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public interface IInventoryItemEffect {
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
    /// @param uvs         The uv coordinates of the texture in the framebuffer
    /// @param localPos    The local position of the item in the current gui container
    /// @param absolutePos The absolute position of the item on the entire screen
    default void renderPass(
            @Nonnull Tessellator tessellator,
            @Nonnull BufferBuilder buffer,
            @Nonnull EntityLivingBase player,
            @Nonnull ItemStack stack,
            @Nonnull AbsoluteItemTextureUV uvs,
            @Nonnull LocalItemCoordinates localPos,
            @Nonnull AbsoluteItemCoordinates absolutePos
    ) {
        int programId = this.getShaderProgramId();
        ARBShaderObjects.glUseProgramObjectARB(programId);

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);

        int itemPosUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_absoluteItemPosition");
        int textureUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_texture");
        int scaledScreenSizeUniform = ARBShaderObjects.glGetUniformLocationARB(programId, "u_scaledScreenSize");

        if (itemPosUniform != -1) ARBShaderObjects.glUniform2fARB(itemPosUniform, absolutePos.left, absolutePos.top);
        if (textureUniform != -1) ARBShaderObjects.glUniform1iARB(textureUniform, 0);
        if (scaledScreenSizeUniform != -1)
            ARBShaderObjects.glUniform2fARB(scaledScreenSizeUniform, res.getScaledWidth(), res.getScaledHeight());

    }
}
