package brilliant_items.internal.registry;

import brilliant_items.BrilliantItems;
import brilliant_items.api.IHasEffects;
import brilliant_items.api.entity_item_effects.IEntityItemEffect;
import brilliant_items.api.entity_item_effects.builtin.GlowPillarEffect;
import brilliant_items.api.inventory_item_effects.IInventoryItemEffect;
import brilliant_items.api.inventory_item_effects.builtin.GlowEffect;
import brilliant_items.api.inventory_item_effects.builtin.SparkleEffect;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.Random;

public class TestItem extends Item implements IHasEffects {
    public TestItem() {
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName(BrilliantItems.MODID, "test_item");
        this.setTranslationKey("brilliant_items.test_item");
    }

    @Nonnull
    @Override
    public NonNullList<IEntityItemEffect> getEntityEffects(ItemStack stack) {
        Random rand = new Random((long) stack.hashCode() * stack.hashCode());
        float height = rand.nextFloat() / 2 + 1.5F;

        NonNullList<IEntityItemEffect> effects = NonNullList.create();
        effects.add(new GlowPillarEffect(0.3F, height, 0xFFFFD700));

        return effects;
    }

    @Nonnull
    @Override
    public NonNullList<IInventoryItemEffect> getInventoryEffects(ItemStack stack) {
        NonNullList<IInventoryItemEffect> inventoryEffects = NonNullList.create();
        inventoryEffects.add(new GlowEffect(0xAAFFD700));

        SparkleEffect.SparkleEffectOptions options = new SparkleEffect.SparkleEffectOptions();
        options.color = 0xAAFFD700;
        inventoryEffects.add(new SparkleEffect(options));

        return inventoryEffects;
    }
}
