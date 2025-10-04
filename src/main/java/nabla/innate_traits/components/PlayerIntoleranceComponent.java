package nabla.innate_traits.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerIntoleranceComponent implements IntoleranceComponent {
    // Deine bisherige Logik ist hier perfekt, keine Änderungen nötig
    private final Map<Intolerance, Integer> intoleranceLevels = new EnumMap<>(Intolerance.class);
    private final Set<Intolerance> discoveredIntolerances = new HashSet<>();

    public PlayerIntoleranceComponent() {
        for (Intolerance type : Intolerance.values()) {
            intoleranceLevels.put(type, 0);
        }
    }

    @Override
    public int getLevel(Intolerance type) {
        return intoleranceLevels.getOrDefault(type, 0);
    }

    @Override
    public void setLevel(Intolerance type, int level) {
        if (level >= 0 && level <= 2) {
            intoleranceLevels.put(type, level);
        }
    }

    @Override
    public boolean isDiscovered(Intolerance type) {
        return discoveredIntolerances.contains(type);
    }

    @Override
    public void setDiscovered(Intolerance type, boolean discovered) {
        if (discovered) {
            discoveredIntolerances.add(type);
        } else {
            discoveredIntolerances.remove(type);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtCompound levelsTag = tag.getCompound("intoleranceLevels");
        for (Intolerance type : Intolerance.values()) {
            if (levelsTag.contains(type.name())) {
                intoleranceLevels.put(type, levelsTag.getInt(type.name()));
            }
        }
        NbtCompound discoveredTag = tag.getCompound("discoveredIntolerances");
        discoveredIntolerances.clear();
        for (String key : discoveredTag.getKeys()) {
            try {
                discoveredIntolerances.add(Intolerance.valueOf(key));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound levelsTag = new NbtCompound();
        intoleranceLevels.forEach((type, level) -> levelsTag.putInt(type.name(), level));
        tag.put("intoleranceLevels", levelsTag);

        NbtCompound discoveredTag = new NbtCompound();
        discoveredIntolerances.forEach(type -> discoveredTag.putBoolean(type.name(), true));
        tag.put("discoveredIntolerances", discoveredTag);
    }

    @Override
    public void copyFrom(IntoleranceComponent original, RegistryWrapper.WrapperLookup wrapperLookup) {
        for (Intolerance type : Intolerance.values()) {
            this.setLevel(type, original.getLevel(type));
            this.setDiscovered(type, original.isDiscovered(type));
        }
    }
}