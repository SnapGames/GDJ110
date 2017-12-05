/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj106
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.ResourceManager;
import com.snapgames.gdj.core.gfx.RenderHelper;

/**
 * AbstractGameObject
 * 
 * @author Frédéric Delorme
 *
 */
public class AbstractGameObject implements GameObject {
	/**
	 * internal indexCounter to generate the default object name.
	 */
	private static int indexCounter = 0;
	public int index;

	/**
	 * default internal constants.
	 */
	/**
	 * default width for any object
	 */
	public final static int DEFAULT_WIDTH = 16;
	/**
	 * default height for any object
	 */
	public final static int DEFAULT_HEIGHT = 16;
	/**
	 * default horizontal speed for any object
	 */
	public final static float DEFAULT_HSPEED = 0.6f;
	/**
	 * default vertical speed for any object
	 */
	public final static float DEFAULT_VSPEED = 0.2f;
	/**
	 * Name of this object.
	 */
	public String name;
	/**
	 * position of this object onto the display space.
	 */
	public float x = 0, y = 0;

	/**
	 * default speed for this object.
	 */
	public float vSpeed, hSpeed;

	/**
	 * Velocity of this object.
	 */
	public float dx = 0, dy = 0;
	/**
	 * Size of this object.
	 */
	public int width = 32, height = 32;

	public float scale = 1.0f;

	public Rectangle rectangle;

	public Actions action = Actions.IDLE;

	public Direction direction = Direction.NONE;

	public Map<String, Object> attributes = new ConcurrentHashMap<>();

	public Point2D offsetInfo;

	public boolean showDebuginfo;

	/**
	 * Rendering depth and priority.
	 */
	public int layer = 0, priority = 1;

	public Color color = Color.GREEN;

	/**
	 * Debug info if needed.
	 */
	protected List<String> debugInfo = new ArrayList<>();

	/**
	 * internal debug font.
	 */
	private Font debugFont;

	/**
	 * Default constructor for this AbstractGameObject.
	 */
	public AbstractGameObject() {
		super();
		indexCounter++;
		index = indexCounter;
		debugFont = ResourceManager.getFont("debugFont");

	}

	/**
	 * Create a AbstractGameObject with <code>name</code>, position
	 * (<code>x</code>,<code>y</code>) with a velocity of
	 * (<code>dx</code>,<code>dy</code>).
	 * 
	 * @param name
	 *            the name for this object.
	 * @param x
	 *            x position in the (x,y) for this object
	 * @param y
	 *            y position in the (x,y) for this object
	 * @param dx
	 *            velocity on x direction
	 * @param dy
	 *            velocity on y direction.
	 */
	protected AbstractGameObject(String name, int x, int y, int dx, int dy) {
		this();
		// if name is null, generate a default name.
		this.name = (name == null || name.equals("") ? "noname_" + indexCounter : name);
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;
		this.hSpeed = DEFAULT_HSPEED;
		this.vSpeed = DEFAULT_VSPEED;
		this.rectangle = new Rectangle(x, y, width, height);
	}

