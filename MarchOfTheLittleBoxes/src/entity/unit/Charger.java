package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/Charger.java
 * <P>
 * A unit that moves extremely quickly and deals high knockback
 * with a melee attack.
 * 
 * @author Samuel Tan
 *
 */
public class Charger extends MeleeUnit
{

  public Charger(Battle b, int team, Vector2D pos, double ang)
  {
    super(b, team, pos, ang);
    
    topSpeed = 6;
    rotationSpeed = 0.3;
    
    attackInterval = 1;
    attackStrength = 1;
    knockback = 3;
    inertia = 30;
    
    particleRate = 0.6;
    particleDuration = 100;
  }
  
  @Override
  protected void renderSpecial(Graphics2D g)
  {
    // equilateral triangle centered at position
    double triangleSize = 5;
    
    Vector2D vec = new Vector2D(triangleSize,0);
    vec.rotateTo(hitbox.angle);
    
    Path2D.Double triangle = new Path2D.Double();
    triangle.moveTo(position.x + vec.x, position.y + vec.y);
    
    vec.rotateBy(Math.PI * 2.0/3.0);
    triangle.lineTo(position.x + vec.x, position.y + vec.y);
    
    vec.rotateBy(Math.PI * 2.0/3.0);
    triangle.lineTo(position.x + vec.x, position.y + vec.y);
    
    triangle.closePath();
    g.setColor(Color.BLACK);
    g.fill(triangle);
  }
  
  @Override
  public String type()
  {
    return "Charger";
  }

}
