package nabla.innate_traits.components;

import nabla.innate_traits.Innate_traits;
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
    private boolean initialized = false;
    private static final String NBT_PREFIX_LEVEL = "innatetraits.level.";
    private static final String NBT_PREFIX_DISCOVERED = "innatetraits.discovered.";
    private static final String NBT_KEY_INITIALIZED = "innatetraits.initialized";

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
    public boolean hasBeenInitialized() { return this.initialized; }
    @Override
    public void setInitialized(boolean initialized) { this.initialized = initialized; }


    @Override
    public void readFromNbt(NbtCompound tag) {
        Innate_traits.LOGGER.info("Reading from NBT. Tag contains initialized key: " + tag.contains(NBT_KEY_INITIALIZED));

        this.discoveredIntolerances.clear();
        for (Intolerance type : Intolerance.values()) {
            String levelKey = NBT_PREFIX_LEVEL + type.name();
            intoleranceLevels.put(type, tag.getInt(levelKey, 0));

            String discoveredKey = NBT_PREFIX_DISCOVERED + type.name();
            if (tag.getBoolean(discoveredKey, false)) {
                discoveredIntolerances.add(type);
            }
        }

        this.initialized = tag.getBoolean(NBT_KEY_INITIALIZED, false);
        Innate_traits.LOGGER.info("Finished reading from NBT. 'initialized' is now: " + this.initialized);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        Innate_traits.LOGGER.info("Writing to NBT. 'initialized' is: " + this.initialized);

        intoleranceLevels.forEach((type, level) -> {
            if (level > 0) {
                tag.putInt(NBT_PREFIX_LEVEL + type.name(), level);
            }
        });

        discoveredIntolerances.forEach(type -> {
            tag.putBoolean(NBT_PREFIX_DISCOVERED + type.name(), true);
        });

        tag.putBoolean(NBT_KEY_INITIALIZED, this.initialized);
    }

    @Override
    public void copyFrom(IntoleranceComponent original, RegistryWrapper.WrapperLookup registryLookup) {
        for (Intolerance type : Intolerance.values()) {
            this.setLevel(type, original.getLevel(type));
            this.setDiscovered(type, original.isDiscovered(type));
        }
        this.setInitialized(original.hasBeenInitialized());
    }

    @Override
    public void markDirty() { }

    @Override
    public void readData(ReadView readView) { }
    @Override
    public void writeData(WriteView writeView) { }
}