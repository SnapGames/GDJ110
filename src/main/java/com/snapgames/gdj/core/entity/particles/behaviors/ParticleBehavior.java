/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj107
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.entity.particles.behaviors;

import java.awt.Graphics2D;

import com.snapgames.gdj.core.entity.particles.Particle;
import com.snapgames.gdj.core.entity.particles.ParticleSystem;

/**
 * Defining a Particle behavior to implement any kind of particle system.
 * 
 * @author Frédéric Delorme
 *
 */
public interface ParticleBehavior {

	public Particle create(ParticleSystem ps);

	public Particle update(ParticleSystem ps, Particle p, float dt);

	public Particle render(ParticleSystem ps, Particle p, Graphics2D g);

}
