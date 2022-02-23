package entity.projectile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import entity.unit.Unit;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/projectile/Cannonball.java
 * <P>
 * Describes a large projectile, generally with a high speed and durability.
 * Fired by the {@code Cannon} unit.
 * 
 * @author Samuel Tan
 *
 */
public class Cannonball extends Projectile
{
  protected List<Unit> alreadyHit;
  
  public Cannonball(Battle b, Unit parent, Vector2D pos, Vector2D vel,
      double power, double inert, int t, boolean ff, int durability)
  {
    super(b, parent, pos, vel, power, inert, t, ff, durability);
    
    radius = 6;

    alreadyHit = new ArrayList<Unit>();
  }
  
  /**
   * Cannonball should only hit each unit once to prevent insta-kill
   */
  @Override
  protected void checkHit()
  {
    for (Unit u : battle.units())
    {
      if (u.isActive() && !alreadyHit.contains(u) 
          && (friendlyFire || u.team() != team) 
          && u.hitbox().containsAbsPoint(position))
      {
        // attack
        u.receiveAttack(this, attackStrength, velocity.scaledBy(inertia));
        remainingHits--;
        alreadyHit.add(u);
        break;
      } 
    }
  }
  
  @Override
  protected Color fillColor()
  {
    if (friendlyFire)
      return Color.BLACK;
    else
      return teamColor();
  }

}
