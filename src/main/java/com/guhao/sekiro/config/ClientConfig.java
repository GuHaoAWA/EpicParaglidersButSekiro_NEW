package com.guhao.sekiro.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;


public class ClientConfig {

    private final ForgeConfigSpec.BooleanValue useStaminaWheel;

    public boolean useStaminaWheel() {
        return this.useStaminaWheel.get();
    }

    public ClientConfig(ForgeConfigSpec.Builder client) {
        client.push("gui");

        this.useStaminaWheel = client.comment("If true, the mod will use the Paragliders stamina wheel to display the player's current stamina.\n" +
                        "If false, the mod will use the Epic Fight stamina bar to display the player's current stamina.")
                .define("gui.use_stamina_wheel", true);

        client.pop();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client.build());
    }
}
