/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj106
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj107.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.AbstractGameObject;

/**
 * An object to be eat by the player to upgrade its energy.
 * 
 * @author Frédéric Delorme
 *
 */
public class Eatable extends AbstractGameObject {

	/**
	 * A default constructor.
	 */
	public Eatable() {
		super();
		initDefaultValues();
	}

	/**
	 * Create an eatable object with <code>name</code>.
	 * 
	 * @param name
	 *            the name of this object.
	 */
	public Eatable(String name) {
		super(name, 0, 0, 0, 0);
		initDefaultValues();
	}

	/**
	 * Initialize the default Eatable attributes values.
	 */
	protected void initDefaultValues() {
		setSize(8, 8);
		setLayer(4);
		setPriority(2);
		setColor(Color.CYAN);
		addAttribute("power", 20);
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
		g.setColor(color);
		g.fillArc((int) x, (int) y, width, height, 0, 360);
		g.setColor(Color.BLACK);
		g.drawArc((int) x, (int) y, width, height, 0, 360);
	}

}
