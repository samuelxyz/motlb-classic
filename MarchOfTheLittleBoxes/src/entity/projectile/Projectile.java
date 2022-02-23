package entity.projectile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import entity.Entity;
import entity.unit.Unit;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/projectile/Projectile.java
 * <P>
 * Fired by certain classes of units. Default appearance is a circle with
 * outline. Friendly fire and/or multiple hits may be enabled or disabled.
 * The damage, speed, and other details are provided by the shooter.
 * 
 * @author xinyi
 *
 */
public class Projectile extends Entity
{
  protected double attackStrength, inertia;
  protected boolean friendlyFire;
  protected int remainingHits; // how many times it can hit something before
                               // vanishing
  // if this is 1, then the projectile will vanish immediately upon hitting
  // something
  // if more than 1, then the projectile will be able to hit several targets.
  // if less than 1, then the projectile will be practically invulnerable.
  protected double radius = 3;
  protected Unit owner;
  protected boolean blockable = true; // for ShieldBearers

  /**
   * Sets fields. Ensures that this unit's position isn't inside the parent.
   */
  public Projectile(Battle b, Unit parent, Vector2D pos, Vector2D vel,
    double power, double inert, int t, boolean ff, int durability)
  {
    super(t, b, pos, vel, true);
    attackStrength = power;
    inertia = inert;
    friendlyFire = ff;
    remainingHits = durability;
    owner = parent;
    
    if (owner != null && velocity.length() > 0)
      while (owner.hitbox().containsAbsPoint(position))
        move();
  }

  /**
   * Inactive projectiles will be removed automatically by the {@code battle}.
   */
  @Override
  public void update()
  {
    if (!isActive())
      return;

    // remainingHits can be -1 and the projectile will last forever
    if (remainingHits == 0 || !battle.borders().containsAbsPoint(position))
    {
      setActive(false);
    }

    move();

    checkHit();
  }

  /**
   * Checks all <code>Unit</code>s in this <code>Unit</code>'s
   * <code>Battle</code> to see whether there's a hit, and handles it.
   */
  protected void checkHit()
  {
    for (Unit u : battle.units())
    {
      if (u.isActive() && (friendlyFire || u.team() != team)
          && u.hitbox().containsAbsPoint(position))
      {
        // attack
        if (u.receiveAttack(this, attackStrength, velocity.scaledBy(inertia)))
          remainingHits--;
        break;
      }
    }
  }

  public boolean isBlockable()
  {
    return blockable;
  }

  @Override
  public String toString()
  {
    return getClass().getName() + ":" + "\nAttack Strength: " + attackStrength
        + "\nSpeed: " + String.format("%.3f", velocity.length())
        + "\nFriendly Fire: " + friendlyFire + "\nRemaining Hits: "
        + remainingHits + "\nPosition: " + position + "\nVelocity: " + velocity;
  }

  @Override
  public void render(Graphics2D g)
  {
    Ellipse2D.Double circle = new Ellipse2D.Double(position.x - radius,
        position.y - radius, 2 * radius, 2 * radius);

    g.setColor(outlineColor());
    g.draw(circle);
    g.setColor(fillColor());
    g.fill(circle);
  }

  protected Color outlineColor()
  {
    return Color.BLACK;
  }

  protected Color fillColor()
  {
    if (friendlyFire)
      return Color.WHITE;
    else
      return teamColor();
  }
}
