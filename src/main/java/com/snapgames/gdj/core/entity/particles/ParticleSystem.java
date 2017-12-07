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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapgames.gdj.core.Game;
import com.snapgames.gdj.core.entity.AbstractGameObject;
import com.snapgames.gdj.core.entity.CameraObject;
import com.snapgames.gdj.core.entity.particles.behaviors.ParticleBehavior;

/**
 * The particle System is the main class to manage some particles in a
 * GameObject.
 * 
 * @author Frédéric Delorme
 *
 */
public class ParticleSystem extends AbstractGameObject {
	/**
	 * Internal Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ParticleSystem.class);

	/**
	 * Particles linked to this system.
	 */
	public List<Particle> systemParticles = new ArrayList<>();

	/**
	 * Behavior to be applied to all particles.
	 */
	private ParticleBehavior behavior;

	/**
	 * Camera to set Particle Focus.
	 */
	public CameraObject camera;

	/**
	 * Default constructor to create a new Particle System.
	 * 
	 * @param name
	 */
	public ParticleSystem(String name) {
		super(name);
		this.attributes.put("particle.life", 100);
		logger.debug("create particle system with particle's life set to 100");
	}

	/**
	 * Initialize all particles.
	 */
	public void initialize() {
		for (int i = 0; i < systemParticles.size(); i++) {

			Particle part = behavior.create(this);
			part.initialize(this);
			if (part.getLife() > 0) {
				systemParticles.set(i, part);
			}
		}
	}

	/**
	 * resize the ParticleSystem number of particles.
	 * 
	 * @param size
	 *            number of particles for this ParticleSystem.
	 * @return
	 */
	public ParticleSystem setNbParticles(int size) {
		systemParticles = Arrays.asList(new Particle[size]);
		initialize();
		logger.debug("set particles number to {}", size);
		return this;
	}

	/**
	 * initialize attributes for this ParticleSystem.
	 * 
	 * @param attributes
	 * @return
	 */
	public ParticleSystem setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
		logger.debug("set attributes to {}", attributes.toString());
		return this;
	}

	/**
	 * Add a behavior to this particle system.
	 * 
	 * @param b
	 * @return
	 */
	public ParticleSystem addBehavior(ParticleBehavior b) {
		logger.debug("Add beahavior {}", b.getClass().getName());
		this.behavior = b;
		return this;
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
		Iterator<Particle> i = systemParticles.iterator();
		while (i.hasNext()) {
			Particle particle = i.next();
			behavior.update(this, particle, dt);
			if (particle.getLife() == 0) {
				i.remove();
			}
		}
		behavior.create(this);
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
		for (Particle particle : systemParticles) {
			behavior.render(this, particle, g);
		}

	}

	/**
	 * define the camera the RainBehavior generator must be stick to.
	 * 
	 * @param camera
	 * @return
	 */
	public ParticleSystem setCamera(CameraObject camera) {
		this.camera = camera;
		logger.debug("Set camera to {}", camera.name);
		return this;
	}

	/**
	 * return the currently set behavior on this particle System.
	 * 
	 * @return
	 */
	public ParticleBehavior getBehavior() {
		return behavior;
	}

}