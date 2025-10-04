package nabla.innate_traits.components;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface IntoleranceComponent extends Component {

    int getLevel(Intolerance type);

    void setLevel(Intolerance type, int level);

    boolean isDiscovered(Intolerance type);

    void setDiscovered(Intolerance type, boolean discovered);

    @Override
    void writeData(WriteView writeView);

    @Override
    void readData(ReadView readView);
}
