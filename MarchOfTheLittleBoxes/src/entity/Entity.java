package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import mechanics.Battle;
import mechanics.BoundingBox;
import mechanics.Vector2D;

/**
 * File: src/entity/Entity.java
 * <P>
 * Anything that participates in a battle is a subclass of this.
 * Handles basic details such as position, velocity, and team.
 * Team number/color correspondence is specified here.
 * 
 * @author Samuel Tan
 *
 */
public abstract class Entity
{
  public static final int TEAMS = 6;
  protected int team;
  protected Battle battle;
  protected Vector2D position, velocity;
  protected boolean active;

  /**
   * Sets fields, only.
   */
  public Entity(int team, Battle battle, Vector2D position, Vector2D velocity, boolean active)
  {
    this.team = team;
    this.battle = battle;
    
    this.position = position;
    this.velocity = velocity;
    
    this.active = active;
  }

  public abstract void update();

  public abstract void render(Graphics2D g);

  public void move()
  {
    position.add(velocity);
  }

  public Color teamColor()
  {
    return teamColor(team);
  }
  
  public static Color teamColor(int team)
  {
    switch (team)
    {
    case 0: // red
      return Color.RED;
    case 1: // blue
      return new Color(0.0f, 0.4f, 1.0f);
    case 2: // green
      return Color.GREEN;
    case 3: // yellow
      return Color.ORANGE;
    case 4: // purple
      return Color.MAGENTA;
    default: // gray
      return Color.LIGHT_GRAY;
    }
  }
  
  public static String teamName(int team)
  {
    switch (team)
    {
    case 0:
      return "Red";
    case 1:
      return "Blue";
    case 2:
      return "Green";
    case 3:
      return "Yellow";
    case 4:
      return "Purple";
    case 5:
      return "Gray";
    default:
      return "Team " + team;
    }
  }

  public boolean isActive()
  {
    return active;
  }
  
  public void setActive(boolean b)
  {
    active = b;
  }
  
  public void checkBorders()
  {
    BoundingBox borders = battle.borders();
    
    if (position.x > borders.xMax)
    {
      position.x = borders.xMax;
      velocity.x = 0;
    }
    if (position.x < borders.xMin)
    {
      position.x = borders.xMin;
      velocity.x = 0;
    }
    if (position.y > borders.yMax)
    {
      position.y = borders.yMax;
      velocity.y = 0;
    }
    if (position.y < borders.yMin)
    {
      position.y = borders.yMin;
      velocity.y = 0;
    }
  }
  
  public Vector2D position()
  {
    return position;
  }
  
  public Vector2D velocity()
  {
    return velocity;
  }
  
  public int team()
  {
    return team;
  }

}
