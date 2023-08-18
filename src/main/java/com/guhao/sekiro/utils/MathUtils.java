package com.guhao.sekiro.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import yesman.epicfight.world.item.GreatswordItem;

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

    //TODO: Create one more method here that takes in two numbers and combines them.
    //      Since this will be a very common piece of math used in this project.


    /**
     * TODO: Compare with BetterParagliders stamina algorithm.
     *
     * Some math done here to determine how much stamina should be consumed from each weapon type.
     * Takes in the attack delay of the weapon as well as its attack strength to create a balanced
     * stamina cost. Also, factors in the extra damage that Epic Fight applies to certain weapons.
     *
     * @param player The given player attacking
     * @return The amount of stamina that should be drained from the attacking weapon
     */
    public static int getAttackStaminaCost(Player player) {
        int itemAttackStrDelay = (int) player.getCurrentItemAttackStrengthDelay();
        Item weaponItem = player.getMainHandItem().getItem();
        if (weaponItem instanceof SwordItem swordItem) {
            //TODO: May have to factor this in. Test with a datapack in the future.
//            if (this.player.getMainHandItem().getTag().contains("damage_bonus")) {
//                EpicParaglidersMod.LOGGER.info(swordItem.getRegistryName() + " contains a damage bonus!");
//            }

            if (swordItem instanceof GreatswordItem) {
                return (int) (itemAttackStrDelay + (swordItem.getDamage() + 12) / 2);
            }
            else {
                return (int) (itemAttackStrDelay + (swordItem.getDamage() + 1));
            }
        }
        else if (weaponItem instanceof AxeItem axeItem) {
            return (int) ((itemAttackStrDelay / 2) + (axeItem.getAttackDamage() + 1));
        }
        else {
            return itemAttackStrDelay;
        }
    }
}
