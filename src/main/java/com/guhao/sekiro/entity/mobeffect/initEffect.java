package com.guhao.sekiro.entity.mobeffect;

import com.guhao.sekiro.EpicParaglidersButSekiroMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class initEffect {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EpicParaglidersButSekiroMod.MOD_ID);
    public static final RegistryObject<MobEffect> TOUGHNESS_EFFECT = REGISTRY.register("toughness", com.guhao.sekiro.entity.mobeffect.ToughnessEffect::new);
}
