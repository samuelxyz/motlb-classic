package entity.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import entity.Entity;
import entity.Particle;
import mechanics.Battle;
import mechanics.BoundingBox;
import mechanics.Vector2D;

/**
 * File: src/entity/unit/Unit.java
 * <P>
 * The basic outline for a unit. Handles universal tasks such as
 * integrating into the {@code battle}, keeping track of health, and
 * detecting collisions.
 * 
 * @author Samuel Tan
 *
 */
public abstract class Unit extends Entity
{
  protected double health;
  protected int attackCooldown;
  protected BoundingBox hitbox;
  
  protected Unit nearestTarget;
  
  // stats
  protected /*static*/ double
  
    inertia = 10,
    acceleration = 0.1,
    topSpeed = 1,
    rotationSpeed = 0.1,
    
    baseHealth = 100,
    attackStrength = 0,
    knockback = 0,
    
    particleRate = 0.1, // per tick
    particleDuration = 200,
//    particleLimit = particleDuration * particleRate,
    particleSpeed = 0.05,
    particleSize = 3;
  
  protected /*static*/ int attackInterval = 30;

  // visual effects
//  protected List<Particle> movementParticles;
//  protected int particleTime; // for particle initialization

  /**
   * IMPORTANT: CALL {@code resurrect()} AFTERWARD! 
   * {@code Active} is initially set to {@code false}
   *  to avoid rendering before fully constructed. 
   *  
   *  If battle is supplied, this is added to it.
   */
  public Unit(Battle b, int t, Vector2D pos, double ang)
  {
    super(t, b, new Vector2D(pos), new Vector2D(), false);
    
//    movementParticles = new ArrayList<Particle>();
//    particleTime = 0;
    
    if (battle != null)
      battle.add(this);
    
    health = baseHealth;
    attackCooldown = (int) (Math.random() * attackInterval); // initialize randomly
    hitbox = new BoundingBox(position, 10, -10, 10, -10, ang);
    
    if (battle != null)
    {
      findNearestTarget();
    }
    
//    System.out.println(getClass().getName() + " constructed at " + pos + ": Team " + t);
  }
  
  public void update()
  {
//    System.out.println(this + " update");

    if (!isActive())
    {
//      movementParticles.clear();
//      particleTime = 0;
      return;
    }
    
    checkHealth();
    
    findNearestTarget();
    rotate();
    accelerate();
    move();
    checkCollision();
    
    checkAttack();
    
    createMovtParticles();
//    for (Particle p : movementParticles)
//      p.update();
      
    // any other stuff?
    
  }
  
//  protected void createMovtParticles()
//  {
//    
//    if (movementParticles.size() < particleLimit)
//    {
//      while (movementParticles.size() < particleTime * particleRate)
//      // while (number of particles < what it should be given the time)
//      {
////        System.out.println("Attempting to add new particle");
//
//        movementParticles.add(new Particle(battle, team,
//            hitbox.randomInteriorPos(), 
//            Vector2D.randomDirection(particleSpeed),
//            particleSize, -particleSize/particleDuration));
//      }
//      particleTime++;
//    }
//    else
//    {
//      // all particles already exist
//      ListIterator<Particle> iter = movementParticles.listIterator();
//
//      // if any inactive, replace with new particle
//      while (iter.hasNext())
//      {
//        if (!(iter.next().isActive()))
//        {
//          iter.set(new Particle(battle, team, hitbox.randomInteriorPos(), 
//            Vector2D.randomDirection(particleSpeed),
//            particleSize, -particleSize/particleDuration));
//        }
//      }
//    }
//  }
  
  protected void createMovtParticles()
  {
    double particles = particleRate;
    while (particles >= 1)
    {
      particles--;
      addParticle();
    }
    // now particles should be fractional or zero; handle this remaining portion
    if (Math.random() < particles)
      addParticle();
  }
  
  private void addParticle()
  {
    battle.add(new Particle(battle, -1, hitbox.randomInteriorPos(), 
        Vector2D.randomDirection(particleSpeed), particleSize, 
        -particleSize/particleDuration));
  }

  /**
   * Updates the unit's position according to its velocity.
   * Checks to ensure in bounds.
   */
  public void move()
  {
    super.move();
    checkBorders();
  }

  protected void checkHealth()
  {
    if (health <= 0)
    {
//      System.out.println(this + ": Going inactive");
      setActive(false);
      health = 0;
    }
  }
  
  public void checkCollision()
  {
    for (Unit u : battle.units())
    {
      if(u.isActive() && !(u == this))
      {
        doCollision(u);
      }
    }
  }
  
  protected void doCollision(Unit other)
  {
    if (!BoundingBox.detectOverlapTwoWay(this.hitbox, other.hitbox))
      return;
    
    Vector2D translation = BoundingBox
        .calcCollisionTwoWay(this.hitbox, other.hitbox);
    
    if (translation.length() == 0)
      return;
    
//    System.out.println("Colliding units: " + this + "\n\tagainst: " + other
//        + "\n\tCalculated translation: " + translation);
    
    double totalInertia = this.inertia + other.inertia;
    
    // distribute the translation between the two units
    this.position.add(translation.scaledBy(-this.inertia / totalInertia));
    other.position.add(translation.scaledBy(other.inertia / totalInertia));
    
//    System.out.println("\tResults: " + this + "\n\t\t " + other);
    
    this.checkBorders();
    other.checkBorders();
  }
  
