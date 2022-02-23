package entity;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/Particle.java
 * <P>
 * Particles are generated continuously by different units
 * as "trails". They may or may not be rendered
 * depending on the "Toggle visual effects" option
 * in the control panel. Turning them off may be useful if
 * many projectiles are also on the screen.
 * 
 * @author Samuel Tan
 *
 */
public class Particle extends Entity
{
  protected double radius, dr; // radius and rate of change of radius
  
  public Particle(Battle b, int team, Vector2D pos, Vector2D vel, double radius, double dr)
  {
    super(team, b, pos, vel, true);
    this.radius = radius;
    this.dr = dr;
  }

  public void update()
  {
    if (!active)
      return;
    
    move();
    
    radius += dr;
    
    if (radius <= 0)
      active = false;
  }

  public void render(Graphics2D g)
  {
    if (!active)
      return;
    
    if (team >= 0)
      g.setColor(teamColor());
    else
      g.setColor(battle.color().darker());
    g.fill(new Ellipse2D.Double(position.x - radius, position.y - radius,
        2 * radius, 2 * radius));
    
//    g.fillOval((int)(position.x - radius), (int)(position.y - radius), 
//        (int)(2 * radius), (int)(2 * radius));
  }

}
