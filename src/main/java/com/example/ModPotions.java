package com.example;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModPotions {
    // A general method for registering custom potions
    private static RegistryEntry<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(SimonMod.MOD_ID, name), potion);
    }

    // Create the Suspicious Potion
    public static final RegistryEntry<Potion> SUSPICIOUS_POTION = registerPotion("suspicious_potion",
            new Potion("suspicious_potion", new StatusEffectInstance(ModEffects.SUSPICIOUS, 1200, 0))
    );

    // This is run on startup, should register all potion recipes
    public static void registerPotions() {
        SimonMod.LOGGER.info("Registering mod potions for " + SimonMod.MOD_ID);

        // Register recipe for suspicious potion
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, ModItems.SUSPICIOUS_SUBSTANCE, ModPotions.SUSPICIOUS_POTION);
        });
    }
}