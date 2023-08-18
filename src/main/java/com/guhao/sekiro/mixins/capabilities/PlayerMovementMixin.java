package com.guhao.sekiro.mixins.capabilities;

import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.ModCfg;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.PlayerState;

@Mixin(value = PlayerMovement.class , remap = false)
public abstract class PlayerMovementMixin implements PlayerMovementInterface {
    @Shadow private PlayerState state;
    @Shadow private int recoveryDelay;
    @Shadow private boolean depleted;
    @Shadow private int stamina;
    @Shadow public abstract int getMaxStamina();
    @Shadow @Final public Player player;
    public int totalActionStaminaCost;


    /**
     * Functions very similarly to the updateStamina() method of PlayerMovement in the Paragliders mod.
     * The main difference here is that the amount of stamina to drain also factors in actionStaminaCost,
     * which will be any action performed that drains stamina (attacking, rolling, dodging, etc.).
     *
     * @param ci
     */
    @Inject(method = "updateStamina", at = @At("HEAD"), cancellable = true, remap = false)
    public void updateStamina(CallbackInfo ci) {

        //TODO: Small bug where stamina isn't depleted if attacking and going up a block at the same time.
        //      State and action consumption are both combined, so can't be an issue with one being chose
        //      over the other. Look into this in a later release.
        if (this.totalActionStaminaCost != 0 || this.state.isConsume()) {
            this.recoveryDelay = 10;
            int stateChange = (state.isConsume()) ? state.change() - this.totalActionStaminaCost : -this.totalActionStaminaCost;

            if (!this.depleted && ((state.isParagliding()
                    ? ModCfg.paraglidingConsumesStamina()
                    : ModCfg.runningConsumesStamina()) || this.totalActionStaminaCost != 0)) {
                this.stamina = Math.max(0, this.stamina + stateChange);
            }
        }
        else if (this.recoveryDelay > 0) {
            --this.recoveryDelay;
        }
        else if (this.state.change() > 0) {
            this.stamina = Math.min(this.getMaxStamina(), this.stamina + this.state.change());
        }

        if (this.totalActionStaminaCost > 0) {
            this.totalActionStaminaCost--;
        }
        else if(this.totalActionStaminaCost < 0) {
            this.totalActionStaminaCost++;
        }

        if (this.player instanceof ServerPlayer) {
            this.setTotalActionStaminaCostServerSide(this.totalActionStaminaCost);
        }
        else if (this.player instanceof LocalPlayer) {
            this.setTotalActionStaminaCostClientSide(this.totalActionStaminaCost);
        }

        addEffects();
        ci.cancel();
    }

    @Override
    public int getTotalActionStaminaCost() {
        return this.totalActionStaminaCost;
    }

    @Override
    public void setTotalActionStaminaCost(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
    }

    /**
     * Adds all the effects to be applied whenever the player's stamina is depleted.
     */
    protected void addEffects() {
        if(!this.player.isCreative() && this.depleted) {
            this.player.addEffect(new MobEffectInstance(MobEffect.byId(18))); // Adds weakness
        }
    }
}