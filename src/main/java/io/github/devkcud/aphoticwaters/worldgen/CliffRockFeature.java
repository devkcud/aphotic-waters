package io.github.devkcud.aphoticwaters.worldgen;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;

public record CliffRockFeature(Holder<MaterialRule> material) implements Feature {
    public static final MapCodec<CliffRockFeature> CODEC = MaterialRule.HOLDER_CODEC.fieldOf("material")
            .xmap(CliffRockFeature::new, CliffRockFeature::material);

    private static final int REACH = 8;
    private static final int SIZE = SectionPos.SECTION_SIZE;
    private static final int WIDTH = SIZE + 2 * REACH;
    private static final Direction[] SIDES = { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

    @Override
    public MapCodec<CliffRockFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(final WorldGenLevel level, final ChunkGenerator generator, final RandomSource random,
            final BlockPos origin) {
        if (!level.getLevel().dimension().equals(Level.OVERWORLD) || !(generator instanceof NoiseBasedChunkGenerator)) {
            return false;
        }

        final int minY = level.getMinY();
        final int seaLevel = generator.getSeaLevel();

        if (seaLevel <= minY) {
            return false;
        }

        final int seaY = Math.min(level.getMaxY(), seaLevel - 1);
        final var chunk = level.getChunk(origin);
        final int startX = chunk.getPos().getMinBlockX();
        final int startZ = chunk.getPos().getMinBlockZ();
        final var probe = new BlockPos.MutableBlockPos();
        final int[] floors = new int[WIDTH * WIDTH];
        boolean hasSubmergedFloor = false;

        for (int z = -REACH; z < SIZE + REACH; z++) {
            final int margin = z < 0 || z >= SIZE ? 0 : REACH;

            for (int x = -margin; x < SIZE + margin; x++) {
                final int floor = floorHeight(level, probe, startX + x, startZ + z);
                floors[index(x, z)] = floor;
                hasSubmergedFloor |= floor < seaY;
            }
        }

        if (!hasSubmergedFloor) {
            return false;
        }

        final var state = level.getLevel().getChunkSource().randomState();
        final var samplers = state.samplersWithContext(SamplerContext.EMPTY_UNCACHED);
        final var volume = new DensityVolume(SIZE, seaY - minY + 1, SIZE, startX, minY, startZ);
        final var context = new MaterialRuleContext(state.surfaceSystem(), state, volume, samplers,
                level::getBiome, new WorldGenerationContext(generator, chunk), null);
        final var rule = material.value().compile(context);
        final var pos = new BlockPos.MutableBlockPos();
        boolean placed = false;

        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                final int index = index(x, z);
                final int top = Math.min(seaY, floors[index] - 1);
                final int bottom = lowestFloor(floors, index) + 1;
                boolean first = true;

                for (int y = bottom; y <= top; y++) {
                    pos.set(startX + x, y, startZ + z);
                    final BlockState host = chunk.getBlockState(pos);
                    if (!host.is(Blocks.DEEPSLATE) && !host.is(Blocks.STONE)) {
                        continue;
                    }

                    final BlockState above = chunk.getBlockState(probe.setWithOffset(pos, Direction.UP));
                    if (!above.is(BlockTags.BLOCKS_MOTION_IN_HEIGHTMAP) || !above.getFluidState().isEmpty()
                            || !facesOcean(level, pos, probe, floors, index, seaY)) {
                        continue;
                    }

                    if (first) {
                        final var surface = Heightmap.Types.WORLD_SURFACE;
                        final int slopeX = chunk.getHeight(surface, Math.min(x + 1, SIZE - 1), z)
                                - chunk.getHeight(surface, Math.max(x - 1, 0), z);
                        final int slopeZ = chunk.getHeight(surface, x, Math.min(z + 1, SIZE - 1))
                                - chunk.getHeight(surface, x, Math.max(z - 1, 0));

                        context.updateXZ(pos.getX(), pos.getZ(), slopeX, slopeZ);
                        first = false;
                    }

                    context.updateY(1, 1, y + 1, y);
                    final BlockState rock = rule.tryApply(pos.getX(), y, pos.getZ());
                    if (rock != null && rock != host) {
                        placed |= level.setBlock(pos, rock, Block.UPDATE_CLIENTS);
                    }
                }
            }
        }

        return placed;
    }

    private static int index(final int x, final int z) {
        return (z + REACH) * WIDTH + x + REACH;
    }

    private static int floorHeight(final WorldGenLevel level, final BlockPos.MutableBlockPos probe,
            final int x, final int z) {
        final int minY = level.getMinY();
        int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, x, z) - 1;

        if (y < minY || !isIceCover(level.getBlockState(probe.set(x, y, z)))) {
            return y;
        }

        while (--y >= minY) {
            final BlockState state = level.getBlockState(probe.setY(y));

            if (!isIceCover(state) && !isOpenWater(state)) {
                break;
            }
        }

        return y;
    }

    private static boolean isIceCover(final BlockState state) {
        return state.is(BlockTags.ICE) || state.is(BlockTags.SNOW);
    }

    private static boolean isOpenWater(final BlockState state) {
        return state.getFluidState().is(FluidTags.WATER) && !state.is(BlockTags.BLOCKS_MOTION_IN_HEIGHTMAP);
    }

    private static int lowestFloor(final int[] floors, final int index) {
        int lowest = floors[index];
        for (final Direction side : SIDES) {
            final int step = side.getStepX() + side.getStepZ() * WIDTH;
            for (int distance = 1; distance <= REACH; distance++) {
                lowest = Math.min(lowest, floors[index + step * distance]);
            }
        }

        return lowest;
    }

    private static boolean facesOcean(final WorldGenLevel level, final BlockPos pos,
            final BlockPos.MutableBlockPos probe,
            final int[] floors, final int index, final int seaY) {
        final int y = pos.getY();

        for (final Direction side : SIDES) {
            final int step = side.getStepX() + side.getStepZ() * WIDTH;
            probe.set(pos);

            for (int distance = 1; distance <= REACH; distance++) {
                probe.move(side);

                if (!isOpenWater(level.getBlockState(probe))) {
                    break;
                }

                if (y <= floors[index + step * distance]) {
                    continue;
                }

                final BlockState sea = level.getBlockState(probe.setY(seaY));

                if ((isOpenWater(sea) || isIceCover(sea)) && level.getBiome(probe).is(BiomeTags.IS_OCEAN)) {
                    return true;
                }

                probe.setY(y);
            }
        }

        return false;
    }
}
