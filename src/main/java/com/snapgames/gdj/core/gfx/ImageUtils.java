/**
 * SnapGames
 * <p>
 * Game Development Java
 * <p>
 * gdj105
 *
 * @year 2017
 */
package com.snapgames.gdj.core.gfx;

import com.snapgames.gdj.core.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Image utilities
 *
 * @author Frédéric Delorme
 *
 */
public class ImageUtils {
	/**
	 * Internal Logger for this utils class.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);
	/**
	 * default path to store image captures.
	 */
	private static String path = System.getProperty("user.home");

	/**
	 * Internal image counter.
	 */
	private static int recordingCount = 0;

	/**
	 * Sequence counter
	 */
	private static int seqCount = 0;

	/**
	 * Take a screenshot from the image to the default `user.dir`.
	 *
	 * @param image
	 *            image to be saved to disk.
	 */
	public static void screenshot(BufferedImage image) {
		try {
			java.io.File out = new java.io.File(path + File.separator + "screenshot " + System.nanoTime() + ".jpg");
			javax.imageio.ImageIO.write(image.getSubimage(0, 0, Game.WIDTH, Game.HEIGHT), "JPG", out);
		} catch (Exception e) {
			System.err.println("Unable to write screenshot to user.dir: " + path);
		}
	}

	/**
	 * Record a sequence of image to default `user.dir`.
	 *
	 * @param image
	 *            image to be saved to disk.
	 */
	public static void record(BufferedImage image) {
		String filename = "";
		try {
			filename = path + File.separator + "sequence_" + seqCount + "_frame_" + recordingCount + ".jpg";
			java.io.File out = new java.io.File(
					filename);
			javax.imageio.ImageIO.write(image, "JPG", out);
			recordingCount++;
		} catch (Exception e) {
			logger.error("Unable to save image to {}", filename,e);
		}
	}

	public static void startRecord() {
		seqCount++;
		recordingCount = 0;
	}

	public static void stopRecord() {

	}
}
