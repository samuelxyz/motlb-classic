package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import entity.projectile.Cannonball;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/Cannon.java
 * <P>
 * Fires a {@code CannonBall} as its attack. It's even slower than the
 * basic ranged unit, but is more powerful.
 * 
 * @author Samuel Tan
 *
 */
public class Cannon extends RangedUnit
{

  public Cannon(Battle b, int t, Vector2D pos, double ang)
  {
    super(b, t, pos, ang);
    
    attackStrength = 50;
    knockback = 75;
    projVel = 6;
    attackInterval = 200;
    
    standoffDistance = 400;
    topSpeed = 0.5;
  }
  
  @Override
  public void attack()
  {
    if (nearestTarget == null)
      return;
    
    Vector2D projVelVec = new Vector2D(projVel, 0);
    projVelVec.rotateTo(hitbox.angle);
    
    battle.add(new Cannonball(battle, this, new Vector2D(position), projVelVec, 
        attackStrength, knockback/projVel, team, true, 10));
  }

  @Override
  protected void renderSpecial(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    
    double dotRadius = 6;
    g.fill(new Ellipse2D.Double(position.x - dotRadius,
        position.y - dotRadius, 2*dotRadius, 2*dotRadius));
    
  }
  
  @Override
  public String type()
  {
    return "Cannon";
  }
  
}
