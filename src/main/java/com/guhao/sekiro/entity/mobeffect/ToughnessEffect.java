package com.guhao.sekiro.entity.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class ToughnessEffect extends MobEffect {
    public ToughnessEffect() {
        super(MobEffectCategory.NEUTRAL, -13421569);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.toughness";
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
