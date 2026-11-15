package com.example.commandkeybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandKeybindsClient implements ClientModInitializer {
	public static final String MOD_ID = "commandkeybinds";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final KeyMapping[] SLOT_KEYS = new KeyMapping[Config.SLOTS];
	private static KeyMapping openEditorKey;

	@Override
	public void onInitializeClient() {
		Config.load();

		KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));

		openEditorKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.commandkeybinds.open_editor", InputConstants.Type.KEYBOARD, InputConstants.KEY_K, category));

		for (int i = 0; i < Config.SLOTS; i++) {
			SLOT_KEYS[i] = KeyMappingHelper.registerKeyMapping(new KeyMapping(
					"key.commandkeybinds.slot_" + (i + 1), InputConstants.Type.KEYBOARD,
					InputConstants.UNKNOWN.getValue(), category));
		}

		ClientTickEvents.END_CLIENT_TICK.register(CommandKeybindsClient::onTick);
	}

	private static void onTick(Minecraft client) {
		if (client.player == null) return;

		while (openEditorKey.consumeClick()) {
			client.gui.setScreen(new CommandEditorScreen());
		}

		for (int i = 0; i < Config.SLOTS; i++) {
			while (SLOT_KEYS[i].consumeClick()) {
				run(client, Config.get().commands[i]);
			}
		}
	}

	private static void run(Minecraft client, String text) {
		if (text == null) return;
		text = text.trim();
		if (text.isEmpty()) return;
		if (text.startsWith("/")) {
			client.player.connection.sendCommand(text.substring(1));
		} else {
			client.player.connection.sendChat(text);
		}
	}
}
