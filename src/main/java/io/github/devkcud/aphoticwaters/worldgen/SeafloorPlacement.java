package io.github.devkcud.aphoticwaters.worldgen;

import java.util.Arrays;

import io.github.devkcud.aphoticwaters.AphoticWaters;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.MonumentBuilding;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import net.minecraft.world.level.levelgen.structure.structures.ShipwreckPieces.ShipwreckPiece;
import net.minecraft.world.level.levelgen.structure.structures.ShipwreckStructure;

public final class SeafloorPlacement {
    private static final TagKey<Structure> STRUCTURES = TagKey.create(Registries.STRUCTURE,
            Identifier.fromNamespaceAndPath(AphoticWaters.MOD_ID, "seafloor_structures"));
    private static final int SAMPLES = 5;
    private static final int MAX_SPAN = 96;

    private SeafloorPlacement() {
    }

    public static void fit(final StructureStart start, final Holder<Structure> selected,
            final ResourceKey<Level> dimension, final ChunkGenerator generator, final LevelHeightAccessor height,
            final RandomState random) {
        if (!start.isValid() || !dimension.equals(Level.OVERWORLD) || !(generator instanceof NoiseBasedChunkGenerator)
                || !selected.is(STRUCTURES) || selected.value().terrainAdaptation() != TerrainAdjustment.NONE
                || start.getPieces().size() != 1) {
            return;
        }

        final var structure = selected.value();
        final var piece = start.getPieces().getFirst();
        final boolean monument = structure.getClass() == OceanMonumentStructure.class
                && piece.getClass() == MonumentBuilding.class;
        final BoundingBox box = piece.getBoundingBox();
        final int minY = height.getMinY() + 5;
        final int maxY = Math.min(height.getMaxY() + 1, generator.getSeaLevel() - 1) - box.getYSpan();
        if (box.getXSpan() > MAX_SPAN || box.getZSpan() > MAX_SPAN || box.getYSpan() > MAX_SPAN || minY > maxY) {
            return;
        }

        if (monument) {
            final int baseY = Mth.clamp(floorHeight(box, generator, height, random), minY, maxY);
            moveMonument((MonumentBuilding) piece, baseY);
            SeafloorStart.of(start).aphoticwaters$setBaseY(baseY);
        } else if (structure.getClass() == ShipwreckStructure.class && !((ShipwreckStructure) structure).isBeached
                && piece.getClass() == ShipwreckPiece.class) {
            final var ship = (ShipwreckPiece) piece;
            if (ship.heightAdjusted && !ship.isTooBigToFitInWorldGenRegion()) {
                return;
            }

            final int keel = keelHeight(ship);
            if (keel < 0) {
                return;
            }

            final int originY = Mth.clamp(floorHeight(box, generator, height, random) - keel, minY, maxY);
            box.move(0, originY - box.minY(), 0);
            ship.adjustPositionHeight(originY);
            SeafloorStart.of(start).aphoticwaters$setBaseY(originY + keel);
        }
    }

    public static void moveMonument(final MonumentBuilding building, final int baseY) {
        final int offset = baseY - building.getBoundingBox().minY();
        if (offset == 0) {
            return;
        }

        building.move(0, offset, 0);
        for (final StructurePiece room : building.childPieces) {
            room.move(0, offset, 0);
        }
    }

    private static int keelHeight(final ShipwreckPiece ship) {
        int keel = -1;
        for (final var palette : ship.template().palettes) {
            int bottom = Integer.MAX_VALUE;
            for (final var block : palette.blocks()) {
                if (block.pos().getY() < bottom && Heightmap.Types.OCEAN_FLOOR_WG.isOpaque().test(block.state())
                        && !block.state().is(Blocks.STRUCTURE_BLOCK) && !block.state().is(Blocks.JIGSAW)) {
                    bottom = block.pos().getY();
                    if (bottom == 0) {
                        break;
                    }
                }
            }

            if (bottom == Integer.MAX_VALUE) {
                return -1;
            }

            keel = Math.max(keel, bottom);
        }

        return keel;
    }

    private static int floorHeight(final BoundingBox box, final ChunkGenerator generator,
            final LevelHeightAccessor height, final RandomState random) {
        final int[] floors = new int[SAMPLES * SAMPLES];
        for (int z = 0; z < SAMPLES; z++) {
            final int blockZ = box.minZ() + z * (box.getZSpan() - 1) / (SAMPLES - 1);
            for (int x = 0; x < SAMPLES; x++) {
                final int blockX = box.minX() + x * (box.getXSpan() - 1) / (SAMPLES - 1);
                floors[z * SAMPLES + x] = generator.getBaseHeight(blockX, blockZ, Heightmap.Types.OCEAN_FLOOR_WG,
                        height, random);
            }
        }

        Arrays.sort(floors);
        return floors[floors.length / 2];
    }
}
