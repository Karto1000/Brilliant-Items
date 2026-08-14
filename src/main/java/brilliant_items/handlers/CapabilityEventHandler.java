package brilliant_items.handlers;

import brilliant_items.BrilliantItems;
import brilliant_items.api.IHasEffects;
import brilliant_items.api.effects.IBrilliantItemEffect;
import brilliant_items.api.effects.builtin.GlowPillarEffect;
import brilliant_items.api.effects.builtin.PinwheelEffect;
import brilliant_items.capabilities.ItemEffects;
import brilliant_items.capabilities.ItemEffectsCapability;
import brilliant_items.capabilities.ItemEffectsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = BrilliantItems.MODID)
public class CapabilityEventHandler {
    private static final ResourceLocation CAP_KEY = new ResourceLocation(BrilliantItems.MODID, "item_effects");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        boolean shouldShineForRareItem = ForgeConfigHandler.client.SHOULD_RARITY_INFLUENCE_COLOR &&
                stack.getItem().getForgeRarity(stack) != EnumRarity.COMMON;

        if (stack.getItem() instanceof IHasEffects) {
            IHasEffects effectHaver = (IHasEffects) stack.getItem();

            ItemEffectsProvider provider = new ItemEffectsProvider();
            ItemEffects effects = provider.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);
            if (effects == null) return;

            for (IBrilliantItemEffect effect : effectHaver.getEffects(stack)) effects.addEffect(effect);

            event.addCapability(CAP_KEY, provider);
        } else if (shouldShineForRareItem) {
            ItemEffectsProvider provider = new ItemEffectsProvider();
            ItemEffects effects = provider.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);
            if (effects == null) return;

            char colorChar = stack.getItem().getForgeRarity(stack).getColor().toString().charAt(1);
            int color = Minecraft.getMinecraft().fontRenderer.getColorCode(colorChar) | 0x55000000;

            Random rand = new Random((long) stack.hashCode() * stack.hashCode());
            float height = rand.nextFloat() / 2 + 1.5F;
            effects.addEffect(new GlowPillarEffect(0.3F, height, color));
            effects.addEffect(new PinwheelEffect(0.75F, 0.75F, color));

            event.addCapability(CAP_KEY, provider);
        }
    }
}
