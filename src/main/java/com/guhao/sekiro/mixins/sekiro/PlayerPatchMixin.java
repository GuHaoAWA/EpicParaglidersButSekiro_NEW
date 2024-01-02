package com.guhao.sekiro.mixins.sekiro;

import com.guhao.sekiro.EpicParaglidersAttributes;
import com.guhao.sekiro.EpicParaglidersButSekiroMod;
import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import com.guhao.sekiro.config.ConfigManager;
import com.guhao.sekiro.gameasset.ExhaustionAnimations;
import com.guhao.sekiro.gameasset.ExhaustionMotions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.mover.DemolitionLeapSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

import static yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch.STAMINA;

@Mixin(value = PlayerPatch.class, remap = false, priority = 10000)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {
    double MAX_STAMINA = EpicParaglidersButSekiroMod.MAX_STAMINA.get();
    @Shadow
    protected int tickSinceLastAction;

    @Shadow public abstract float getStamina();

    @Shadow protected ChargeableSkill chargingSkill;

    @Shadow
    public abstract void setStamina(float value);
    @Shadow
    protected double xo;
    @Shadow
    protected double yo;
    @Shadow
    protected double zo;

    @Inject(method = "initAttributes", at = @At("TAIL"), remap = false)
    public void modifyInitAttributes(CallbackInfo ci) {
        super.initAttributes();
        ((Player) this.original).getAttribute((Attribute) EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(MAX_STAMINA);
        ((Player)this.original).getAttribute((Attribute)EpicFightAttributes.STAMINA_REGEN.get()).setBaseValue(1.0);
        ((Player)this.original).getAttribute((Attribute)EpicFightAttributes.OFFHAND_IMPACT.get()).setBaseValue(0.5);
    }

    @Inject(method = "serverTick", at = @At("TAIL"), remap = false)
    public void healthforstamina(LivingEvent.LivingUpdateEvent event, CallbackInfo ci) {
        super.serverTick(event);

        if (!this.state.inaction()) {
            ++this.tickSinceLastAction;
        }
        float stamina = this.getStamina();
        double maxStamina = MAX_STAMINA;
        float staminaRegen = (float)((Player)this.original).getAttributeValue((Attribute)EpicFightAttributes.STAMINA_REGEN.get());
        int regenStandbyTime = 900 / (int)(30.0F * staminaRegen);
        if (stamina < maxStamina && this.tickSinceLastAction > regenStandbyTime) {
            if (event.getEntityLiving().getHealth() <= event.getEntityLiving().getMaxHealth()*1 && event.getEntityLiving().getHealth() > event.getEntityLiving().getMaxHealth()*0.75) {
                float staminaFactor = 1.0F + (float) Math.pow((double) (stamina / (maxStamina - stamina * 0.5F)), 2.0);
                this.setStamina((float)(stamina + maxStamina * 0.01F * staminaFactor));
            }
            if (event.getEntityLiving().getHealth() <= event.getEntityLiving().getMaxHealth()*0.75 && event.getEntityLiving().getHealth() > event.getEntityLiving().getMaxHealth()*0.5) {
                float staminaFactor = 0.7F + (float) Math.pow((double) (stamina / (maxStamina - stamina * 0.5F)), 2.0);
                this.setStamina((float) ((stamina + maxStamina * 0.01F * staminaFactor)));
            }
            if (event.getEntityLiving().getHealth() <= event.getEntityLiving().getMaxHealth()*0.5) {
                float staminaFactor = 0.45F + (float) Math.pow((double) (stamina / (maxStamina - stamina * 0.5F)), 2.0);
                this.setStamina((float) ((stamina + maxStamina * 0.01F * staminaFactor)));
            }
        }
        if (maxStamina < stamina) {
            this.setStamina((float) maxStamina);
        }
        this.xo = ((Player)this.original).getX();
        this.yo = ((Player)this.original).getY();
        this.zo = ((Player)this.original).getZ();
    }


    @OnlyIn(Dist.CLIENT)
    @Inject(method = "initAnimator", at = @At("HEAD"), remap = false)
    private void addExhaustionAnimations(ClientAnimator clientAnimator, CallbackInfo ci) {
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE, ExhaustionAnimations.EXHAUSTED_IDLE);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK, ExhaustionAnimations.EXHAUSTED_WALK);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_CROSSBOW, ExhaustionAnimations.EXHAUSTED_IDLE_CROSSBOW);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_CROSSBOW, ExhaustionAnimations.EXHAUSTED_WALK_CROSSBOW);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_GREATSWORD, ExhaustionAnimations.EXHAUSTED_IDLE_GREATSWORD);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_GREATSWORD, ExhaustionAnimations.EXHAUSTED_WALK_GREATSWORD);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_TACHI, ExhaustionAnimations.EXHAUSTED_IDLE_TACHI);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_TACHI, ExhaustionAnimations.EXHAUSTED_WALK_TACHI);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_SPEAR, ExhaustionAnimations.EXHAUSTED_IDLE_SPEAR);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_SPEAR, ExhaustionAnimations.EXHAUSTED_WALK_SPEAR);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_LIECHTENAUER, ExhaustionAnimations.EXHAUSTED_IDLE_LIECHTENAUER);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_LIECHTENAUER, ExhaustionAnimations.EXHAUSTED_WALK_LIECHTENAUER);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_SHEATH, ExhaustionAnimations.EXHAUSTED_IDLE_SHEATH);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_SHEATH, ExhaustionAnimations.EXHAUSTED_WALK_SHEATH);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_UNSHEATH, ExhaustionAnimations.EXHAUSTED_IDLE_UNSHEATH);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_UNSHEATH, ExhaustionAnimations.EXHAUSTED_WALK_UNSHEATH);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_KATANA, ExhaustionAnimations.EXHAUSTED_WALK_KATANA);
    }
}



