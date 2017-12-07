/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj107
 * 
 * @year 2017
 */
package com.snapgames.gdj.core.entity.particles.effects.rain;

import java.awt.Graphics2D;

import com.snapgames.gdj.core.entity.particles.Particle;
import com.snapgames.gdj.core.entity.particles.ParticleSystem;
import com.snapgames.gdj.core.entity.particles.behaviors.ParticleBehavior;

/**
 * 
 * @author Frédéric Delorme
 *
 */
public class RainBehavior implements ParticleBehavior {

	private float rainChance;
	public float dropDiameter;
	public float dropInitialVelocity;
	public float mWind = 0;
	public float mGravity = 9.81f;

	/**
	 * Initialize this Behavior to produce {@link Rain} with a
	 * <code>rainChance</code> range from 0 to 1 factor.
	 * 
	 * @param rainChance
	 */
	public RainBehavior(float rainChance) {
		this.rainChance = rainChance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.behaviors.ParticleBehavior#create(com
	 * .snapgames.gdj.core.entity.particles.ParticleSystem)
	 */
	@Override
	public Particle create(ParticleSystem ps) {

		Rain p = new Rain(ps);
		// CREATE NEW RAIN
		if (Math.random() < rainChance) {
			p.life = 100;
		} else {
			p.life = 0;
		}
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.behaviors.ParticleBehavior#update(com
	 * .snapgames.gdj.core.entity.particles.ParticleSystem,
	 * com.snapgames.gdj.core.entity.particles.Particle, float)
	 */
	@Override
	public Particle update(ParticleSystem ps, Particle p, float dt) {
		if (p != null) {
			switch (p.getClass().getName()) {
			/**
			 * Compute update for Rain particle.
			 */
			case "Rain":
				Rain r = (Rain) p;
				if (r.life > 0) {
					r.update(ps, dt);
				} else {
					r.life -= 1;
				}
				break;
			/**
			 * Compute update for Drop particle.
			 */
			case "Drop":
				Drop drop = (Drop) p;
				drop.update(ps, dt);
				break;
			default:
				break;
			}
		}
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snapgames.gdj.core.entity.particles.behaviors.ParticleBehavior#render(com
	 * .snapgames.gdj.core.entity.particles.ParticleSystem,
	 * com.snapgames.gdj.core.entity.particles.Particle, java.awt.Graphics2D)
	 */
	@Override
	public Particle render(ParticleSystem ps, Particle p, Graphics2D g) {
		if (p != null)
			p.draw(ps, g);
		return p;
	}

	/**
	 * Set the rainChance factor to <code>rc</code> value.
	 * 
	 * @param rc
	 */
	public RainBehavior setRainChance(float rc) {
		this.rainChance = rc;
		return this;
	}

	/**
	 * Set the diameter for a newly created Drop.
	 * 
	 * @param dropDiam
	 * @return
	 */
	public RainBehavior setDropDiameter(float dropDiam) {
		this.dropDiameter = dropDiam;
		return this;
	}

	/**
	 * @param dropInitialVelocity
	 *            the dropInitialVelocity to set
	 */
	public RainBehavior setDropInitialVelocity(float dropInitialVelocity) {
		this.dropInitialVelocity = dropInitialVelocity;
		return this;
	}

	public RainBehavior setGravity(float g) {
		this.mGravity = g;
		return this;
	}

	public RainBehavior setWind(float w) {
		this.mWind = w;
		return this;
	}

}
