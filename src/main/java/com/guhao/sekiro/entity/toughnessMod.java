package com.guhao.sekiro.entity;

import com.guhao.sekiro.entity.mobeffect.initEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class toughnessMod {
    private static boolean triggered = false;
    public static final String TOUGHNESS_KEY = "toughness";
    private static long lastToughnessChangeTime;
    static float MAX_TOUGHNESS = 64F;


    @SubscribeEvent
    public void onEntitySpawn(EntityEvent.EntityConstructing event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.getPersistentData().putFloat(TOUGHNESS_KEY, 64F);
        }
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!entity.hasEffect(initEffect.TOUGHNESS_EFFECT.get()) && entity instanceof Monster) {
            float toughness = entity.getPersistentData().getFloat(TOUGHNESS_KEY);
            if (event.getAmount() > 0) {
                float newtoughness = (float) (toughness - (event.getAmount() + (Math.log(6 * (Math.pow(event.getAmount(), 3.2))) / 1.85) / 1.05));
                entity.getPersistentData().putFloat(TOUGHNESS_KEY, newtoughness);
            }
            //float a = entity.getPersistentData().getFloat(TOUGHNESS_KEY);
            //System.out.println("Toughness Value: " + a);
            lastToughnessChangeTime = System.currentTimeMillis();
        }
        if (entity.hasEffect(initEffect.TOUGHNESS_EFFECT.get())) {
            float hurt = (float) (event.getAmount() * 2.15);
            event.setAmount(hurt);
            entity.removeEffect(initEffect.TOUGHNESS_EFFECT.get());
        }
    }

    @SubscribeEvent
    public static void onEntityConstructing(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.getPersistentData().getFloat(TOUGHNESS_KEY) <= 0) {
            if (!triggered) {
                entity.addEffect(new MobEffectInstance(initEffect.TOUGHNESS_EFFECT.get(), 90, 0));
                playSound(entity);
                triggered = true;
            }
            entity.getPersistentData().putFloat(TOUGHNESS_KEY, 64F);
        }

        if (event.getEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.getPersistentData().getFloat(TOUGHNESS_KEY) >= MAX_TOUGHNESS) {
                livingEntity.getPersistentData().putFloat(TOUGHNESS_KEY, MAX_TOUGHNESS);
            }
            if (!livingEntity.getPersistentData().contains(TOUGHNESS_KEY)) {
                livingEntity.getPersistentData().putFloat(TOUGHNESS_KEY, 64F);
            }
            if (livingEntity.getPersistentData().getFloat(TOUGHNESS_KEY) <= 0) {
                if (livingEntity instanceof Monster | livingEntity instanceof Slime) {
                    if (!livingEntity.hasEffect(initEffect.TOUGHNESS_EFFECT.get())) {
                        livingEntity.addEffect(new MobEffectInstance(initEffect.TOUGHNESS_EFFECT.get(), 90, 0));
                    }
                }
            }
        }
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastToughnessChangeTime;
        if (timeElapsed >= 4000) {
            if (entity.getPersistentData().getFloat(TOUGHNESS_KEY) < MAX_TOUGHNESS) {
                float toughness = entity.getPersistentData().getFloat(TOUGHNESS_KEY);
                toughness += 4.5f;
                entity.getPersistentData().putFloat(TOUGHNESS_KEY, toughness);
                lastToughnessChangeTime = currentTime;
            }
        }
    }

    @SubscribeEvent
    public static void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
        MobEffect removedEffect = event.getPotion();
        if (event.getPotion() == initEffect.TOUGHNESS_EFFECT.get()) {
            triggered = false;
        }
        if (removedEffect == initEffect.TOUGHNESS_EFFECT.get()) {
            LivingEntity entity = event.getEntityLiving();
            entity.getPersistentData().putFloat(TOUGHNESS_KEY, 64F);
        }
    }

    private static void playSound(LivingEntity entity) {
        ResourceLocation soundName = new ResourceLocation("epicparaglidersbutsekiro:toughness");
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundName);
        if (soundEvent != null) {
            Level world = entity.level;
            world.playSound(null, entity, soundEvent, SoundSource.HOSTILE, 1.0f, 1.0f);
        }
    }
}