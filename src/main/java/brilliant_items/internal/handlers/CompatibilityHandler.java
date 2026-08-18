package brilliant_items.internal.handlers;

import net.minecraftforge.fml.common.Loader;

public class CompatibilityHandler {
    public static boolean isItemPhysicsInstalled() {
        return Loader.isModLoaded("itemphysiclite") || Loader.isModLoaded("itemphysic");
    }
}
