package io.github.devkcud.aphoticwaters.worldgen;

import net.minecraft.world.level.levelgen.structure.StructureStart;

public interface SeafloorStart {
    public static final int UNSET = Integer.MIN_VALUE;

    public int aphoticwaters$baseY();

    public void aphoticwaters$setBaseY(int baseY);

    public static SeafloorStart of(final StructureStart start) {
        return (SeafloorStart) (Object) start;
    }
}
