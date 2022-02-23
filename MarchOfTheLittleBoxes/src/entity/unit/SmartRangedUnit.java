package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import entity.projectile.GuidedProjectile;
import entity.projectile.Projectile;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/SmartRangedUnit.java
 * <P>
 * Fires a {@code GuidedProjectile} as its attack.
 * 
 * @author Samuel Tan
 *
 */
public class SmartRangedUnit extends RangedUnit
{

  public SmartRangedUnit(Battle b, int t, Vector2D pos, double ang)
  {
    super(b, t, pos, ang);
    attackInterval = 100;
    standoffDistance = 400;
    projVel = 3;
  }
  
  @Override
  public void attack()
  {
    if (nearestTarget == null)
      return;
    
    Vector2D projVelVec = new Vector2D(projVel, 0);
    projVelVec.rotateTo(hitbox.angle);
    
    Projectile p = new GuidedProjectile(battle, this, new Vector2D(position), projVelVec, 
        attackStrength, knockback/projVel, team, false, 1, nearestTarget);
    
    battle.add(p);
  }
  
  @Override
  protected void renderSpecial(Graphics2D g)
  {
    // facing is "north"
    Vector2D north = new Vector2D(1, 0);
    north.rotateTo(hitbox.angle);
    north.scaleTo(4);
    Vector2D east = new Vector2D(north);
    east.scaleTo(2);
    east.rotateBy(-Math.PI/2);
    
    Path2D.Double path = new Path2D.Double();
    
    path.moveTo(position.x + north.x + east.x, position.y + north.y + east.y);
    path.lineTo(position.x + north.x - east.x, position.y + north.y - east.y);
    path.lineTo(position.x - north.x - east.x, position.y - north.y - east.y);
    path.lineTo(position.x - north.x + east.x, position.y - north.y + east.y);
    path.lineTo(position.x + north.x + east.x, position.y + north.y + east.y);
    
    g.setColor(Color.BLACK);
    g.fill(path);
  }
  
  @Override
  public String type()
  {
    return "Smart Ranged Unit";
  }
}
