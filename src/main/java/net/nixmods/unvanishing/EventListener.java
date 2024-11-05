package net.nixmods.unvanishing;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventListener {

    private static final Identifier SUSTAINING_ENCHANTMENT_ID = new Identifier("unvanishing:sustaining");
    private static final Enchantment SUSTAINING_ENCHANTMENT = Registries.ENCHANTMENT.get(SUSTAINING_ENCHANTMENT_ID);

    private final List<NbtCompound> sustainingItems = new ArrayList<>();

    public static void register() {
        new EventListener();
    }

    public EventListener() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(this::onPlayerHurt);

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof PlayerEntity player) {
                onPlayerRespawn(player);
            }
        });
    }

    private boolean onPlayerHurt(LivingEntity entity, DamageSource source, float amount) {
        if (entity instanceof PlayerEntity player) {
            if (player.getHealth() - amount <= 0) {
                sustainingItems.clear();

                List<ItemStack> itemsToRemove = new ArrayList<>();
                player.getInventory().main.forEach(stack -> {
                    if (hasSustainingEnchantment(stack)) {
                        itemsToRemove.add(stack.copy());
                        stack.setCount(0);
                    }
                });
                player.getInventory().offHand.forEach(stack -> {
                    if (hasSustainingEnchantment(stack)) {
                        itemsToRemove.add(stack.copy());
                        stack.setCount(0);
                    }
                });
                player.getInventory().armor.forEach(stack -> {
                    if (hasSustainingEnchantment(stack)) {
                        itemsToRemove.add(stack.copy());
                        stack.setCount(0);
                    }
                });

                itemsToRemove.forEach(item -> {
                    NbtCompound itemNbt = item.writeNbt(new NbtCompound());
                    sustainingItems.add(itemNbt);
                });
                return true;
            }
        }
        return true;
    }

    private boolean hasSustainingEnchantment(ItemStack stack) {
        if (!stack.hasEnchantments()) return false;

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        return enchantments.containsKey(SUSTAINING_ENCHANTMENT);
    }

    private void onPlayerRespawn(PlayerEntity player) {
        for (NbtCompound itemNbt : sustainingItems) {
            ItemStack restoredStack = ItemStack.fromNbt(itemNbt);

            if (restoredStack.isDamageable()) {
                int maxDurability = restoredStack.getMaxDamage();
                int currentDurability = restoredStack.getDamage();
                int damageToApply = maxDurability / 2;
                int newDamage = Math.min(currentDurability + damageToApply, maxDurability);
                restoredStack.setDamage(newDamage);

                if (newDamage >= maxDurability - 1) {
                    restoredStack.setCount(0);
                }
            }
            if (restoredStack.getCount() > 0) {
                player.getInventory().insertStack(restoredStack);
            }
        }
        sustainingItems.clear();
    }
}