/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ104
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.ResourceManager;
import com.snapgames.gdj.core.collision.QuadTree;
import com.snapgames.gdj.core.entity.AbstractGameObject;
import com.snapgames.gdj.core.entity.CameraObject;
import com.snapgames.gdj.core.entity.GameObject;
import com.snapgames.gdj.core.entity.Layer;
import com.snapgames.gdj.core.gfx.DebugLevel;
import com.snapgames.gdj.core.gfx.RenderHelper;

/**
 * An Abstract Game State to manage all states of the Game !
 */
public abstract class AbstractGameState implements GameState {

	private static final Logger logger = LoggerFactory.getLogger(AbstractGameState.class);

	/**
	 * Internal statistics for the engine.
	 */
	protected Map<String, Integer> statistics = new ConcurrentHashMap<>();

	/**
	 * Internal context to share attributes and their values across states.
	 */
	protected Map<String, Object> context = new ConcurrentHashMap<>();

	/**
	 * The QuadTree to manage objects collision and visibility.
	 */
	public QuadTree quadTree;

	/**
	 * Referring GameStateManager
	 */
	protected GameStateManager gsm = null;

	/**
	 * Internal rendering layers. by default 4 layers (0->3) are initialized.
	 */
	protected Layer[] layers = new Layer[3];
	/**
	 * Layers to be managed and see through camera view.
	 */
	protected List<Layer> layersWith = new CopyOnWriteArrayList<>();
	/**
	 * Layers to be managed outside any camera view.
	 */
	protected List<Layer> layersWithoutCamera = new CopyOnWriteArrayList<>();

	/**
	 * Embedded debug font to draw on screen debug information.
	 */
	protected Font debugFont;

	/**
	 * Background color (for clear purpose).
	 */
	protected Color backgroundColor = Color.BLACK;

	/**
	 * List of managed objects. Use a list that can put up with concurrent access.
	 */
	protected List<GameObject> objects = new CopyOnWriteArrayList<>();

	/**
	 * List Of camera
	 */
	protected List<CameraObject> cameras = new CopyOnWriteArrayList<>();

	/**
	 * current active camera.
	 */
	protected CameraObject defaultCamera = null;;

	/**
	 * Default constructor for the AbstractGameState
	 */
	public AbstractGameState() {
		super();
	}

