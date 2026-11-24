package io.github.devkcud.aphoticwaters.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import io.github.devkcud.aphoticwaters.worldgen.SeafloorTerrain;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {
    @Inject(method = "lambda$buildTerrain$0", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/NoiseBasedChunkGenerator;buildSurface"))
    private void aphoticwaters$shapeSeafloor(final CallbackInfoReturnable<ChunkAccess> result,
            @Local(argsOnly = true) final ChunkAccess chunk, @Local(argsOnly = true) final StructureManager structures) {
        final var generator = (NoiseBasedChunkGenerator) (Object) this;
        SeafloorTerrain.blend(chunk, structures, generator.generatorSettings().value());
    }
}
