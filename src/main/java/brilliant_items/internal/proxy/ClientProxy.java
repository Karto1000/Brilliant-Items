package brilliant_items.internal.proxy;

import brilliant_items.BrilliantItems;
import brilliant_items.api.inventory_item_effects.builtin.GlowEffect;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import brilliant_items.internal.handlers.EntityItemRendererCreationHandler;
import brilliant_items.internal.handlers.ItemEntityRenderHandler;
import brilliant_items.api.inventory_item_effects.InventoryItemEffectShaderManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

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
        EntityItemRendererCreationHandler.init();
        ItemEntityRenderHandler.init();
        ItemEffectsCapability.register();

        try {
            InventoryItemEffectShaderManager.registerShader(
                    GlowEffect.GLOW_SHADER_DESIGNATION,
                    new ResourceLocation(BrilliantItems.MODID, "glow"),
                    new ResourceLocation(BrilliantItems.MODID, "glow")
            );
        } catch (Exception e) {
            BrilliantItems.LOGGER.error("Failed to register shader", e);
        }
    }
}