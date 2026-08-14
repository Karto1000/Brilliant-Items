package brilliant_items.internal.rendering;

import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InventoryEffectFramebuffer {
    private static Framebuffer target;

    public static Framebuffer getTarget(int width, int height) {
        if (target == null) target = new Framebuffer(width, height, true);
        else if (target.framebufferWidth != width || target.framebufferHeight != height) {
            target.createBindFramebuffer(width, height);
            target.setFramebufferColor(0, 0, 0, 0);
        }

        return target;
    }
}
