/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj105
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj107.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.AbstractGameObject;
import com.snapgames.gdj.core.gfx.RenderHelper;
import com.snapgames.gdj.core.gfx.RenderHelper.TextPosition;

/**
 * @author Frédéric Delorme
 *
 */
public class ItemContainerObject extends AbstractGameObject {
	private static final Color borderColor = new Color(0.9f, 0.9f, 0.9f);
	private static final Color backgroundColor = new Color(0.0f, 0.0f, 0.0f, 0.6f);
	public BufferedImage image;
	public Font containerTextfont;

	/**
	 * Create a new Item container object with <code>name</code>.
	 * 
	 * @param name
	 */
	public ItemContainerObject(String name) {
		super(name, 0, 0, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.AbstractGameObject#draw(com.snapgames.gdj.core.
	 * Game, java.awt.Graphics2D)
	 */
	@Override
	public void draw(Game game, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect((int) x - 3, (int) y - 3, width + 6, height + 6);
		g.setColor(borderColor);
		g.drawRect((int) x - 2, (int) y - 2, width + 4, height + 4);
		g.setColor(Color.WHITE);
		g.drawRect((int) x - 1, (int) y - 1, 1, 1);
		g.setColor(Color.BLACK);
		g.drawRect((int) x - 1, (int) y - 1, width + 2, height + 2);
		g.setColor(backgroundColor);
		g.fillRect((int) x, (int) y, width + 1, height + 1);
		if (image != null) {
			g.drawImage(image, (int) x, (int) y, null);
		}
		if (!attributes.isEmpty()) {
			if (attributes.containsKey("number")) {
				int number = (int) attributes.get("number");
				g.setFont(containerTextfont);
				RenderHelper.drawShadowString(g, String.format("%2d", number), (int) x + 18, (int) y + 24, Color.WHITE,
						backgroundColor, TextPosition.RIGHT, 2);
			}
		}
	};

	/*--------------------- Chain API ---------------------*/

	/**
	 * Set the text value for this TextObject.
	 * 
	 * @param text
	 * @return
	 */
	public ItemContainerObject setFont(Font font) {
		this.containerTextfont = font;
		return this;
	}

}
