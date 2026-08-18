package brilliant_items.internal.rendering;

import brilliant_items.internal.capabilities.ItemEffects;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import brilliant_items.internal.config.ForgeConfigManager;
import brilliant_items.internal.handlers.ItemEntityRenderHandler;
import net.minecraft.client.Minecraft;
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

/// Class that is responsible for enqueuing items entities that should receive the brilliant effect
@SideOnly(Side.CLIENT)
public class BrilliantItemRenderer extends Render<EntityItem> {
    @Nonnull
    private final RenderEntityItem vanillaRenderEntityItem;

    public BrilliantItemRenderer(@Nonnull RenderManager manager) {
        super(manager);

        Minecraft mc = Minecraft.getMinecraft();
        this.vanillaRenderEntityItem = new RenderEntityItem(manager, mc.getRenderItem());
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

        boolean isEntityValid = ForgeConfigManager.client.SHOULD_RENDER_ENTITY_ITEM_EFFECTS
                && !entity.isDead
                && stack.hasCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);

        if (isEntityValid) {
            ItemEffects cap = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);
            boolean isCapacityValid = cap != null && !cap.getEntityEffects().isEmpty();
            if (isCapacityValid) ItemEntityRenderHandler.enqueue(entity);
        }

        vanillaRenderEntityItem.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
