package brilliant_items.internal.mixin.vanilla;

import brilliant_items.internal.proxy.ClientProxy;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class GuiIngameMixin {
    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void onRenderHotbarItemHead(ScaledResolution res, float partialTicks, CallbackInfo ci) {
        ClientProxy.isRenderingInsideSlot = true;
    }

    @Inject(method = "renderHotbar", at = @At("RETURN"))
    private void onRenderHotbarItemReturn(ScaledResolution res, float partialTicks, CallbackInfo ci) {
        ClientProxy.isRenderingInsideSlot = false;
    }
}
