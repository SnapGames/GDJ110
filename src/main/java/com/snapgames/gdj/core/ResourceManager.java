/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj104
 * 
 * @year 2017
 */
package com.snapgames.gdj.core;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Frédéric Delorme
 *
 */
public class ResourceManager {

	private static ResourceManager instance = null;

	private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

	private Map<String, Object> resources = new ConcurrentHashMap<>();

	private ResourceManager() {

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private Object addResource(String name) {
		assert (resources != null);
		assert (name != null);
		if (!resources.containsKey(name)) {
			String extension = name.substring(name.lastIndexOf("."), name.length());
			switch (extension) {
			case ".jpg":
			case ".png":
				try {
					BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(name));
					resources.put(name, image);
				} catch (IOException e) {
					logger.error("unable to find resource for {}", name);
				}
				break;
			}
		}
		return resources.get(name);
	}

	private void addResource(String name, Object value) {
		assert (resources != null);
		assert (name != null);
		assert (value != null);
		if (!resources.containsKey(name)) {
			resources.put(name, value);
		}
	}

	/**
	 * retrieve a resource for this name
	 * 
	 * @param name
	 * @return
	 */
	public static Object get(String name) {
		return getInstance().addResource(name);
	}

	/**
	 * Add an external (<code>name</code>,<code>value</code>) resource to the
	 * manager.
	 * 
	 * @param name
	 * @param value
	 */
	public static void add(String name, Object value) {
		getInstance().addResource(name, value);
	}

	/**
	 * Retrieve an image <code>name</code> from the resources.
	 * 
	 * @param name
	 * @return
	 */
	public static BufferedImage getImage(String name) {
		return (BufferedImage) getInstance().addResource(name);
	}

	/**
	 * Retrieve a Font <code>name</code> from the resources
	 * 
	 * @param name
	 * @return
	 */
	public static Font getFont(String name) {
		return (Font) getInstance().addResource(name);
	}

	/**
	 * return the instance of this Resource Manager.
	 * 
	 * @return
	 */
	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}
}
