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
		Rain p = (Rain) ps.getNextFreeParticle(Rain.class);
		if(p==null) {
			if (Math.random() < rainChance) {
				p.life = 100;
			} else {
				p.life = 0;
			}
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
			if (p.getLife() > 0) {
				p.update(ps, dt);
				if (p.getClass().equals(Rain.class)) {
					Rain r = (Rain) p;
					if (r.y > ps.camera.height + ps.camera.y) {
						r.life = 0;
						for (int i = 0; i < 3; i++) {
							ps.addParticle(new Drop(ps, r.x, ps.camera.height + ps.camera.y));
						}
						ps.addParticle(create(ps));
					}
				}
				p.computeLife(1);
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
	 * Initialize the default Drop velocity.
	 * 
	 * @param dropInitialVelocity
	 *            the dropInitialVelocity to set
	 */
	public RainBehavior setDropInitialVelocity(float dropInitialVelocity) {
		this.dropInitialVelocity = dropInitialVelocity;
		return this;
	}

	/**
	 * Set gravity value.
	 * 
	 * @param g
	 * @return
	 */
	public RainBehavior setGravity(float g) {
		this.mGravity = g;
		return this;
	}

	/**
	 * Set wind value.
	 * 
	 * @param w
	 * @return
	 */
	public RainBehavior setWind(float w) {
		this.mWind = w;
		return this;
	}

}
