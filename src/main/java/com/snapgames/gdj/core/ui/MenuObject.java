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
import java.util.ArrayList;
import java.util.List;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.AbstractGameObject;
import com.snapgames.gdj.core.gfx.RenderHelper;
import com.snapgames.gdj.core.gfx.RenderHelper.TextPosition;
import com.snapgames.gdj.core.ui.i18n.Messages;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public class MenuObject extends AbstractGameObject {

	private int index = 0;

	/**
	 * Menu item element.
	 * 
	 * @author Frédéric Delorme
	 *
	 */
	public class MenuItem {
		/*
		 * this element item id.
		 */
		private int id;

		/**
		 * Displayed Label value
		 */
		private String label;

		/**
		 * Label key from translated message.
		 */
		private String labelKey;

		/**
		 * item value returned on item selection.
		 */
		private String value;

		/**
		 * 
		 * @param labelKey
		 * @param value
		 */
		public MenuItem(int id, String labelKey, String value, String defaultText) {
			this(id, labelKey, value, defaultText, (Object[]) null);
		}

		/**
		 * 
		 * @param labelKey
		 * @param value
		 * @param args
		 */
		public MenuItem(int id, String labelKey, String value, String defaultText, Object... args) {
			this.labelKey = labelKey;
			this.value = value;
			String translatedLabel = Messages.getString(labelKey);
			if (translatedLabel.contains(labelKey) && defaultText != null && !defaultText.equals("")) {
				this.label = defaultText;
			} else {
				this.label = String.format(translatedLabel, args);
			}
			this.id = index++;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * 
		 * @return
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 
		 * @return
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * 
		 * @param args
		 * @return
		 */
		public String getLabel(Object... args) {
			return String.format(Messages.getString(labelKey), args);
		}

		/**
		 * Set value according to list of arguments.
		 * 
		 * @param args
		 */
		public void setLabelArgs(Object... args) {
			label = String.format(Messages.getString(labelKey), args);
		}

		/**
		 * Set value of the MenuItem
		 * 
		 * @param value
		 */
		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * active menu item.
	 */
	private int activeItem = 0;
	/**
	 * Shadow color for those items.
	 */
	private Color shadowColor = Color.BLACK;
	/**
	 * List of Items for this menu.
	 */
	private List<MenuItem> items = new ArrayList<>();
	private Font menuFont;
	private TextPosition textPosition;

	/**
	 * Create a Menu object with name.
	 * 
	 * @param name
	 */
	public MenuObject(String name) {
		super(name, 0, 0, 0, 0);
	}

	/**
	 * Add a new Item to the menu.
	 * 
	 * @param value
	 *            value to return if this item is selected
	 * @param labelKey
	 *            label key in the translated text (see messages.properties)
	 * @param text
	 *            the default text to draw if the labelKey does not exists. if this
	 *            text is empty, will display the labelKey;
	 */
	public MenuItem addItem(String value, String labelKey, String defaultText, Object... args) {
		MenuItem item = new MenuItem(index++, labelKey, value, defaultText, args);
		items.add(item);
		return item;
	}

	/**
	 * 
	 * @param value
	 * @param labelKey
	 * @param defaultText
	 */
	public MenuItem addItem(String value, String labelKey, String defaultText) {
		MenuItem item = new MenuItem(index++, labelKey, value, defaultText);
		items.add(item);
		computeSize();
		return item;
	}

	/**
	 * Compute new size after any geometric change
	 */
	private void computeSize() {

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
		// TODO Auto-generated method stub
		super.update(game, dt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.AbstractGameObject#draw(com.snapgames.gdj.core.
	 * Game, java.awt.Graphics2D)
	 */
	@Override
	public void draw(Game game, Graphics2D g) {
		g.setFont(menuFont);
		FontMetrics fm = g.getFontMetrics();
		height = 0;
		int i = 0;
		Color drawColor = color;
		for (MenuItem item : items) {
			if (i != activeItem) {
				drawColor = Color.GRAY;
			} else {
				drawColor = color;
			}
			Rectangle rect = RenderHelper.drawShadowString(g, item.getLabel(), (int) x, (int) y + (i * fm.getHeight()),
					drawColor, shadowColor, (textPosition != null ? textPosition : TextPosition.LEFT), 2);
			i++;
			// update rectangle Bounding Box for this object.
			rectangle.x = (int) (rect.x < rectangle.x ? rect.x : rectangle.x);
			rectangle.y = (int) (y - fm.getHeight());
			rectangle.width = (int) (rect.width > width ? rect.width : width);
			rectangle.height = i * fm.getHeight();
			rectangle.width = width = (fm.stringWidth(item.getLabel()) > width ? fm.stringWidth(item.getLabel())
					: width);
		}
	}

	/**
	 * Activate the itemID menu item.
	 * 
	 * @param itemId
	 */
	public void activate(int itemId) {
		assert (activeItem < 0);
		assert (activeItem >= items.size());

		this.activeItem = itemId;
	}

	/**
	 * switch to previous item of the menu.
	 */
	public void previous() {
		activeItem--;
		if (activeItem < 0) {
			activeItem = items.size() - 1;
		}
	}

	/**
	 * switch to next item in the menu.
	 */
	public void next() {
		activeItem++;
		if (activeItem >= items.size()) {
			activeItem = 0;
		}
	}

	/**
	 * return the current active menu item.
	 * 
	 * @return
	 */
	public MenuItem getActiveItem() {
		return items.get(activeItem);
	}

	/**
	 * Set the active item in the menu.
	 * 
	 * @param item
	 * @return
	 */
	public MenuObject setActiveItem(int item) {
		this.activeItem = item;
		return this;
	}

	/**
	 * Set hte default background color for the menu.
	 * 
	 * @param backgroundColor
	 * @return
	 */
	public MenuObject setBackgroundColor(Color backgroundColor) {
		Color backgroundColor1 = backgroundColor;
		return this;
	}

	/**
	 * set the shadow color fo this menu.
	 * 
	 * @param shadowColor
	 * @return
	 */
	public MenuObject setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
		return this;
	}

	/**
	 * Seth how to align text in the menu.
	 * 
	 * @param align
	 * @return
	 */
	public MenuObject setAlignText(TextPosition align) {
		this.textPosition = align;
		return this;
	}
	
	
	/**
	 * Set the text value for this TextObject.
	 * 
	 * @param text
	 * @return
	 */
	public MenuObject setFont(Font font) {
		this.menuFont= font;
		return this;
	}

}
