/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ104
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.snapgames.gdj.core.entity.AbstractGameObject;
import com.snapgames.gdj.core.entity.GameObject;

/**
 * This class is a Render helper class to draw some shiny things.
 * 
 * @author Frédéric Delorme
 *
 */
public class RenderHelper {

	/**
	 * An internal Text POsition enumeration to provide a justification pattern.
	 * 
	 * @author Frédéric Delorme
	 *
	 */
	public enum TextPosition {
		LEFT, RIGHT, CENTER
	}

	public static void display(Graphics2D g, int x, int y, Font f, Object[] objects) {
		String[] helps = new String[objects.length];
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int maxWidth = 0;
		for (Object o : objects) {
			int strWidth = g.getFontMetrics().stringWidth(o.toString());
			if (strWidth > maxWidth) {
				maxWidth = strWidth;
			}

		}
		g.setColor(new Color(.5f, .5f, .5f, .3f));
		g.fillRect(x - 10, y - fm.getHeight(), (maxWidth + 2 * 16), (objects.length + 1) * fm.getHeight());
		g.setColor(Color.WHITE);

		int i = 0;
		for (Object o : objects) {
			helps[i] = o.toString();
			drawShadowString(g, helps[i], x + 5, y + i * fm.getHeight(), Color.WHITE, Color.BLACK, TextPosition.LEFT,
					2);
			i++;
		}

	}

	public static String showBoolean(boolean b) {

		return showBoolean(b, "X", "_");
	}

	public static String showBoolean(boolean b, String on, String off) {

		return (b ? on : off);
	}

	/**
	 * Display String with front and back color.
	 * 
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param front
	 * @param back
	 */
	public static void drawShadowString(Graphics2D g, String text, int x, int y, Color front, Color back) {
		drawShadowString(g, text, x, y, front, back, TextPosition.LEFT);
	}

	/**
	 * Display String with front and back color at x,y positioning the text
	 * according to TextPosition.
	 * 
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param front
	 * @param back
	 * @param txtPos
	 */
	public static void drawShadowString(Graphics2D g, String text, int x, int y, Color front, Color back,
			TextPosition txtPos) {
		drawShadowString(g, text, x, y, front, back, txtPos, 1);
	}

	/**
	 * Display String with front and back color at x,y positioning the text
	 * according to TextPosition. The back color will be drawn with a border.
	 * 
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param front
	 * @param back
	 * @param border
	 */
	public static Rectangle drawShadowString(Graphics2D g, String text, int x, int y, Color front, Color back,
			TextPosition txtPos, int border) {
		int textWidth = g.getFontMetrics().stringWidth(text);
		int textHeight = g.getFontMetrics().getHeight();
		int dx = 0;
		switch (txtPos) {
		case LEFT:
			break;
		case RIGHT:
			dx = -textWidth;
			break;
		case CENTER:
			dx = -(textWidth / 2);
			break;
		}
		g.setColor(back);
		for (int i = 0; i < border; i++) {
			for (int ix = -i; ix < i + 1; ix++) {
				for (int iy = -i; iy < i + 1; iy++) {
					g.drawString(text, x+dx - ix, y + iy);
				}
			}
		}
		g.setColor(front);
		g.drawString(text, x+dx, y);
		return new Rectangle(x+dx, y, textWidth, textHeight);
	}

	/**
	 * Display debug information for the game Object.
	 * 
	 * @param g
	 *            the graphic interface to use to draw things
	 * @param o
	 *            the object to be debugged.
	 */
	public static void drawDebugInfoObject(Graphics2D g, GameObject o, Font f, int debugLevel) {

		AbstractGameObject ago = (AbstractGameObject) o;
		g.setFont(f);
		int fontHeight = g.getFontMetrics().getHeight();

		int pane_padding = 4;
		int pane_width = 60;
		int pane_height = 40;

		List<String> lines = new ArrayList<>();
		o.addDebugInfo();
		lines.addAll(o.getDebugInfo());
		for (int i = 0; i < lines.size(); i++) {
			pane_width = (g.getFontMetrics().stringWidth(lines.get(i) + pane_padding) > pane_width
					? g.getFontMetrics().stringWidth(lines.get(i)) + (pane_padding * 2)
					: pane_width);
		}
		pane_height = lines.size() * fontHeight + fontHeight / 2;

		int pane_x=0, pane_y=0;
		int link = 2;
		if (ago.offsetInfo != null) {
			pane_x = (int) ago.offsetInfo.getX();
			pane_y = (int) ago.offsetInfo.getY();
		} else {
			pane_x = (int) (ago.rectangle.x + ago.width + pane_padding);
			pane_y = (int) (ago.rectangle.y + ago.height + pane_padding);
		}
		if (o.getScale() != 1.0f) {
			g.scale(o.getScale(), o.getScale());
		}
		if (debugLevel >= DebugLevel.DEBUG_FPS_BOX.ordinal()) {
			g.setColor(Color.YELLOW);
			g.drawRect((int) ago.rectangle.x, (int) ago.rectangle.y, ago.rectangle.width, ago.rectangle.height);
			g.drawString("" + ago.index, (int) ago.rectangle.x, (int) ago.rectangle.y);
		}
		if (debugLevel >= DebugLevel.DEBUG_FPS_BOX_DIRECTION.ordinal()) {
			g.setColor(Color.GREEN);
			switch (ago.direction) {
			case UP:
				g.drawLine((int) ago.rectangle.x, (int) ago.rectangle.y, (int) ago.rectangle.x + ago.rectangle.width, (int) ago.rectangle.y);
				break;
			case LEFT:
				g.drawLine((int) ago.rectangle.x, (int) ago.rectangle.y + (int) ago.rectangle.height, (int) ago.rectangle.x, (int) ago.rectangle.y);
				break;
			case RIGHT:
				g.drawLine((int) ago.rectangle.x + (int) ago.rectangle.width, (int) ago.rectangle.y + (int) ago.rectangle.height, (int) ago.rectangle.x + (int) ago.rectangle.width,
						(int) ago.rectangle.y);
				break;
			case DOWN:
				g.drawLine((int) ago.rectangle.x, (int) ago.rectangle.y + (int) ago.rectangle.height, (int) ago.rectangle.x + ago.rectangle.width,
						(int) ago.rectangle.y + (int) ago.rectangle.height);
				break;
			case NONE:
				break;
			}
		}
		if (debugLevel >= DebugLevel.DEBUG_FPS_BOX_DIRECTION_ATTRS.ordinal()) {
			g.setColor(new Color(0.5f, .5f, .5f, .6f));
			g.fillRect(pane_x + link, pane_y + link, pane_width, pane_height);

			g.setColor(new Color(0.8f, .8f, .8f, .8f));
			g.drawRect(pane_x + link, pane_y + link, pane_width, pane_height);

			g.setColor(Color.GREEN);
			g.drawLine((int) ago.rectangle.x + ago.rectangle.width, (int) ago.rectangle.y + ago.rectangle.height, (int) pane_x + link, pane_y + link);
			for (int i = 0; i < lines.size(); i++) {
				g.drawString(lines.get(i), pane_x + link + pane_padding, pane_y + link + (i + 1) * fontHeight);
			}
		}
		if (o.getScale() != 1.0f) {
			g.scale(1 / o.getScale(), 1 / o.getScale());
		}
	}
}
