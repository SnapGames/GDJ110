/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj107
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.ResourceManager;

/**
 * The {@link Window} class to contains and display all the game.
 * 
 * @author Frédéric Delorme
 *
 */
public class Window extends JFrame {

	/**
	 * Internal serial version UID.
	 */
	private static final long serialVersionUID = 3295181469904415221L;

	/**
	 * The default unique constructor to initialize a {@link Window} on the
	 * <code>game</code>.
	 * 
	 * @param game
	 *            the game to display in.
	 */
	public Window(Game game) {
		super(game.getTitle());
		// set game size.
		game.setDimension(Game.WIDTH, Game.HEIGHT, Game.SCALE);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(game);
		setLayout(new BorderLayout());
		setSize(game.getDimension());
		setPreferredSize(game.getDimension());
		setMaximumSize(game.getDimension());
		setResizable(false);

		// center window on default display monitor.
		setLocationRelativeTo(null);

		// set window icon.
		setIconImage(ResourceManager.getImage("/res/icons/gdj-app.png"));

		// add the Game InputHandler as a KeyListener
		addKeyListener(game.getInputHandler());

		pack();
		setVisible(true);
		game.setWindow(this);
	}
}
