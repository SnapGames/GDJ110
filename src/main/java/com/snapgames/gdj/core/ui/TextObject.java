/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj105
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.AbstractGameObject;
import com.snapgames.gdj.core.gfx.RenderHelper;
import com.snapgames.gdj.core.gfx.RenderHelper.TextPosition;

/**
 * A textObject is a UI element to display Text.
 * 
 * @author Frédéric Delorme
 *
 */
public class TextObject extends AbstractGameObject {

	public String text;
	private Font writeFont;
	private Color frontColor;
	private Color shadowColor;
	private int shadowBold;
	private Color backgroundColor;
	private TextPosition textPosition;

	public TextObject() {
		super();
		initDefaultValues();
	}

	public TextObject(String name) {
		super(name, 0, 0, 0, 0);
		initDefaultValues();
	}

	/**
	 * 
	 */
	protected void initDefaultValues() {
		text = "notext";
		frontColor = Color.WHITE;
		backgroundColor = null;
		this.shadowBold = 1;
		shadowColor = Color.BLACK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.AbstractGameObject#draw(com.snapgames.gdj.
	 * gdj105.core. Game, java.awt.Graphics2D)
	 */
	@Override
	public void draw(Game game, Graphics2D g) {
		if (writeFont != null) {
			g.setFont(writeFont);
		}
		FontMetrics fm = g.getFontMetrics();
		this.width = fm.stringWidth(this.text);
		this.height = fm.getHeight();

		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect((int) x - 2, (int) y + 2, width + 4, height + 4);
		}

		Rectangle rect = RenderHelper.drawShadowString(g, text, (int) x, (int) y + height - 2, frontColor, shadowColor,
				(textPosition != null ? textPosition : TextPosition.LEFT), shadowBold);
		rectangle.x = (int) (rect.x < rectangle.x ? rect.x : rectangle.x);
		rectangle.y = (int) (y);
		rectangle.width = (int) (rect.width > width ? rect.width : width);
		rectangle.height = fm.getHeight();
		rectangle.width = width = (fm.stringWidth(text) > width ? fm.stringWidth(text) : width);
	}

	public void addDebugInfo() {
		super.addDebugInfo();
		debugInfo.add(String.format("text:(%s)", text));
	}

	/*---------------------- Chaining API ----------------------*/

	/**
	 * Set the text value for this TextObject.
	 * 
	 * @param text
	 * @return
	 */
	public TextObject setText(String text) {
		this.text = text;
		return this;
	}

	
	/**
	 * Set the text value for this TextObject.
	 * 
	 * @param text
	 * @return
	 */
	public TextObject setFont(Font font) {
		this.writeFont= font;
		return this;
	}
	
	public TextObject setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public TextObject setFrontColor(Color frontColor) {
		this.frontColor = frontColor;
		return this;
	}

	public TextObject setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
		return this;
	}

	public TextObject setShadowBold(int shadowBold) {
		this.shadowBold = shadowBold;
		return this;
	}

	/**
	 * @param center
	 * @return
	 */
	public TextObject setTextAlign(TextPosition textPosition) {
		this.textPosition = textPosition;
		return this;
	}

}
