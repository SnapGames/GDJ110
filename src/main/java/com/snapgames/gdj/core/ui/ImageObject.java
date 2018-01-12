/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ105
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.AbstractGameObject;

/**
 * The Background image is a simple image to be displayed in background.
 * 
 * @author Frédéric Delorme
 *
 */
public class ImageObject extends AbstractGameObject {

	public BufferedImage image;

	public ImageObject(String name) {
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
		g.drawImage(image, (int) (x), (int) (y), null);
		g.drawImage(image, (int) (x) + width, (int) (y), null);
	}

	public ImageObject setImage(BufferedImage image) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
		return this;
	}

}
