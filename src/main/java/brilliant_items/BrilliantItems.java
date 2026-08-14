package brilliant_items;

import brilliant_items.internal.proxy.CommonProxy;
import brilliant_items.internal.registry.TestItemRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BrilliantItems.MODID, version = BrilliantItems.VERSION, name = BrilliantItems.NAME, dependencies = "required-after:fermiumbooter")
public class BrilliantItems {
    public static final String MODID = "brilliant_items";
    public static final String VERSION = "0.1.0";
    public static final String NAME = "Brilliant Items";
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean completedLoading = false;

    @SidedProxy(clientSide = "brilliant_items.proxy.ClientProxy", serverSide = "brilliant_items.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Instance(MODID)
    public static BrilliantItems instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BrilliantItems.PROXY.preInit();
        TestItemRegistry.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        completedLoading = true;
    }
}