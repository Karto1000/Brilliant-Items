package brilliant_items.internal.proxy;

import brilliant_items.BrilliantItems;
import brilliant_items.api.BrilliantItemsAPI;
import brilliant_items.api.entity_item_effects.builtin.BackgroundGlowEffect;
import brilliant_items.api.entity_item_effects.builtin.GlowPillarEffect;
import brilliant_items.api.entity_item_effects.builtin.ParticleSpawningEffect;
import brilliant_items.api.entity_item_effects.builtin.PinwheelEffect;
import brilliant_items.api.inventory_item_effects.ShaderManager;
import brilliant_items.api.inventory_item_effects.builtin.GlowOutlineEffect;
import brilliant_items.api.inventory_item_effects.builtin.RadialGlowEffect;
import brilliant_items.api.inventory_item_effects.builtin.SparkleEffect;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import brilliant_items.internal.config.JsonConfigManager;
import brilliant_items.internal.config.JsonConfigWatcher;
import brilliant_items.internal.handlers.EntityItemRendererCreationHandler;
import brilliant_items.internal.handlers.ItemEntityRenderHandler;
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
        BrilliantItemsAPI.registerForJSON(
                RadialGlowEffect.class, SparkleEffect.class, GlowPillarEffect.class,
                PinwheelEffect.class, GlowOutlineEffect.class, BackgroundGlowEffect.class,
                ParticleSpawningEffect.class
        );

        EntityItemRendererCreationHandler.init();
        ItemEntityRenderHandler.init();
        JsonConfigManager.init();
        ItemEffectsCapability.init();

        JsonConfigWatcher watcher = new JsonConfigWatcher();
        Thread thread = new Thread(watcher);
        thread.start();

        try {
            ShaderManager.registerShader(
                    RadialGlowEffect.GLOW_SHADER_DESIGNATION,
                    new ResourceLocation(BrilliantItems.MODID, "radial_glow"),
                    new ResourceLocation(BrilliantItems.MODID, "radial_glow")
            );

            ShaderManager.registerShader(
                    GlowOutlineEffect.OUTLINE_SHADER_DEFINITION,
                    new ResourceLocation(BrilliantItems.MODID, "glow_outline"),
                    new ResourceLocation(BrilliantItems.MODID, "glow_outline")
            );
        } catch (Exception e) {
            BrilliantItems.LOGGER.error("Failed to register shader", e);
        }
    }
}