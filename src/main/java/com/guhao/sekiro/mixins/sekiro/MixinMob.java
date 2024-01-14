
package com.guhao.sekiro.mixins.sekiro;

import com.guhao.sekiro.entity.mobeffect.initEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity {

    public MixinMob(Level level) {
        super(null, level);
    }

    @Inject(at=@At("HEAD"), method = "isNoAi()Z", cancellable = true)
    public void isNoAi(CallbackInfoReturnable<Boolean> callback) {
        if (hasEffect(initEffect.TOUGHNESS_EFFECT.get())) {
            callback.setReturnValue(true);
            callback.cancel();
        }
    }

}