  protected void checkAttack()
  {
    attackCooldown--;
    if (attackCooldown <= 0)
    {
      attack();
      attackCooldown = attackInterval; // reset 
    }
  }
  
  /**
   * What happens when i attack? O_o
   */
  public abstract void attack();

  protected void rotate()
  {    
    if (nearestTarget != null)
    {
      double targetAngle = nearestTarget.position
          .minus(this.position)
          .angle();
      
//      System.out.println("targetAngle: " + targetAngle);
      
      double rotation;
      
      if (Math.abs(targetAngle - hitbox.angle)
          <= rotationSpeed)
      {
        rotation = targetAngle - hitbox.angle;
      }
      else
      {
        rotation = Math.copySign(
            rotationSpeed, targetAngle - hitbox.angle);
      }
      
//      System.out.println(this + ": Rotating by " + rotation);
      hitbox.rotateBy(rotation);
    }
  }
  
  /**
   * Accelerates the unit in the direction it's facing, respecting top speeds;
   * if it's going faster than top speed, slows down.
   * 
   * If it's drifting sideways in a direction that it's not facing, slows
   * down in that direction.
   */
  protected void accelerate()
  {
    Vector2D idealVelocity = new Vector2D(idealSpeed(), 0);
    idealVelocity.rotateBy(hitbox.angle);
    
    Vector2D deltaV = idealVelocity.minus(velocity);
    if (deltaV.length() > acceleration)
      deltaV.scaleTo(acceleration);
    
    velocity.add(deltaV);
  }
  
  /**
   * Updates nearestTarget to be the nearest <code>Unit</code> from 
   * this unit's <code>Battle</code> who is an enemy 
   * (<code>team</code is different), or <code>null</code> if no enemies exist. 
   */
  protected void findNearestTarget()
  { 

    boolean targetsExist = false;
    
    // select a starting point, making sure it isn't someone from our team
    for (Unit u : battle.units())
    {
      if (u.team != team && u.isActive())
      {
        nearestTarget = u;
        targetsExist = true;
        break;
      }
    }
    
    if (!targetsExist)
    {
      nearestTarget = null;
      battle.setBanner(team);
      return;
    }
    battle.setBanner(-1);
    
    // if we get to this point, we know enemies exist. Find the closest
    for (Unit u : battle.units())
    {
      if (u.team != team // our own team doesn't count
          && u.isActive()
          && u.position
            .minus(this.position)
            .length() // distance to u
          < 
          nearestTarget.position
            .minus(this.position)
            .length()) // distance to nearest
      {
        nearestTarget = u;
      }
    }
  }
  
  /**
   * @param source Can be a unit or projectile.
   * @param attackValue The amount of damage to be dealt
   * @param knockback A vector describing the impulse imparted
   * @return true if attack was not blocked
   */
  public boolean receiveAttack(Entity source, double attackValue, Vector2D knockback)
  {
    health -= attackValue;
//    System.out.println(toString() + " Receiving attack of " + attackValue);
    checkHealth();
    receiveImpulse(knockback);
    return true;
  }
  
  public void receiveImpulse(Vector2D impulse)
  {
//    System.out.println(toString() + " Receiving impulse of " + impulse);
    velocity.add(impulse.scaledBy(1.0 / inertia));
    move(); // maybe? Helps with weird collision thing
  }
  
  public void resurrect()
  {
    health = baseHealth;
    setActive(true);
  }

  public void setHealth(double h)
  {
    health = h;
  }
  
  /**
   * @return the "ideal" speed to move at, which is topSpeed
   * unless there are no enemies to worry about.
   */
  protected double idealSpeed()
  {
    return (nearestTarget == null)? 0 : topSpeed;
  }
  
  public BoundingBox hitbox()
  {
    return hitbox;
  }
  
  public double health()
  {
    return health;
  }
  
  public String toString()
  {
    return getClass().getName() + "["
        + "Team: " + team
        + ", Health: " + health
        + ", Pos: " + position
        + ", Vel: " + velocity
        + String.format(", Rot: %.3f", hitbox.angle)
        + "]";
  }
  
  @Override
  public void render(Graphics2D g)
  {
//    System.out.println(this + " render");
    
    if (!isActive())
      return;
    
//    for (Particle p : movementParticles)
//      p.render(g);
    
    Shape border = hitbox.toPath2DD();
    
    g.setColor(teamColor());
    g.fill(border);
    g.setColor(Color.BLACK);
    g.draw(border);
    
    renderSpecial(g);
  }
  
  /**
   * A distinct appearance or symbol.
   */
  protected abstract void renderSpecial(Graphics2D g);
  
  /**
   * Sets this.battle to b, adds this to b, resurrects this,
   * and initializes nearest target.
   */
  public void addBattle(Battle b)
  {
    this.battle = b;
    b.add(this);
    resurrect();
    findNearestTarget();
  }
  
  @Override
  public void checkBorders()
  {
    position.add(battle.borders().calcContainment(hitbox));
  }
  
  public String type()
  {
    return "Unit";
  }
}
