package io.github.devkcud.aphoticwaters;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;

@Mod(AphoticWaters.MOD_ID)
public final class AphoticWaters {
    public static final String MOD_ID = "aphoticwaters";

    public AphoticWaters() {
        LogUtils.getLogger().info("Loading Aphotic Waters");
    }
}
