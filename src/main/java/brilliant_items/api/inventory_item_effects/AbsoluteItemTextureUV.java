package brilliant_items.api.inventory_item_effects;

import lombok.AllArgsConstructor;

/// These uv coordinates are absolute across the entire screen because the framebuffer is also that big
@AllArgsConstructor
public class AbsoluteItemTextureUV {
    public final float top;
    public final float right;
    public final float bottom;
    public final float left;
}
