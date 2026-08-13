package brilliant_items.mixin.vanilla;

import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(Side.CLIENT)
@Mixin(RenderEntityItem.class)
public class RenderEntityItemMixin {
//    @Inject(method = "shouldBob", cancellable = true, at = @At("HEAD"), remap = false)
//    public void brilliant_items_shouldBob(CallbackInfoReturnable<Boolean> cir) {
//        cir.setReturnValue(false);
//        cir.cancel();
//    }
}