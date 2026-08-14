package brilliant_items.internal.handlers;

import brilliant_items.BrilliantItems;
import brilliant_items.api.IHasEffects;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.api.entity_item_effects.builtin.DynamicRarityEffect;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.internal.capabilities.ItemEffects;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import brilliant_items.internal.capabilities.ItemEffectsProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

@Mod.EventBusSubscriber(modid = BrilliantItems.MODID, value = Side.CLIENT)
public class ItemCapabilityHandler {
    private static final ResourceLocation CAP_KEY = new ResourceLocation(BrilliantItems.MODID, "item_effects");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        ItemEffectsProvider provider = new ItemEffectsProvider();
        ItemEffects effects = provider.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);
        // This should never be null
        assert effects != null;

        if (stack.getItem() instanceof IHasEffects) {
            IHasEffects effectHaver = (IHasEffects) stack.getItem();

            for (IEntityItemEffect effect : effectHaver.getEntityEffects(stack)) effects.add(effect);
            for (IInventoryItemEffect effect : effectHaver.getInventoryEffects(stack)) effects.add(effect);
        } else {
            Random rand = new Random((long) stack.hashCode() * stack.hashCode());
            float height = rand.nextFloat() / 2 + 1.5F;
            effects.add(new DynamicRarityEffect(height));
        }

        event.addCapability(CAP_KEY, provider);
    }
}
