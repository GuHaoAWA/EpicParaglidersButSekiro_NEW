package com.guhao.sekiro.utils;

import com.guhao.sekiro.capabilities.WeaponType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import yesman.epicfight.api.data.reloader.ItemCapabilityReloadListener;

public class MathUtils {

    public static double calculateTriangularNumber(int baseNumber) {
        int absBaseNumber = Math.abs(baseNumber);
        double triangularNumber = (absBaseNumber * (absBaseNumber + 1)) / 2;
        return (baseNumber > 0) ? triangularNumber : -triangularNumber;
    }

    public static double calculateTriangularRoot(double triangularNumber) {
        double triangularRoot = ((Math.sqrt(Math.abs(triangularNumber) * 8 - 1) - 1) / 2);
        return (triangularNumber > 0) ? triangularRoot : -triangularRoot;
    }

    public static double calculateModifiedTriangularRoot(double triangularNumber, double percentageOfTriangularNumber) {
        double triangularRoot = ((Math.sqrt(Math.abs(triangularNumber) * percentageOfTriangularNumber * 8 - 1) - 1) / 2);
        return (triangularNumber > 0) ? triangularRoot : -triangularRoot;
    }

    /**
     * Some math done here to determine how much stamina should be consumed from each weapon type.
     * Takes in a weapon's attack strength and tier, and the world's config setting to create a balanced stamina cost.
     *
     * @param player The given player attacking
     * @return The amount of stamina that should be drained from the attacking weapon
     */
    public static int getAttackStaminaCost(Player player) {
        //TODO: Double check duel wielding with this.
        //      Could easily add offhand support too by checking which
        //      hand is swinging the weapon.
        Item weaponItem = player.getMainHandItem().getItem();
        CompoundTag weaponTag = ItemCapabilityReloadListener.getWeaponDataStream()
                .filter(compoundTag -> compoundTag.getInt("id") == Item.getId(player.getMainHandItem().getItem()))
                .findFirst()
                .orElse(null);


        double weaponAttackDamage = weaponItem.getAttributeModifiers(EquipmentSlot.MAINHAND, weaponItem.getDefaultInstance())
                .get(Attributes.ATTACK_DAMAGE).stream()
                .filter(attributeModifier -> attributeModifier.getName().contains("Weapon"))
                .findFirst()
                .map(AttributeModifier::getAmount)
                .orElse(0.0);


        int weaponStaminaCostOverride;
        double totalStaminaCost = 0;
        WeaponType weaponType;
        if (weaponTag != null) {
            weaponStaminaCostOverride = weaponTag
                    .getCompound("attributes")
                    .getCompound("common")
                    .getInt("stamina_cost");
            try {
                weaponType = WeaponType.valueOf(weaponTag.get("type").getAsString().toUpperCase());
            } catch (IllegalArgumentException e) {
                // 当捕获到异常时，执行跳过的逻辑
                weaponType = WeaponType.HOE;
            }
            if (weaponStaminaCostOverride > 0) {
                totalStaminaCost = weaponStaminaCostOverride;
            } else {
                totalStaminaCost = (weaponType.getStaminaFixedCost() > 0) ? weaponType.getStaminaFixedCost() : weaponType.getStaminaMultiplier() * weaponAttackDamage;
            }


            totalStaminaCost *= weaponType.getStaminaReduction(player);
        }
        return (int) Math.round(totalStaminaCost);
    }
}
