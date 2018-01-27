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

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;
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
	private static String BUNDLE_NAME = "res.i18n.messages"; //$NON-NLS-1$

	/**
	 * Load bundle from path.
	 */
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static final ArrayList<Locale> LOCALES = new ArrayList<>();

	static {
		try {
			File f = new File(Messages.class.getResource("/res/i18n").toURI());
			final String bundle = "messages_";// Bundle name prefix.
			for (String s : f.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(bundle);
				}
			})) {
				LOCALES.add(new Locale(s.substring(bundle.length(), s.indexOf('.'))));
			}
		} catch (URISyntaxException x) {
			throw new RuntimeException(x);
		}
		LOCALES.trimToSize();
	}

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
