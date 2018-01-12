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
import java.awt.geom.Ellipse2D;

import com.snapgames.gdj.core.entity.particles.AbstractParticle;
import com.snapgames.gdj.core.entity.particles.ParticleSystem;

/**
 * A Drop Particle.
 * 
 * @author Frédéric Delorme
 *
 */
public class Drop extends AbstractParticle {

	RainBehavior rb;

	float t = 0;
	float x0 = 0, y0 = 0;
	float v0, angle;

	/**
	 * create a Drop at position x.
	 * 
	 * @param x
	 */
	public Drop(ParticleSystem ps, float x, float y) {
		super();
		rb = (RainBehavior) ps.getBehavior();
		this.x0 = x;
		this.y0 = y;
		this.color = Color.WHITE;
		v0 = rb.dropInitialVelocity;
		angle = (float) Math.toRadians(Math.round(Math.random() * 180)); // from 0 - 180 degrees

	}

	@Override
	public void create(ParticleSystem particleSystem) {
		super.create(particleSystem);
	}

	@Override
	public void update(ParticleSystem ps, float time) {
		// double g=10;
		t += time;
		x = x0 + v0 * t * (float) Math.cos(angle);
		y = y0 - (v0 * t * (float) Math.sin(angle) - rb.mGravity * t * t / 2);
		if (y >= ps.camera.height + ps.camera.y) {
			life = 0;
		}
		life -= 1;

	}

	@Override
	public void draw(ParticleSystem ps, Graphics2D g) {
		g.setColor(color);
		Ellipse2D.Double circle = new Ellipse2D.Double(x, y, rb.dropDiameter, rb.dropDiameter);
		g.fill(circle);
	}
}
