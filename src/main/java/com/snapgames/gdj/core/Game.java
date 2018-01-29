/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ105
 * 
 * @year 2017
 */
package com.snapgames.gdj.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapgames.gdj.core.gfx.DebugLevel;
import com.snapgames.gdj.core.gfx.ImageUtils;
import com.snapgames.gdj.core.io.InputHandler;
import com.snapgames.gdj.core.state.GameStateManager;
import com.snapgames.gdj.core.ui.Window;

/**
 * the basic Game container is a JPanel child.
 * 
 * @author Frédéric Delorme
 *
 */
public class Game extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1159822737613073769L;

	/**
	 * Internal logger.
	 */
	public static final Logger logger = LoggerFactory.getLogger(Game.class);

	/**
	 * Game screen ratio.
	 */
	public static float RATIO = 16/9;
	/**
	 * Game screen width.
	 */
	public static int WIDTH = 640;
	/**
	 * Game screen height.
	 */
	public static int HEIGHT = (int)(WIDTH/RATIO);
	/**
	 * game screen scaling
	 */
	public static float SCALE = 2.0f;

	public static float SCREEN_FONT_RATIO = HEIGHT / (44/RATIO);

	/**
	 * Number of frame per seconds
	 */
	public long FPS = 60;
	/**
	 * duration of a frame.
	 */
	public long fpsTargetTime = 1000 / 60;

	/**
	 * Number of frames in a second.
	 */
	public long framesPerSecond = 0;

	/**
	 * The rectangle containing the Game screen.
	 */
	public final static Rectangle bbox = new Rectangle(0, 0, WIDTH, HEIGHT);

	/**
	 * The title for the game instance.
	 */
	private String title = "game";

	/**
	 * Game display space dimension
	 */
	private Dimension dimension = null;

	/**
	 * Active window for this Game.
	 */
	private Window window;

	/**
	 * internal rendering buffer
	 */
	private BufferedImage image;

	/**
	 * Flag to activate debug information display.
	 */
	private int debug = DebugLevel.DEBUG_NONE.ordinal();

	/**
	 * flag representing the exit request status. true => exit
	 */
	private boolean exit = false;

	/**
	 * Flag to track pause request.
	 */
	private boolean isPause = false;

	/**
	 * Flag to activate screenshot recording.
	 */
	private boolean screenshot = false;

	/**
	 * Rendering interface.
	 */
	private Graphics2D g;

	/**
	 * Input manager
	 */
	private InputHandler inputHandler;

	private GameStateManager gsm;

	/**
	 * the default constructor for the {@link Game} panel with a game
	 * <code>title</code>.
	 * 
	 * @param title
	 *            the title for the game.
	 */
	private Game(String title) {
		this.title = title;
		this.dimension = new Dimension((int) (WIDTH * SCALE), (int) (HEIGHT * SCALE));
		exit = false;
		gsm = new GameStateManager(this);
		inputHandler = new InputHandler(gsm);
	}

	/**
	 * Initialize the Game object with <code>g</code>, the Graphics2D interface to
	 * render things.
	 */
	private void initialize() {

		// Internal display buffer
		image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		gsm.activateDefaultState();
	}

	/**
	 * The main Loop !
	 */
	private void loop() {
		long currentTime = System.currentTimeMillis();
		long lastTime = currentTime;
		long second = 0;
		long framesCounter = 0;
		while (!exit) {
			currentTime = System.currentTimeMillis();
			long dt = currentTime - lastTime;

			// manage input
			input();
			if (!isPause) {
				// update all game's objects
				update(dt);
			}
			// render all Game's objects
			render(g);
			// copy buffer
			drawToScreen();

			// manage wait time
			long laps = System.currentTimeMillis() - lastTime;
			second += laps;
			framesCounter += 1;
			if (second >= 1000) {
				second = 0;
				framesPerSecond = framesCounter;
				framesCounter = 0;
			}
			long wait = fpsTargetTime - laps;

			logger.debug("FPS: {} (laps:{}, wait:{})", framesPerSecond, laps, wait);

			if (wait > 0) {
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
					logger.error("unable to wait 1 ms");
				}
			}
			lastTime = currentTime;

		}
	}

	/**
	 * Copy buffer to window.
	 */
	private void drawToScreen() {

		// copy buffer to window.
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, (int) (WIDTH * SCALE), (int) (HEIGHT * SCALE), 0, 0, WIDTH, HEIGHT, Color.BLACK,
				null);
		g2.dispose();

		if (screenshot) {
			ImageUtils.screenshot(image);
			screenshot = false;
		}
	}

	/**
	 * Manage Game input.
	 */
	private void input() {
		gsm.input(inputHandler);
	}

	/**
	 * Update game internals
	 * 
	 * @param dt
	 */
	private void update(long dt) {
		gsm.update(dt);
	}

	/**
	 * render all the beautiful things.
	 * 
	 * @param g
	 */
	private void render(Graphics2D g) {
		// clear display
		clearBuffer(g);

		gsm.render(g);
	}

	/**
	 * @param g
	 */
	private void clearBuffer(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * free all resources used by the Game.
	 */
	private void release() {
		gsm.dispose();
		window.dispose();
	}

	/**
	 * the only public method to start game.
	 */
	public void run() {
		initialize();
		loop();
		release();
		System.exit(0);
	}

	/**
	 * return the title of the game.
	 * 
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * return the dimension of the Game display.
	 * 
	 * @return
	 */
	public Dimension getDimension() {
		return dimension;
	}

	/**
	 * Set the active window for this game.
	 * 
	 * @param window
	 *            the window to set as active window for the game.
	 */
	public void setWindow(Window window) {
		this.window = window;
	}

	/**
	 * @return the exit
	 */
	public boolean isExit() {
		return exit;
	}

	/**
	 * @param exit
	 *            the exit to set
	 */
	public void setExit(boolean exit) {
		this.exit = exit;
	}

	/**
	 * @return the inputHandler
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}

	/**
	 * return debug activation flag. true, visual debug is activated, false, normal
	 * rendering.
	 * 
	 * @return
	 */
	public boolean isDebug(int level) {
		return debug >= level;
	}

	/**
	 * @return the pause
	 */
	public boolean isPause() {
		return isPause;
	}

	public Graphics2D getRender() {
		return g;
	}

	public float getScale() {
		return SCALE;
	}

	public void requestPause() {
		isPause = !isPause;

	}

	/**
	 * Activate the debug mode.
	 * 
	 * @param b
	 */
	public void setDebug(int level) {
		debug = level;
	}

	/**
	 * @return the debug
	 */
	public int getDebug() {
		return debug;
	}

	/**
	 * request for a screen shot.
	 */
	public void captureScreenshot() {
		screenshot = true;

	}

	/**
	 * Set the new Game Dimensions according to HEIGHT, WIDTH and SCALE.
	 * 
	 * @param width
	 * @param height
	 * @param scale
	 */
	public void setDimension(int width, int height, float scale) {

		this.dimension = new Dimension((int) (width * scale), (int) (height * scale));

	}

	/**
	 * return the GameStateManager for this game.
	 * 
	 * @return
	 */
	public GameStateManager getGSM() {
		return gsm;
	}

	/**
	 * The main entry point to start our GDJ104 game.
	 * 
	 * @param argv
	 *            list of arguments from command line.
	 */
	public static void main(String[] argv) {

		Game game = new Game("GDJ");

		game.parse(argv);
		new Window(game);
		game.run();

	}

	/**
	 * Parse all console attributes and set the corresponding values.
	 * 
	 * @param argv
	 */
	private void parse(String[] argv) {
		Options options = buildOptions();
		try {
			CommandLineParser clp = new DefaultParser();
			CommandLine line = clp.parse(options, argv);
			int debug = Integer.parseInt(line.getOptionValue("debug", "0"));
			int width = Integer.parseInt(line.getOptionValue("width", "320"));
			int height = Integer.parseInt(line.getOptionValue("height", "240"));
			int scale = Integer.parseInt(line.getOptionValue("scale", "2"));
			// boolean fullScreen = Boolean.parseBoolean(line.getOptionValue("fullscreen",
			// "false"));

			WIDTH = width;
			HEIGHT = height;
			SCALE = scale;
			this.debug = debug;
			// TODO implement full screen management.

		} catch (ParseException e) {
			logger.error("unable to parse command line. Try executing command this the -help option.");
		}

	}

	/**
	 * <p>
	 * Build options for CLI command line options.
	 * </p>
	 * <p>
	 * Options are:
	 * </p>
	 * <ul>
	 * <li><code>debug</code> (<code>-d</code>) activate some console output for
	 * debug purpose.</li>
	 * <li><code>width</code> (<code>-w</code>) set the width of the game window
	 * (not in full screen mode)</li>
	 * <li><code>height</code> (<code>-h</code>) set the height of the game window
	 * (not in full screen mode)</li>
	 * <li><code>fullscreen</code> (<code>-f</code>) set GameContainer in full
	 * screen mode.</li>
	 * </ul>
	 *
	 * @return the Options object to be provided to the command line analyzer.
	 */
	private static Options buildOptions() {
		Options options = new Options();
		// Declare options
		Option debugOption = Option.builder("d").desc("activate the debug mode").longOpt("debug").hasArg().build();
		Option widthOption = Option.builder("w").desc("set width of window").longOpt("width").hasArg().build();
		Option heightOption = Option.builder("h").desc("activate the debug mode").longOpt("height").hasArg().build();
		Option scaleOption = Option.builder("s").desc("scale the all window").longOpt("scale").hasArg().build();
		Option fullScreenOption = Option.builder("f").desc("activate the fullscreen display mode").longOpt("fullscreen")
				.hasArg().build();
		// Add all these new possible options to the bundle :)
		options.addOption(debugOption);
		options.addOption(widthOption);
		options.addOption(heightOption);
		options.addOption(scaleOption);
		options.addOption(fullScreenOption);
		return options;
	}

}
