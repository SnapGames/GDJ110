/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ106
 * 
 * @year 2017
 * 
 */
package com.snapgames.gdj.gdj107.entity;

import static com.snapgames.gdj.core.utils.MathHelpers.generateFloatValue;

import java.awt.Color;
import java.awt.Graphics2D;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.AbstractGameObject;

/**
 * A bad enemy !
 * 
 * @author Frédéric Delorme
 *
 */
public class Enemy extends AbstractGameObject {

	/**
	 * the internal colors map for all objects.
	 */
	private static Color[] colors = new Color[20];

	/**
	 * Initialize the color map for all enemies.
	 */
	protected static void initialize() {
		for (int i = 0; i < colors.length; i++) {
			colors[i] = new Color(generateFloatValue(0.8f, 0.2f), generateFloatValue(0.8f, 0.2f),
					generateFloatValue(0.8f, 0.2f), generateFloatValue(0.5f, 0.5f));
		}
	}

	/**
	 * Enemy Initialization.
	 */
	public Enemy() {
		super();
		initDefaultValues();
	}

	/**
	 * Create an Enemy object with <code>name</code>.
	 * 
	 * @param name
	 */
	public Enemy(String name) {
		super(name, 0, 0, 0, 0);
		initDefaultValues();
	}

	/**
	 * Initialize default values.
	 */
	protected void initDefaultValues() {
		if (colors[0] == null) {
			initialize();
		}
		setSize(16, 16);
		setColor(randomColor());
		setLayer(3);
		setPriority(1);
		setInitialVelocity(0.042f, 0.042f);
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
		super.draw(game, g);
		g.setColor(Color.BLACK);
		g.drawRect((int) x, (int) y, width, height);
	}

	/**
	 * randomly selecting a color from the map.
	 * 
	 * @return
	 */
	public static Color randomColor() {
		return colors[(int) (Math.random() * (colors.length - 1.0f)) + 1];
	}
}
