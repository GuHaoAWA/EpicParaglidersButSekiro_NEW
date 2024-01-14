package com.guhao.sekiro;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.guhao.sekiro.EpicParaglidersButSekiroMod.MOD_ID;

public class EpicParaglidersAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES,MOD_ID);

    /**
     * Weapon Attributes
     */
    public static final RegistryObject<Attribute> DEFAULT_STAMINA_REDUCTION = ATTRIBUTES.register("default_stamina_reduction", () ->
            new RangedAttribute("default_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> DAGGER_STAMINA_REDUCTION = ATTRIBUTES.register("dagger_stamina_reduction", () ->
            new RangedAttribute("dagger_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> SWORD_STAMINA_REDUCTION = ATTRIBUTES.register("sword_stamina_reduction", () ->
            new RangedAttribute("sword_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> LONGSWORD_STAMINA_REDUCTION = ATTRIBUTES.register("longsword_stamina_reduction", () ->
            new RangedAttribute("longsword_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> GREATSWORD_STAMINA_REDUCTION = ATTRIBUTES.register("greatsword_stamina_reduction", () ->
            new RangedAttribute("greatsword_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> UCHIGATANA_STAMINA_REDUCTION = ATTRIBUTES.register("uchigatana_stamina_reduction", () ->
            new RangedAttribute("uchigatana_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> TACHI_STAMINA_REDUCTION = ATTRIBUTES.register("tachi_stamina_reduction", () ->
            new RangedAttribute("tachi_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> SPEAR_STAMINA_REDUCTION = ATTRIBUTES.register("spear_stamina_reduction", () ->
            new RangedAttribute("spear_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> KNUCKLE_STAMINA_REDUCTION = ATTRIBUTES.register("knuckle_stamina_reduction", () ->
            new RangedAttribute("knuckle_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> AXE_STAMINA_REDUCTION = ATTRIBUTES.register("axe_stamina_reduction", () ->
            new RangedAttribute("axe_stamina_reduction", 1.0, 0.0, 10.0));

    /**
     * Skill Attributes
     */
    public static final RegistryObject<Attribute> BLOCK_STAMINA_REDUCTION = ATTRIBUTES.register("block_stamina_reduction", () ->
            new RangedAttribute("block_stamina_reduction", 1.0, 0.0, 10.0));
    public static final RegistryObject<Attribute> DODGE_STAMINA_REDUCTION = ATTRIBUTES.register("dodge_stamina_reduction", () ->
            new RangedAttribute("dodge_stamina_reduction", 1.0, 0.0, 10.0));

    /**
     * Attributes needed for datapack editing
     */
    public static final RegistryObject<Attribute> WEAPON_STAMINA_CONSUMPTION = ATTRIBUTES.register("weapon_stamina_consumption", () -> new RangedAttribute("weapon_stamina_consumption", 0.0, 0.0, 1000.0).setSyncable(true));
    public static final RegistryObject<Attribute> WEAPON_TYPE = ATTRIBUTES.register("weapon_type", () -> new RangedAttribute("weapon_type", 0.0, 0.0, 1000.0).setSyncable(true));

    public static void registerEventHandlers(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}