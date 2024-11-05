package net.nixmods.unvanishing;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final Enchantment sustaining = new Sustaining();

    public static void registerEnchantments() {
        Registry.register(Registries.ENCHANTMENT, new Identifier("unvanishing", "sustaining"), sustaining);
    }
}
