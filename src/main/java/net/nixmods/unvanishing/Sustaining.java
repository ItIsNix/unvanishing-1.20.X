package net.nixmods.unvanishing;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class Sustaining extends Enchantment {
    public Sustaining() {
        super(Rarity.VERY_RARE, EnchantmentTarget.VANISHABLE, new EquipmentSlot[]{
                EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND,
                EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET
        });
    }

    @Override
    public int getMinPower(int level) { return 20; }

    @Override
    public int getMaxPower(int level) { return 40; }
}