	/**
	 * First call on building a new GameObject with the Chained API.
	 * 
	 * @param name
	 *            Name of this new object.
	 */
	public AbstractGameObject(String name) {
		this(name, 0, 0, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.GameObject#update(com.snapgames.gdj.core .Game,
	 * long)
	 */
	@Override
	public void update(Game game, long dt) {
		// compute basic physic mechanic
		x += dx * dt;
		y += dy * dt;

		// limit speed
		if (Math.abs(dx) < 0.005) {
			dx = 0.0f;
		}
		if (Math.abs(dy) < 0.005) {
			dy = 0.0f;
		}
		rectangle.setBounds((int) x, (int) y, width, height);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.GameObject#draw(com.snapgames.gdj.core. Game,
	 * java.awt.Graphics2D)
	 */
	@Override
	public void draw(Game game, Graphics2D g) {
		g.setColor(color);
		g.fillRect((int) x, (int) y, width, height);
		// Extended object will use their own draw process.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.GameObject#drawSpecialDebugInfo(com.snapgames.
	 * gdj.core.Game, java.awt.Graphics2D)
	 */
	public void drawSpecialDebugInfo(Game game, Graphics2D g) {

		RenderHelper.drawDebugInfoObject(g, this, debugFont, game.getDebug());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractGameObject [name=").append(name).append(", x=").append(x).append(", y=").append(y)
				.append(", vSpeed=").append(vSpeed).append(", hSpeed=").append(hSpeed).append(", dx=").append(dx)
				.append(", dy=").append(dy).append(", width=").append(width).append(", height=").append(height)
				.append(", layer=").append(layer).append(", priority=").append(priority).append(", color=")
				.append(color).append("]");
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.GameObject#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.GameObject#getLayer()
	 */
	@Override
	public int getLayer() {
		return layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.GameObject#getPriority()
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.entity.GameObject#addDebugInfo()
	 */
	@Override
	public void addDebugInfo() {
		debugInfo.clear();
		debugInfo.add(name);
		debugInfo.add(String.format("pos:(%4.0f,%4.0f)", x, y));
		debugInfo.add(String.format("spd:(%4.2f,%4.2f)", dx, dy));
		debugInfo.add(String.format("lyr,prio(:(%d,%d)", layer, priority));
		debugInfo.add(String.format("action:(%s)", action));
		debugInfo.add(String.format("dir:(%s)", direction));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.entity.GameObject#getDebugInfo()
	 */
	@Override
	public List<String> getDebugInfo() {
		return debugInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.collision.Sizeable#getX()
	 */
	@Override
	public float getX() {
		return x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.collision.Sizeable#getY()
	 */
	@Override
	public float getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.collision.Sizeable#getWidth()
	 */
	@Override
	public float getWidth() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.collision.Sizeable#getHeight()
	 */
	@Override
	public float getHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.entity.GameObject#getScale()
	 */
	@Override
	public float getScale() {
		return scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.entity.GameObject#isDebugInfoDisplayed()
	 */
	@Override
	public boolean isDebugInfoDisplayed() {
		return showDebuginfo;
	}

	/*---------------------- Chaining API ----------------------*/

	/**
	 * Set position fpr this object.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public AbstractGameObject setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Set position fpr this object.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public AbstractGameObject setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Set size for this Object.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public AbstractGameObject setSize(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	/**
	 * Set the color for this object.
	 * 
	 * @param color
	 * @return
	 */
	public AbstractGameObject setColor(Color color) {
		this.color = color;
		return this;
	}

	/**
	 * Set layer;
	 * 
	 * @param layer
	 * @return
	 */
	public AbstractGameObject setLayer(int layer) {
		this.layer = layer;
		return this;
	}

	/**
	 * Set priority;
	 * 
	 * @param layer
	 * @return
	 */
	public AbstractGameObject setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	/**
	 * Set he velocity for this object;
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	public AbstractGameObject setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
		return this;
	}

	/**
	 * Set the initial velocity for this object.
	 * 
	 * @param hSpeed
	 * @param vSpeed
	 * @return
	 */
	public AbstractGameObject setInitialVelocity(float hSpeed, float vSpeed) {
		this.hSpeed = hSpeed;
		this.vSpeed = vSpeed;
		return this;
	}

	/**
	 * Set the scale for this object;
	 * 
	 * @param scale
	 * @return
	 */
	public AbstractGameObject setScale(float scale) {
		this.scale = scale;
		return this;
	}

	/**
	 * Add a new attribute to this object.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public AbstractGameObject addAttribute(String name, Object value) {
		this.attributes.put(name, value);
		return this;
	}

	/**
	 * retrieve an attribute <code>name</code> in this object.
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	/**
	 * Set the font to render number of items in this container.
	 * 
	 * @param derivedFont
	 * @return
	 */
	public AbstractGameObject setDebugFont(Font debugFont) {
		this.debugFont = debugFont;
		return this;
	}

}
