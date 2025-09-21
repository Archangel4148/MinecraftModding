package com.example;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModDamageTypes {

    // Get the Suspicious registry key
    public static final RegistryKey<DamageType> SUSPICIOUS_DAMAGE_TYPE =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(SimonMod.MOD_ID, "suspicious"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        var registry = world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE);
        var type = registry.get(key);
        var entry = registry.getEntry(type);
        return new DamageSource(entry);
    }
}
