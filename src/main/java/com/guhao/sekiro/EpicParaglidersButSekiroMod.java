package com.guhao.sekiro;

import com.guhao.sekiro.config.ConfigManager;
import com.guhao.sekiro.entity.mobeffect.initEffect;
import com.guhao.sekiro.entity.toughnessMod;
import com.guhao.sekiro.gameasset.ExhaustionAnimations;
import com.guhao.sekiro.network.ModNet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
public class EpicParaglidersButSekiroMod {

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

    public EpicParaglidersButSekiroMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ExhaustionAnimations::registerAnimations); //TODO: Can I just use @SubscribeEvent in the original method to bypass this?
        Contents.registerEventHandlers(eventBus);
        EpicParaglidersAttributes.registerEventHandlers(eventBus);
        ModNet.init();
        ConfigManager.registerConfigs();
        initEffect.REGISTRY.register(bus);
        MinecraftForge.EVENT_BUS.register(new toughnessMod());
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        MAX_STAMINA = builder.comment("MaxStamina").defineInRange("MaxStamina", 12.0D, 0.1D, Double.MAX_VALUE);
        CONFIG_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        // Attributes for modifying stamina consumption in-game
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.DAGGER_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.SWORD_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.LONGSWORD_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.GREATSWORD_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.UCHIGATANA_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.TACHI_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.SPEAR_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.KNUCKLE_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.AXE_STAMINA_REDUCTION.get());

        event.add(EntityType.PLAYER, EpicParaglidersAttributes.BLOCK_STAMINA_REDUCTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.DODGE_STAMINA_REDUCTION.get());


        // Attributes needed for datapack editing
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.WEAPON_STAMINA_CONSUMPTION.get());
        event.add(EntityType.PLAYER, EpicParaglidersAttributes.WEAPON_TYPE.get());
    }
}
