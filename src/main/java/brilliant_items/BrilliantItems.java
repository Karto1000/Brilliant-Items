package brilliant_items;

import brilliant_items.internal.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BrilliantItems.MODID, version = BrilliantItems.VERSION, name = BrilliantItems.NAME, dependencies = "required-after:mixinbooter")
public class BrilliantItems {
    public static final String MODID = "brilliant_items";
    public static final String VERSION = "1.0.1";
    public static final String NAME = "Brilliant Items";
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean completedLoading = false;

    @SidedProxy(clientSide = "brilliant_items.internal.proxy.ClientProxy", serverSide = "brilliant_items.internal.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Instance(MODID)
    public static BrilliantItems instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BrilliantItems.PROXY.preInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        completedLoading = true;
    }
}