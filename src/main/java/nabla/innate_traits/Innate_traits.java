package nabla.innate_traits;

import nabla.innate_traits.components.IntoleranceComponent;
import nabla.innate_traits.components.PlayerIntoleranceComponent;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Innate_traits implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "innate_traits";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ComponentKey<IntoleranceComponent> INTOLERANCES = ComponentRegistry.getOrCreate(Identifier.of(MOD_ID, "intolerances"), IntoleranceComponent.class);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Innate Traits ...");
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(INTOLERANCES, player -> new PlayerIntoleranceComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
