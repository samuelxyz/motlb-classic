package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.List;

import entity.Entity;
import entity.Particle;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/Resurrector.java
 * <P>
 * A very slow unit that has negligible health.
 * However, if hit, it will exchange its life for all of its
 * fallen teammates'.
 * 
 * @author Samuel Tan
 */
public class Resurrector extends Unit
{

  public Resurrector(Battle b, int t, Vector2D pos, double ang)
  {
    super(b, t, pos, ang);
    
    topSpeed = 0.2;
    rotationSpeed = 0.01;
  }

  @Override
  public void attack() {}
  
  @Override
  public boolean receiveAttack(Entity source, double attackValue, Vector2D knockback)
  {
    for (Unit u : battle.units())
      if (u.team() == team && !u.isActive() && !(u instanceof Resurrector))
      {
        u.resurrect();
        for (int i = 0; i < 40; i++)
        {
          battle.add(new Particle(
              battle, u.team(), new Vector2D(u.hitbox().randomInteriorPos()), 
              u.velocity().plus(Vector2D.randomDirection(1)), 3, -3.0/100));
        }
      }

    setActive(false);
    return true;
  }

  @Override
  protected void renderSpecial(Graphics2D g)
  {
    drawBox(g, 0.6);
    drawBox(g, 0.2);
  }
  
  private void drawBox(Graphics2D g, double scale)
  {
    List<Vector2D> corners = hitbox.absCorners();
    
    Vector2D.dilate(corners, position, scale);
    
    Path2D.Double path = new Path2D.Double();
    path.moveTo(corners.get(0).x, corners.get(0).y);
    for (int i = 1; i < corners.size(); i++)
      path.lineTo(corners.get(i).x, corners.get(i).y);
    path.closePath();
    
    g.setColor(Color.BLACK);
    g.draw(path);
  }
  
  @Override
  public String type()
  {
    return "Resurrector";
  }

}
