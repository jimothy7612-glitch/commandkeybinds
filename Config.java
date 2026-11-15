package com.example.commandkeybinds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
	public static final int SLOTS = 12;
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("commandkeybinds.json");
	private static Config instance = new Config();

	public String[] commands = new String[SLOTS];

	private Config() {
		for (int i = 0; i < SLOTS; i++) commands[i] = "";
	}

	public static Config get() {
		return instance;
	}

	public static void load() {
		try {
			if (Files.exists(FILE)) {
				try (Reader r = Files.newBufferedReader(FILE)) {
					Config loaded = GSON.fromJson(r, Config.class);
					if (loaded != null && loaded.commands != null) {
						Config c = new Config();
						for (int i = 0; i < SLOTS && i < loaded.commands.length; i++) {
							if (loaded.commands[i] != null) c.commands[i] = loaded.commands[i];
						}
						instance = c;
					}
				}
			} else {
				save();
			}
		} catch (Exception e) {
			CommandKeybindsClient.LOGGER.error("Could not read config, using defaults", e);
		}
	}

	public static void save() {
		try (Writer w = Files.newBufferedWriter(FILE)) {
			GSON.toJson(instance, w);
		} catch (Exception e) {
			CommandKeybindsClient.LOGGER.error("Could not save config", e);
		}
	}
}
