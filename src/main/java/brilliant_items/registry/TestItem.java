package brilliant_items.registry;

import brilliant_items.BrilliantItems;
import brilliant_items.api.IHasEffects;
import brilliant_items.api.effects.builtin.GlowPillarEffect;
import brilliant_items.api.effects.IBrilliantItemEffect;
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
    public NonNullList<IBrilliantItemEffect> getEffects(ItemStack stack) {
        Random rand = new Random((long) stack.hashCode() * stack.hashCode());
        float height = rand.nextFloat() / 2 + 1.5F;

        NonNullList<IBrilliantItemEffect> effects = NonNullList.create();
        effects.add(new GlowPillarEffect(0.3F, height, 0xFFFFFFFF));

        return effects;
    }
}
