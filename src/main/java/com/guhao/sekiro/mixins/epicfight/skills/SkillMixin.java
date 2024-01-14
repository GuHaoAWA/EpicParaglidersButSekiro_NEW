package com.guhao.sekiro.mixins.epicfight.skills;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(Skill.Resource.class)
public abstract class SkillMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/entitypatch/player/PlayerPatch;hasStamina(F)Z"), remap = false, method = "lambda$static$6")
    private static boolean testRedirect(PlayerPatch playerPatch, float amount) {
        if (PlayerMovement.of(playerPatch.getOriginal()).isDepleted()) {
            return false;
        }
        else {
            return true;
        }
    }
}