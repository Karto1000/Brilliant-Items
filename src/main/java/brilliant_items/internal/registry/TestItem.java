package brilliant_items.internal.registry;

import brilliant_items.BrilliantItems;
import brilliant_items.api.IHasEffects;
import brilliant_items.api.entity_item_effects.IBrilliantEntityItemEffect;
import brilliant_items.api.entity_item_effects.builtin.GlowPillarEffect;
import brilliant_items.api.item_effects.IBrilliantInventoryEffect;
import brilliant_items.api.item_effects.builtin.GlowEffect;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import java.time.Duration;

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
    public NonNullList<IBrilliantEntityItemEffect> getEntityEffects(ItemStack stack) {
        Random rand = new Random((long) stack.hashCode() * stack.hashCode());
        float height = rand.nextFloat() / 2 + 1.5F;

        NonNullList<IBrilliantEntityItemEffect> effects = NonNullList.create();
        effects.add(new GlowPillarEffect(0.3F, height, 0xFFFFD700));

        return effects;
    }

    @Nonnull
    @Override
    public NonNullList<IBrilliantInventoryEffect> getInventoryEffects(ItemStack stack) {
        NonNullList<Integer> colors = NonNullList.create();
        colors.add(0xAAFFD700);
        colors.add(0xAAFFFFFF);
        colors.add(0xAAD4AF37);
        return NonNullList.withSize(1, new GlowEffect(0xAAFFD700));
    }
}
