package brilliant_items.handlers;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import brilliant_items.BrilliantItems;

@Config(modid = BrilliantItems.MODID)
public class ForgeConfigHandler {
	
	@Config.Comment("Server-Side Options")
	@Config.Name("Server Options")
	public static final ServerConfig server = new ServerConfig();

	@Config.Comment("Client-Side Options")
	@Config.Name("Client Options")
	public static final ClientConfig client = new ClientConfig();

	@MixinConfig(name = BrilliantItems.MODID) //Needed on config classes that contain MixinToggles for those mixins to be added
	public static class ServerConfig {
	}

	public static class ClientConfig {
		public boolean SHOULD_RARITY_INFLUENCE_COLOR = true;
	}

	@Mod.EventBusSubscriber(modid = BrilliantItems.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(BrilliantItems.MODID)) {
				ConfigManager.sync(BrilliantItems.MODID, Config.Type.INSTANCE);
			}
		}
	}
}