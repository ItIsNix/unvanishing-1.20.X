package net.nixmods.unvanishing;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unvanishing implements ModInitializer {
	public static final String MOD_ID = "unvanishing";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModEnchantments.registerEnchantments();
		EventListener.register();
		ModLootTableModifiers.ModifyLootTables();

		LOGGER.info("Hello Fabric world!");
	}
}