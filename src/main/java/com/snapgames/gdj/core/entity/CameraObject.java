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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.gfx.DebugLevel;
import com.snapgames.gdj.core.state.AbstractGameState;

/**
 * compute and move the point of view to the Camera coordinates.
 * 
 * @author Frédéric Delorme.
 *
 */
public class CameraObject extends AbstractGameObject {

	private GameObject target;
	private float tween = 1.0f;
	private float margin = 0.02f;
	private float inverseMargin = 1 - margin;
	int cWidth = (int) (width * inverseMargin);
	int cHeight = (int) (height * inverseMargin);

	/**
	 * Default CameraObject constructor.
	 */
	public CameraObject() {
		super();
	}

	/**
	 * Initialize the Camera object with a <code>name</code>, a <code>target</code>
	 * to follow and a <code>tween</code> factor to compute camera reaction speed.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param layer
	 * @param priority
	 * @param color
	 */
	public CameraObject(String name, GameObject target, float tween) {
		super(name, 0,0,0,0);
		this.target = target;
		this.tween = tween;
		initDefaultValues();
	}

	/**
	 * 
	 */
	protected void initDefaultValues() {
		this.inverseMargin = 1 - margin;
		setSize(Game.WIDTH, Game.HEIGHT);
		setLayer(1);
		setPriority(100);
		setColor(Color.ORANGE);
	}

	/**
	 * @param name
	 * @param x
	 * @param y
	 * @param dx
	 * @param dy
	 */
	public CameraObject(String name, int x, int y, int dx, int dy) {
		super(name, x, y, dx, dy);
	}

	/**
	 * @param string
	 */
	public CameraObject(String name) {
		super(name, 0, 0, 0, 0);
		initDefaultValues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.AbstractGameObject#update(com.snapgames.gdj.
	 * core.Game, long)
	 */
	@Override
	public void update(Game game, long dt) {
		if (target != null) {
			this.x += ((target.getX()) - (Game.WIDTH / 2) - x) * tween * dt;
			this.y += ((target.getY()) - (Game.HEIGHT / 2) - y) * tween * dt;
		}

		rectangle.x = (int) x - 16;
		rectangle.y = (int) y - 16;
		rectangle.width = (int) width + 32;
		rectangle.height = (int) height + 32;

	}

	/**
	 * Camera debug info are drawn at the point with coordinates (0,0), because it's
	 * all other objects that are moved according to camera position (see the method
	 * from the render of AbstractGameState object.
	 * 
	 * @see AbstractGameState#render(Game,Graphics2D)
	 * @see com.snapgames.gdj.core.entity.AbstractGameObject#draw(com.snapgames.gdj.core.Game,
	 *      java.awt.Graphics2D)
	 */
	@Override
	public void draw(Game game, Graphics2D g) {
		if (game.isDebug(DebugLevel.DEBUG_FPS_BOX.ordinal())) {
			g.setColor(Color.GREEN);
			g.drawRect(16, 16, rectangle.width - 64, height - 48);
			g.drawString(name, 16, 16);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.entity.AbstractGameObject#addDebugInfo()
	 */
	@Override
	public void addDebugInfo() {
		debugInfo.clear();
		offsetInfo = new Point2D.Float(-10.0f, -10.0f);
		debugInfo.add(String.format("trgt:[%s]", target.getName()));
		debugInfo.add(String.format("post:[%04.2f,%04.2f]", x, y));
		debugInfo.add(String.format("size:[%d,%d]", width, height));
	}

	/**
	 * @return the margin
	 */
	public float getMargin() {
		return margin;
	}

	/**
	 * @param margin
	 *            the margin to set
	 */
	public void setMargin(float margin) {
		this.margin = margin;
	}

	/**
	 * @return the inverseMargin
	 */
	public float getInverseMargin() {
		return inverseMargin;
	}

	/**
	 * @param inverseMargin
	 *            the inverseMargin to set
	 */
	public void setInverseMargin(float inverseMargin) {
		this.inverseMargin = inverseMargin;
	}

	/**
	 * Set the camera target to follow.
	 * 
	 * @param player
	 * @return
	 */
	public CameraObject setTarget(GameObject target) {
		this.target = target;
		// setPosition((int)target.getX(), (int)target.getY());
		return this;
	}

	/**
	 * Set the tween factor for the camera following delay to the target.
	 * 
	 * @param tween
	 * @return
	 */
	public CameraObject setTweenFactor(float tween) {
		this.tween = tween;
		return this;
	}

}
