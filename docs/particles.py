"""
simpleParticle01.py
Eric Pavey - 2010-06-21

Draw particles with the mouse:
LMB draws particles, RMB clears the screen.
When particles get near one another, they connect via a line.
When new connections are made, there is a particle burst.

Mainly just did it for fun, not very fast.
"""

#--------------
# Imports & Inits
import sys
import math
import random
# Vec2D comes from here: http://pygame.org/wiki/2DVectorClass
from vec2d import Vec2d

import pygame
import pygame.gfxdraw
from pygame.locals import *
#pygame.init()

#--------------
# Constants
WIDTH = 512
HEIGHT = 512
FRAMERATE = 30	
GRAVITY = (0, .1)

#-------------
# screenSurf Setup
screenSurf = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Simple Particle 01 - Eric Pavey")
clock = pygame.time.Clock()

#-------------
# Define helper functions, classes

class Particle(object):
    """
    Superclass for other particle types.
    """
    def __init__(self, surface, pos, vel, gravity, container, color='red'):
        """
        surface : Surface :  The Surface to draw on.
        pos : (x,y) : tuple\list x,y position at time of creation.
        vel : (x,y) : tuple\list x,y velocity at time of creation.
        gravity : (x,y) : tuple\list x,y gravity effecting the particle.
        container : list : The passed in list that contains all the particles to draw.
            Used so particles can be deleted.
        color : str : String color value, default 'red'.
        """
        self.surface = surface
        self.pos = Vec2d(pos)
        vel = [vel[0]+random.uniform(-3,3), vel[1]+random.uniform(-3,3)]
        self.vel = Vec2d(vel)
        # Clamp any huge velocities:
        if self.vel.length > 10:
            self.vel.length = 10
            
        self.gravity = Vec2d(gravity)
        self.container = container
        self.color = Color(color)
        
        self.radius = random.randint(4,16)
        self.connectDist = self.radius * 4
        self.surfSize = surface.get_size()
        self.drag = .9
        # This is updated each frame with a list of all other particles connected
        # to, which helps to define if lines should be drawn.
        self.connections = []
        
    def update(self):
        """
        Update position and existance per frame.
        """
        self.connections = []
        self.vel = (self.vel + self.gravity) * self.drag
        self.pos = self.pos + self.vel
        if self.outOfBounds():
            self.container.remove(self)
        
    def draw(self):
        """
        Override with subclass drawing method.
        """
        pass

    def outOfBounds(self):
        """
        Calculate if particle still exists based on exiting the screen.
        """
        outOfBounds = False
        if self.pos[0] < -self.radius or self.pos[0] > self.surfSize[0] + self.radius:
            outOfBounds = True
        elif self.pos[1] < -self.radius or self.pos[1] > self.surfSize[1] + self.radius:
            outOfBounds = True
        return outOfBounds
    

class ParticleBall(Particle):
    """
    Draw 'particle balls', that when close to other particles, will draw a line
    to them.  Will also trigger particle 'sparkles' when line connections occur.
    """
    
    def __init__(self, surface, pos, vel, gravity, container, sparkleContainer, color='red'):
        # init superclass:
        super(ParticleBall, self).__init__(surface, pos, vel, gravity, container, color)

        self.sparkleContainer = sparkleContainer
        # This is updated each frame with a list of all other particles connected
        # to, which helps to define if lines should be drawn.
        self.connections = []
        self.createdSparkles = []
        
    def draw(self):
        """
        Draw on-screen.
        """
        # Draw circle
        pygame.gfxdraw.aacircle(self.surface, int(self.pos[0]), int(self.pos[1]), int(self.radius), self.color)
        
        # Draw connective lines, check against all other particles:
        for p in self.container:
            # Do not connect to self...
            if p is not self:
                # If this particle hasn't already been connected to the target particle...
                if self not in p.connections:                
                    dist = math.sqrt(abs(pow(p.pos.x-self.pos.x, 2) + pow(p.pos.y-self.pos.y, 2)))
                    if dist < self.connectDist:
                        self.connections.append(p)
                        pygame.draw.aaline(self.surface, self.color, (self.pos[0], self.pos[1]), (p.pos[0], p.pos[1]))
                        pygame.gfxdraw.filled_circle(self.surface, int(self.pos[0]), int(self.pos[1]), int(self.radius), Color(255,0,0,128))
                        self.createSparkles(p)
                        
    def createSparkles(self, target):
        """
        Create 'sparkle' particles based on this ParticleBall connectint with another:
        
        target : ParticleBall : Test to see if we've already connected to the target,
            if so, stop making sparkles.
        """
        if target not in self.createdSparkles:
            for r in range(self.radius):
                self.sparkleContainer.append(ParticleSparkle(self.surface, (self.pos.x, self.pos.y), (self.vel.x, self.vel.y), 
                                                             self.gravity, self.sparkleContainer) )
            self.createdSparkles.append(target)
            
                        
class ParticleSparkle(Particle):
    """
    Draw particle 'sparkles'.  They are generated when two ParticleBalls connect with a line.
    """
    def __init__(self, surface, pos, vel, gravity, container, color='white', age=60):
        """
        age : How many frames the particle will live for.  Will get darker as it gets older.
        """
        # Init superclass:
        super(ParticleSparkle, self).__init__(surface, pos, vel, gravity, container, color)
        self.valueStep = 100.0/float(age)
        
    def update(self):
        # Override superclass, but call to superclass method first:
        super(ParticleSparkle, self).update()
        
        # Update color, and existance based on color:
        hsva = self.color.hsva
        value = hsva[2]
        alpha = hsva[3]
        value -= self.valueStep
        alpha -= self.valueStep
        if value < 0 or alpha < 0:
            # It's possible this particle was removed already by the superclass.
            try:
                self.container.remove(self)
            except ValueError:
                pass
        else:
            self.color.hsva = (int(hsva[0]), int(hsva[1]), value, alpha)        
        
        
    def draw(self):
        # Draw just a simple point:
        pygame.draw.line(self.surface, self.color, (self.pos[0], self.pos[1]), (self.pos[0], self.pos[1]))

#------------
# Main Program
def main():
    print ("Running Python version: %s" % sys.version)
    print ("Running PyGame version: %s" % pygame.ver)
    looping = True
    
    particles = []
    sparkles = []
    
    # main loop  
    while looping:
        # maintain our framerate:
        clock.tick(FRAMERATE)
        
        #------------------------------- 
        # detect for events
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                looping = False
            elif event.type == MOUSEBUTTONDOWN:
                if event.button == 3:
                    particles = []
                    sparkles = []
            elif event.type == KEYDOWN:
                if event.key == K_ESCAPE:
                    looping = False
                 
        mouseButtons = pygame.mouse.get_pressed()
        if mouseButtons[0]:
            mousePosition = pygame.mouse.get_pos()
            mouseVelocity = pygame.mouse.get_rel()
            particles.append(ParticleBall(screenSurf, mousePosition, mouseVelocity, 
                                          GRAVITY, particles, sparkles) )
                    
        #-------------------------------       
        # Do stuff!
        
        screenSurf.fill(Color('black'))
        
        for p in particles:
            p.update()
            p.draw()
            
        for s in sparkles:
            s.update()
            s.draw()

        #------------------------------- 
        # update our display:
        pygame.display.update()
        
    print ("Exiting")
    

#------------
# Execution from shell\icon:         
if __name__ == "__main__":
    sys.exit(main())