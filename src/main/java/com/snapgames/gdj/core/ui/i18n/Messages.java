/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj104
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.ui.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public class Messages {
	/**
	 * Path to the resource messages file
	 */
	private static final String BUNDLE_NAME = "res.messages"; //$NON-NLS-1$

	/**
	 * Load bundle from path.
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * private Constructor to not instantiate this class outside itself
	 */
	private Messages() {
	}

	/**
	 * Get a message from bundle file.
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Search for key. if does not exist return defaultValue.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(String key, String defaultValue) {
		if (RESOURCE_BUNDLE.containsKey(key)) {
			return RESOURCE_BUNDLE.getString(key);
		} else {
			return defaultValue;
		}

	}
}
