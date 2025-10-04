package nabla.innate_traits.util;

import nabla.innate_traits.components.Intolerance;

public interface IInnateTraitsData {
    int getLevel(Intolerance type);
    void setLevel(Intolerance type, int level);

    boolean isDiscovered(Intolerance type);
    void setDiscovered(Intolerance type, boolean discovered);

    boolean hasBeenInitialized();
    void setInitialized(boolean initialized);
}