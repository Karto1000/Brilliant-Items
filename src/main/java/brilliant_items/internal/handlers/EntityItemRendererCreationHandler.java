package brilliant_items.internal.handlers;

import brilliant_items.internal.rendering.BrilliantItemRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityItemRendererCreationHandler implements IRenderFactory<EntityItem> {
    public static void init() {
        RenderingRegistry.registerEntityRenderingHandler(EntityItem.class, new EntityItemRendererCreationHandler());
    }

    @Override
    public Render<? super EntityItem> createRenderFor(RenderManager manager) {
        return new BrilliantItemRenderer(manager);
    }
}
