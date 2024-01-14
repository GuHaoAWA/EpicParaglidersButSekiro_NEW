package com.guhao.sekiro.mixins.epicfight.skills.passive;

import com.guhao.sekiro.config.ConfigManager;
import com.guhao.sekiro.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.EnduranceSkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(EnduranceSkill.class)
public abstract class EnduranceSkillMixin extends PassiveSkill {

    public EnduranceSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Redirects the max stun shield for the Endurance skill. Uses the Paragliders
     * stamina system now along with some extra math to ensure it's fairly balanced.
     *
     * @param playerPatch
     * @param staminaConsumed
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;setMaxStunShield(F)V"), remap = false, method = "executeOnServer")
    private void modifyMaxStunShield(ServerPlayerPatch playerPatch, float staminaConsumed) {
        playerPatch.setMaxStunShield((15.0F / playerPatch.getMaxStamina()) * staminaConsumed);
    }

    /**
     * Redirects the max stun shield for the Endurance skill. Uses the Paragliders
     * stamina system now along with some extra math to ensure it's fairly balanced.
     *
     * @param playerPatch
     * @param staminaConsumed
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;setStunShield(F)V"), remap = false, method = "executeOnServer")
    private void modifyStunShield(ServerPlayerPatch playerPatch, float staminaConsumed) {
        playerPatch.setStunShield((15.0F / playerPatch.getMaxStamina()) * staminaConsumed);
    }

    /**
     * Redirects the consumed stamina for the Endurance skill. Uses the Paragliders
     * stamina system now along with some extra math to ensure it's fairly balanced.
     *
     * @param playerPatch
     * @param staminaConsumed
     * @return
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;consumeStamina(F)Z"), remap = false, method = "executeOnServer")
    private boolean modifyStaminaConsumed(ServerPlayerPatch playerPatch, float staminaConsumed) {
        return playerPatch.consumeStamina((float) (MathUtils.calculateTriangularRoot(staminaConsumed) * ConfigManager.SERVER_CONFIG.enduranceMultiplier()));
    }
}