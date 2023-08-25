package com.guhao.sekiro.mixins.capabilities;

import com.guhao.sekiro.Config;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
@Mixin(value = PlayerPatch.class , remap = false , priority = 10000)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {
    Double MAX_STAMINA = Config.MAX_STAMINA.get();
    @Inject(method = "initAttributes", at = @At("TAIL"), remap = false)
    public void modifyInitAttributes(CallbackInfo ci) {
        ((Player)this.original).getAttribute((Attribute)EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(MAX_STAMINA);
        ((Player)this.original).getAttribute((Attribute)EpicFightAttributes.OFFHAND_IMPACT.get()).setBaseValue(0.5);
    }
}



