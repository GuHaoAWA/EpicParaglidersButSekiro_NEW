package com.guhao.sekiro.mixins.skills;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.DodgeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

@Mixin(value = DodgeSkill.class , remap = false)
public abstract class MixinDodge{

@Shadow
public static DodgeSkill.Builder createBuilder(ResourceLocation registryName){
    return (new DodgeSkill.Builder(registryName)).setCategory(SkillCategories.DODGE).setActivateType(Skill.ActivateType.ONE_SHOT).setResource(Skill.Resource.STAMINA).setRequiredXp(5);
}
    @Inject(method = "createBuilder", at = @At("RETURN") , remap = false)
    private static void setResourceToNone(ResourceLocation registryName, CallbackInfoReturnable<DodgeSkill.Builder> cir) {
        DodgeSkill.Builder builder = cir.getReturnValue();
        builder.setResource(Skill.Resource.NONE);
    }
}