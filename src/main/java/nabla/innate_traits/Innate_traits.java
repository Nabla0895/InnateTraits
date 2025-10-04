package nabla.innate_traits;

import nabla.innate_traits.components.Intolerance;
import nabla.innate_traits.util.IInnateTraitsData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Innate_traits implements ModInitializer {
    public static final String MOD_ID = "innate_traits";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Innate Traits (Mixin-based)...");

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            IInnateTraitsData data = (IInnateTraitsData) handler.player;
            assignTraitsOnFirstJoin(data);
        });
    }

    private void assignTraitsOnFirstJoin(IInnateTraitsData data) {
        if(!data.hasBeenInitialized()) {
            LOGGER.info("First join. Assigning traits...");

            Random random = new Random();
            List<Intolerance> availableIntolerances = new java.util.ArrayList<>(List.of(Intolerance.values()));
            Collections.shuffle(availableIntolerances);

            int strongCount = random.nextInt(2);
            for (int i = 0; i < strongCount && !availableIntolerances.isEmpty(); i++) {
                data.setLevel(availableIntolerances.removeFirst(), 2);
            }

            int weakCount = random.nextInt(2) + 1;
            for (int i = 0; i < weakCount && !availableIntolerances.isEmpty(); i++) {
                data.setLevel(availableIntolerances.removeFirst(), 1);
            }
            data.setInitialized(true);
            LOGGER.info("Traits assigned.");
        } else {
            LOGGER.info("Returning player joined.");
        }
    }
}