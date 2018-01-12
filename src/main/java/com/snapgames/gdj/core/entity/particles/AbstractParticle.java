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

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This is a default implementation of all the operation for a basic Particle.
 * 
 * @author Frédéric Delorme
 *
 */
public class AbstractParticle implements Particle {

	/**
	 * Position of this particle
	 */
	public float x, y;
	/**
	 * Speed of this particle
	 */
	public float sx, sy;
	/**
	 * Sizing of the particle
	 */
	public float w = 2, h = 2;
	/**
	 * Life duration (in frames)
	 */
	public int life;
	/**
	 * start color for this particle.
	 */
	public Color color = Color.WHITE;

	/**
	 * Default constructor for a particle. Only an inheriting class can call this
	 * within a super() call.
	 */
	protected AbstractParticle() {
		x = y = 0.0f;
		sx = sy = 0.0f;
		life = 100;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.Particle#initialize(com.snapgames.gdj
	 * .core.entity.particles.ParticleSystem)
	 */
	@Override
	public void initialize(ParticleSystem particleSystem) {
		this.create(particleSystem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.Particle#create(com.snapgames.gdj.
	 * core.entity.particles.ParticleSystem)
	 */
	@Override
	public void create(ParticleSystem particleSystem) {
		if (particleSystem.attributes.containsKey("particle.life")) {
			this.life = (int) particleSystem.attributes.get("particle.life");
		} else {
			this.life = 100;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.Particle#update(com.snapgames.gdj.
	 * core.entity.particles.ParticleSystem, float)
	 */
	@Override
	public void update(ParticleSystem ps, float time) {
		x = x + sx * time;
		y = y + sy * time;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.Particle#draw(com.snapgames.gdj.core.
	 * entity.particles.ParticleSystem, java.awt.Graphics2D)
	 */
	@Override
	public void draw(ParticleSystem ps, Graphics2D g) {
		g.setColor(color);
		g.drawLine((int) x, (int) y, (int) (x + w), (int) (y + h));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.snapgames.gdj.core.entity.particles.Particle#getLife()
	 */
	@Override
	public int getLife() {
		return life;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractParticle [x=").append(x).append(", y=").append(y).append(", sx=").append(sx)
				.append(", sy=").append(sy).append(", w=").append(w).append(", h=").append(h).append(", life=")
				.append(life).append(", color=").append(color).append("]");
		return builder.toString();
	}

	@Override
	public void computeLife(int i) {
		life += i;
	}
}
