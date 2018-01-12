/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ106
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.entity;

import java.awt.Graphics2D;
import java.util.List;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.collision.Sizeable;

/**
 * <p>
 * The <code>GameObject</code> interface define all the needed actions for an
 * object to be maintained and managed by this Game engine.
 * </p>
 * 
 * <p>
 * Here is the list of the main actions for this interface:
 * <ul>
 * <li><code>{@link GameObject#update}</code> update the status and attributes
 * of the object, according to its own specificities and interaction with other
 * objects,</li>
 * <li><code>{@link GameObject#draw}</code> draw this entity, if the default
 * draw methods from {@link AbstractGameObject} does no fit,</li>
 * <li><code>{@link GameObject#getName}</code> return the name of this
 * object,</li>
 * <li><code>{@link GameObject#getLayer}</code> return the display layer of this
 * object,</li>
 * <li><code>{@link GameObject#getPriority}</code> return the priority of this
 * object in the layer, for update and rendering purpose.</li>
 * <li><code>{@link GameObject#getScale}</code> use to set the scaling of the
 * object (for drawing purpose only).</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Other methods must be implemented (or just rely in the
 * {@link AbstractGameObject} ones) to define some new debug information.
 * <ul>
 * <li><code>{@link GameObject#addDebugInfo}</code> add new or replace existing
 * debug information.</li>
 * <li><code>{@link GameObject#drawSpecialDebugInfo}</code> overwrite debug
 * information drawing rpocedure (see {@link CameraObject} for sample
 * implementation}.</li>
 * </ul>
 * </p>
 * 
 * @author Frédéric Delorme
 *
 */
public interface GameObject extends Sizeable {

	/**
	 * Update object position, physic and potentially all attributes of this object
	 * according to its needs.
	 * 
	 * @param game
	 *            the parent game animating this object
	 * @param dt
	 *            the elapsed time from last call.
	 */
	void update(Game game, long dt);

	/**
	 * Draw this object, according to his attributes and position, velocity, etc...
	 * 
	 * @param game
	 *            the parent game animating this object
	 * @param g
	 *            Graphics interface to draw anything (-see {@link Graphics2D}
	 */
	void draw(Game game, Graphics2D g);

	/**
	 * return the name of this object.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * return the layer this object belongs to.
	 * 
	 * @return
	 */
	int getLayer();

	/**
	 * return the rendering priority for the layer this object belongs to.
	 * 
	 * @return
	 */
	int getPriority();

	/**
	 * Add some debug information to display if needed.
	 * 
	 * @return
	 */
	void addDebugInfo();

	List<String> getDebugInfo();

	/**
	 * Return the object scale factor.
	 * 
	 * @return
	 */
	float getScale();

	/**
	 * Does the debug info must be displayed for this object ?
	 * 
	 * @return
	 */
	boolean isDebugInfoDisplayed();

	/**
	 * If an object need to add specific debug information at display time, this
	 * method can be overridden.
	 * 
	 * @param game
	 *            the parent game.
	 * @param g
	 *            the Graphics interface to render things.
	 */
	void drawSpecialDebugInfo(Game game, Graphics2D g);
}