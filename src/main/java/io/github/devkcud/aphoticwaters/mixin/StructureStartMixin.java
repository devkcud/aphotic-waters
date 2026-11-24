package io.github.devkcud.aphoticwaters.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.devkcud.aphoticwaters.worldgen.SeafloorPlacement;
import io.github.devkcud.aphoticwaters.worldgen.SeafloorStart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.MonumentBuilding;
import net.minecraft.world.level.levelgen.structure.structures.ShipwreckPieces.ShipwreckPiece;

@Mixin(StructureStart.class)
public abstract class StructureStartMixin implements SeafloorStart {
    @Shadow
    private volatile BoundingBox cachedBoundingBox;

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
        cachedBoundingBox = null;
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
        final StructureStart start = result.getReturnValue();
        if (!(tag.get(aphoticwaters$baseKey) instanceof IntTag(int baseY))
                || baseY < DimensionType.MIN_Y || baseY > DimensionType.MAX_Y
                || start == null || !SeafloorPlacement.supports(start)) {
            return;
        }

        final var piece = start.getPieces().getFirst();
        if (piece instanceof MonumentBuilding building) {
            final var child = tag.getListOrEmpty("Children").getCompoundOrEmpty(0);
            if (child.read("BB", BoundingBox.CODEC).filter(saved -> saved.minY() == baseY).isEmpty()) {
                return;
            }

            SeafloorPlacement.moveMonument(building, baseY);
        } else if (piece instanceof ShipwreckPiece ship && !SeafloorPlacement.canRestoreShip(ship, baseY)) {
            return;
        }

        SeafloorStart.of(start).aphoticwaters$setBaseY(baseY);
    }
}
