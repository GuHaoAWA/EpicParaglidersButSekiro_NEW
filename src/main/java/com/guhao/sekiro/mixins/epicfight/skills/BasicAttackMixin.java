package com.guhao.sekiro.mixins.epicfight.skills;

import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import com.guhao.sekiro.utils.MathUtils;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(BasicAttack.class)
public abstract class BasicAttackMixin extends Skill {

    //TODO: Major issue with attacking stamina and dodging stamina. Look into later
    public BasicAttackMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "executeOnServer", at = @At("TAIL"), remap = false)
    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {
        PlayerMovement playerMovement = PlayerMovement.of(executer.getOriginal());
        PlayerMovementInterface playerMovementInterface = ((PlayerMovementInterface) playerMovement);
        int attackStaminaConsumption = MathUtils.getAttackStaminaCost(executer.getOriginal());

        playerMovementInterface.attackingServerSide(true);
        playerMovementInterface.setActionStaminaCostServerSide(attackStaminaConsumption);
    }
}
