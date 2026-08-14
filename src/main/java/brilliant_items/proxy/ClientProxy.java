package brilliant_items.proxy;

import brilliant_items.capabilities.ItemEffectsCapability;
import brilliant_items.handlers.ItemRenderHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.Objects;

public class ClientProxy extends CommonProxy {
    public void registerItemRenderer(
            Item item,
            int meta,
            String id
    ) {
        ModelLoader.setCustomModelResourceLocation(
                item,
                meta,
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory")
        );
    }

    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityItem.class, new ItemRenderHandler());
        ItemEffectsCapability.register();
    }
}