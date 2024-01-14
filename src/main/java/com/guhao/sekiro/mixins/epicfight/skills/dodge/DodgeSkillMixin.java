package com.guhao.sekiro.mixins.epicfight.skills.dodge;

import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import com.guhao.sekiro.config.ConfigManager;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(DodgeSkill.class)
public abstract class DodgeSkillMixin extends Skill {

    public DodgeSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Modifies the base stamina consumed when performing a Step or Roll
     * dodge skill.
     *
     * @param executer
     * @param args
     * @param ci
     */
    @Inject(method = "executeOnServer", at = @At("HEAD"), remap = false, cancellable = true)
    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {
        PlayerMovement playerMovement = PlayerMovement.of(executer.getOriginal());
        String skillName = this.registryName.getPath();

        if (skillName.equals("step")) {
            this.consumption = ConfigManager.SERVER_CONFIG.baseStepStaminaCost();
        }
        else if (skillName.equals("roll")) {
            this.consumption = ConfigManager.SERVER_CONFIG.baseRollStaminaCost();
        }

        ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
    }
}