package com.guhao.sekiro.mixins.skills;

import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.PassiveSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.TechnicianSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(TechnicianSkill.class)
public abstract class TechnicianSkillMixin extends PassiveSkill {
    private static final float ROLL_STAMINA_COST_REDUCTION = 0.5f;
    private static PlayerPatch PLAYER_PATCH;

    public TechnicianSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "lambda$onInitiate$1", at = @At("HEAD"), remap = false)
    private static void getPlayerPatch(SkillContainer container, HurtEvent.Pre event, CallbackInfo ci) {
        PLAYER_PATCH = container.getExecuter();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "lambda$onInitiate$1", at = @At(value = "STORE"), ordinal = 0, remap = false)
    private static float setTechnicianStaminaCost(float consumption) {
        PlayerMovement playerMovement = PlayerMovement.of(PLAYER_PATCH.getOriginal());
        PlayerMovementInterface playerMovementInterface = ((PlayerMovementInterface) playerMovement);
        int currentStaminaCost = playerMovementInterface.getTotalActionStaminaCost();

        int staminaReduction = (int) (currentStaminaCost * ROLL_STAMINA_COST_REDUCTION);

        ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(-staminaReduction);
        ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
        return 0.0f;
    }
}
