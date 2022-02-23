package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import entity.Entity;
import entity.projectile.Projectile;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/ShieldBearer.java
 * <P>
 * An unusually wide unit that is slower than the typical melee
 * unit, but is strong. It has the special ability to reflect certain
 * types of projectiles if they come from within 45 degrees of the
 * direction it's facing.
 * 
 * @author Samuel Tan
 *
 */
public class ShieldBearer extends MeleeUnit
{
  protected double RICOCHET_VARIANCE = 0.1;
  
  public ShieldBearer(Battle b, int t, Vector2D pos, double ang)
  {
    super(b, t, pos, ang);

    hitbox.yMin = -20;
    hitbox.yMax = 20;
    
    baseHealth = 200;
    inertia = 70;
    topSpeed = 0.7;
    acceleration = 0.07;
    rotationSpeed = 0.01;
  }
  
  @Override
  public boolean receiveAttack(Entity source, double attackValue,
      Vector2D knockback)
  {
    if (source instanceof Projectile)
    {
      Projectile proj = (Projectile) source;
      if (proj.isBlockable())
      {
        Vector2D facing = new Vector2D(1, 0);
        facing.rotateTo(hitbox.angle);
        facing.scaleBy(-1);
        
        if (Math.abs(proj.velocity().angle() - facing.angle()) < Math.PI * 1/4)
        {
          receiveImpulse(knockback);
          
          // reflect
          Vector2D vel = new Vector2D(proj.velocity());
          double ang = vel.angle() - hitbox.angle;
          vel.rotateBy(-2*ang);
          vel.scaleBy(-1);
          
          // add some randomness
          vel.rotateBy(Math.random() * RICOCHET_VARIANCE);
          
          proj.velocity().set(vel);
          proj.move();
          
          return false;
        }
      }
    }
    
    return super.receiveAttack(source, attackValue, knockback);
  }

  @Override
  protected void renderSpecial(Graphics2D g)
  {
    Path2D.Double line = new Path2D.Double();
    
    Vector2D start = new Vector2D( (2*hitbox.xMax + hitbox.xMin)/3, hitbox.yMin);
    start.rotateBy(hitbox.angle);
    start.add(position);
    line.moveTo(start.x, start.y);
    
    Vector2D end = new Vector2D( (2*hitbox.xMax + hitbox.xMin)/3, hitbox.yMax);
    end.rotateBy(hitbox.angle);
    end.add(position);
    line.lineTo(end.x, end.y);
    
    g.setColor(Color.BLACK);
    g.draw(line);
  }
  
  @Override
  public String type()
  {
    return "Shield Bearer";
  }

}
