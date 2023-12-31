package com.guhao.sekiro;

import com.guhao.sekiro.network.ModNet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
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

    public EpicParaglidersButSekiroMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Contents.registerEventHandlers(eventBus);
        EPModCfg.init();
        ModNet.init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
