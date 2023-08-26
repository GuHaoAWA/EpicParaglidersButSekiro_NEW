package com.guhao.sekiro;

import com.guhao.sekiro.entity.mobeffect.InitEffect;
import com.guhao.sekiro.entity.toughnessMod;
import com.guhao.sekiro.network.ModNet;
import joptsimple.internal.AbbreviationMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tictim.paraglider.contents.Contents;

@Mod(EpicParaglidersButSekiroMod.MOD_ID)
@Mod.EventBusSubscriber(modid = EpicParaglidersButSekiroMod.MOD_ID, bus = Bus.MOD)
public class EpicParaglidersButSekiroMod
{

    public static final Logger LOGGER = LogManager.getLogger("EpicParaglidersButSekiro");
    public static final String MOD_ID = "epicparaglidersbutsekiro";
    public static ForgeConfigSpec.DoubleValue MAX_STAMINA;

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    public static String locStr(String path) {
        return loc(path).toString();
    }
    public static ForgeConfigSpec CONFIG_SPEC;
    public EpicParaglidersButSekiroMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Contents.registerEventHandlers(eventBus);
        EPModCfg.init();
        ModNet.init();
        InitEffect.REGISTRY.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new toughnessMod());
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        MAX_STAMINA = builder.comment("MaxStamina").defineInRange("MaxStamina", 12.0D, 0.1D, 999999999999.0D);
        CONFIG_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);

    }
}
