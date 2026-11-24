package io.github.devkcud.aphoticwaters.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import io.github.devkcud.aphoticwaters.worldgen.SeafloorStart;
import io.github.devkcud.aphoticwaters.worldgen.SeafloorTerrain;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @ModifyExpressionValue(method = "createReferences", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/StructureStart;getBoundingBox"))
    private BoundingBox aphoticwaters$includeSlope(final BoundingBox box, @Local final StructureStart start) {
        return SeafloorStart.of(start).aphoticwaters$baseY() == SeafloorStart.UNSET ? box
                : box.inflatedBy(SeafloorTerrain.MARGIN, 0, SeafloorTerrain.MARGIN);
    }
}
