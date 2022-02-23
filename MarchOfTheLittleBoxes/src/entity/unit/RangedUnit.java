package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import entity.projectile.Projectile;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/RangedUnit.java
 * <P>
 * This unit is slower than the typical melee unit, but
 * compensates with a longer offensive range. By default, it
 * does not lead its shots and so will have difficulty hitting
 * targets that are far away and moving. Finally, this type of
 * unit will back away from enemies until it reaches a
 * comfortable distance.
 * 
 * @author Samuel Tan
 *
 */
public class RangedUnit extends Unit
{
  protected double projVel = 2;
  protected double standoffDistance = 200;
  
  public RangedUnit(Battle b, int t, Vector2D pos, double ang)
  {
    super(b, t, pos, ang);
    attackStrength = 10;
    knockback = 10;
    topSpeed = 0.7;
  }

  @Override
  public void attack()
  {
    if (nearestTarget == null)
      return;
    
    Vector2D projVelVec = new Vector2D(projVel, 0);
    projVelVec.rotateTo(hitbox.angle);
    
    Projectile p = new Projectile(battle, this, new Vector2D(position), projVelVec, 
        attackStrength, knockback/projVel, team, true, 1);
    battle.add(p);
  }
  
  @Override
  public double idealSpeed()
  {
    if (nearestTarget == null)
    {
      return 0.0;
    }
    else
    {
      double dist = nearestTarget.position().minus(this.position).length();
      if (dist < standoffDistance)
        return -topSpeed;
      else
        return topSpeed;
    }
  }
  
  @Override
  protected void renderSpecial(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    
    double dotRadius = 4;
    g.fill(new Ellipse2D.Double(position.x - dotRadius,
        position.y - dotRadius, 2*dotRadius, 2*dotRadius));
    
  }
  
  @Override
  public String type()
  {
    return "Ranged Unit";
  }

}
