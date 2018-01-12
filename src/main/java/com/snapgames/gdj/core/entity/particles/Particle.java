/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj107
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.entity.particles;

import java.awt.Graphics2D;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public interface Particle {
	/**
	 * Update particles according to its parent {@link ParticleSystem} behavior.
	 * 
	 * @param ps
	 * @param time
	 */
	public void update(ParticleSystem ps, float time);

	/**
	 * Draw particles according to its parent {@link ParticleSystem}.
	 * 
	 * @param ps
	 * @param g
	 */
	public void draw(ParticleSystem ps, Graphics2D g);

	/**
	 * Initialize particles according to parent ParticleSystem.
	 * 
	 * @param particleSystem
	 * @param attributes
	 */
	public void initialize(ParticleSystem particleSystem);

	/**
	 * method to be call at particle creation(to reset to some specific values
	 * internal attributes)
	 * 
	 * @param particleSystem
	 */
	public void create(ParticleSystem particleSystem);

	/**
	 * compute life for this particle.
	 * 
	 * @return
	 */
	public int getLife();

	/**
	 * Translate attributes of the Particle to a String, manly for debug purpose.
	 * 
	 * @return
	 */
	public String toString();

	/**
	 * Compute life for Particle.
	 * 
	 * @param i
	 */
	public void computeLife(int i);

}
