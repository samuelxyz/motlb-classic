package mechanics.lineTool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;

import entity.unit.Unit;
import mechanics.Battle;
import mechanics.Vector2D;

/**
 * File: src/mechanics/lineTool/LineTool.java
 * <P>
 * Provides methods to facilitate the creation of lines of units,
 * which may take additional information from a dialog.
 * 
 * @author Samuel Tan
 *
 */
public class LineTool implements ActionListener
{
  private Vector2D start, end, ray, facing;
  private Battle battle;
  private static double ARROW_LENGTH = 50;
  private static double ARROW_HEAD = 10;
  private JDialog dialog;
  private List<Unit> list;
  private int count;
  public static final int EAST = 0, SOUTH = 1, WEST = 2, NORTH = 3;
  
  public LineTool(Battle battle, Vector2D start, Vector2D end, boolean showDialog)
  {
    this.battle = battle;
    list = Collections.synchronizedList(new ArrayList<Unit>());
    
    this.start = new Vector2D(start);
    this.end = new Vector2D(end);
    ray = end.minus(start);
    facing = new Vector2D(ray);
    facing.normalize();
    facing.rotateBy(Math.PI/2);
    
    if (showDialog)
      dialog = new LineToolDialog(battle.getWindow(), this);
  }
  
  /**
   * Use this to automatically generate a line of units with the given parameters.
   */
  public static void doLineTool(Battle battle, Vector2D start, Vector2D end, int facing, int count)
  {
    LineTool tool = new LineTool(battle, start, end, false);
    for (int i = 0; i < facing; i++)
      tool.facing.rotateBy(Math.PI/2);
    tool.makeLine(count);
    tool.confirm();
  }
  
  public void render(Graphics2D g)
  {
    // draw line
    Path2D.Double path = new Path2D.Double();
    path.moveTo(start.x, start.y);
    path.lineTo(end.x, end.y);
    g.setColor(Color.BLACK);
    g.draw(path);
    
    // draw "arrow" indicating facing
    // arrow shaft
    path = new Path2D.Double();
    Vector2D half = start.plus(end);
    half.scaleBy(0.5); // midpoint of the line
    path.moveTo(half.x, half.y);
    
    Vector2D point = facing.scaledBy(ARROW_LENGTH);
    point.add(half);
    path.lineTo(point.x, point.y);
    
    g.draw(path);
    
    // arrow head
    path = new Path2D.Double();
    path.moveTo(point.x, point.y);
    
    Vector2D side = ray.getUnitVector();
    side.scaleBy(ARROW_HEAD / 2);
    point.add(facing.scaledBy(-ARROW_HEAD));
    point.add(side);
    path.lineTo(point.x, point.y);
    
    point.add(side.scaledBy(-2));
    path.lineTo(point.x, point.y);
    path.closePath();
    
    g.fill(path);
    
    // render unit previews
    synchronized (list)
    {
      for (Unit u : list)
        u.render(g);
    }
  }
  
  public void toggleFacing()
  {
    facing.scaleBy(-1);
  }
  
  /**
   * Sets count to num. 
   */
  public void makeLine(int num)
  {
    if (num < 1)
      return;
    count = num;
    
    for (Unit u : list)
      battle.refund(u);
    list.clear();
    
    double ang = facing.angle();
    
    if (count == 1) // special case
    {
      Vector2D point = start.plus(ray.scaledBy(0.5)); // midpoint
      Unit u = battle.unitHelper(point, ang, false, false);
      if (u == null)
        return;
      u.setActive(true);
      
      synchronized(list)
      {
        list.add(u);
      }
    }
    else
    {
      Vector2D interval = ray.scaledBy(1.0 / (count - 1));
      Vector2D point = new Vector2D(start);
      for (int i = 0; i < count; i++)
      {
        Unit u = battle.unitHelper(point, ang, false, false);
        if (u == null)
          continue;
        u.setActive(true);
        synchronized(list)
        {
          list.add(u);
        }
        point.add(interval);
      }
    }
  }
  
  public void confirm()
  {
    for (Unit u : list)
      u.addBattle(battle);
    list.clear();
    dispose();
  }
  
  public void refresh()
  {
    makeLine(count);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getActionCommand().equals("OK"))
    {
      confirm();
    }
    else if (e.getActionCommand().equals("Cancel"))
    {
      dispose();
    }
    else if (e.getActionCommand().equals("Flip direction"))
    {
      toggleFacing();
      refresh();
    }
  }
  
  public void dispose()
  {
    for (Unit u : list)
      battle.refund(u);
    
    battle.disposeLineTool();
    if (dialog != null)
      dialog.dispose();
  }
}
