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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public class GameOptions {

	private static GameOptions instance;

	private static final Logger logger = LoggerFactory.getLogger(GameOptions.class);

	protected static Properties props = new Properties();

	/**
	 * the name of the Option for Music.
	 */
	public static final String OPTION_MUSIC_FLAG = "Options.MusicFlag";

	/**
	 * the name of the Option for Sound.
	 */
	public static final String OPTION_SOUND_FLAG = "Options.SoundFlag";

	/**
	 * file to store configuration of options.
	 */
	public static final String OPTIONS_PROPERTIES_FILE = "/options.properties";

	private GameOptions() {

	}

	public static void set(String name, Object value) {
		props.put(name, value.toString());
		logger.debug("Add option {} with value {}", name, value.toString());
	}

	public static Boolean getBoolean(String name) throws OptionDoesNotExistsException {
		if (props.containsKey(name)) {
			return Boolean.parseBoolean((String) props.get(name));
		} else {
			throw new OptionDoesNotExistsException(name);
		}
	}

	public static Integer getInteger(String name) throws OptionDoesNotExistsException {
		if (props.containsKey(name)) {
			return Integer.parseInt((String) props.get(name));
		} else {
			throw new OptionDoesNotExistsException(name);
		}
	}

	public static String getString(String name) throws OptionDoesNotExistsException {
		if (props.containsKey(name)) {
			return (String) props.get(name);
		} else {
			throw new OptionDoesNotExistsException(name);
		}
	}

	public static Float getFloat(String name) throws OptionDoesNotExistsException {
		if (props.containsKey(name)) {
			return Float.parseFloat((String) props.get(name));
		} else {
			throw new OptionDoesNotExistsException(name);
		}
	}

	public static void load(InputStream inStream) {
		try {
			props.load(inStream);
			logger.debug("Options loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(OutputStream outStream) {
		try {
			props.store(outStream, null);
			logger.debug("Options stored");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load options from file <code>options.properties</code>.
	 */
	public void load() {
		try {
			if (Files.isRegularFile(Paths
					.get(this.getClass().getResource(GameOptions.OPTIONS_PROPERTIES_FILE).getFile().substring(1)))) {
				GameOptions.load(this.getClass().getResourceAsStream(GameOptions.OPTIONS_PROPERTIES_FILE));
				logger.info("Options loaded from {}", GameOptions.OPTIONS_PROPERTIES_FILE);
			}
		} catch (NullPointerException e) {
			logger.info("Unable to read Options from {}", GameOptions.OPTIONS_PROPERTIES_FILE);
			save();
		}
	}

	/**
	 * Save game options to file <code>options.properties</code>.
	 */
	public void save() {

		File fout = new File(this.getClass().getResource("/").getPath() + "options.properties");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fout);
			GameOptions.write(fos);
			logger.info("Options saved to {}", GameOptions.OPTIONS_PROPERTIES_FILE);
		} catch (FileNotFoundException e) {
			logger.error("Unable to save Options to {}", GameOptions.OPTIONS_PROPERTIES_FILE);
		}
	}

	public static GameOptions getInstance() {
		if (instance == null) {
			instance = new GameOptions();
			instance.load();
		}
		return instance;
	}

	public static void loadOptions() {
		getInstance();
	}

	public static void saveOptions() {
		getInstance().save();
	}

}
