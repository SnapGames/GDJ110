/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj107
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj110.entity.particles.effects.rain;

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

	float prevX = 0;
	float prevY = 0;

	public Rain(ParticleSystem ps) {
		initialize(ps);
	}

	/**
	 * @param ps
	 */
	public void initialize(ParticleSystem ps) {
		Random r = new Random();
		this.x = r.nextInt((int) (ps.camera.width + ps.camera.y));
		this.y = 0;
		this.color = Color.GRAY;
	}

	@Override
	public void create(ParticleSystem particleSystem) {
		super.create(particleSystem);
	}

	@Override
	public void update(ParticleSystem ps, float time) {
		RainBehavior rb = (RainBehavior) ps.getBehavior();
		prevX = x;
		prevY = y;

		x += rb.mWind + sx;
		y -= (rb.mGravity*0.1f) + sy;

	}

	@Override
	public void draw(ParticleSystem ps, Graphics2D g) {
		g.setColor(color);
		Line2D line = new Line2D.Double(x, y, prevX, prevY);
		g.draw(line);
	}
	
	
}