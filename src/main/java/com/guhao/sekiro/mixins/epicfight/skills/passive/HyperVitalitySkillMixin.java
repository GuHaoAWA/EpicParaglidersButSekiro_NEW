package com.guhao.sekiro.mixins.epicfight.skills.passive;

import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import com.guhao.sekiro.config.ConfigManager;
import com.guhao.sekiro.utils.MathUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSkillExecutionFeedback;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.HyperVitalitySkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;

@Mixin(HyperVitalitySkill.class)
public abstract class HyperVitalitySkillMixin extends PassiveSkill {

    public HyperVitalitySkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Modifies the Hyper Vitality skill by integrating with the Paragliders stamina system,
     * Innate weapon skills will consume the stamina of a basic attack with the given weapon
     * multiplied by the multiplier set in the server config for this skill.
     *
     * @param container
     * @param event
     * @param ci
     */
    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getPlayerPatch(SkillContainer container, SkillConsumeEvent event, CallbackInfo ci) {
        if (!container.getExecuter().getSkill(event.getSkill()).isDisabled() && event.getSkill().getCategory() == SkillCategories.WEAPON_INNATE) {
            PlayerPatch<?> playerpatch = event.getPlayerPatch();
            PlayerMovement playerMovement = PlayerMovement.of(playerpatch.getOriginal());
            if (playerpatch.getSkill(SkillSlots.WEAPON_INNATE).getStack() < 1 && container.getStack() > 0 && !((Player)playerpatch.getOriginal()).isCreative()) {
                float consumption = event.getSkill().getConsumption();
                if (!playerMovement.isDepleted()) {
                    event.setResourceType(Resource.NONE);
                    container.setMaxResource(consumption * 0.2F);
                    if (event.shouldConsume()) {
                        ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
                        container.getExecuter().consumeStamina((float) (MathUtils.getAttackStaminaCost(playerpatch.getOriginal()) * ConfigManager.SERVER_CONFIG.hyperVitalityMultiplier()));
                        container.setMaxDuration(event.getSkill().getMaxDuration());
                        container.activate();
                        EpicFightNetworkManager.sendToPlayer(SPSkillExecutionFeedback.executed(container.getSlotId()), (ServerPlayer)playerpatch.getOriginal());
                    }
                }
            }
        }
        ci.cancel();
    }
}