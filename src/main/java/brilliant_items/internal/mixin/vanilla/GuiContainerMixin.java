package brilliant_items.internal.mixin.vanilla;

import brilliant_items.internal.proxy.ClientProxy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {
    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void brilliant_item_onDrawSlotHead(Slot slot, CallbackInfo ci) {
        ClientProxy.isRenderingInsideSlot = true;
    }

    @Inject(method = "drawSlot", at = @At("RETURN"))
    private void brilliant_item_onDrawSlotReturn(Slot slot, CallbackInfo ci) {
        ClientProxy.isRenderingInsideSlot = false;
    }

    @Inject(method = "drawItemStack", at = @At("HEAD"))
    private void brilliant_item_onDrawItemStackHead(ItemStack stack, int x, int y, String altText, CallbackInfo ci) {
        ClientProxy.isRenderingInsideSlot = false;
    }
}
