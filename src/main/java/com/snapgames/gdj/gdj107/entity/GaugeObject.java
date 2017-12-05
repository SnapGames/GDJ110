/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ105
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj107.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.AbstractGameObject;

/**
 * a Jauge display (for energy, life, mana)
 * 
 * @author Frédéric Delorme
 *
 */
public class GaugeObject extends AbstractGameObject {
	private static final Color borderColor = new Color(0.9f, 0.9f, 0.9f);
	private static final Color backgroundColor = new Color(0.0f, 0.0f, 0.0f, 0.6f);
	public int minValue = 0, maxValue = 100;
	public int value = maxValue;

	/**
	 * 
	 */
	public GaugeObject() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GaugeObject(String name) {
		super(name,0,0,0,0);
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
		g.drawRect((int) x - 2, (int) y - 2, (int) width + 4, (int) height + 4);

		g.setColor(borderColor);
		g.drawRect((int) x - 1, (int) y - 1, (int) width + 2, (int) height + 2);

		g.setColor(Color.WHITE);
		g.drawRect((int) x - 1, (int) y - 1, 1, 1);

		int eWidth = (int) (width * (float) ((float) (value + 1) / (float) (maxValue - minValue)));
		if (value > 0) {
			g.setColor(color);
			g.fillRect((int) x, (int) y, eWidth + 1, (int) height + 1);
		}

		g.setColor(backgroundColor);
		g.fillRect((int) x + eWidth, (int) y, width - eWidth, (int) height + 1);

		g.setColor(Color.BLACK);
		g.drawRect((int) x, (int) y, (int) width, (int) height);
		g.drawLine((int) x + eWidth, (int) y, (int) x + eWidth, (int) y + height);

	}

	/*-------------------- Chaining API ------------------*/

	/**
	 * Set minimum value for this Gauge
	 * 
	 * @param min
	 * @return
	 */
	public GaugeObject setMinValue(int min) {
		this.minValue = min;
		return this;
	}

	/**
	 * Set maximum value for this Gauge
	 * 
	 * @param max
	 * @return
	 */
	public GaugeObject setMaxValue(int max) {
		this.maxValue = max;
		return this;
	}

	/**
	 * Set value for this Gauge
	 * 
	 * @param value
	 * @return
	 */
	public GaugeObject setValue(int value) {
		this.value = value;
		return this;
	}

}
