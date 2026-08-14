package brilliant_items.handlers;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRenderHandler implements IRenderFactory<EntityItem> {
    @Override
    public Render<? super EntityItem> createRenderFor(RenderManager manager) {
        return new BrilliantItemRenderHandler(manager);
    }
}
