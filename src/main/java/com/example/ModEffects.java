package com.example;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    // Register the Suspicious status effect
    public static final RegistryEntry<StatusEffect> SUSPICIOUS = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(SimonMod.MOD_ID, "suspicious"),
            new SuspiciousEffect()
    );

    public static void registerEffects() {
        SimonMod.LOGGER.info("Registering status effects for " + SimonMod.MOD_ID);
    }
}
