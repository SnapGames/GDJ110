/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ106
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.entity;

/**
 * this enumeration will help to manage the animation process for any animated
 * entity
 * 
 * @author Frédéric Delorme
 *
 */
public enum Actions {
	IDLE("idle"), 
	WALK("walk"), 
	RUN("run"), 
	UP("up"), 
	DOWN("down"), 
	FALL("fall"), 
	DEAD("dead"), 
	ACTION1("a1"), 
	ACTION2("a2"), 
	ACTION3("a3"), 
	ACTION4("a4");

	/**
	 * Initialize enumeration with a specific value
	 * @param value
	 */
	Actions(String value) {
		this.action = value;
	}

	/**
	 * action attribute to keep name of this action.
	 */
	String action = "";

	/**
	 * return action value for the enumeration value.
	 * @return
	 */
	public String getValue() {
		return action;
	}
}
