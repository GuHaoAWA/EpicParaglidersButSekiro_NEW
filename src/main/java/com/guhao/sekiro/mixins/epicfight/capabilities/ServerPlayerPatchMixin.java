package com.guhao.sekiro.mixins.epicfight.capabilities;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(ServerPlayerPatch.class)
public abstract class ServerPlayerPatchMixin extends PlayerPatch<ServerPlayer> {

    /**
     * Stamina is always consumed now since the Paragliders section of the code
     * will take care of not processing any stamina consumption if it's depleted.
     *
     * @param amount
     * @param cir
     */
    @Inject(method = "consumeStamina", at = @At("HEAD"), remap = false, cancellable = true)
    private void consumeStamina(float amount, CallbackInfoReturnable<Boolean> cir) {
        this.setStamina(amount);
        cir.setReturnValue(true);
    }
}
