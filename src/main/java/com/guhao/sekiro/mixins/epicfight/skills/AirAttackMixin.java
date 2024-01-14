package com.guhao.sekiro.mixins.epicfight.skills;

import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import com.guhao.sekiro.utils.MathUtils;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.AirAttack;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(AirAttack.class)
public abstract class AirAttackMixin extends Skill {

    public AirAttackMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "executeOnServer", at = @At("TAIL"), remap = false)
    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {
        PlayerMovement playerMovement = PlayerMovement.of(executer.getOriginal());
        int specialAttackStaminaConsumption = MathUtils.getAttackStaminaCost(executer.getOriginal());
        assert playerMovement != null;
        ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(specialAttackStaminaConsumption + 3);
        ((PlayerMovementInterface) playerMovement).attackingServerSide(true);
    }
    @Inject(method = "createAirAttackBuilder",at = @At("RETURN"),remap = false)
    private static void setResourceToNone(CallbackInfoReturnable<Builder<AirAttack>> cir) {
        Skill.Builder<AirAttack> builder = cir.getReturnValue();builder.setResource(Skill.Resource.NONE);
    }
}
