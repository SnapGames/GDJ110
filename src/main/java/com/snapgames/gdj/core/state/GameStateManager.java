/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ105
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.io.InputHandler;
import com.snapgames.gdj.core.state.factory.GameStateFactory;
import com.snapgames.gdj.core.state.factory.GameStateFactory.StateDefinition;
import com.snapgames.gdj.core.state.factory.NoDefaultStateException;

/**
 * The Game State Manager is the state machine to manage all the states of the
 * game.
 * 
 * @author Frédéric Delorme
 *
 */

public class GameStateManager {
	/**
	 * Internal Logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(GameStateManager.class);

	/**
	 * The factory to instatiate the GameState from the games.xml file.
	 */
	private GameStateFactory gsf = GameStateFactory.getInstance();

	/**
	 * Internal reference to parent game.
	 */
	private Game game;

	/**
	 * The internal buffer to store instances of the available states.
	 */
	private Map<String, GameState> states = new ConcurrentHashMap<>();

	/**
	 * The current active state.
	 */
	private GameState currentState = null;

	/**
	 * Initialize the Game state manager
	 * 
	 * @param game
	 *            the parent game object.
	 */
	public GameStateManager(Game game) {
		this.game = game;
		initialize();
	}

	/**
	 * Initialize internal state.
	 */
	public void initialize() {
		states = new HashMap<>();
		gsf.load();

	}

	/**
	 * Load state to memory and instantiate the corresponding <code>class</code> to
	 * the <code>name</code> of the GameState.
	 * 
	 * @param name
	 *            the name of the GameState to be instantiated.
	 * @return The GameState instantiate corresponding to the <code>name</code>.
	 */
	public AbstractGameState loadState(String name) {
		AbstractGameState state = null;
		try {
			StateDefinition stateDef = gsf.getStateDefintion(name);
			state = stateDef.classState.newInstance();
			logger.info("State named '{}' with class '{}' has been instantiated with success", stateDef.name,
					stateDef.className);
		} catch (InstantiationException | IllegalAccessException | NoDefaultStateException e) {
			logger.error("Unable to instatiate the class for state '{}'", name);
		}
		if (null != state) {
			states.put(name, state);
		}

		return state;
	}

	/**
	 * Add a state to the internal buffer.
	 * 
	 * @param name
	 * @param state
	 */
	public void addState(String name, GameState state) {
		assert (name != null && !name.equals(""));
		assert (state != null);
		assert (states != null);
		states.put(name, state);
		logger.info("add the state '{}' to the cache.", name);
	}

	/**
	 * Activate the specific state with this <code>name</code>.
	 * 
	 * @param name
	 *            the name of the state to activate.
	 */
	public void activate(String name) {
		logger.info("Request state '{}' to be activated", name);
		if (!states.containsKey(name)) {
			loadState(name);
		}
		if (states.containsKey(name)) {
			/*if(currentState!=null) {
				currentState.dispose(game);
			}*/

			currentState = states.get(name);
			currentState.initialize(game);
			logger.info("State '{}' activated with success", name);
		} else {
			logger.error("Unable to load state '{}'", name);
		}
	}

	/**
	 * Delegate input management to the state.
	 * 
	 * @param input
	 *            the input handler to connect to.
	 */
	public void input(InputHandler input) {
		assert (currentState != null);
		assert (input != null);
		currentState.input(game, input);
	}

	/**
	 * Delegate the update process to the current state.
	 * 
	 * @param dt
	 *            elapsed time since previous call.
	 */
	public void update(long dt) {
		currentState.update(game, dt);
	}

	/**
	 * Delegate the rendering process to the current state.
	 * 
	 * @param g
	 *            The Graphics2D interface to render things !
	 */
	public void render(Graphics2D g) {
		currentState.render(game, g);
	}

	public void dispose() {

		for (Entry<String, GameState> gs : states.entrySet()) {
			gs.getValue().dispose(game);
			logger.info("Remove all states from cache.");
		}
		states.clear();
	}

	/**
	 * return the parent Game
	 * 
	 * @return
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Key typed event intercepter
	 * 
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {
		currentState.keyTyped(this.getGame(), e);
	}

	/**
	 * key pressed event intercepter
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		currentState.keyPressed(this.getGame(), e);
	}

	/**
	 * key released event intercepter
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		currentState.keyReleased(this.getGame(), e);
	}

	public void activateDefaultState() {
		try {
			this.activate(gsf.getDefault().name);
		} catch (NoDefaultStateException e) {
			logger.error("Unable to get the default state", e);
		}

	}

	/**
	 * @return the currentState
	 */
	public GameState getCurrentState() {
		return currentState;
	}
}
