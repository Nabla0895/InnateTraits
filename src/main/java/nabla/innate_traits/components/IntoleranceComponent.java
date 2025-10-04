package nabla.innate_traits.components;

import net.minecraft.nbt.NbtCompound;
import org.ladysnake.cca.api.v3.component.CopyableComponent;


public interface IntoleranceComponent extends CopyableComponent<IntoleranceComponent> {

    int getLevel(Intolerance type);
    void setLevel(Intolerance type, int level);
    boolean isDiscovered(Intolerance type);
    void setDiscovered(Intolerance type, boolean discovered);

    @Override
    void readFromNbt(NbtCompound tag);

    @Override
    void writeToNbt(NbtCompound tag);
}