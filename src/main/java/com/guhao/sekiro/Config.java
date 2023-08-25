package com.guhao.sekiro;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.DoubleValue MAX_STAMINA;
    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        MAX_STAMINA = COMMON_BUILDER.comment("Maximum stamina").defineInRange("maxStamina", 12, 0.1, 999999999.99);
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}