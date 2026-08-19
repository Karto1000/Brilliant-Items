package brilliant_items.internal.handlers;

import brilliant_items.BrilliantItems;
import brilliant_items.api.IHasEffects;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.api.entity_item_effects.builtin.BackgroundGlowEffect;
import brilliant_items.api.entity_item_effects.builtin.GlowPillarEffect;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.api.inventory_item_effects.builtin.ItemBorderEffect;
import brilliant_items.internal.capabilities.ItemEffects;
import brilliant_items.internal.capabilities.ItemEffectsCapability;
import brilliant_items.internal.capabilities.ItemEffectsProvider;
import brilliant_items.internal.config.ForgeConfigManager;
import brilliant_items.internal.config.JsonConfig;
import brilliant_items.internal.config.JsonConfigManager;
import brilliant_items.internal.registry.EffectRegistry;
import brilliant_items.internal.util.ColorUtil;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Optional;
import java.util.Random;

@Mod.EventBusSubscriber(modid = BrilliantItems.MODID, value = Side.CLIENT)
public class ItemCapabilityHandler {
    private static final ResourceLocation CAP_KEY = new ResourceLocation(BrilliantItems.MODID, "item_effects");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.isEmpty()) return;

        ItemEffectsProvider provider = new ItemEffectsProvider();
        ItemEffects effects = provider.getCapability(ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY, null);
        if (effects == null) return;

        ResourceLocation registryName = stack.getItem().getRegistryName();
        // We have a custom-defined effect in the config
        if (JsonConfigManager.config.mappings.containsKey(registryName)) {
            JsonConfig.ItemMapping itemMapping = JsonConfigManager.config.mappings.get(registryName);

            for (JsonConfig.EffectMapping entityEffect : itemMapping.entityEffects) {
                Optional<? extends IEntityItemEffect> effectO = EffectRegistry.createEntityItemEffect(
                        entityEffect.identifier,
                        entityEffect.arguments
                );

                if (!effectO.isPresent()) {
                    BrilliantItems.LOGGER.error(
                            "Could not create entity effect '{}' with the specified arguments '{}' for item '{}'",
                            entityEffect.identifier,
                            entityEffect.arguments,
                            registryName
                    );
                    continue;
                }

                IEntityItemEffect effect = effectO.get();
                effects.add(effect);
            }

            for (JsonConfig.EffectMapping inventoryEffect : itemMapping.inventoryEffects) {
                Optional<? extends IInventoryItemEffect> effectO = EffectRegistry.createInventoryItemEffect(
                        inventoryEffect.identifier,
                        inventoryEffect.arguments
                );

                if (!effectO.isPresent()) {
                    BrilliantItems.LOGGER.error(
                            "Could not create inventory effect '{}' with the specified arguments '{}'",
                            inventoryEffect.identifier,
                            inventoryEffect.arguments
                    );
                    continue;
                }

                IInventoryItemEffect effect = effectO.get();
                effects.add(effect);
            }
        } else if (stack.getItem() instanceof IHasEffects) {
            IHasEffects effectHaver = (IHasEffects) stack.getItem();

            for (IEntityItemEffect effect : effectHaver.getEntityEffects(stack)) effects.add(effect);
            for (IInventoryItemEffect effect : effectHaver.getInventoryEffects(stack)) effects.add(effect);
        } else {
            if (ForgeConfigManager.client.ASSIGN_EFFECTS_BASED_ON_RARITY && stack.getItem().getForgeRarity(stack) != EnumRarity.COMMON) {
                Random rand = new Random((long) stack.hashCode() * stack.hashCode());
                float height = rand.nextFloat() / 2 + 1.5F;

                int color = ColorUtil.getColorOfItemStack(stack);
                int lowerAlphaColor = color & 0x00FFFFFF | 0x55000000;

                effects.add(new GlowPillarEffect(GlowPillarEffect.Args.builder().color(lowerAlphaColor).height(height).build()));
                effects.add(new BackgroundGlowEffect(BackgroundGlowEffect.Args.builder().color(lowerAlphaColor).build()));
                effects.add(new ItemBorderEffect(new ItemBorderEffect.Args(color)));
            }
        }

        event.addCapability(CAP_KEY, provider);
    }
}
