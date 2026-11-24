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
import net.minecraft.world.level.levelgen.material.MaterialSystem;
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

        final var chunk = level.getChunk(origin);
        final int startX = chunk.getPos().getMinBlockX();
        final int startZ = chunk.getPos().getMinBlockZ();
        final int seaY = generator.getSeaLevel() - 1;
        final int maxY = Math.min(-1, seaY);
        final var probe = new BlockPos.MutableBlockPos();
        final int[] floors = new int[WIDTH * WIDTH];
        boolean hasDeepFloor = false;

        for (int z = -REACH; z < SIZE + REACH; z++) {
            for (int x = -REACH; x < SIZE + REACH; x++) {
                final int floor = floorHeight(level, probe, startX + x, startZ + z);
                floors[index(x, z)] = floor;
                hasDeepFloor |= floor < maxY;
            }
        }

        if (!hasDeepFloor) {
            return false;
        }

        final var state = level.getLevel().getChunkSource().randomState();
        final var samplers = state.samplersWithContext(SamplerContext.EMPTY_UNCACHED);
        final int minY = level.getMinY();
        final var volume = new DensityVolume(SIZE, maxY - minY + 1, SIZE, startX, minY, startZ);
        final var context = new MaterialRuleContext(state.surfaceSystem(), state, volume, samplers,
                level::getBiome, new WorldGenerationContext(generator, chunk), null);
        final var rule = material.value().compile(context);
        final var pos = new BlockPos.MutableBlockPos();
        boolean placed = false;

        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                final int index = index(x, z);
                final int top = Math.min(maxY, floors[index] - 1);
                final int bottom = lowestFloor(floors, index) + 1;
                boolean first = true;

                for (int y = bottom; y <= top; y++) {
                    pos.set(startX + x, y, startZ + z);
                    if (!chunk.getBlockState(pos).is(Blocks.DEEPSLATE)) {
                        continue;
                    }

                    final BlockState above = chunk.getBlockState(probe.setWithOffset(pos, Direction.UP));
                    if (!above.is(BlockTags.BLOCKS_MOTION_IN_HEIGHTMAP) || !above.getFluidState().isEmpty()
                            || !facesOcean(level, pos, probe, floors, index, seaY)) {
                        continue;
                    }

                    if (first) {
                        context.updateXZ(pos.getX(), pos.getZ(), MaterialSystem.getSurfaceGradientX(chunk, x, z),
                                MaterialSystem.getSurfaceGradientZ(chunk, x, z));
                        first = false;
                    }

                    context.updateY(1, 1, y + 1, y);
                    final BlockState rock = rule.tryApply(pos.getX(), y, pos.getZ());
                    if (rock != null) {
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
        int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, x, z) - 1;
        if (y < 0) {
            return y;
        }

        BlockState state = level.getBlockState(probe.set(x, y, z));
        if (isIceCover(state)) {
            while (y > level.getMinY() && (isIceCover(state) || state.getFluidState().is(FluidTags.WATER))) {
                state = level.getBlockState(probe.setY(--y));
            }
        }

        return y;
    }

    private static boolean isIceCover(final BlockState state) {
        return state.is(BlockTags.ICE) || state.is(BlockTags.SNOW);
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
        for (final Direction side : SIDES) {
            final int step = side.getStepX() + side.getStepZ() * WIDTH;
            probe.set(pos);

            for (int distance = 1; distance <= REACH; distance++) {
                probe.move(side);
                if (!level.getFluidState(probe).is(FluidTags.WATER)) {
                    break;
                }

                if (pos.getY() > floors[index + step * distance]) {
                    final BlockState sea = level.getBlockState(probe.setY(seaY));
                    if ((sea.getFluidState().is(FluidTags.WATER) || isIceCover(sea))
                            && level.getBiome(probe).is(BiomeTags.IS_OCEAN)) {
                        return true;
                    }

                    break;
                }
            }
        }

        return false;
    }
}
