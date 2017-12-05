/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj106
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.utils;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public class OptionDoesNotExistsException extends Exception {

	String optionName;

	/**
	 * @param name
	 */
	public OptionDoesNotExistsException(String name) {
		super(String.format("option %s does not exists", name));
		optionName = name;
	}

}
