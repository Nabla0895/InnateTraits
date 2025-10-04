package nabla.innate_traits;

import nabla.innate_traits.components.Intolerance;
import nabla.innate_traits.components.IntoleranceComponent;
import nabla.innate_traits.components.PlayerIntoleranceComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Innate_traits implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "innate_traits";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ComponentKey<IntoleranceComponent> INTOLERANCES = ComponentRegistry.getOrCreate(Identifier.of(MOD_ID, "intolerances"), IntoleranceComponent.class);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Innate Traits ...");

//      ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
//            assignTraitsOnFirstJoin(newPlayer);
//      });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            assignTraitsOnFirstJoin(handler.player);
        });
    }

    private void assignTraitsOnFirstJoin(ServerPlayerEntity player) {
        IntoleranceComponent component = INTOLERANCES.get(player);

        if(!component.hasBeenInitialized()) {
            LOGGER.info("First join for player " + player.getName().getString() + ". Assigning traits ...");

            Random random = new Random();
            List<Intolerance> availableIntolerances = new java.util.ArrayList<>(List.of(Intolerance.values()));
            Collections.shuffle(availableIntolerances);

            // 0-1 strong intolerances
            int strongCount = random.nextInt(2); // Result: 0 or 1
            for (int i = 0; i < strongCount && !availableIntolerances.isEmpty(); i++) {
                Intolerance toAssign = availableIntolerances.removeFirst();
                component.setLevel(toAssign, 2);
                LOGGER.info("Assigned STRONG intolerance for " + toAssign.name());
            }

            // 1-2 weak intolerances
            int weakCount = random.nextInt(2) + 1; // Result 1 or 2
            for (int i = 0; i < weakCount && !availableIntolerances.isEmpty(); i++) {
                Intolerance toAssign = availableIntolerances.removeFirst();
                component.setLevel(toAssign, 1);
                LOGGER.info("Assigned WEAK intolerance for " + toAssign.name());
            }
            component.setInitialized(true);
            INTOLERANCES.sync(player);
        } else {
            StringBuilder traitsLog = new StringBuilder();
            for (Intolerance type : Intolerance.values()) {
                int level = component.getLevel(type);
                if (level > 0) {
                    if (!traitsLog.isEmpty()) {
                        traitsLog.append(", ");
                    }
                    traitsLog.append(type.name()).append("(").append(level).append(")");
                }
            }
            LOGGER.info("Returning player " + player.getName().getString() + " joined. Loaded traits: [" + traitsLog + "]");
        }
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(INTOLERANCES, player -> new PlayerIntoleranceComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
