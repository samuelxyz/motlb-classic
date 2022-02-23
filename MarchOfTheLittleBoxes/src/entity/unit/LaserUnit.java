package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import entity.projectile.LaserBeam;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/LaserUnit.java
 * <P>
 * Fires a {@code LaserBeam} as its attack. This unit is very slow
 * and has little health, but its laser is powerful enough to 
 * take out most varieties of enemy with a single shot. Additionally,
 * it is immobilized while firing.
 * 
 * @author Samuel Tan
 *
 */
public class LaserUnit extends RangedUnit
{
  protected LaserBeam beam;
  
  public LaserUnit(Battle b, int t, Vector2D pos, double ang)
  {
    super(b, t, pos, ang);
    
    topSpeed = 0.5;
    standoffDistance = 600;
    rotationSpeed = 0.03;
    
    baseHealth = 30;
    
    projVel = 1;
    attackStrength = 100;
    knockback = 50;
    attackInterval = 200;
    
  }
  
  @Override
  public void attack()
  {
    if (nearestTarget == null)
      return;
    
    Vector2D projVelVec = new Vector2D(projVel, 0);
    projVelVec.rotateTo(hitbox.angle);
    
    beam = new LaserBeam(battle, this, position, projVelVec, 
        attackStrength, knockback/projVel, team, false, -1);
    battle.add(beam);
    
    velocity.set(0, 0);
  }
  
  @Override
  public double idealSpeed()
  {
    if (beam != null && beam.isActive())
      return 0;
    else
      return super.idealSpeed();
  }
  
  @Override
  protected void renderSpecial(Graphics2D g)
  {
    // facing is "north"
    Vector2D north = new Vector2D(1, 0);
    north.rotateTo(hitbox.angle);
    north.scaleTo(3);
    Vector2D east = new Vector2D(north);
    east.scaleTo(3);
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
    return "Laser Unit";
  }
}
