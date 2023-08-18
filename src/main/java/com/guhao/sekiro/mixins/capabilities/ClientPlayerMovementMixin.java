package com.guhao.sekiro.mixins.capabilities;

import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;

@Mixin(ClientPlayerMovement.class)
public abstract class ClientPlayerMovementMixin extends PlayerMovement implements PlayerMovementInterface {
    private int totalActionStaminaCost;

    public ClientPlayerMovementMixin(Player player) {
        super(player);
    }

    /**
     * Updates the client side player action stamina whenever it receives data from the server side.
     */
    @Inject(method = "update", at = @At(value = "HEAD"), remap=false)
    public void update(CallbackInfo ci) {
        this.setTotalActionStaminaCost(this.totalActionStaminaCost);
    }

    @Override
    public int getTotalActionStaminaCost() {
        return this.totalActionStaminaCost;
    }

    @Override
    public void setTotalActionStaminaCostClientSide(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
    }
}
