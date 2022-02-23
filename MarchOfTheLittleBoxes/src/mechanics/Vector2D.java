package mechanics;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;

/**
 * File: src/mechanics/Vector2D.java
 * <P>
 * Vectors for math and physics, with useful methods.
 * These are 2-dimensional and use <code>double</code> precision.
 * Most often used to represent positions, velocities, or directions.
 * 
 * @author Samuel Tan
 */


public class Vector2D
{
  public double x, y;
  
  /**
   * Creates a new zero vector.
   */
  public Vector2D()
  {
    x = 0;
    y = 0;
  }
  
  /**
   * Creates a new vector given x and y coordinates of the tip.
   * 
   * @param x The x-value of the vector
   * @param y The y-value of the vector
   */  
  public Vector2D(double x, double y)
  {
    this.x = x;
    this.y = y;
  }
  
  /**
   * Creates a new vector by copying an existing vector.
   * @param other The vector to be copied.
   */
  public Vector2D(Vector2D other)
  {
    this.set(other);
  }
 
  public Vector2D(Point p)
  {
    x = p.getX();
    y = p.getY();
  }
  
  public void set(Vector2D input)
  {
    x = input.x;
    y = input.y;
  }
  
  public void set(double x, double y)
  {
    this.x = x;
    this.y = y;
  }
  
  /** 
   * @return The magnitude of the vector.
   */  
  public double length()
  {
    return Math.hypot(x, y);
  }
  
  /** 
   * @return The angle of the vector measured in radians,
   * counterclockwise from the positive x-axis.
   */  
  public double angle()
  {
    return Math.atan2(y, x);
  }
  
  /**
   * 
   * @param v1 A vector.
   * @param v2 Another vector.
   * 
   * @return The angle between the two given vectors, in radians.
   *         Always positive.
   */
  public static double angleBetween(Vector2D v1, Vector2D v2)
  {
    return Math.abs( v1.angle() - v2.angle() );
  }
  
  /**
   * Rotates the current vector counterclockwise around (0,0) 
   * by the given amount. Automatically calls fixRounding().
   * 
   * @param angle Measured in radians.
   */
  public void rotateBy(double angle)
  {
    double oldX = x;
    double oldY = y;
    x = Math.cos(angle) * oldX - Math.sin(angle) * oldY;
    y = Math.sin(angle) * oldX + Math.cos(angle) * oldY;
    fixRounding();
  }
  /**
   * Rotates the current vector around (0,0) so that it is pointing 
   * at the given angle.
   * 
   * @param targetAngle Measured counterclockwise from the positive
   *                    x-axis, in radians.
   */
  public void rotateTo(double targetAngle)
  {
    rotateBy(targetAngle - angle());
  }
  
  /**
   * 
   * @param v1
   * @param v2
   * 
   * @return The dot product of the given vectors.
   */
  public static double dotProduct(Vector2D v1, Vector2D v2)
  {
    return v1.x * v2.x + v1.y * v2.y;
  }
  
  /**
   * Adds the given vector to the current vector.
   * 
   * @param addend
   */
  public void add(Vector2D addend)
  {
    x += addend.x;
    y += addend.y;
  }
  
  public void add(double x, double y)
  {
    this.x += x;
    this.y += y;
  }
  
  /**
   * Doesn't modify this vector.
   * @param other
   */
  public Vector2D plus(Vector2D other)
  {
    Vector2D result = new Vector2D(this);
    result.add(other);
    return result;
  }
  
  /**
   * Subtracts a given vector from the current vector.
   * The result is the vector pointing from the given vector 
   * to the current vector.
   * 
   * @param subtrahend The vector to be subtracted by.
   */
  public void subtract(Vector2D subtrahend)
  {
    add(subtrahend.scaledBy(-1));
  }
  
  /**
   * Doesn't modify this vector.
   */
  public Vector2D minus(Vector2D other)
  {
    Vector2D result = new Vector2D(this);
    result.subtract(other);
    return result;
  }
  
  /**
   * Scales the current vector by the given ratio.
   * @param scalar
   */
  public void scaleBy(double scalar)
  {
    x *= scalar;
    y *= scalar;
  }
  
  /**
   * Scales this to the given length.
   * @param length
   */
  public void scaleTo(double length)
  {
    normalize();
    scaleBy(length);
  }
  
  /**
   * Returns the result of scaling the current vector 
   * by the given ratio, leaving the original unchanged.
   * 
   * @param scalar
   */
  public Vector2D scaledBy(double scalar)
  {
    Vector2D result = new Vector2D(this);
    result.scaleBy(scalar);
    return result;
  }
  
  /**
   * Scales the current vector so that its length is 1, 
   * without changing the angle. 
   * Automatically calls fixRounding().
   */
  public void normalize()
  {
    scaleBy(1.0 / length());
    fixRounding();
  }
  
  /**
   * Returns the result of normalizing the current vector,
   * without actually modifying the current vector.
   */
  public Vector2D getUnitVector()
  {
    Vector2D result = new Vector2D(this);
    result.normalize();
    return result;
  }
    
  /**
   * Returns whichever of the two input vectors is farthest 
   * in the direction of the input direction vector, or 
   * if they're equal, returns the first one.
   * 
   * If there are no input vectors or the direction is undefined,
   * returns null.
   */
  public static Vector2D mostExtreme(Collection<Vector2D> vectors,
      Vector2D direction)
  {
    if (vectors.isEmpty() || direction.length() == 0)
      return null;
    
    Iterator<Vector2D> iter = vectors.iterator();
    
    Vector2D farthest = iter.next();
    double maxVal = Vector2D.dotProduct(farthest, direction);
    
    while (iter.hasNext())
    {
      Vector2D vec = iter.next();
      
      double val = Vector2D.dotProduct(vec, direction);
      if (val > maxVal)
      {
        maxVal = val;
        farthest = vec;
      }
    }
    return farthest;
  }
  
  public static void dilate(Collection<Vector2D> vectors, 
      Vector2D focus, double factor)
  {
    for (Vector2D v : vectors)
    {
      v.subtract(focus);
      v.scaleBy(factor);
      v.add(focus);
    }
  }
  
  /**
   * Three decimal places.
   */
  @Override
  public String toString()
  {
    return String.format("[%.3f, %.3f]", x, y);
  }
  
  /**
   * Uses a tolerance of 10^-12 and checks whole numbers only.
   */
  public void fixRounding()
  {
    if (Math.abs(Math.round(x) - x) < 1.0e-12)
      x = Math.round(x);
    if (Math.abs(Math.round(y) - y) < 1.0e-12)
      y = Math.round(y);
  }
  
  /**
   * Truncates each component to an integer.
   */
  public Point toPoint()
  {
    return new Point((int) x, (int) y);
  }
  
  /**
   * Compares exact x and y values.
   */
  @Override
  public boolean equals(Object other)
  {
    return other instanceof Vector2D
        && x == ((Vector2D) other).x 
        && y == ((Vector2D) other).y;
  }
  
  /**
   * 
   * @return A vector with the specified length,
   * pointing in a random direction.
   */
  public static Vector2D randomDirection(double length)
  {
    Vector2D res = new Vector2D(length, 0);
    res.rotateBy(Math.random() * 2 * Math.PI);
    return res;
  }
}
