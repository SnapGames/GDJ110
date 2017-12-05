/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj105
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public class Layer {
	public List<GameObject> objects = new CopyOnWriteArrayList<>();
	public boolean active = false;
	public boolean moveWithCamera = false;

	public Layer() {
	}

	public Layer(boolean active, boolean moveWithCamera) {
		this();
		this.active = active;
		this.moveWithCamera = moveWithCamera;
	}

	public void add(GameObject o) {
		objects.add(o);
	}

}
