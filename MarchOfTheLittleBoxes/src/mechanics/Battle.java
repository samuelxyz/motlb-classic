package mechanics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFrame;

import entity.Entity;
import entity.Particle;
import entity.projectile.Projectile;
import entity.unit.Cannon;
import entity.unit.Charger;
import entity.unit.Juggernaut;
import entity.unit.LaserUnit;
import entity.unit.MeleeUnit;
import entity.unit.RangedUnit;
import entity.unit.Resurrector;
import entity.unit.ShieldBearer;
import entity.unit.SmartRangedUnit;
import entity.unit.Unit;
import mechanics.lineTool.LineTool;

/**
 * File: src/mechanics/Battle.java
 * <P>
 * Organizes, runs, and draws the battle occurring.
 * Handles some user interaction such as mouse clicks.
 * 
 * @author Samuel Tan
 *
 */
public class Battle extends Canvas 
implements MouseListener, MouseMotionListener, Runnable
{
  private static long tick = 0;
  private int tps = 60;
  private boolean running = false;
  private boolean paused = true;
  private Thread battleThread;
  private JFrame window;
  private ControlPanel controlPanel;

  private BoundingBox borders, teamArea;
  
  private Color color;
  public static final Color 
  SAND = new Color(1.0f, 0.94f, 0.7f),
  GRASS = new Color(0.43f, 0.68f, 0.27f),
  DIRT = new Color(0.7f, 0.5f, 0.3f),
  SNOW = Color.WHITE,
  STONE = Color.LIGHT_GRAY;


  private List<Unit> units;
  private List<Projectile> projectiles;
  private List<Particle> particles;
  
  private LineTool lineTool;
  private boolean lineToolActive = false;
  private Vector2D mouseClick1, mousePos;
  
  private String action;
  private int selectedTeam;
  private int bannerTeam;

  private double defaultAngle = 0;

  public static final int HEIGHT = 800, WIDTH = 800;
  private boolean particlesEnabled, antialiasing;

  public Battle(JFrame window)
  {
    super();

    particlesEnabled = true;
    antialiasing = true;

    this.window = window;
    setSize(HEIGHT, WIDTH);
    setMinimumSize(new Dimension(HEIGHT, WIDTH));
    setPreferredSize(new Dimension(HEIGHT, WIDTH));
    setMaximumSize(new Dimension(HEIGHT, WIDTH));

    addMouseListener(this);
    addMouseMotionListener(this);
    setFocusable(true);

    setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

    selectedTeam = 0;
    action = "";
    bannerTeam = -1;

    borders = new BoundingBox(new Vector2D(), 
        Battle.WIDTH, 0, Battle.HEIGHT, 0, 0);
    
    color = Color.LIGHT_GRAY;

    units = Collections.synchronizedList(new ArrayList<Unit>());
    projectiles = Collections.synchronizedList(new ArrayList<Projectile>());
    particles = Collections.synchronizedList(new ArrayList<Particle>());
  }

  public void setControlPanel(ControlPanel controlPanel)
  {
    this.controlPanel = controlPanel;
  }

  public JFrame getWindow()
  {
    return window;
  }

  public synchronized void start()
  {
    battleThread = new Thread(this, "battle loop");
    running = true;
    battleThread.start();

    //    System.out.println("Battle: Thread started");
  }

  public synchronized void stop()
  {
    running = false;
    try
    {
      battleThread.join();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void run()
  {
    long lastTime = System.nanoTime();
    double tickLength = 1000000000 / tps;
    double dt = 0;
//    int frames = 0;
//    long frameTimer = System.currentTimeMillis();

    while (running)
    {
      long now = System.nanoTime();
      dt += (now - lastTime) / tickLength;
      lastTime = now;
      
      while (dt >= 1) // update multiple times if running late
      {
        if (!paused)
          update();
          
        dt--;
        
        if (dt < 1) // only once per tick
        {
          render();
//          frames++;
        }
      }

//      // print fps every second
//      if (System.currentTimeMillis() - timer > 1000)
//      {
//        frameTimer += 1000;
//        System.out.println("Performance: " + frames + " fps");
//        frames = 0;
//      }
    }
//    stop();
  }

  public boolean isRunning()
  {
    //    return clock.isRunning();
    return running;
  }

  public void setPaused(boolean p)
  {
    paused = p;

    //    String msg = "Battle: ";
    //    if (p)
    //      msg += "un";
    //    System.out.println(msg + "paused");
    //    System.out.println(units);
  }

  public boolean isPaused()
  {
    return paused;
  }

  /**
   * Updates units and projectiles. Removes inactive projectiles.
   */
  public void update()
  {
    //    System.out.println("Updoot " + tick);
    tick++;
    
    synchronized (units)
    {
//      boolean allDead = true;
      for (Unit u : units)
      {
        u.update();
//        if (u.isActive())
//          allDead = false;
      }
//      if (allDead)
//        setBanner(-1);
    }
    synchronized (projectiles)
    {
      ListIterator<Projectile> iter = projectiles.listIterator();
      while (iter.hasNext())
      {
        Projectile p = iter.next();
        p.update();
        if (!p.isActive())
          iter.remove();
      }
    }
    synchronized (particles)
    {
      ListIterator<Particle> iter = particles.listIterator();
      while (iter.hasNext())
      {
        Particle p = iter.next();
        p.update();
        if (!p.isActive())
          iter.remove();
      }
    }
  }

  public void render()
  {    
    BufferStrategy bs = this.getBufferStrategy();
    if (bs == null)
    {
      this.createBufferStrategy(3);
      return;
    }

    Graphics2D g = (Graphics2D) bs.getDrawGraphics(); 

    // background
    g.setColor(color);
    g.fillRect(0, 0, WIDTH, HEIGHT);
    
    if (antialiasing)
      g.setRenderingHints(new RenderingHints(
          RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON));
    
    // team area currently hardcoded to team 0, for campaign mode
    if (teamArea != null)
    {
//      g.setColor(Entity.teamColor(0));
//      g.setColor(Color.BLACK);
      g.setColor(color.darker());
      g.draw(teamArea.toPath2DD());
    }

    // line tool
    if (lineToolActive && mouseClick1 != null && mousePos != null)
    {
      g.setColor(Color.BLACK);
      g.drawLine((int) mouseClick1.x, (int) mouseClick1.y, (int) mousePos.x, (int) mousePos.y);
    }

    // TODO rendering priorities?
    if (particlesEnabled)
      synchronized(units)
      {
        for (Particle p : particles)
          p.render(g);
      }
    
    drawBanner(g);
    
    synchronized(units)
    {
      for (Unit u : units)
        u.render(g);
    }
    synchronized(projectiles)
    {
      for (Projectile p : projectiles)
        p.render(g);
    }
    
    if (lineTool != null)
      lineTool.render(g);

    g.dispose();
    bs.show();
  }


  /**
   * Draws a banner as a background at top of screen,
   * if only one team remains.
   */
  private void drawBanner(Graphics2D g)
  {
    if (bannerTeam != -1 && !paused)
    {
      String banner;
      if (controlPanel instanceof CampaignPanel)
      { // campaign mode, red vs blue
        if (bannerTeam == 0)
        {
          if (isBankrupt())
            banner = "Defeat!";
          else
            return; // no need for banner, player isn't done
        }
        else
        {
          banner = "Victory!";
        }
      }
      else // sandbox mode
      {
        banner = Entity.teamName(bannerTeam) + " victory!";
      }
  
      g.setColor(Entity.teamColor(bannerTeam));
      g.fillRect(0, 0, WIDTH, 21);
  
      g.setColor(Color.WHITE);
//      g.setFont(g.getFont().deriveFont(Font.BOLD));
      // general antialiasing is already enabled
//      g.addRenderingHints(new RenderingHints(
//          RenderingHints.KEY_TEXT_ANTIALIASING,
//          RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
      g.drawString(banner, 5, 15);
    }
  }

  public boolean particlesEnabled()
  {
    return particlesEnabled;
  }
  
  public void setParticlesEnabled(boolean enabled)
  {
    particlesEnabled = enabled;
  }
  
  public boolean antialiasing()
  {
    return antialiasing;
  }
  
  public void setAntialiasing(boolean enabled)
  {
    antialiasing = enabled;
  }
  
  public boolean add(Entity e)
  {
    if (e instanceof Unit)
    {
      synchronized(units)
      {
        return units.add((Unit) e);
      }
    }
    else if (e instanceof Projectile)
    {
      synchronized(projectiles)
      {
        return projectiles.add((Projectile) e);
      }
    }
    else if (e instanceof Particle)
    {
      synchronized(particles)
      {
        return particles.add((Particle) e);
      }
    }
    else
    {
      return false;
    }
  }

  /**
   * If e is a unit, it will be removed from the battle.
   * Corresponding projectiles and particles will remain.
   * In campaign mode, the unit's price will be refunded.
   */
  public boolean remove(Entity e)
  {
    if (e instanceof Unit)
    {
      refund((Unit)e);
//      System.out.println("Tick " + tick + ": Removed " + o);
      return units.remove(e);
    }
    // shouldn't need to use
    //    else if (o instanceof Projectile)
    //    {
    //      synchronized(projectiles)
    //      {
    //        return projectiles.remove(o);
    //      }
    //    }
    else
      return false;
  }
  
  public int refund(Unit u)
  {
    int p = actionPrice(u.type());
    if (controlPanel instanceof CampaignPanel)
    {
      ((CampaignPanel)controlPanel).incrementResources(p);
      return p;
    }
    return 0;
  }

  public long tick()
  {
    return tick;
  }

  public List<Unit> units()
  {
    return units;
  }

  public List<Projectile> projectiles()
  {
    return projectiles;
  }

  public BoundingBox borders()
  {
    return borders;
  }
  
  public void setColor(Color c)
  {
    color = c;
  }
  
  public Color color()
  {
    return color;
  }

  public void clearAll()
  {
    boolean temp = paused;
    paused = true;
    
    synchronized(units)
    {
      units.clear();
    }
    synchronized(projectiles)
    {
      projectiles.clear();
    }
    synchronized(particles)
    {
      particles.clear();
    }
    
    teamArea = null;
    mouseClick1 = null;
    bannerTeam = -1;
    if (lineTool != null)
      lineTool.dispose();
    
    paused = temp;
    //    System.out.println("Cleared battlefield");
  }

  public void resurrectAll()
  {
    synchronized(units)
    {
      for (Unit u : units)
        u.resurrect();
    }
    //    System.out.println("Resurrected everyone");
  }

  public void setAction(String action)
  {
      this.action = action;
    //    System.out.println("Action updated to " + action);
  }
  
  public int actionPrice(String name)
  {
    for (String type : controlPanel.types())
    {
      if (type.startsWith("["))
      {
//        System.out.println(type.substring(type.indexOf("]") + 2));
        if (type.equals(name) || type.substring(type.indexOf("]") + 2).equals(name))
        {
          try
          {
//            System.out.println(type.substring(1, type.indexOf("]", 1)));
            return Integer.parseInt(type.substring(1, type.indexOf("]", 1)));
          }
          catch (NumberFormatException ex)
          {
            ex.printStackTrace();
          }
          break;
        }
      }
    }
    
    return 0;
  }
  
  /**
   * For campaign mode.
   * @return Whether there's nothing more the player can buy.
   */
  private boolean isBankrupt()
  {
    if (controlPanel instanceof CampaignPanel)
    {
      int resources = ((CampaignPanel)controlPanel).remainingResources();
      for (String type : controlPanel.types)
      {
        int price = actionPrice(type);
        if (0 < price && price <= resources)
          return false; // can still afford something
      }
      return true;
    }
    else // not campaign mode
      return false;
  }

  public int selectedTeam()
  {
    return selectedTeam;
  }

  public void setSelectedTeam(int team)
  {
    selectedTeam = team;
  }
  
  public void setTeamArea(BoundingBox area)
  {
    teamArea = area;
  }
  
  public BoundingBox teamArea()
  {
    return teamArea;
  }
  
  public void setBanner(int team)
  {
    bannerTeam = team;
  }
  
  public void startLineTool()
  {
    lineToolActive = true;
  }
  
  public void refreshLineTool()
  {
    if (lineTool != null)
      lineTool.refresh();
  }

  public void disposeLineTool()
  {
    lineTool = null;
  }

  public void mouseClicked(MouseEvent ev)
  {
    //    System.out.println("Mouse click detected at " + ev.getPoint());

    if (lineToolActive)
    {
      if (mouseClick1 != null)
      {
        lineTool = new LineTool(this, mouseClick1, new Vector2D(ev.getPoint()), true);
        controlPanel.setText("");
        lineToolActive = false;
        mouseClick1 = null;
      }
      else
      {
        mouseClick1 = new Vector2D(ev.getPoint());
      }

      return;
    }

    lineTool = null;
    if (action.equals("None"))
    { 
      // this might do something eventually?
    }
    else if (action.equals("Remove (click target)"))
    {
      for (Unit u : units)
      {
        if (u.isActive()
            && u.hitbox().containsAbsPoint(new Vector2D(ev.getPoint())))
        {
          remove(u);
//          render();
          return;
        }
      }
    }
    else
      newUnit(new Vector2D(ev.getPoint()), defaultAngle, true);
  }

  public void newUnit(Vector2D pos, double ang, boolean autoFace)
  {
    if (autoFace)
    {
      Vector2D facing = borders.findClosestEdge(pos);
      facing.scaleBy(-1);
      ang = facing.angle();
    }

    Unit u = unitHelper(pos, ang, autoFace, true);

    if (u == null)
      return;

    u.resurrect();
//    render();
  }

  /**
   * Specifies the different types of units. All unit types must be specified here.
   * This method is called to create a new unit in the battle.
   * <P>
   * In campaign mode, this method will decrement the level resources by the price
   * of the unit, if it is successfully created.
   * 
   * @param pos         Will be copied to determine the unit's position.
   * @param ang         The angle that the created unit will face if autoFace is false.
   * @param autoFace    If true, the created unit will face the cardinal direction
   *                    closest to that facing the center of the battle.
   * @param addToBattle If true, the unit will be automatically added to the battle.
   * 
   * @return the created unit, which will be of whatever type is named in {@code action}.
   *    Returns null if funds are insufficient in campaign mode, 
   *    or if the position specified was inside an opposing team area.
   */
  public Unit unitHelper(Vector2D pos, double ang, boolean autoFace, boolean addToBattle)
  {
    if (teamArea != null && teamArea.containsAbsPoint(pos) && selectedTeam != 0 || action == null)
      return null;
    
    if (controlPanel instanceof CampaignPanel && selectedTeam != 0)
    {
      if (!((CampaignPanel) controlPanel).incrementResources(-actionPrice(action)))
      {
//        System.out.println("Intercepted! Insufficient resources");
        return null;
      }
    }
    
    Battle b;
    if (addToBattle)
      b = this;
    else
      b = null;

    if (action.contains("Melee Unit"))
    {
      return new MeleeUnit(b, selectedTeam, new Vector2D(pos), ang);
    }
    else if (action.endsWith("Juggernaut"))
    {
      return new Juggernaut(b, selectedTeam, new Vector2D(pos), ang);
    }
    else if (action.endsWith("Smart Ranged Unit")) // careful, must be before Ranged Unit
    {
      return new SmartRangedUnit(b, selectedTeam, new Vector2D(pos), ang);
    }
    else if (action.endsWith("Ranged Unit"))
    {
      return new RangedUnit(b, selectedTeam, new Vector2D(pos), ang);
    }
    else if (action.endsWith("Laser Unit"))
    {
      return new LaserUnit(b, selectedTeam, new Vector2D(pos), ang);
    }
    else if (action.endsWith("Cannon"))
    {
      return new Cannon(b, selectedTeam, new Vector2D(pos), ang);
    }
    else if (action.endsWith("Shield Bearer"))
    {
      return new ShieldBearer(b, selectedTeam, new Vector2D(pos), ang);
    }
    else if (action.endsWith("Charger"))
    {
      return new Charger(b, selectedTeam, new Vector2D(pos), ang);
    }
    else if (action.endsWith("Resurrector"))
    {
      return new Resurrector(b, selectedTeam, new Vector2D(pos), ang);
    }

    else
      return null;
  }

  public void mousePressed(MouseEvent e){}

  public void mouseReleased(MouseEvent e){}

  public void mouseEntered(MouseEvent e)
  {
    requestFocusInWindow();
  }

  public void mouseExited(MouseEvent e){}

  public void mouseDragged(MouseEvent e) {}

  public void mouseMoved(MouseEvent e)
  {
    if (lineToolActive)
    {
      mousePos = new Vector2D(e.getPoint().getX(), e.getPoint().getY());
    }
    if ("Remove (click target)".equals(action))
    {
      boolean hovering = false;
      for (Unit u : units)
        if (u.isActive() 
            && u.hitbox().containsAbsPoint(new Vector2D(e.getPoint())))
        {
          setCursor(new Cursor(Cursor.HAND_CURSOR));
          hovering = true;
          break;
        }
      if (!hovering)
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }
  }

}