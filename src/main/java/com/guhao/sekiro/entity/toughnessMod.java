package com.guhao.sekiro.entity;

import com.guhao.sekiro.entity.mobeffect.InitEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class toughnessMod {
    private static boolean triggered = false;
    public static final String TOUGHNESS_KEY = "toughness";
    private static long lastToughnessChangeTime;
    static int MAX_TOUGHNESS = 60;

    @SubscribeEvent
    public void onEntitySpawn(EntityEvent.EntityConstructing event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.getPersistentData().putFloat(TOUGHNESS_KEY, 60);
        }
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!entity.hasEffect(InitEffect.TOUGHNESS_EFFECT.get()) && entity instanceof Monster) {
            float toughness = entity.getPersistentData().getFloat(TOUGHNESS_KEY);
            toughness -= event.getAmount() * 0.2 + 8.5;
            entity.getPersistentData().putFloat(TOUGHNESS_KEY, toughness);

        }
        if (entity.hasEffect(InitEffect.TOUGHNESS_EFFECT.get())) {
            float hurt = (float) (event.getAmount() * 2.5);
            event.setAmount(hurt);
            entity.removeEffect(InitEffect.TOUGHNESS_EFFECT.get());
        }
        lastToughnessChangeTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public static void onEntityConstructing(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if(entity.getPersistentData().getFloat(TOUGHNESS_KEY) <= 0){
            if(!triggered){
                entity.addEffect(new MobEffectInstance(InitEffect.TOUGHNESS_EFFECT.get(), 90, 0));
                triggered = true;
            }
            entity.getPersistentData().putFloat(TOUGHNESS_KEY, 60);
        }

        if (event.getEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.getPersistentData().getFloat(TOUGHNESS_KEY) >= MAX_TOUGHNESS) {
                livingEntity.getPersistentData().putFloat(TOUGHNESS_KEY, MAX_TOUGHNESS);
            }
            if (!livingEntity.getPersistentData().contains(TOUGHNESS_KEY)) {
                livingEntity.getPersistentData().putFloat(TOUGHNESS_KEY, 60);
            }
            if (livingEntity.getPersistentData().getFloat(TOUGHNESS_KEY) <= 0) {
                if (livingEntity instanceof Monster) {
                    if (!livingEntity.hasEffect(InitEffect.TOUGHNESS_EFFECT.get())) {
                        livingEntity.addEffect(new MobEffectInstance(InitEffect.TOUGHNESS_EFFECT.get(), 90, 0));
                    }
                }
            }
        }
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastToughnessChangeTime;
        if (timeElapsed >= 3250) {
            if (entity.getPersistentData().getFloat(TOUGHNESS_KEY) < MAX_TOUGHNESS) {
                float toughness = entity.getPersistentData().getFloat(TOUGHNESS_KEY);
                toughness += 0.2f;
                entity.getPersistentData().putFloat(TOUGHNESS_KEY, toughness);
                lastToughnessChangeTime = currentTime;
            }
        }
    }

    @SubscribeEvent
    public static void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
        MobEffect removedEffect = event.getPotion();
        if (event.getPotion() == InitEffect.TOUGHNESS_EFFECT.get()) {
            triggered = false;
        }
        if (removedEffect == InitEffect.TOUGHNESS_EFFECT.get()) {
            LivingEntity entity = event.getEntityLiving();
            entity.getPersistentData().putFloat(TOUGHNESS_KEY, 60);
        }
    }
}