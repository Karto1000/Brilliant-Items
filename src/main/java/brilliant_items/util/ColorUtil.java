package brilliant_items.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.HashMap;
import java.util.Map;

public class ColorUtil {
    private final static HashMap<TextureAtlasSprite, Integer> colorCache = new HashMap<>();

    public static int getPredominantColorOfTexture(TextureAtlasSprite texture) {
        Integer existingColor = colorCache.get(texture);
        if (existingColor != null) return existingColor;

        int[][] data = texture.getFrameTextureData(0);

        Map<Integer, Integer> colorMap = new HashMap<>();
        int maxCount = 0;
        int dominantColor = 0;

        for (int[] row : data) {
            for (int color : row) {
                // Skip semi-transparent pixels
                if (color >> 24 <= 0.1) continue;

                // Skip full black pixels
                if (color == 0) continue;

                Integer existingCount = colorMap.get(color);
                if (existingCount == null) existingCount = 0;
                colorMap.put(color, existingCount + 1);

                if (existingCount + 1 > maxCount) {
                    maxCount = existingCount + 1;
                    dominantColor = color;
                }
            }
        }

        colorCache.put(texture, dominantColor);
        return dominantColor;
    }

    public static int darken(int color, float factor) {
        factor = Math.max(0.0f, factor);

        int a = (color >> 24) & 0xFF;

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = Math.min(255, Math.max(0, Math.round(r * (1.F - factor))));
        g = Math.min(255, Math.max(0, Math.round(g * (1.F - factor))));
        b = Math.min(255, Math.max(0, Math.round(b * (1.F - factor))));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int brighten(int color, float factor) {
        factor = Math.max(0.0f, factor);

        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = Math.min(255, Math.max(0, Math.round(r * (1.F + factor))));
        g = Math.min(255, Math.max(0, Math.round(g * (1.F + factor))));
        b = Math.min(255, Math.max(0, Math.round(b * (1.F + factor))));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int mix(int color1, int color2, float ratio) {
        ratio = Math.max(0.0f, Math.min(1.0f, ratio));

        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = Math.round(a1 * (1 - ratio) + a2 * ratio);
        int r = Math.round(r1 * (1 - ratio) + r2 * ratio);
        int g = Math.round(g1 * (1 - ratio) + g2 * ratio);
        int b = Math.round(b1 * (1 - ratio) + b2 * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
