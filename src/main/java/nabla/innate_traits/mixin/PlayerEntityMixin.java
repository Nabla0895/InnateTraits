package nabla.innate_traits.mixin;

import nabla.innate_traits.Innate_traits;
import nabla.innate_traits.components.Intolerance;
import nabla.innate_traits.util.IInnateTraitsData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IInnateTraitsData {

    private final Map<Intolerance, Integer> intoleranceLevels = new EnumMap<>(Intolerance.class);
    private final Set<Intolerance> discoveredIntolerances = new HashSet<>();
    private boolean initialized = false;

    private static final String NBT_PREFIX_LEVEL = "innatetraits.level.";
    private static final String NBT_PREFIX_DISCOVERED = "innatetraits.discovered.";
    private static final String NBT_KEY_INITIALIZED = "innatetraits.initialized";


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public int getLevel(Intolerance type) { return this.intoleranceLevels.getOrDefault(type, 0); }
    @Override
    public void setLevel(Intolerance type, int level) { if (level >= 0 && level <= 2) this.intoleranceLevels.put(type, level); }
    @Override
    public boolean isDiscovered(Intolerance type) { return this.discoveredIntolerances.contains(type); }
    @Override
    public void setDiscovered(Intolerance type, boolean discovered) { if (discovered) this.discoveredIntolerances.add(type); else this.discoveredIntolerances.remove(type); }
    @Override
    public boolean hasBeenInitialized() { return this.initialized; }
    @Override
    public void setInitialized(boolean initialized) { this.initialized = initialized; }


    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void onWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        Innate_traits.LOGGER.info("INJECT: Writing NBT data...");
        intoleranceLevels.forEach((type, level) -> {
            if (level > 0) nbt.putInt(NBT_PREFIX_LEVEL + type.name(), level);
        });
        discoveredIntolerances.forEach(type -> nbt.putBoolean(NBT_PREFIX_DISCOVERED + type.name(), true));
        nbt.putBoolean(NBT_KEY_INITIALIZED, this.initialized);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        Innate_traits.LOGGER.info("INJECT: Reading NBT data...");
        this.discoveredIntolerances.clear();
        for (Intolerance type : Intolerance.values()) {
            this.intoleranceLevels.put(type, nbt.getInt(NBT_PREFIX_LEVEL + type.name()));
            if (nbt.getBoolean(NBT_PREFIX_DISCOVERED + type.name())) {
                this.discoveredIntolerances.add(type);
            }
        }
        this.initialized = nbt.getBoolean(NBT_KEY_INITIALIZED);
    }
}