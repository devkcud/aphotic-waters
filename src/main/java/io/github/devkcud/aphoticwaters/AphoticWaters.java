package io.github.devkcud.aphoticwaters;

import com.mojang.logging.LogUtils;

import io.github.devkcud.aphoticwaters.worldgen.CliffRockFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(AphoticWaters.MOD_ID)
public final class AphoticWaters {
    public static final String MOD_ID = "aphoticwaters";

    public AphoticWaters(final FMLJavaModLoadingContext context) {
        RegisterEvent.getBus(context.getModBusGroup())
                .addListener(event -> event.register(Registries.FEATURE_TYPE,
                        Identifier.fromNamespaceAndPath(MOD_ID, "cliff_rock"),
                        () -> CliffRockFeature.CODEC));

        LogUtils.getLogger().info("Loading Aphotic Waters");
    }
}
