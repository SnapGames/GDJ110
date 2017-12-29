/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ106
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj110;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.ResourceManager;
import com.snapgames.gdj.core.collision.Sizeable;
import com.snapgames.gdj.core.entity.AbstractGameObject;
import com.snapgames.gdj.core.entity.Actions;
import com.snapgames.gdj.core.entity.CameraObject;
import com.snapgames.gdj.core.entity.Direction;
import com.snapgames.gdj.core.entity.GameObject;
import com.snapgames.gdj.core.entity.Layer;
import com.snapgames.gdj.core.entity.particles.ParticleSystem;
import com.snapgames.gdj.core.entity.particles.effects.rain.RainBehavior;
import com.snapgames.gdj.core.gfx.RenderHelper;
import com.snapgames.gdj.core.io.InputHandler;
import com.snapgames.gdj.core.state.AbstractGameState;
import com.snapgames.gdj.core.state.GameState;
import com.snapgames.gdj.core.state.GameStateManager;
import com.snapgames.gdj.core.ui.TextObject;
import com.snapgames.gdj.gdj107.entity.Eatable;
import com.snapgames.gdj.gdj107.entity.Enemy;
import com.snapgames.gdj.gdj107.entity.GaugeObject;
import com.snapgames.gdj.gdj107.entity.ItemContainerObject;
import com.snapgames.gdj.gdj107.entity.Player;

/**
 * The Play State class defines default behavior for the playable game state.
 * This where all the rendering process, the default input processing and update
 * are performed.
 * 
 * @author Frédéric Delorme
 *
 */
public class PlayState extends AbstractGameState implements GameState {

	/**
	 * objects to be animated on the game display.
	 */
	// Object moved by player
	private Player player = null;
	// list of other entities to demonstrate AbstractGameObject usage.
	private List<AbstractGameObject> entities = new CopyOnWriteArrayList<>();

	// Object moved by player
	private TextObject scoreTextObject = null;

	// Rain effect
	ParticleSystem rain;
	
	int dEnergy = 1;
	private GaugeObject energy;
	int dMana = 1;
	private GaugeObject mana;

	// score
	private int score = 0;

	/**
	 * internal Font to draw any text on the screen !
	 */
	private Font font;
	private Font helpFont;

