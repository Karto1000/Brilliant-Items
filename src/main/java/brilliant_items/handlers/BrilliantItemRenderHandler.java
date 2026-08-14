package brilliant_items.handlers;

import brilliant_items.api.effects.IBrilliantItemEffect;
import brilliant_items.capabilities.ItemEffects;
import brilliant_items.capabilities.ItemEffectsCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class BrilliantItemRenderHandler extends Render<EntityItem> {
    @Nonnull
    private final RenderEntityItem vanillaRenderEntityItem;

    @Nonnull
    private final RenderItem vanillaRenderItem;

    public BrilliantItemRenderHandler(@Nonnull RenderManager manager) {
        super(manager);

        Minecraft mc = Minecraft.getMinecraft();
        this.vanillaRenderEntityItem = new RenderEntityItem(manager, mc.getRenderItem());
        this.vanillaRenderItem = new RenderItem(
                mc.getTextureManager(),
                mc.getRenderItem().getItemModelMesher().getModelManager(),
                mc.getItemColors()
        );
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityItem entity) {
        return null;
    }

    @Override
    public void doRender(
            @Nonnull EntityItem entity,
            double x,
            double y,
            double z,
            float entityYaw,
            float partialTicks
    ) {
        ItemStack stack = entity.getItem();
        if (stack.hasCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null)) {
            ItemEffects cap = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);
            if (cap == null) return;

            for (IBrilliantItemEffect effect : cap.getEffects()) {
                effect.renderPass(
                        entity,
                        this.renderManager,
                        this.vanillaRenderEntityItem,
                        this.vanillaRenderItem,
                        x, y, z,
                        partialTicks
                );
            }
        }

        vanillaRenderEntityItem.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
