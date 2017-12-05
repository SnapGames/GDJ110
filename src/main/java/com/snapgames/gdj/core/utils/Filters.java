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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public class Filters {

	public static List<Object> onType(Collection<Object> list, Class<?> classtype) {
		List<Object> outputList = new ArrayList<>();
		for (Object o : list) {
			if (o.getClass().equals(classtype)) {
				outputList.add(o);
			}
		}
		return outputList;
	}
}
