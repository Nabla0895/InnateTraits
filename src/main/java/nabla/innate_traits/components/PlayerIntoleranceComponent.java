package nabla.innate_traits.components;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerIntoleranceComponent implements IntoleranceComponent {
    private final Map<Intolerance, Integer> intoleranceLevel = new EnumMap<>(Intolerance.class);
    private final Set<Intolerance> discoveredIntolerance = new HashSet<>();

    public PlayerIntoleranceComponent() {
        for (Intolerance type : Intolerance.values()) {
            intoleranceLevel.put(type, 0);
        }
    }

    @Override
    public int getLevel(Intolerance type) {
        return intoleranceLevel.getOrDefault(type, 0);
    }

    @Override
    public void setLevel(Intolerance type, int level) {
        if (level >= 0 && level <= 2) {
            intoleranceLevel.put(type, level);
        }
    }

    @Override
    public boolean isDiscovered(Intolerance type) {
        return discoveredIntolerance.contains(type);
    }

    @Override
    public void setDiscovered(Intolerance type, boolean discovered) {
        if (discovered) {
            discoveredIntolerance.add(type);
        } else {
            discoveredIntolerance.remove(type);
        }
    }

    @Override
    public void readData(ReadView readView) {

    }

    @Override
    public void writeData(WriteView writeView) {

    }



}
