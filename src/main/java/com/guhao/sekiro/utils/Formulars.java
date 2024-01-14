package com.guhao.sekiro.utils;

import net.minecraft.util.Mth;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

public class Formulars {
    public Formulars() {
    }
    public static float getStaminarConsumePenalty(double weight, float originalConsumption, EntityPatch<?> entitypatch) {
        float attenuation = (float)Mth.clamp(entitypatch.getOriginal().level.getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
        return ((float)(weight / 40.0 - 1.0) * attenuation + 1.0F) * originalConsumption;
    }
}
