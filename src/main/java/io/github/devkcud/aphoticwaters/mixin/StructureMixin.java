package io.github.devkcud.aphoticwaters.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import io.github.devkcud.aphoticwaters.worldgen.SeafloorPlacement;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

@Mixin(Structure.class)
public abstract class StructureMixin {
    @Inject(method = "generate", at = @At("RETURN"))
    private void aphoticwaters$fit(final CallbackInfoReturnable<StructureStart> result,
            @Local(argsOnly = true) final Holder<Structure> selected,
            @Local(argsOnly = true) final ResourceKey<Level> dimension,
            @Local final Structure.GenerationContext context) {
        if (context.heightAccessor() instanceof ChunkAccess chunk && !chunk.isUpgrading()) {
            SeafloorPlacement.fit(result.getReturnValue(), selected, dimension, context);
        }
    }
}
