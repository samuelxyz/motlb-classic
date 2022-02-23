package entity.projectile;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import entity.unit.Unit;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/projectile/GuidedProjectile.java
 * <P>
 * A projectile that automatically turns toward its target.
 * Will fly straight if the target is no longer in the battle.
 * Visual appearance is rectangular rather than circular.
 * 
 * @author Samuel Tan
 *
 */
public class GuidedProjectile extends Projectile
{
  protected Unit target;
  protected double agility; // turning ability in rad/tick
  
  public GuidedProjectile(Battle b, Unit parent, Vector2D pos,
      Vector2D vel, double power, double inert, int t,
      boolean ff, int durability, Unit target)
  {
    super(b, parent, pos, vel, power, inert, t, ff, durability);
    this.target = target;
    agility = 0.05;
    blockable = false;
  }
  
  @Override
  public void update()
  {
    // rotate velocity toward target
    if (target != null && target.isActive())
    {
      double targetAng = ( target.position().minus(this.position) ).angle();
      double ang = velocity.angle();
      
      if (Math.abs(targetAng - ang) <= agility)
        velocity.rotateTo(targetAng);
      else
        velocity.rotateBy(Math.copySign(agility, targetAng - ang));
    }
    
    super.update();
  }
  
  /**
   * This one looks sort of like a "laser bolt" from sci-fi
   */
  @Override
  public void render(Graphics2D g)
  {
    // velocity is "north"
    Vector2D north = new Vector2D(velocity);
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
    
    g.setColor(outlineColor());
    g.draw(path);
    g.setColor(fillColor());
    g.fill(path);
  }
}
