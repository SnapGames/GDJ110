/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj105
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

import com.snapgames.gdj.core.state.GameStateManager;

/**
 * InputHandler
 * 
 * @author Frédéric Delorme
 *
 */
public class InputHandler implements KeyListener {

	boolean[] keys = new boolean[256];
	boolean[] keysPrevious = new boolean[256];

	Stack<KeyEvent> events = new Stack<>();
	private GameStateManager gsm;

	public InputHandler(GameStateManager gsm) {
		this.gsm = gsm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		gsm.keyTyped(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		events.push(e);
		if (e.getKeyCode() < 256 && e.getKeyCode() >= 0) {
			keysPrevious[e.getKeyCode()] = keys[e.getKeyCode()];
			keys[e.getKeyCode()] = true;
		}
		gsm.keyPressed(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < 256 && e.getKeyCode() >= 0) {
			keysPrevious[e.getKeyCode()] = keys[e.getKeyCode()];
			keys[e.getKeyCode()] = false;
		}
		gsm.keyReleased(e);

	}

	/**
	 * return the status pressed for the key <code>k</code>.
	 * 
	 * @param k
	 *            the KeyEvent.VK_xxx code.
	 * @return true if key is pressed.
	 */
	public boolean getKeyPressed(int k) {
		assert (k >= 0 && k < 256);
		return keys[k];
	}

	/**
	 * return the status released for the key <code>k</code>.
	 * 
	 * @param k
	 *            the KeyEvent.VK_xxx code.
	 * @return true if key is released.
	 */
	public boolean getKeyReleased(int k) {
		assert (k >= 0 && k < 256);
		return keys[k] && !keysPrevious[k];
	}

	public KeyEvent getEvent() {
		if (!events.isEmpty()) {
			return events.pop();
		} else {
			return null;
		}
	}
}
