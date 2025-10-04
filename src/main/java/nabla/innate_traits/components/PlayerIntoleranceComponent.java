package nabla.innate_traits.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerIntoleranceComponent implements IntoleranceComponent {
    private final Map<Intolerance, Integer> intoleranceLevels = new EnumMap<>(Intolerance.class);
    private final Set<Intolerance> discoveredIntolerances = new HashSet<>();

    private static final String NBT_PREFIX_LEVEL = "innatetraits.level.";
    private static final String NBT_PREFIX_DISCOVERED = "innatetraits.discovered.";

    public PlayerIntoleranceComponent() {
        for (Intolerance type : Intolerance.values()) {
            intoleranceLevels.put(type, 0);
        }
    }

    @Override
    public int getLevel(Intolerance type) { return intoleranceLevels.getOrDefault(type, 0); }
    @Override
    public void setLevel(Intolerance type, int level) { if (level >= 0 && level <= 2) intoleranceLevels.put(type, level); }
    @Override
    public boolean isDiscovered(Intolerance type) { return discoveredIntolerances.contains(type); }
    @Override
    public void setDiscovered(Intolerance type, boolean discovered) { if (discovered) discoveredIntolerances.add(type); else discoveredIntolerances.remove(type); }


    @Override
    public void readFromNbt(NbtCompound tag) {
        for (Intolerance type : Intolerance.values()) {
            intoleranceLevels.put(type, 0);
        }
        discoveredIntolerances.clear();

        for (Intolerance type : Intolerance.values()) {
            String levelKey = NBT_PREFIX_LEVEL + type.name();
            if (tag.contains(levelKey)) {
                intoleranceLevels.put(type, tag.getInt(levelKey, 0));
            }

            String discoveredKey = NBT_PREFIX_DISCOVERED + type.name();
            if (tag.contains(discoveredKey) && tag.getBoolean(discoveredKey, false)) {
                discoveredIntolerances.add(type);
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        intoleranceLevels.forEach((type, level) -> {
            if (level > 0) {
                tag.putInt(NBT_PREFIX_LEVEL + type.name(), level);
            }
        });

        discoveredIntolerances.forEach(type -> {
            tag.putBoolean(NBT_PREFIX_DISCOVERED + type.name(), true);
        });
    }

    @Override
    public void copyFrom(IntoleranceComponent original, RegistryWrapper.WrapperLookup registryLookup) {
        for (Intolerance type : Intolerance.values()) {
            this.setLevel(type, original.getLevel(type));
            this.setDiscovered(type, original.isDiscovered(type));
        }
    }

    @Override
    public void readData(ReadView readView) {

    }

    @Override
    public void writeData(WriteView writeView) {

    }
}