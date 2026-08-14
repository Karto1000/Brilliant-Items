package brilliant_items.internal.handlers;

import brilliant_items.api.entity_item_effects.IBrilliantEntityItemEffect;
import brilliant_items.internal.capabilities.ItemEffects;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ItemEntityRenderHandler {
    private static final List<EntityItem> QUEUED_ITEMS = new ArrayList<>();

    private RenderEntityItem vanillaRenderEntityItem;
    private RenderItem vanillaRenderItem;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new ItemEntityRenderHandler());
    }

    public static void enqueue(EntityItem entity) {
        QUEUED_ITEMS.add(entity);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (QUEUED_ITEMS.isEmpty()) return;

        Minecraft mc = Minecraft.getMinecraft();
        RenderManager manager = mc.getRenderManager();
        float partialTicks = event.getPartialTicks();

        if (vanillaRenderEntityItem == null) {
            vanillaRenderEntityItem = new RenderEntityItem(manager, mc.getRenderItem());
            vanillaRenderItem = new RenderItem(
                    mc.getTextureManager(),
                    mc.getRenderItem().getItemModelMesher().getModelManager(),
                    mc.getItemColors()
            );
        }

        double cameraX = manager.viewerPosX;
        double cameraY = manager.viewerPosY;
        double cameraZ = manager.viewerPosZ;

        for (EntityItem entityItem : QUEUED_ITEMS) {
            ItemStack stack = entityItem.getItem();
            ItemEffects cap = stack.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);
            if (cap == null) continue;

            double x = (entityItem.lastTickPosX + (entityItem.posX - entityItem.lastTickPosX) * partialTicks) - cameraX;
            double y = (entityItem.lastTickPosY + (entityItem.posY - entityItem.lastTickPosY) * partialTicks) - cameraY;
            double z = (entityItem.lastTickPosZ + (entityItem.posZ - entityItem.lastTickPosZ) * partialTicks) - cameraZ;

            for (IBrilliantEntityItemEffect effect : cap.getEntityEffects()) {
                effect.renderPass(
                        entityItem,
                        manager,
                        this.vanillaRenderEntityItem,
                        this.vanillaRenderItem,
                        x, y, z,
                        partialTicks
                );
            }
        }

        QUEUED_ITEMS.clear();
    }
}
