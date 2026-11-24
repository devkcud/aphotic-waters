package io.github.devkcud.aphoticwaters.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import io.github.devkcud.aphoticwaters.worldgen.SeafloorPlacement;
import io.github.devkcud.aphoticwaters.worldgen.SeafloorStart;
import io.github.devkcud.aphoticwaters.worldgen.SeafloorTerrain;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @ModifyExpressionValue(method = "tryGenerateStructure", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/Structure;generate"))
    private StructureStart aphoticwaters$fit(final StructureStart start,
            @Local(argsOnly = true) final StructureSet.StructureSelectionEntry selected,
            @Local(argsOnly = true) final ResourceKey<Level> dimension, @Local(argsOnly = true) final ChunkAccess chunk,
            @Local(argsOnly = true) final RandomState random) {
        if (!chunk.isUpgrading()) {
            SeafloorPlacement.fit(start, selected.structure(), dimension, (ChunkGenerator) (Object) this,
                    chunk.getHeightAccessorForGeneration(), random);
        }

        return start;
    }

    @ModifyExpressionValue(method = "createReferences", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/StructureStart;getBoundingBox"))
    private BoundingBox aphoticwaters$includeSlope(final BoundingBox box, @Local final StructureStart start) {
        return SeafloorStart.of(start).aphoticwaters$baseY() == SeafloorStart.UNSET ? box
                : box.inflatedBy(SeafloorTerrain.MARGIN, 0, SeafloorTerrain.MARGIN);
    }
}
