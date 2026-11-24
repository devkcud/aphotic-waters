package io.github.devkcud.aphoticwaters.worldgen;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import net.minecraft.world.level.levelgen.structure.structures.ShipwreckStructure;

public final class SeafloorTerrain {
    public static final int MARGIN = 32;
    private static final int MAX_CHANGE = 96;
    private static final int FOOTING_DEPTH = 8;

    private SeafloorTerrain() {
    }

    public static void blend(final ChunkAccess chunk, final StructureManager structures,
            final NoiseGeneratorSettings settings) {
        if (chunk.isUpgrading() || chunk.getAllReferences().isEmpty()) {
            return;
        }

        final var pos = chunk.getPos();
        final List<StructureStart> starts = structures.startsForStructure(pos.x(), pos.z(),
                structure -> structure instanceof OceanMonumentStructure || structure instanceof ShipwreckStructure);
        if (starts.isEmpty()) {
            return;
        }

        final var beds = starts.stream()
                .filter(start -> SeafloorStart.of(start).aphoticwaters$baseY() != SeafloorStart.UNSET)
                .map(Bed::from).toList();
        if (beds.isEmpty()) {
            return;
        }

        final var block = new BlockPos.MutableBlockPos();
        for (int z = pos.getMinBlockZ(); z <= pos.getMaxBlockZ(); z++) {
            for (int x = pos.getMinBlockX(); x <= pos.getMaxBlockX(); x++) {
                final int floor = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
                blendColumn(beds, chunk, settings, block.set(x, floor, z));
            }
        }
    }

    private static void blendColumn(final List<Bed> beds, final ChunkAccess chunk,
            final NoiseGeneratorSettings settings,
            final BlockPos.MutableBlockPos pos) {
        final int floor = pos.getY();
        int inside = Integer.MAX_VALUE;
        double total = 0.0;
        double weights = 1.0;
        for (final Bed bed : beds) {
            final int distance = bed.distanceSquared(pos.getX(), pos.getZ());
            if (distance == 0) {
                inside = Math.min(inside, bed.y);
            } else if (distance < MARGIN * MARGIN) {
                final double t = 1.0 - Math.sqrt(distance) / MARGIN;
                final double slope = t * t * (3.0 - 2.0 * t);
                final double weight = slope / (1.0 - slope);
                total += weight * (Mth.clamp(floor, bed.y - 1, bed.y + 1) - floor);
                weights += weight;
            }
        }

        final int delta = inside == Integer.MAX_VALUE ? (int) Math.round(total / weights)
                : Mth.clamp(floor, inside - 1, inside + 1) - floor;
        if (delta != 0 || inside != Integer.MAX_VALUE) {
            final int target = floor + Mth.clamp(delta, -MAX_CHANGE, MAX_CHANGE);
            shapeColumn(chunk, settings, pos, floor, target, inside != Integer.MAX_VALUE);
        }
    }

    private static void shapeColumn(final ChunkAccess chunk, final NoiseGeneratorSettings settings,
            final BlockPos.MutableBlockPos pos, final int floor, final int target, final boolean footing) {
        for (int y = target + 1; y <= floor; y++) {
            final var state = y < settings.seaLevel() ? settings.defaultFluid() : Blocks.AIR.defaultBlockState();
            chunk.setBlockState(pos.setY(y), state);
        }

        final int ground = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        final int top = Math.min(target, ground + MAX_CHANGE);
        final int bottom = footing ? Math.min(ground + 1, target - FOOTING_DEPTH + 1) : ground + 1;
        for (int y = Math.max(bottom, chunk.getMinY() + 1); y <= top; y++) {
            pos.setY(y);
            if (!Heightmap.Types.OCEAN_FLOOR_WG.isOpaque().test(chunk.getBlockState(pos))) {
                chunk.setBlockState(pos, settings.defaultBlock());
            }
        }
    }

    private record Bed(BoundingBox box, int y) {
        private static Bed from(final StructureStart start) {
            final int base = SeafloorStart.of(start).aphoticwaters$baseY();
            final int floor = start.getStructure() instanceof OceanMonumentStructure ? base - 1 : base;
            return new Bed(start.getPieces().getFirst().getBoundingBox(), floor);
        }

        private int distanceSquared(final int x, final int z) {
            final int dx = Math.max(0, Math.max(box.minX() - x, x - box.maxX()));
            final int dz = Math.max(0, Math.max(box.minZ() - z, z - box.maxZ()));
            return dx * dx + dz * dz;
        }
    }
}
