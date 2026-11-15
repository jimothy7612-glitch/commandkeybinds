package com.example.commandkeybinds;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class CommandEditorScreen extends Screen {
	private static final int TOP = 40;
	private static final int ROW = 20;
	private final EditBox[] fields = new EditBox[Config.SLOTS];

	public CommandEditorScreen() {
		super(Component.translatable("screen.commandkeybinds.title"));
	}

	@Override
	protected void init() {
		for (int i = 0; i < Config.SLOTS; i++) {
			EditBox box = new EditBox(this.font, this.width / 2 - 20, TOP + i * ROW, 220, 18,
					Component.literal("Slot " + (i + 1)));
			box.setMaxLength(256);
			box.setValue(Config.get().commands[i]);
			box.setHint(Component.translatable("screen.commandkeybinds.placeholder"));
			fields[i] = box;
			this.addRenderableWidget(box);
		}
		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
				.bounds(this.width / 2 - 100, TOP + Config.SLOTS * ROW + 8, 200, 20).build());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		graphics.centeredText(this.font, this.title, this.width / 2, 10, 0xFFFFFFFF);
		graphics.centeredText(this.font, Component.translatable("screen.commandkeybinds.hint"), this.width / 2, 24, 0xFFAAAAAA);
		for (int i = 0; i < Config.SLOTS; i++) {
			Component label = Component.literal((i + 1) + ": ").append(CommandKeybindsClient.SLOT_KEYS[i].getTranslatedKeyMessage());
			graphics.text(this.font, label, this.width / 2 - 200, TOP + i * ROW + 5, 0xFFFFFFFF, true);
		}
	}

	@Override
	public void onClose() {
		for (int i = 0; i < Config.SLOTS; i++) {
			Config.get().commands[i] = fields[i].getValue();
		}
		Config.save();
		this.minecraft.gui.setScreen(null);
	}
}
