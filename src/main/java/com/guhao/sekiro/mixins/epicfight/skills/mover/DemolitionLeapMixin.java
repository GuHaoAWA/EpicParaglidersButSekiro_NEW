package com.guhao.sekiro.mixins.epicfight.skills.mover;

import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import com.guhao.sekiro.config.ConfigManager;
import com.guhao.sekiro.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.ServerPlayerMovement;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.mover.DemolitionLeapSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(DemolitionLeapSkill.class)
public abstract class DemolitionLeapMixin extends Skill implements ChargeableSkill {

    public DemolitionLeapMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(at = @At("HEAD"), remap = false, cancellable = true, method = "chargingTick")
    private void modifyCharginStaminaCost(PlayerPatch<?> caster, CallbackInfo ci) {
        this.consumption = caster.getModifiedStaminaConsume(ConfigManager.SERVER_CONFIG.baseDemolitionLeapStaminaCost());
        int chargingTicks = caster.getSkillChargingTicks();

        if (chargingTicks % 5 == 0 && caster.getAccumulatedChargeAmount() < this.getMaxChargingTicks()) {
            PlayerMovement playerMovement = PlayerMovement.of(caster.getOriginal());
            int totalStaminaConsumption = (int) MathUtils.calculateTriangularNumber(((PlayerMovementInterface) playerMovement).getTotalActionStaminaCost());

            if (caster.getStamina() > totalStaminaConsumption) {
                if (playerMovement instanceof ServerPlayerMovement) {
                    ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
                    caster.setStamina(this.consumption);
                }

                caster.setChargingAmount(caster.getChargingAmount() + 5);
            }
        }
        ci.cancel();
    }
}