package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/MeleeUnit.java
 * <P>
 * This unit is the basic combat fighter, dealing melee damage
 * as its attack. It is more maneuverable than the typical ranged unit.
 * 
 * @author Samuel Tan
 *
 */
public class MeleeUnit extends Unit
{

  Vector2D attackPoint, absAttackPoint;
  
  /**
   * IMPORTANT: CALL {@code resurrect()} AFTERWARD! 
   * {@code Active} is initially set to {@code false}
   * to avoid rendering before fully constructed. 
   */
  public MeleeUnit(Battle b, int team, Vector2D pos, double ang)
  {
    super(b, team, pos, ang);
    
    attackStrength = 20;
    knockback = 20;
    
    attackPoint = new Vector2D(15, 0);
    updateAttackPoint();
  }
  
  @Override
  public void move()
  {
    super.move();
    updateAttackPoint();
  }
  
  @Override
  public void attack()
  {
//    System.out.println(this + ": Attempting attack, tick " + battle.tick());
    Vector2D knockback2D = new Vector2D(knockback, 0);
    knockback2D.rotateTo(hitbox.angle);
    
    for (Unit u : battle.units())
    {
      if (u.team() != team && u.isActive()
          && u.hitbox.containsAbsPoint(absAttackPoint))
      {
        u.receiveAttack(this, attackStrength, knockback2D);
      }
    }
  }
  
  /**
   * Will also initialize absAttackPoint if it is currently null.
   */
  protected void updateAttackPoint()
  {
    Vector2D result = new Vector2D(attackPoint);
    result.rotateTo(hitbox.angle);
    result.add(position);
    
    if (absAttackPoint == null)
      absAttackPoint = result;
    else
      absAttackPoint.set(result);
  }
  
  @Override
  protected void renderSpecial(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    List<Vector2D> c = hitbox.absCorners();
    g.drawLine((int)c.get(0).x, (int)c.get(0).y, (int)c.get(2).x, (int)c.get(2).y);
    g.drawLine((int)c.get(1).x, (int)c.get(1).y, (int)c.get(3).x, (int)c.get(3).y);
  }
  
  @Override
  public String type()
  {
    return "Melee Unit";
  }
  
}
