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
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.ResourceManager;
import com.snapgames.gdj.core.entity.Layer;
import com.snapgames.gdj.core.gfx.RenderHelper.TextPosition;
import com.snapgames.gdj.core.io.InputHandler;
import com.snapgames.gdj.core.state.AbstractGameState;
import com.snapgames.gdj.core.ui.ImageObject;
import com.snapgames.gdj.core.ui.MenuObject;
import com.snapgames.gdj.core.ui.TextObject;
import com.snapgames.gdj.core.ui.i18n.Messages;

/**
 * This is the Title Screen for the game.
 * 
 * @author Frédéric Delorme
 *
 */
public class TitleState extends AbstractGameState {

	private static final Logger logger = LoggerFactory.getLogger(TitleState.class);

	private ImageObject bgi;

	private MenuObject menu;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.GameState#getName()
	 */
	@Override
	public String getName() {
		return "title";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.AbstractGameState#initialize(com.
	 * snapgames.gdj.gdj105.core.Game)
	 */
	@Override
	public void initialize(Game game) {
		super.initialize(game);

		// activate needed layers
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Layer(true, false);
		}

		Font titleFont = game.getGraphics().getFont().deriveFont(3.0f * Game.SCREEN_FONT_RATIO);
		Font menuItemFont = game.getGraphics().getFont().deriveFont(1.2f * Game.SCREEN_FONT_RATIO);
		Font copyFont = game.getGraphics().getFont().deriveFont(0.9f * Game.SCREEN_FONT_RATIO);

		String titleLabel = Messages.getString("TitleState.label.title");
		String copyrightLabel = Messages.getString("TitleState.label.copyright");

		BufferedImage bgImg = ResourceManager.getImage("/res/images/background-large.jpg");
		bgi = (ImageObject) new ImageObject("background")
				.setImage(bgImg)
				.setPosition( 0, (Game.HEIGHT - bgImg.getHeight()) / 2)
				.setLayer(2)
				.setPriority(1)
				.setScale(1.0f)
				.setVelocity(0.029f,0);
		addObject(bgi);

		TextObject titleText = (TextObject) new TextObject("title")
				.setText(titleLabel)
				.setFont(titleFont)
				.setTextAlign(TextPosition.CENTER)
				.setPosition((int) (Game.WIDTH) / 2, (int) (Game.HEIGHT * 0.10f))
				.setLayer(1)
				.setPriority(1)
				.setColor(Color.WHITE);
		addObject(titleText);

		menu = (MenuObject) new MenuObject("menu")
				.setActiveItem(0)
				.setShadowColor(Color.BLACK)
				.setAlignText(TextPosition.CENTER)
				.setFont(menuItemFont)				
				.setPosition((int) (Game.WIDTH * 0.50f), (int) (Game.HEIGHT * 0.50f))
				.setLayer(1)
				.setPriority(1)
				.setColor(Color.WHITE);
		menu.layer = 2;
		menu.priority = 1;

		menu.addItem("start", "TitleState.label.start", "Start");
		menu.addItem("options", "TitleState.label.options", "Options");
		menu.addItem("quit", "TitleState.label.quit", "Quit");

		addObject(menu);

		TextObject cpyText = (TextObject) new TextObject("copyright")
				.setText(copyrightLabel)
				.setTextAlign(TextPosition.CENTER)
				.setFont(copyFont)
				.setPosition((int) (Game.WIDTH) / 2, (int) (Game.HEIGHT * 0.85f))
				.setLayer(2)
				.setPriority(1)
				.setColor(Color.WHITE);
		addObject(cpyText);

		logger.info("State TitleState initialized");
	}

	@Override
	public void input(Game game, InputHandler input) {
	}

	@Override
	public void update(Game game, long dt) {
		bgi.x = bgi.x - bgi.dx * dt;
		if (bgi.x < -Game.WIDTH) {
			bgi.x = 0.0f;
		}
		menu.update(game, dt);
	}

	@Override
	public void keyReleased(Game game, KeyEvent e) {
		super.keyReleased(game, e);
		switch (e.getKeyCode()) {

		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
			menu.previous();
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
			menu.next();
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			switch (menu.getActiveItem().getValue()) {
			case "start":
				game.getGSM().activate("play");
				break;
			case "options":
				game.getGSM().activate("options");
				break;
			case "quit":
				game.setExit(true);
				break;
			}
			break;
		}
	}

}
