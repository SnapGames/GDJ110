/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj107
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj110.entity.particles.rain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;

import com.snapgames.gdj.core.entity.particles.AbstractParticle;
import com.snapgames.gdj.core.entity.particles.ParticleSystem;

/**
 * A Rain Particle.
 * 
 * @author Frédéric Delorme
 *
 */
public class Rain extends AbstractParticle {

	public static int initLife = 200;

	float prevX = 0;
	float prevY = 0;

	/**
	 * Initialize the Rain Particle attached to its ParticleSystem.
	 * 
	 * @param ps
	 */
	public Rain(ParticleSystem ps) {
		initialize(ps);
	}

	/**
	 * Initialize default value for this particle.
	 * 
	 * @param ps
	 */
	public void initialize(ParticleSystem ps) {
		Random r = new Random();
		// this.x = r.nextInt((int) (ps.camera.width + ps.camera.x));
		// this.y = r.nextInt((int) (ps.camera.height + ps.camera.y));
		this.x = r.nextInt((int) (ps.getWidth()));
		this.y = r.nextInt((int) (ps.getHeight()));
		this.prevX = x;
		this.prevY = y;
		this.color = Color.WHITE;
		this.life = initLife;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.AbstractParticle#create(com.snapgames
	 * .gdj.core.entity.particles.ParticleSystem)
	 */
	@Override
	public void create(ParticleSystem particleSystem) {
		super.create(particleSystem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.AbstractParticle#update(com.snapgames
	 * .gdj.core.entity.particles.ParticleSystem, float)
	 */
	@Override
	public void update(ParticleSystem ps, float time) {
		RainBehavior rb = (RainBehavior) ps.getBehavior();
		// If its a living particle, draw the particle
		if (life > 0) {
			// keep previous position
			prevX = x;
			prevY = y;
			// compute next position
			x += rb.mWind + sx;
			y -= (rb.mGravity) + sy;
			// reduce life duration.
			life -= 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.AbstractParticle#draw(com.snapgames.
	 * gdj.core.entity.particles.ParticleSystem, java.awt.Graphics2D)
	 */
	@Override
	public void draw(ParticleSystem ps, Graphics2D g) {
		// draw a simple line between previous and new position.
		g.setColor(color);
		Line2D line = new Line2D.Double(x, y, prevX, prevY);
		g.draw(line);
	}
}
