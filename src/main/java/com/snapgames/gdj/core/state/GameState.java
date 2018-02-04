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

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.CameraObject;
import com.snapgames.gdj.core.io.InputHandler;

/**
 * An interface to design all states
 */
public interface GameState {

	/**
	 * Return the name of this state.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Initialize the state on the game.
	 *
	 * @param game
	 *            the game parent of this state.
	 *
	 */
	void initialize(Game game);

	/**
	 * manage input in this State.
	 * 
	 * @param game
	 *            the parent game of this state
	 * @param input
	 *            the input handler
	 */
	void input(Game game, InputHandler input);

	/**
	 * update the GameState
	 * 
	 * @param game
	 *            the parent game of this state
	 * @param dt
	 *            the elapsed time since previous call.
	 */
	void update(Game game, long dt);

	/**
	 * Rendering this state
	 * 
	 * @param game
	 * @param g
	 */
	void render(Game game, Graphics2D g);

	/**
	 * Free some resources and close the state.
	 * 
	 * @param game
	 */
	void dispose(Game game);

	/**
	 * Intercept Key typed Event
	 * 
	 * @param game
	 * @param e
	 */
	void keyTyped(Game game, KeyEvent e);

	/**
	 * Intercept Key pressed Event
	 * 
	 * @param game
	 * @param e
	 */
	void keyPressed(Game game, KeyEvent e);

	/**
	 * Intercept Key released Event
	 * 
	 * @param game
	 * @param e
	 */
	void keyReleased(Game game, KeyEvent e);

	/**
	 * return the coirrent active trackedObject object.
	 * 
	 * @return
	 */
	CameraObject getDefaultCamera();

}