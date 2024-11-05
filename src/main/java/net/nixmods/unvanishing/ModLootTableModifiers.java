package net.nixmods.unvanishing;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;


public class ModLootTableModifiers {
    private static final Identifier EndCityID =
            new Identifier("minecraft","chests/end_city_treasure");

    public static void ModifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, identifier, builder, lootTableSource) -> {
            if (EndCityID.equals(identifier)) {
                NbtCompound bookNbt = getNbtCompound();

                LootPool pool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(Items.ENCHANTED_BOOK)
                                .weight(5)
                                .apply(SetNbtLootFunction.builder(bookNbt))
                                .conditionally(RandomChanceLootCondition.builder(0.1f))
                        )
                        .build();
                builder.pool(pool);
            }
        });
    }

    private static @NotNull NbtCompound getNbtCompound() {
        NbtCompound enchantment = new NbtCompound();
        enchantment.putString("id", "unvanishing:sustaining");
        enchantment.putInt("lvl", 1);

        NbtList storedEnchantments = new NbtList();
        storedEnchantments.add(enchantment);

        NbtCompound bookNbt = new NbtCompound();
        bookNbt.put("StoredEnchantments", storedEnchantments);
        return bookNbt;
    }
}
