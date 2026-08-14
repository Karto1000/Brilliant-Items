package brilliant_items.api.effects.builtin;

import brilliant_items.api.effects.IBrilliantItemEffect;
import brilliant_items.handlers.ForgeConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class DynamicRarityEffect implements IBrilliantItemEffect {
    private final GlowPillarEffect pillarEffect = new GlowPillarEffect(0.3F, 1.5F, 0x00000000);
    private final PinwheelEffect pinwheelEffect = new PinwheelEffect(0.75F, 0.75F, 0x00000000);

    public DynamicRarityEffect(float pillarHeight) {
        this.pillarEffect.setHeight(pillarHeight);
    }

    public DynamicRarityEffect() {

    }

    /// Called each frame before the item is rendered
    ///
    /// @param entity                  The Entity item to be rendered
    /// @param manager                 The instance of the Render Manager
    /// @param vanillaRenderEntityItem The vanilla implementation for rendering the EntityItem
    /// @param vanillaRenderItem       The vanilla implementation for rendering an Item
    /// @param x                       The x offset of the item in the world from the player
    /// @param y                       The y offset of the item in the world from the player
    /// @param z                       The z offset of the item in the world from the player
    /// @param partialTicks            The partial Ticks
    @Override
    public void renderPass(
            @Nonnull EntityItem entity,
            @Nonnull RenderManager manager,
            @Nonnull RenderEntityItem vanillaRenderEntityItem,
            @Nonnull RenderItem vanillaRenderItem,
            double x,
            double y,
            double z,
            float partialTicks
    ) {
        ItemStack stack = entity.getItem();

        if (!ForgeConfigHandler.client.SHOULD_RARITY_INFLUENCE_COLOR) return;
        if (stack.getItem().getForgeRarity(stack) == EnumRarity.COMMON) return;

        char colorChar = stack.getItem().getForgeRarity(stack).getColor().toString().charAt(1);
        int color = Minecraft.getMinecraft().fontRenderer.getColorCode(colorChar) | 0x55000000;

        this.pillarEffect.setColor(color);
        this.pinwheelEffect.setColor(color);

        this.pinwheelEffect.renderPass(
                entity,
                manager,
                vanillaRenderEntityItem,
                vanillaRenderItem,
                x,
                y,
                z,
                partialTicks
        );
        this.pillarEffect.renderPass(
                entity,
                manager,
                vanillaRenderEntityItem,
                vanillaRenderItem,
                x,
                y,
                z,
                partialTicks
        );
    }
}
