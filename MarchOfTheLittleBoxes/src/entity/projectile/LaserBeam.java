package entity.projectile;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import entity.Particle;
import entity.unit.Unit;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/projectile/LaserBeam.java
 * <P>
 * Models a laser beam by behaving as a point projectile
 * and instantly traveling until the projectile is "done"
 * (spent or out of bounds). The beam sticks around for a while, 
 * doing nothing, for visual effect, and also makes a particle trail.
 * 
 * @author Samuel Tan
 *
 */
public class LaserBeam extends Projectile
{
  protected Vector2D origin;
  protected int lifetime = 15;
  protected int timer;
  protected List<Unit> alreadyHit;

  public LaserBeam(Battle b, Unit parent, Vector2D pos, Vector2D vel,
      double power, double inert, int t, boolean ff, int durability)
  {
    super(b, parent, new Vector2D(pos), vel, power, inert, t, ff, durability);
    origin = pos;
    timer = 0;
    alreadyHit = new ArrayList<Unit>();
    blockable = false;
  }
  
  @Override
  public void update()
  {
    if (owner == null || !owner.isActive())
    {
      setActive(false);
    }
    else if (timer == 0)
    {
      synchronized (battle.projectiles())
      {
        while (isActive()) // laser travels instantaneously
        {
          super.update();
          battle.add(new Particle(
              battle, -1, new Vector2D(position), 
              Vector2D.randomDirection(0.1), 3, -3.0/60));
        }
        setActive(true);
      }
      timer = 1;
    }
    else
    {
      timer++;
      if (timer > lifetime)
        setActive(false);
    }
  }
  
  @Override
  /**
   * Laser beam version can only hit each unit once.
   */
  protected void checkHit()
  {
    for (Unit u : battle.units())
    {
      if (u.isActive() && u != owner && !alreadyHit.contains(u) 
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
  public void render(Graphics2D g)
  {
//    System.out.println("Rendering beam");
    
    // velocity is "north"
    // "north" goes to just beyond the borders of the battle
    Vector2D north = new Vector2D(velocity);
    do
    {
      north.add(velocity);
    }
    while (battle.borders().containsAbsPoint(origin.plus(north)));
    
    Vector2D east = new Vector2D(velocity);
    east.scaleTo(2);
    east.rotateBy(-Math.PI/2);
    
    Path2D.Double path = new Path2D.Double();
    
    path.moveTo(origin.x + north.x + east.x, origin.y + north.y + east.y);
    path.lineTo(origin.x + north.x - east.x, origin.y + north.y - east.y);
    path.lineTo(origin.x           - east.x, origin.y           - east.y);
    path.lineTo(origin.x           + east.x, origin.y           + east.y);
    path.lineTo(origin.x + north.x + east.x, origin.y + north.y + east.y);
    
    g.setColor(outlineColor());
    g.draw(path);
    g.setColor(fillColor());
    g.fill(path);
  }

}
