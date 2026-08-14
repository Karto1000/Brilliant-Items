package brilliant_items.internal.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class ItemEffectsCapability {
    @CapabilityInject(ItemEffects.class)
    public static Capability<ItemEffects> ITEM_EFFECTS_CAPABILITY = null;

    public static void register() {
        // We don't need to store anything here because this is all client-side
        CapabilityManager.INSTANCE.register(
                ItemEffects.class,
                new Capability.IStorage<ItemEffects>() {
                    /**
                     * Serialize the capability instance to a NBTTag.
                     * This allows for a central implementation of saving the data.
                     * <p>
                     * It is important to note that it is up to the API defining
                     * the capability what requirements the 'instance' value must have.
                     * <p>
                     * Due to the possibility of manipulating internal data, some
                     * implementations MAY require that the 'instance' be an instance
                     * of the 'default' implementation.
                     * <p>
                     * Review the API docs for more info.
                     *
                     * @param capability The Capability being stored.
                     * @param instance   An instance of that capabilities interface.
                     * @param side       The side of the object the instance is associated with.
                     * @return a NBT holding the data. Null if no data needs to be stored.
                     */
                    @Nullable
                    @Override
                    public NBTBase writeNBT(Capability<ItemEffects> capability, ItemEffects instance, EnumFacing side) {
                        return null;
                    }

                    /**
                     * Read the capability instance from a NBT tag.
                     * <p>
                     * This allows for a central implementation of saving the data.
                     * <p>
                     * It is important to note that it is up to the API defining
                     * the capability what requirements the 'instance' value must have.
                     * <p>
                     * Due to the possibility of manipulating internal data, some
                     * implementations MAY require that the 'instance' be an instance
                     * of the 'default' implementation.
                     * <p>
                     * Review the API docs for more info.         *
                     *
                     * @param capability The Capability being stored.
                     * @param instance   An instance of that capabilities interface.
                     * @param side       The side of the object the instance is associated with.
                     * @param nbt        A NBT holding the data. Must not be null, as doesn't make sense to call this function with nothing to read...
                     */
                    @Override
                    public void readNBT(
                            Capability<ItemEffects> capability,
                            ItemEffects instance,
                            EnumFacing side,
                            NBTBase nbt
                    ) {

                    }
                },
                ItemEffects::new
        );
    }
}
