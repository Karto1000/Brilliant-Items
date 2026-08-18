package brilliant_items.internal.config;

import brilliant_items.BrilliantItems;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BrilliantItems.MODID)
public class ForgeConfigManager {

    @Config.Comment("Client-Side Options")
    @Config.Name("Client Options")
    public static final ClientConfig client = new ClientConfig();

    public static class ClientConfig {
        @Config.Name("Should item rarity add effects?")
        @Config.Comment("If the items rarity should lead to effects being applied with that rarities color.")
        public boolean ASSIGN_EFFECTS_BASED_ON_RARITY = true;

        @Config.Name("Should any inventory effects be rendered?")
        @Config.Comment("Causes no inventory effects to be rendered when false")
        public boolean SHOULD_RENDER_INVENTORY_EFFECTS = true;

        @Config.Name("Should any entity item effects be rendered?")
        @Config.Comment("Causes no entity item effects to be rendered when false")
        public boolean SHOULD_RENDER_ENTITY_ITEM_EFFECTS = true;
    }

    @Mod.EventBusSubscriber(modid = BrilliantItems.MODID)
    private static class EventHandler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(BrilliantItems.MODID)) {
                ConfigManager.sync(BrilliantItems.MODID, Config.Type.INSTANCE);
            }
        }
    }
}