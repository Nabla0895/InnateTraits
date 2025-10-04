package nabla.innate_traits.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.CopyableComponent;


public interface IntoleranceComponent extends CopyableComponent<IntoleranceComponent> {

    int getLevel(Intolerance type);
    void setLevel(Intolerance type, int level);
    boolean isDiscovered(Intolerance type);
    void setDiscovered(Intolerance type, boolean discovered);

    void readFromNbt(NbtCompound tag);

    void writeToNbt(NbtCompound tag);

    @Override
    void copyFrom(IntoleranceComponent intoleranceComponent, RegistryWrapper.WrapperLookup wrapperLookup);
}