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
public class MathHelpers {

	/**
	 * Generate a float value from <code>minimumValue</code> to
	 * <code>maximumValue</code>.
	 * 
	 * @param maximumValue
	 *            the maximum value to produce.
	 * @param minimumValue
	 *            the minimum value to produce.
	 * @return
	 */
	public static float generateFloatValue(float maximumValue, float minimumValue) {
		return (float) ((Math.random() * maximumValue) + minimumValue);
	}

}
