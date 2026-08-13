package brilliant_items.handlers;

import brilliant_items.rendering.BrilliantItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ItemRenderHandler implements IRenderFactory<EntityItem> {
    @Override
    public Render<? super EntityItem> createRenderFor(RenderManager manager) {
        Minecraft mc = Minecraft.getMinecraft();
        return new BrilliantItemRenderer(
                manager,
                new RenderEntityItem(manager, mc.getRenderItem()),
                new RenderItem(
                        mc.getTextureManager(),
                        mc.getRenderItem().getItemModelMesher().getModelManager(),
                        mc.getItemColors()
                )
        );
    }
}
