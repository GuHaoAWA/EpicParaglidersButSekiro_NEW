package com.guhao.sekiro.mixins.sekiro;

import com.guhao.sekiro.EpicParaglidersButSekiroMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(value = HurtEvent.Post.class ,remap = false)
public class HurtEventMixin {
    double MAX_STAMINA = EpicParaglidersButSekiroMod.MAX_STAMINA.get();
    @Inject(method = "<init>(Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;Lyesman/epicfight/world/damagesource/EpicFightDamageSource;F)V", at = @At("RETURN"))
    private void onHurt(ServerPlayerPatch playerpatch, EpicFightDamageSource damageSource, float amount, CallbackInfo ci) {
        if (playerpatch.getStamina() <= 0.11F) {
            damageSource.setStunType(StunType.KNOCKDOWN); // Use the updated StunType enum value
            playerpatch.setStamina((float) MAX_STAMINA);
        }
        if (amount > 0.0F) {
            float staminaReduce = amount * 0.5F;
            staminaReduce = Math.max(2.5F, Math.min(staminaReduce, 5.0F));
            float newStamina = playerpatch.getStamina() - staminaReduce;
            playerpatch.setStamina(newStamina);
        }
    }
}
