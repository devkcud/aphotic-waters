package io.github.devkcud.aphoticwaters.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.devkcud.aphoticwaters.worldgen.SeafloorPlacement;
import io.github.devkcud.aphoticwaters.worldgen.SeafloorStart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.MonumentBuilding;

@Mixin(StructureStart.class)
public abstract class StructureStartMixin implements SeafloorStart {
    @Unique
    private static final String aphoticwaters$baseKey = "aphoticwaters:seafloor_base";
    @Unique
    private int aphoticwaters$baseY = UNSET;

    @Override
    public int aphoticwaters$baseY() {
        return aphoticwaters$baseY;
    }

    @Override
    public void aphoticwaters$setBaseY(final int baseY) {
        aphoticwaters$baseY = baseY;
    }

    @Inject(method = "createTag", at = @At("RETURN"))
    private void aphoticwaters$save(final StructurePieceSerializationContext context, final ChunkPos pos,
            final CallbackInfoReturnable<CompoundTag> result) {
        if (aphoticwaters$baseY != UNSET) {
            result.getReturnValue().putInt(aphoticwaters$baseKey, aphoticwaters$baseY);
        }
    }

    @Inject(method = "loadStaticStart", at = @At("RETURN"))
    private static void aphoticwaters$load(final StructurePieceSerializationContext context, final CompoundTag tag,
            final long seed, final CallbackInfoReturnable<StructureStart> result) {
        final int baseY = tag.getIntOr(aphoticwaters$baseKey, UNSET);
        final StructureStart start = result.getReturnValue();
        if (baseY == UNSET || start == null || !start.isValid()) {
            return;
        }

        SeafloorStart.of(start).aphoticwaters$setBaseY(baseY);
        if (start.getPieces().getFirst() instanceof MonumentBuilding building) {
            SeafloorPlacement.moveMonument(building, baseY);
        }
    }
}