	/**
	 * Flag to display Help.
	 */
	private boolean isHelp = false;
	private Rectangle playZone;
	private static final Logger logger = LoggerFactory.getLogger(PlayState.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.state.GameState#getName()
	 */
	@Override
	public String getName() {
		return "play";
	}

	public PlayState() {
	}

	public PlayState(GameStateManager gsm) {
		super(gsm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.gdj105.statemachine.GameState#initialize(com.snapgames.gdj.
	 * gdj104.Game)
	 */
	@Override
	public void initialize(Game game) {
		super.initialize(game);

		// prepare Fonts
		font = game.getGraphics().getFont();
		Font scoreFont = game.getGraphics().getFont().deriveFont(14.0f);
		helpFont = font.deriveFont(8f);
		ResourceManager.add("font", font);
		ResourceManager.add("helpFont", helpFont);
		ResourceManager.add("scoreFont", scoreFont);

		this.playZone = new Rectangle(0, 0, 1000, 1000);

		// Activate Layers
		layers = new Layer[6];
		resetLayers();
		layers[0].moveWithCamera = false;
		layers[5].moveWithCamera = false;

		// prepare Game objects

		// player (layer 1)
		player = (Player) new Player("player")
				.setPosition(Game.WIDTH / 2, Game.HEIGHT / 2)
				.setSize(16, 16)
				.setLayer(2)
				.setPriority(1)
				.setColor(Color.BLUE);

		addObject(player);

		// Add a camera.
		CameraObject camera = new CameraObject("cam1")
				.setTarget(player)
				.setTweenFactor(0.1f);
		addCamera(camera);

		// Add a Rain particle generator.
		rain = (ParticleSystem) new ParticleSystem("rainyParticles")
				.addBehavior(
						new RainBehavior(0.20f)
							.setRainChance(0.91f)
							.setDropDiameter(2)
							.setDropInitialVelocity(16)
							.setGravity(-9.81f)
							.setWind((float) (Math.random() * -1.0f) + 2.0f))
				.setCamera(camera)
				.setNbParticles(100)
				.setLayer(1)
				.setPriority(100);
		addObject(rain);

		// NPC
		generateEnemies(10);

		int marginLeft = (int) (Game.WIDTH * camera.getMargin() * 2);
		int marginTop = (int) (Game.HEIGHT * camera.getMargin() * 2);
		int marginRight = (int) (Game.WIDTH * (1 - camera.getMargin() * 2));
		int marginBottom = (int) (Game.HEIGHT * (1 - camera.getMargin() * 2));

		// HUD Definition (layer 1)
		scoreTextObject = (TextObject) new TextObject("score")
				.setText(String.format("%06d", score))
				.setShadowColor(Color.BLACK)
				.setShadowBold(2)
				.setFont(scoreFont)
				.setPosition(marginLeft, marginTop)
				.setLayer(1)
				.setPriority(1)
				.setColor(Color.WHITE);
		addObject(scoreTextObject);

		energy = (GaugeObject) new GaugeObject("energy")
				.setMinValue(0)
				.setMaxValue(100)
				.setValue(100)
				.setPosition(marginRight - 50, marginTop)
				.setSize(42, 6)
				.setLayer(1)
				.setPriority(1)
				.setColor(new Color(1.0f, 0.0f, 0.0f, 0.7f));

		addObject(energy);

		mana = (GaugeObject) new GaugeObject("mana")
				.setMinValue(0)
				.setMaxValue(100)
				.setValue(100)
				.setPosition(marginRight - 50, marginTop + 12).setSize(42, 6).setLayer(1).setPriority(1)
				.setColor(new Color(0.0f, 0.0f, 1.0f, 0.9f));
		;
		addObject(mana);

		ItemContainerObject[] itemContainers = new ItemContainerObject[2];
		for (int i = 0; i < itemContainers.length; i++) {
			itemContainers[i] = (ItemContainerObject) new ItemContainerObject("itContainer_" + i)
					.setFont(game.getFont().deriveFont(9.0f))
					.setPosition(marginRight - (6 + (i + 1) * 22), marginBottom - 40).setSize(16, 16).setLayer(1)
					.setPriority(1).addAttribute("number", new Integer((int) Math.random() * 10));
			addObject(itemContainers[i]);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.gdj105.statemachine.GameState#input(com.snapgames.gdj.
	 * gdj104.Game, com.snapgames.gdj.gdj105.InputHandler)
	 */
	@Override
	public void input(Game game, InputHandler input) {
		// left / right
		if (player != null) {
			if (input.getKeyPressed(KeyEvent.VK_SHIFT) && input.getKeyPressed(KeyEvent.VK_CONTROL)) {
				player.hSpeed = 0.4f;
				player.vSpeed = 0.4f;
			} else if (input.getKeyPressed(KeyEvent.VK_SHIFT)) {
				player.hSpeed = 0.1f;
				player.vSpeed = 0.1f;
			} else if (input.getKeyPressed(KeyEvent.VK_CONTROL)) {
				player.hSpeed = 0.2f;
				player.vSpeed = 0.2f;
			} else {
				player.hSpeed = 0.05f;
				player.vSpeed = 0.05f;
			}
			if (input.getKeyPressed(KeyEvent.VK_LEFT)) {
				player.dx = -player.hSpeed;
				player.action = Actions.WALK;
				player.direction = Direction.LEFT;
			} else if (input.getKeyPressed(KeyEvent.VK_RIGHT)) {
				player.dx = +player.hSpeed;
				player.action = Actions.WALK;
				player.direction = Direction.RIGHT;
			} else {
				if (player.dx != 0) {
					player.dx *= 0.980f;
				}
			}

			// up / down
			if (input.getKeyPressed(KeyEvent.VK_UP)) {
				player.dy = -player.vSpeed;
				player.action = Actions.UP;
				player.direction = Direction.UP;
			} else if (input.getKeyPressed(KeyEvent.VK_DOWN)) {
				player.action = Actions.DOWN;
				player.direction = Direction.DOWN;
				player.dy = +player.vSpeed;
			} else {
				if (player.dy != 0) {
					player.dy *= 0.980f;
				}
			}

			if (player.dx == 0.0f && player.dy == 0.0f) {
				player.action = Actions.IDLE;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.gdj105.statemachine.GameState#update(com.snapgames.gdj.
	 * gdj104.Game, long)
	 */
	@Override
	public void update(Game game, long dt) {
		// add all objects to QuadTree
		updateQuadTree(game, dt);

		// compute play zone borders.
		float winborder = 4;
		float wl = winborder;
		float wr = (float) playZone.getWidth() - player.width - winborder;
		float wt = winborder;
		float wb = (float) playZone.getHeight() - player.height - winborder;

		// player limit to playzone
		constrainPlayerTo(wl, wr, wt, wb);
		// entities moving limit to playzone.
		constrainObjectTo();
		// compute score
		updateScore();
		// manage all collision
		manageCollision();
		// update all HUD attributes according to player object attriubtes
		updateHUDAttributes();

		// Update camera
		if (defaultCamera != null) {
			defaultCamera.update(game, dt);
		}
	}

	/**
	 * @param game
	 * @param dt
	 */
	private void updateQuadTree(Game game, long dt) {
		quadTree.clear();
		for (GameObject o : objects) {
			o.update(game, dt);
			// inert object into QuadTree for collision detection.
			quadTree.insert(o);
		}
	}

	/**
	 * update HUD attributes according to player attributes.
	 */
	private void updateHUDAttributes() {
		if (energy != null && mana != null && player.attributes != null && !player.attributes.isEmpty()) {
			energy.value = (Integer) player.attributes.get("energy");
			mana.value = (Integer) player.attributes.get("mana");
		}
	}

	/**
	 * Update Score object on HUD
	 */
	private void updateScore() {
		if (scoreTextObject != null) {
			score = objects.size();
			scoreTextObject.text = String.format("%06d", score);
		}
	}

	/**
	 * Constrain all object to play zone.
	 */
	private void constrainObjectTo() {
		for (AbstractGameObject o : entities) {
			if (o.x <= 0) {
				o.dx = -Math.signum(o.dx) * o.hSpeed;
				o.x = 1;
			}
			if (o.x >= playZone.getWidth()) {
				o.dx = -Math.signum(o.dx) * o.hSpeed;
				o.x = (int) playZone.getWidth() - 1;
			}
			if (o.y <= 0) {
				o.dy = -Math.signum(o.dy) * o.vSpeed;
				o.y = 1;
			}
			if (o.y >= playZone.getHeight()) {
				o.dy = -Math.signum(o.dy) * o.vSpeed;
				o.y = (int) playZone.getHeight() - 1;
			}
			computeEntityAction(o);
		}
	}

	/**
	 * @param wl
	 * @param wr
	 * @param wt
	 * @param wb
	 */
	private void constrainPlayerTo(float wl, float wr, float wt, float wb) {
		if (player.x < wl)
			player.x = wl;
		if (player.y < wt)
			player.y = wt;
		if (player.x > wr)
			player.x = wr;
		if (player.y > wb)
			player.y = wb;
		if (player.dx != 0) {
			player.dx *= 0.980f;
		}
		if (player.dy != 0) {
			player.dy *= 0.980f;
		}
	}

	/**
	 * Manage collision from Player to other objects.
	 */
	private void manageCollision() {
		List<Sizeable> collisionList = new CopyOnWriteArrayList<>();
		quadTree.retrieve(collisionList, player);
		if (collisionList != null && !collisionList.isEmpty()) {
			for (Sizeable s : collisionList) {
				AbstractGameObject ago = (AbstractGameObject) s;
				if (player.rectangle.intersects(ago.rectangle)) {
					int d = 0;
					if (ago.getClass().equals(Eatable.class)) {
						d = (Integer) ago.attributes.get("power");
						// eat only if energy low.
						if (addValueToAttribute(player, "energy", d, 0, 100)) {
							objects.remove(ago);
						}
					}
					if (ago.getClass().equals(Enemy.class)) {
						addValueToAttribute(player, "energy", -1, 0, 100);
					}
				}
			}
		}
	}

	/**
	 * add <code>d</code> to attribute <code>name</code> from game object
	 * <code>ago</code> and verify <code>min</code> and <code>max</code> value. If
	 * value as been updated, return <code>true</code>, else <code>false</code>.
	 * 
	 * @param ago
	 * @param name
	 * @param d
	 * @param min
	 * @param max
	 * @return
	 */
	private boolean addValueToAttribute(AbstractGameObject ago, String name, int d, int min, int max) {
		if (ago != null && ago.attributes != null && ago.attributes.containsKey(name)) {
			int value = (Integer) ago.attributes.get(name);
			if (value + d < max && value + d > min) {
				value += d;
				player.attributes.put(name, value);
				return true;
			}
		} else {
			logger.error("GameObject {} does not have property named {}", ago.name, name);
		}
		return false;
	}

	private void computeEntityAction(AbstractGameObject o) {
		if (o.dx < 0 || o.dx > 0) {
			o.action = Actions.WALK;
		}
		if (o.dy < 0) {
			o.action = Actions.DOWN;
		}
		if (o.dy > 0) {
			o.action = Actions.DOWN;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.gdj105.statemachine.GameState#render(com.snapgames.gdj.
	 * gdj104.Game, java.awt.Graphics2D)
	 */
	@Override
	public void render(Game game, Graphics2D g) {
		super.render(game, g);

		// display Help if requested
		if (isHelp) {
			displayHelp(game, g, 10, 20, helpFont);
		}

		// Display Pause state
		if (game.isPause()) {
			drawPause(game, g);
		}

	}

	/**
	 * draw the Pause label.
	 * 
	 * @param g
	 */
	private void drawPause(Game game, Graphics2D g) {
		String lblPause = "Pause";

		Font bck = ResourceManager.getFont("font");
		Font f = font.deriveFont(14.0f).deriveFont(Font.ITALIC);

		g.setFont(f);
		RenderHelper.drawShadowString(g, lblPause, Game.WIDTH / 2, Game.HEIGHT / 2, Color.WHITE, Color.BLACK,
				RenderHelper.TextPosition.CENTER, 3);
		g.setFont(bck);
		g.setColor(Color.WHITE);
		g.drawLine((Game.WIDTH / 2) - 30, (Game.HEIGHT / 2) + 2, (Game.WIDTH / 2) + 30, (Game.HEIGHT / 2) + 2);
		g.setColor(Color.BLACK);
		g.drawRect((Game.WIDTH / 2) - 31, (Game.HEIGHT / 2) + 1, 62, 2);

	}

	/**
	 * @return the layers
	 */
	public Layer[] getLayers() {
		return layers;
	}

	/**
	 * Display the Help panel.
	 * 
	 * @param game
	 * @param g
	 * @param x
	 * @param y
	 */
	public void displayHelp(Game game, Graphics2D g, int x, int y, Font helpFont) {
		g.setFont(helpFont);
		g.setColor(Color.WHITE);
		String[] text = { "[" + RenderHelper.showBoolean(layers[0].active) + "] 1: show/hide layer 1",
				"[" + RenderHelper.showBoolean(layers[1].active) + "] 2: show/hide layer 2",
				"[" + RenderHelper.showBoolean(layers[2].active) + "] 3: show/hide layer 3",
				"[" + game.getDebug() + "] D: display debug info",
				"[" + RenderHelper.showBoolean(game.isPause()) + "] P/PAUSE: pause the computation",
				"[" + RenderHelper.showBoolean(isHelp) + "] H: display this help", "   CTRL+S: save a screenshot",
				"   Q/ESCAPE: Escape the demo" };
		// TODO Adapt text from i18n messages
		String[] text2 = { "" };

		RenderHelper.display(g, x, y, debugFont, text);
	}

	@Override
	public void keyReleased(Game game, KeyEvent e) {
		super.keyReleased(game, e);
		int nbElem = 10;
		if (e.isControlDown()) {
			nbElem += 50;
		}
		if (e.isShiftDown()) {
			nbElem += 100;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_PAUSE:
		case KeyEvent.VK_P:
			game.requestPause();
			break;
		case KeyEvent.VK_NUMPAD1:
		case KeyEvent.VK_1:
			layers[0].active = !layers[0].active;
			break;
		case KeyEvent.VK_NUMPAD2:
		case KeyEvent.VK_2:
			layers[1].active = !layers[1].active;
			break;
		case KeyEvent.VK_NUMPAD3:
		case KeyEvent.VK_3:
			layers[2].active = !layers[2].active;
			break;
		case KeyEvent.VK_NUMPAD4:
		case KeyEvent.VK_4:
			layers[3].active = !layers[3].active;
			break;
		case KeyEvent.VK_PAGE_UP:
			generateEnemies(nbElem);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			if (score - nbElem >= 0) {
				removeAllObjectOfClass(Enemy.class, nbElem);
				removeAllObjectOfClass(Eatable.class, nbElem);
			}
			break;
		case KeyEvent.VK_BACK_SPACE:
		case KeyEvent.VK_DELETE:
			removeAllObjectOfClass(Enemy.class);
			removeAllObjectOfClass(Eatable.class);
			score = 0;
			break;
		case KeyEvent.VK_H:
			isHelp = !isHelp;
			break;
		}
	}

	private void generateEnemies(int nb) {
		// NPC (layers 3 & 4)
		int halfNb = nb / 2;
		for (int i = 0; i < nb; i++) {

			AbstractGameObject entity = null;
			if (i < halfNb) {
				entity = new Enemy("enemy_" + i)
						.setPosition(((float) Math.random() * Game.WIDTH) + ((Game.WIDTH / 2)),
								((float) Math.random() * Game.HEIGHT) + ((Game.HEIGHT / 2)))
						.setVelocity(((float) Math.random() * 0.05f) - 0.02f, ((float) Math.random() * 0.05f) - 0.02f);
			} else {
				entity = new Eatable("eatable_" + i)
						.setPosition(((float) Math.random() * Game.WIDTH) + ((Game.WIDTH / 2)),
								((float) Math.random() * Game.HEIGHT) + ((Game.HEIGHT / 2)))
						.setVelocity(((float) Math.random() * 0.05f) - 0.02f, ((float) Math.random() * 0.05f) - 0.02f);

			}
			entities.add(entity);
			addObject(entity);
		}

	}

	/**
	 * remove a nbObjectoRemove number of object clazz
	 * 
	 * @param clazz
	 * @param nbObjectToRemove
	 */
	private void removeAllObjectOfClass(Class<? extends AbstractGameObject> clazz, int nbObjectToRemove) {
		List<GameObject> toBeDeleted = new ArrayList<>();
		int idx = nbObjectToRemove;
		for (GameObject o : objects) {
			if (o.getClass().equals(clazz)) {
				toBeDeleted.add(o);
				idx--;
			}
			if (idx <= 0) {
				break;
			}
		}
		objects.removeAll(toBeDeleted);
	}

}
