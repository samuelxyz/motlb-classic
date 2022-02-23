package entity.unit;

import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/Juggernaut.java
 * <P>
 * An extremely large, heavy, slow unit with a powerful melee attack.
 * 
 * @author Samuel Tan
 *
 */
public class Juggernaut extends MeleeUnit
{

  public Juggernaut(Battle b, int team, Vector2D pos, double ang)
  {
    super(b, team, pos, ang);
    
    hitbox.xMin = -20;
    hitbox.xMax = 20;
    hitbox.yMin = -20;
    hitbox.yMax = 20;
    
    attackStrength = 40;
    attackPoint = new Vector2D(25, 0);
    updateAttackPoint();
    knockback = 60;
    
    baseHealth = 600;
    inertia = 100;
    topSpeed = 0.5;
    acceleration = 0.05;
    rotationSpeed = 0.03;
  }
  
  @Override
  public String type()
  {
    return "Juggernaut";
  }

}
