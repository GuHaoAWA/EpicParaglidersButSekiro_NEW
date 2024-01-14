package com.guhao.sekiro.mixins.epicfight.client;

import com.guhao.sekiro.gameasset.ExhaustionMotions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.item.*;

@Mixin(ClientAnimator.class)
public abstract class ClientAnimatorMixin extends Animator {

    @Inject(method = "poseTick", at = @At("TAIL"), remap = false)
    private void addExhaustedAnimations(CallbackInfo ci) {
        if (entitypatch instanceof PlayerPatch playerPatch) {

            boolean isExhausted = false;
            PlayerMovement playerMovement = PlayerMovement.of(playerPatch.getOriginal());

            for (MobEffectInstance mobEffectInstance : playerMovement.player.getActiveEffects()) {
                if (isExhausted = mobEffectInstance.getEffect().getDescriptionId().contains("exhausted")) {
                    break;
                }
            }
            if (isExhausted) {
                this.entitypatch.currentLivingMotion = switchToExhaustedAnimations(this.entitypatch);
            }
        }
    }

    private LivingMotion switchToExhaustedAnimations(LivingEntityPatch<?> entityPatch) {
        Item item = entityPatch.getOriginal().getMainHandItem().getItem();
        Style weaponStyle = EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getMainHandItem()).getStyle(entityPatch);

        if (entityPatch.currentLivingMotion.equals(LivingMotions.IDLE) && !entityPatch.getEntityState().inaction()) {
            if (item instanceof CrossbowItem) {
                return ExhaustionMotions.EXHAUSTED_IDLE_CROSSBOW;
            }
            else if (item instanceof LongswordItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.OCHS)) {
                    return ExhaustionMotions.EXHAUSTED_IDLE_LIECHTENAUER;
                }
                else {
                    return ExhaustionMotions.EXHAUSTED_IDLE_GREATSWORD;
                }
            }
            else if (item instanceof GreatswordItem) {
                return ExhaustionMotions.EXHAUSTED_IDLE_GREATSWORD;
            }
            else if (item instanceof SpearItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.TWO_HAND)) {
                    return ExhaustionMotions.EXHAUSTED_IDLE_SPEAR;
                }
                else {
                    return ExhaustionMotions.EXHAUSTED_IDLE;
                }
            }
            else if (item instanceof TachiItem) {
                return ExhaustionMotions.EXHAUSTED_IDLE_TACHI;
            }
            else if (item instanceof UchigatanaItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.SHEATH)) {
                    return ExhaustionMotions.EXHAUSTED_IDLE_SHEATH;
                }
                else {
                    return ExhaustionMotions.EXHAUSTED_IDLE;
                }
            }
            else {
                return ExhaustionMotions.EXHAUSTED_IDLE;
            }
        }
        else if (entityPatch.currentLivingMotion.equals(LivingMotions.WALK)) {
            if (item instanceof CrossbowItem) {
                return ExhaustionMotions.EXHAUSTED_WALK_CROSSBOW;
            }
            else if (item instanceof LongswordItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.OCHS)) {
                    return ExhaustionMotions.EXHAUSTED_WALK_LIECHTENAUER;
                }
                else {
                    return ExhaustionMotions.EXHAUSTED_WALK_GREATSWORD;
                }
            }
            else if (item instanceof GreatswordItem) {
                return ExhaustionMotions.EXHAUSTED_WALK_GREATSWORD;
            }
            else if (item instanceof SpearItem) {
                return ExhaustionMotions.EXHAUSTED_WALK_SPEAR;
            }
            else if (item instanceof TachiItem) {
                return ExhaustionMotions.EXHAUSTED_WALK_TACHI;
            }
            else if (item instanceof UchigatanaItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.SHEATH)) {
                    return ExhaustionMotions.EXHAUSTED_WALK_SHEATH;
                }
                else {
                    return ExhaustionMotions.EXHAUSTED_WALK_KATANA;
                }
            }
            else {
                return ExhaustionMotions.EXHAUSTED_WALK;
            }
        }
        else {
            return entityPatch.currentLivingMotion;
        }
    }
}