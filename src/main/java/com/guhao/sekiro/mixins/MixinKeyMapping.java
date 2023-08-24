package com.guhao.sekiro.mixins;

import com.guhao.sekiro.entity.mobeffect.InitEffect;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = KeyMapping.class)
public class MixinKeyMapping {

    @Shadow
    boolean isDown;

    @Inject(at = @At("HEAD"), method = "isDown()Z", cancellable = true)
    public void isDown(CallbackInfoReturnable<Boolean> callback) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            if (player.hasEffect(InitEffect.TOUGHNESS_EFFECT.get())) {
                callback.setReturnValue(false);
                callback.cancel();
                if (isDown) isDown = false;
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "consumeClick()Z", cancellable = true)
    public void consumeClick(CallbackInfoReturnable<Boolean> callback) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            if (player.hasEffect(InitEffect.TOUGHNESS_EFFECT.get())) {
                callback.setReturnValue(false);
                callback.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "matches(II)Z", cancellable = true)
    public void matches(int key, int scancode, CallbackInfoReturnable<Boolean> callback) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            if (player.hasEffect(InitEffect.TOUGHNESS_EFFECT.get())) {
                callback.setReturnValue(false);
                callback.cancel();
                return;
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "matchesMouse(I)Z", cancellable = true)
    public void matchesMouse(int button, CallbackInfoReturnable<Boolean> callback) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            if (player.hasEffect(InitEffect.TOUGHNESS_EFFECT.get())) {
                callback.setReturnValue(false);
                callback.cancel();
                return;
            }

        }
    }

}
