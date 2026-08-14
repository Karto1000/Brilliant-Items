package brilliant_items.internal.rendering;

import brilliant_items.BrilliantItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public class ItemEffectShaderManager {
    private final static HashMap<String, Integer> shaderPrograms = new HashMap<>();

    private static int getShaderIdOfShaderLoader(ShaderLoader loader) {
        try {
            Field field = loader.getClass().getDeclaredField("shader");
            field.setAccessible(true);
            return field.getInt(loader);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // This should never fail
            BrilliantItems.LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public static void registerShader(
            String designation,
            ResourceLocation vertexShader,
            ResourceLocation fragmentShader
    ) throws IOException {
        int programId = createShader(vertexShader, fragmentShader);
        shaderPrograms.put(designation, programId);
    }

    public static Optional<Integer> getProgramId(String designation) {
        Integer id = shaderPrograms.get(designation);
        return Optional.ofNullable(id);
    }

    private static int createShader(
            ResourceLocation vertexShader,
            ResourceLocation fragmentShader
    ) throws IOException {
        Minecraft mc = Minecraft.getMinecraft();

        ShaderLoader vertShader = ShaderLoader.loadShader(
                mc.getResourceManager(),
                ShaderLoader.ShaderType.VERTEX,
                vertexShader.toString()
        );

        ShaderLoader fragShader = ShaderLoader.loadShader(
                mc.getResourceManager(),
                ShaderLoader.ShaderType.FRAGMENT,
                fragmentShader.toString()
        );

        int programId = OpenGlHelper.glCreateProgram();

        int vertShaderId = getShaderIdOfShaderLoader(vertShader);
        int fragShaderId = getShaderIdOfShaderLoader(fragShader);

        OpenGlHelper.glAttachShader(programId, vertShaderId);
        OpenGlHelper.glAttachShader(programId, fragShaderId);
        OpenGlHelper.glLinkProgram(programId);

        return programId;
    }
}