	public AbstractGameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.GameState#initialize(com.snapgames.gdj.
	 * gdj105.core.Game)
	 */
	@Override
	public void initialize(Game game) {
		ResourceManager.add("debugFont", game.getRender().getFont().deriveFont(9f));
		quadTree = new QuadTree(Game.WIDTH, Game.HEIGHT);

		// activate needed layers
		resetLayers();
	}

	/**
	 * 
	 */
	protected void resetLayers() {
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Layer(true, true);
		}
		layersWith.clear();
		layersWithoutCamera.clear();
	}

	/**
	 * Add an object to the Object list and sort them according to their layer and
	 * priority.
	 * 
	 * @param object
	 */
	protected void addObject(AbstractGameObject object) {
		// add object to rendering list
		objects.add(object);
		objects.sort(new Comparator<GameObject>() {
			public int compare(GameObject o1, GameObject o2) {
				AbstractGameObject ago1 = (AbstractGameObject) o1;
				AbstractGameObject ago2 = (AbstractGameObject) o2;
				return (ago1.layer > ago2.layer ? -1 : (ago1.priority > ago2.priority ? -1 : 1));
			};
		});
		// add object to a specific Layer.
		addObjectToLayer(object);

		statistics.put("objectCount", objects.size());
		logger.debug("Add {} to the objects list", object.name);
	}

	private void addObjectToLayer(AbstractGameObject object) {
		if (object.layer >= 0 && layers[object.layer] != null) {

			layers[object.layer].objects.add(object);
		}
		if (!layers[object.layer].moveWithCamera) {
			layersWithoutCamera.add(layers[object.layer]);
		} else {
			layersWith.add(layers[object.layer]);
		}
	}

	protected void removeAllObjectOfClass(Class<? extends AbstractGameObject> clazz) {
		List<GameObject> toBeDeleted = new ArrayList<>();
		for (GameObject o : objects) {
			if (o.getClass().equals(clazz)) {
				toBeDeleted.add(o);
			}
		}
		objects.removeAll(toBeDeleted);
	}

	/**
	 * add a Camera object.
	 * 
	 * @param cameraObject
	 */
	public void addCamera(CameraObject cameraObject) {
		cameras.add(cameraObject);
		// objects.add(cameraObject);
		if (defaultCamera == null) {
			defaultCamera = cameraObject;
		}
	}

	/**
	 * activate one of the camera.
	 * 
	 * @param camera
	 */
	public void setCamera(String cameraName) {
		for (CameraObject c : cameras) {
			if (c.name.equals(cameraName)) {
				this.defaultCamera = c;
				return;
			}
		}
		logger.error("Unable to activate the camera, {} does not exist", cameraName);
	}

	public void dispose(Game game) {
		objects.clear();
		logger.info("Dispose {}", this.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.GameState#keyTyped(com.snapgames.gdj.
	 * gdj105.core.Game, java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(Game game, KeyEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.GameState#keyPressed(com.snapgames.gdj.
	 * gdj105.core.Game, java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(Game game, KeyEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.GameState#keyReleased(com.snapgames.gdj.
	 * gdj105.core.Game, java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(Game game, KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_Q:
			game.setExit(true);
			break;
		case KeyEvent.VK_F9:
		case KeyEvent.VK_D:
			int debug = game.getDebug();
			debug++;
			debug = (debug > DebugLevel.DEBUG_FPS_BOX_DIRECTION_ATTRS.ordinal() ? DebugLevel.DEBUG_NONE.ordinal()
					: debug);
			game.setDebug(debug);
			break;
		case KeyEvent.VK_S:
			game.captureScreenshot();
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.GameState#render(com.snapgames.gdj.gdj105
	 * .core.Game, java.awt.Graphics2D)
	 */
	public void render(Game game, Graphics2D g) {
		int renderedObjectCount = 0;
		Rectangle view = (defaultCamera != null && defaultCamera.rectangle != null ? defaultCamera.rectangle
				: Game.bbox);
		if (!objects.isEmpty()) {
			for (GameObject o : objects) {
				if (layers.length > 1) {
					Layer layer = layers[o.getLayer() - 1];
					if (layer.active) {
						if (defaultCamera != null && layer.moveWithCamera) {
							g.translate(-defaultCamera.getX(), -defaultCamera.getY());
						}
						if (viewContainsObject(o, view) || defaultCamera == null || !layer.moveWithCamera) {
							renderedObjectCount++;
							o.draw(game, g);
							if (game.isDebug(DebugLevel.DEBUG_FPS_BOX.ordinal()) || o.isDebugInfoDisplayed()) {
								o.drawSpecialDebugInfo(game, g);

							}
						}
						if (defaultCamera != null && layer.moveWithCamera) {
							g.translate(defaultCamera.getX(), defaultCamera.getY());
						}
					}
				}
			}
			if (defaultCamera != null) {
				defaultCamera.draw(game, g);
			}
			statistics.put("renderedObjCount", renderedObjectCount);
			statistics.put("staticObjCount", objects.size());
		}

		if (game.isDebug(1)) {
			g.setFont(game.getFont());
			RenderHelper.drawShadowString(g,
					String.format("FPS:%03d, ROC:%04d, SOC:%04d", game.framesPerSecond,
							statistics.get("renderedObjCount"), statistics.get("staticObjCount")),
					4, (int) (Game.HEIGHT * 0.93f), Color.BLUE, Color.BLACK);
		}

	}

	/**
	 * Test if object is in screen.
	 * 
	 * @param o
	 * @return
	 */
	private boolean viewContainsObject(GameObject o, Rectangle view) {
		AbstractGameObject ago = (AbstractGameObject) o;

		return view.contains(ago.rectangle);
	}

	/**
	 * @return the defaultCamera
	 */
	public CameraObject getDefaultCamera() {
		return defaultCamera;
	}

	/**
	 * @param defaultCamera
	 *            the defaultCamera to set
	 */
	public void setDefaultCamera(CameraObject defaultCamera) {
		this.defaultCamera = defaultCamera;
	}
}
