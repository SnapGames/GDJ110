/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj106
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj107;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.Layer;
import com.snapgames.gdj.core.gfx.RenderHelper.TextPosition;
import com.snapgames.gdj.core.io.InputHandler;
import com.snapgames.gdj.core.state.AbstractGameState;
import com.snapgames.gdj.core.ui.MenuObject;
import com.snapgames.gdj.core.ui.MenuObject.MenuItem;
import com.snapgames.gdj.core.ui.TextObject;
import com.snapgames.gdj.core.ui.i18n.Messages;
import com.snapgames.gdj.core.utils.GameOptions;
import com.snapgames.gdj.core.utils.OptionDoesNotExistsException;

/**
 * The option state is the screen to switch ON/OFF some options like sound,
 * music, etc...
 * 
 * @author Frédéric Delorme
 *
 */
public class OptionsState extends AbstractGameState {

	private static final Logger logger = LoggerFactory.getLogger(OptionsState.class);

	/**
	 * The options menu
	 */
	MenuObject menu;
	/**
	 * Item for Sound
	 */
	MenuItem soundItemFlag;
	/**
	 * item for Music
	 */
	MenuItem musicItemFlag;
	/**
	 * item for Locale
	 */
	MenuItem localeItemSelector;

	/**
	 * Screen title
	 */
	TextObject title;
	/**
	 * some fonts
	 */
	Font titleFont, optionFont;

	/**
	 * Options values.
	 */
	Boolean soundFlag = true;
	Boolean musicFlag = true;

	/**
	 * Selected Locale for the game.
	 */
	String defaultLocaleSelected = "EN_en";

	String localeSelected = Locale.getDefault().toString();

	private int localeIndex;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.GameState#getName()
	 */
	@Override
	public String getName() {
		return "options";
	}

	/**
	 * @param gsm
	 */
	public void initialize(Game game) {
		super.initialize(game);

		try {
			GameOptions.loadOptions();
			soundFlag = GameOptions.getBoolean(GameOptions.OPTION_SOUND_FLAG);
			musicFlag = GameOptions.getBoolean(GameOptions.OPTION_MUSIC_FLAG);
		} catch (OptionDoesNotExistsException e) {
			updateOptionsFile();
			logger.info("Create {} file", GameOptions.OPTIONS_PROPERTIES_FILE);
		}
		// activate needed layers
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Layer(true, false);
		}
		
		localeSelected = Messages.LOCALES.get(0).toString();

		titleFont = game.getGraphics().getFont().deriveFont(3.0f * Game.SCREEN_FONT_RATIO);
		optionFont = game.getGraphics().getFont().deriveFont(1.2f * Game.SCREEN_FONT_RATIO);

		title = (TextObject) new TextObject("title").setText(Messages.getString("Options.title", "Title"))
				.setTextAlign(TextPosition.CENTER).setFont(titleFont).setPosition(Game.WIDTH / 2, 10).setLayer(1)
				.setPriority(1).setColor(Color.WHITE);
		addObject(title);

		menu = (MenuObject) new MenuObject("options").setActiveItem(0).setShadowColor(Color.BLACK)
				.setAlignText(TextPosition.CENTER).setFont(optionFont).setPosition(Game.WIDTH / 2, Game.HEIGHT / 2)
				.setColor(Color.WHITE);
		soundItemFlag = menu.addItem("sound", "Options.items.sound", "Sound : %s", soundFlag);
		musicItemFlag = menu.addItem("music", "Options.items.music", "music : %s", musicFlag);
		localeItemSelector = menu.addItem("language", "Options.items.languages", "language : %s", Locale.getDefault().getDisplayLanguage());
		menu.addItem("back", "Options.items.back", "Back");
		menu.layer = 1;
		menu.priority = 1;
		addObject(menu);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.state.GameState#update(com.snapgames.gdj.core.Game,
	 * long)
	 */
	@Override
	public void update(Game game, long dt) {
		if (soundItemFlag != null && musicItemFlag != null && localeSelected!=null) {
			soundItemFlag.setLabelArgs((soundFlag ? "ON" : "OFF"));
			musicItemFlag.setLabelArgs((musicFlag ? "ON" : "OFF"));
			Locale locale = Locale.getDefault()!=null?Locale.getDefault():Locale.ENGLISH;
			localeItemSelector.setLabelArgs(locale.getDisplayLanguage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.state.AbstractGameState#keyReleased(com.snapgames.gdj.
	 * core.Game, java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(Game game, KeyEvent e) {
		super.keyReleased(game, e);
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			menu.previous();
			break;
		case KeyEvent.VK_DOWN:
			menu.next();
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			String value = menu.getActiveItem().getValue();
			switch (value) {
			case "back":
				game.getGSM().activate("title");
				break;
			case "sound":
				soundFlag = !soundFlag;
				GameOptions.set(GameOptions.OPTION_SOUND_FLAG, soundFlag);
				break;
			case "music":
				musicFlag = !musicFlag;
				GameOptions.set(GameOptions.OPTION_MUSIC_FLAG, musicFlag);
				break;
			case "language":
				if(localeIndex+1 < Messages.LOCALES.size()) {
					Locale.setDefault(Messages.LOCALES.get(localeIndex));
					//Messages.setLocale(Messages.LOCALES.get(localeIndex));
					localeIndex++;
				}else {
					localeIndex=0;
				}
			}
		}
	}

	@Override
	public void render(Game game, Graphics2D g) {
		g.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);
		super.render(game, g);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.state.GameState#input(com.snapgames.gdj.core.Game,
	 * com.snapgames.gdj.core.io.InputHandler)
	 */
	@Override
	public void input(Game game, InputHandler input) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.state.AbstractGameState#dispose(com.snapgames.gdj.core
	 * .Game)
	 */
	@Override
	public void dispose(Game game) {
		super.dispose(game);
		updateOptionsFile();
	}

	/**
	 * 
	 */
	protected void updateOptionsFile() {
		GameOptions.set(GameOptions.OPTION_SOUND_FLAG, soundFlag);
		GameOptions.set(GameOptions.OPTION_MUSIC_FLAG, musicFlag);
		GameOptions.saveOptions();
	}

}
