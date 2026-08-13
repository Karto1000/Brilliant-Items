package brilliant_items.registry;

import brilliant_items.BrilliantItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = BrilliantItems.MODID)
public class TestItemRegistry {
    public static TestItem TEST_ITEM = new TestItem();

    public static void init() {
        registerItemRenderers();
    }

    public static void registerItemRenderers() {
        BrilliantItems.PROXY.registerItemRenderer(TEST_ITEM, 0, "inventory");
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(TEST_ITEM);
    }
}