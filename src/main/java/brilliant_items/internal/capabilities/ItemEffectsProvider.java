package brilliant_items.internal.capabilities;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class ItemEffectsProvider implements ICapabilityProvider {
    private final ItemEffects instance = new ItemEffects();

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY) {
            return ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY.cast(this.instance);
        }

        return null;
    }

    /**
     * Determines if this object has support for the capability in question on the specific side.
     * The return value of this MIGHT change during runtime if this object gains or loses support
     * for a capability. It is not required to call this function before calling
     * {@link #getCapability(Capability, EnumFacing)}.
     * <p>
     * Basically, this method functions analogously to {@link Map#containsKey(Object)}.
     * <p>
     * <em>Example:</em>
     * A Pipe getting a cover placed on one side causing it lose the Inventory attachment function for that side.
     * </p><p>
     * This is a light weight version of getCapability, intended for metadata uses.
     * </p>
     *
     * @param capability The capability to check
     * @param facing     The Side to check from:
     *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
     * @return True if this object supports the capability. If true, then {@link #getCapability(Capability, EnumFacing)}
     * must not return null.
     */
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ItemEffectsCapability.ITEM_EFFECTS_CAPABILITY;
    }
}
