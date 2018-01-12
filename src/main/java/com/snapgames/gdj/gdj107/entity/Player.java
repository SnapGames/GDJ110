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

import com.snapgames.gdj.core.entity.AbstractGameObject;

/**
 * The Player entity.
 * 
 * @author Frédéric Delorme
 *
 */
public class Player extends AbstractGameObject {

	/**
	 * the default Constructor, doing nothing but creating an instance.
	 */
	public Player() {
		super();
	}

	/**
	 * Initialize the player object with a name and the default values.
	 * 
	 * @param name
	 */
	public Player(String name) {
		super(name, 0, 0, 0, 0);
		initDefaultValues();
	}

	/**
	 * 
	 */
	protected void initDefaultValues() {
		this.hSpeed = 0.05f;
		this.vSpeed = 0.05f;
		this.priority = 1;
		this.layer = 1;
		this.showDebuginfo = true;
		attributes.put("energy", new Integer(100));
		attributes.put("mana", new Integer(100));
		attributes.put("level", new Integer(1));
	}

}
