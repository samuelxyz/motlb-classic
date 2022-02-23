
package mechanics;

import java.awt.Polygon;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * File: src/mechanics/BoundingBox.java
 * <P>
 * These can be used as hitboxes, collision boxes, etc.
 * They must be rectangular, but can rotate. Methods in this
 * class often distinguish between "relative" and "absolute" coordinates
 *  - see the {@code toAbs()} and {@code toRelative()} method descriptions.
 * 
 * @author Samuel Tan
 */
public class BoundingBox
{
  
  public Vector2D position;
  public double xMax, xMin, yMax, yMin, angle;
  
  /**
   * Creates a new BoundingBox based on all this stuff.
   */
  public BoundingBox(Vector2D position, double xMax, double xMin,
      double yMax, double yMin, double angle)
  {
    this.position = position;
    this.xMax = xMax;
    this.xMin = xMin;
    this.yMax = yMax;
    this.yMin = yMin;
    this.angle = angle;
  }
  
  /**
   * Creates an axis-aligned bounding box using the two given
   * absolute corners, placing the {@code position} directly between them.
   */
  public BoundingBox(Vector2D corner1, Vector2D corner2)
  {
    Vector2D pos = corner1.plus(corner2).scaledBy(0.5);
    
    double top = Math.max(corner1.y, corner2.y);
    double bottom = Math.min(corner1.y, corner2.y);
    double right = Math.max(corner1.x, corner2.x);
    double left = Math.min(corner1.x, corner2.x);
    
    this.position = pos;
    this.xMax = right - pos.x;
    this.xMin = left - pos.x;
    this.yMax = top - pos.y;
    this.yMin = bottom - pos.y;
    this.angle = 0;
  }
  
  /** Pretends that this bounding box is aligned to 
   * xy axes at (0, 0), and returns a transformed 
   * version of <code>point</code> so we can compare it 
   * with xMin, xMax, etc.
   *  
   *  @param point - will not be modified
   */
  public Vector2D toRelative(Vector2D point)
  {
    Vector2D p = new Vector2D(point);
    p.subtract(this.position);
    p.rotateBy(-this.angle); 
    return p;
  }
  
  /**
   * Converts the given point from relative coordinates to absolute
   *  - meaning its position in the coordinate space where this
   *  BoundingBox's position is specified.
   */
  public Vector2D toAbs(Vector2D point)
  {
    Vector2D p = new Vector2D(point);
    p.rotateBy(this.angle);
    p.add(this.position);
    return p;
  }
  
  /**
   * Checks if the current bounding box contains the given point.
   * Being on the edge counts as being inside.
   */
  public boolean containsAbsPoint(Vector2D point)
  {
    return containsRelPoint(toRelative(point));
  }
  
  public boolean containsRelPoint(Vector2D point)
  {
    return point.x <= xMax && point.x >= xMin && point.y <= yMax && point.y >= yMin;
  }
  
  /**
   * Returns the vector pointing to the nearest edge of the current bounding box
   * from the given absolute point.
   * Favors the edges with lower x/y coordinates.
   * 
   * Note: Use containsPoint first to check that the point is inside. 
   * 
   * @param point - absolute coordinates
   */
  public Vector2D findClosestEdge(Vector2D point)
  {
    double minDistX, minDistY;
    Vector2D p = toRelative(point);
    
    // which X side is closer?
    minDistX = ( Math.abs(xMax - p.x) < Math.abs(xMin - p.x) )
      ? xMax - p.x : xMin - p.x; // signs will reflect direction
    
    // which Y side is closer?
    minDistY = ( Math.abs(yMax - p.y) < Math.abs(yMin - p.y) )
      ? yMax - p.y : yMin - p.y;
    
    // is X or Y closer?
    return (Math.abs(minDistX) > Math.abs(minDistY))? 
        new Vector2D(0, minDistY) : new Vector2D(minDistX, 0);
    
  }
  
  /**
   * 
   * @param point - Absolute coordinates.
   * @param side - Relative to this box. Must be an exact cardinal direction,
   *  so either x or y must be 0.  
   */
  public double distanceToEdge(Vector2D point, Vector2D side)
  {
    Vector2D p = toRelative(point);
    
    if (side.x > 0 && side.y == 0)
      return xMax - p.x;
    else if (side.x < 0 && side.y == 0)
      return xMin - p.x;
    else if (side.y > 0 && side.x == 0)
      return yMax - p.y;
    else if (side.y < 0 && side.x == 0)
      return yMin - p.y;
    
    // if we still haven't returned then 
    // the vector wasn't a cardinal direction
    throw new IllegalArgumentException(
        "Side wasn't a cardinal direction");
  }
  
  /**
   * Checks if two bounding boxes are overlapping. 
   * Touching counts as overlapping.
   */
  public static boolean detectOverlapTwoWay(
      BoundingBox box1, BoundingBox box2)
  {
    return (box1.detectOverlapOneWay(box2) 
        || box2.detectOverlapOneWay(box1));
  }
  
  /**
   * Checks if a given bounding box has any corners inside the current one. 
   * Touching counts as inside.
   */
  public boolean detectOverlapOneWay(BoundingBox otherBox)
  {
    Vector2D corner1 = otherBox.toAbs(new Vector2D(otherBox.xMax, otherBox.yMax));
    Vector2D corner2 = otherBox.toAbs(new Vector2D(otherBox.xMax, otherBox.yMin));
    Vector2D corner3 = otherBox.toAbs(new Vector2D(otherBox.xMin, otherBox.yMax));
    Vector2D corner4 = otherBox.toAbs(new Vector2D(otherBox.xMin, otherBox.yMin));
    
    return (containsAbsPoint(corner1) || containsAbsPoint(corner2) || containsAbsPoint(corner3) || containsAbsPoint(corner4));
  }
  
/**
 * Returns the shortest absolute vector
 * that removes all corners of other from this box.
 * If other has no corners in this box, returns a zero vector.
 * @param other
 * @return
 */
  private Vector2D calcCollisionOneWay(BoundingBox other)
  {
    if (!detectOverlapOneWay(other))
      return new Vector2D(); // a zero vector
    
    // if we proceed to this part, we know other has at least one corner in this box
    // get corners of other that are in this box
    ArrayList<Vector2D> corners = other.absCorners();
    ArrayList<Vector2D> inCorners = new ArrayList<Vector2D>();
    for (Vector2D c : corners)
    {
      if (other.containsAbsPoint(c))
      {
        inCorners.add(toRelative(c));
      }
    }
    
    // the plan is to try moving other in each cardinal direction, find shortest move
    Vector2D finalMoveDecision = new Vector2D();
    Vector2D shortestMoveCandidate = new Vector2D();
    Vector2D extremePoint = new Vector2D();
    Vector2D north = new Vector2D(0, 1);
    Vector2D south = new Vector2D(0, -1);
    Vector2D east = new Vector2D(1, 0);
    Vector2D west = new Vector2D(-1, 0);
    
    // examine each direction, comparing to see which direction is shortest
    
    // northward first
    extremePoint.set(Vector2D.mostExtreme(inCorners, south));
    
    finalMoveDecision.y = yMax - extremePoint.y;
    
    // southward
    extremePoint.set(Vector2D.mostExtreme(inCorners, north));
    
    shortestMoveCandidate.y = yMin - extremePoint.y;
    if (shortestMoveCandidate.length() < finalMoveDecision.length())
      finalMoveDecision.set(shortestMoveCandidate);
    
    // eastward
    extremePoint = Vector2D.mostExtreme(inCorners, west);      
    
    shortestMoveCandidate.x = xMax - extremePoint.x;
    shortestMoveCandidate.y = 0.0; // almost forgot this lol
    if (shortestMoveCandidate.length() < finalMoveDecision.length())
      finalMoveDecision.set(shortestMoveCandidate);
    
    // westward
    extremePoint.set(Vector2D.mostExtreme(inCorners, east));      
    
    shortestMoveCandidate.x = xMin - extremePoint.x;
    if (shortestMoveCandidate.length() < finalMoveDecision.length())
      finalMoveDecision.set(shortestMoveCandidate);
    
    /* finalMoveDecision should now be set to the shortest relative move 
     * that will take even the farthest corner of box2 out of this box */
    finalMoveDecision.rotateBy(this.angle);
    return finalMoveDecision;
        // convert to absolute vector
  }
  
   /**
   * Returns the shortest vector that will remove box2 from box1.
   * Or if they don't intersect, returns a zero vector.
   * 
   * @param box1
   * @param box2
   * @return
   */
  public static Vector2D calcCollisionTwoWay(BoundingBox box1, BoundingBox box2)
  {
    
    Vector2D box1Candidate = box1.calcCollisionOneWay(box2);
    Vector2D box2Candidate = box2.calcCollisionOneWay(box1);
    box2Candidate.scaleBy(-1); // so we always get the vector that removes box2 from box1, 
                               // not the other way around
//    System.out.println("Calculating two-way collision, with translation candidates "
//        + box1Candidate + " and " + box2Candidate);
    
    if (box2Candidate.length() == 0)
      return box1Candidate; // even if box1Candidate is also 0, we want it to return 0 anyway
    // Btw, if they're both zero then that means the boxes don't intersect, 
    // because that's how processCollisionOneWay works.
    
    if (box1Candidate.length() == 0)
      return box2Candidate;
    
    return (box1Candidate.length() <= box2Candidate.length())
     ? box1Candidate : box2Candidate;
    
  }
  
  public BoundingBox setAngle(double ang)
  {
    angle = ang;
    return this;
  }
 
  public BoundingBox rotateBy(double ang)
  {
    angle += ang;
    return this;
  }
  
  /**
   * 
   * @return Absolute coordinates.
   */
  public Polygon toPolygon()
  {
    ArrayList<Vector2D> corners = absCorners();
    int[] xPoints = new int[corners.size()];
    int[] yPoints = new int[corners.size()];
    int nPoints = corners.size();
    
    for (int i = 0; i < nPoints; i++)
    {
      xPoints[i] = (int) corners.get(i).x;
      yPoints[i] = (int) corners.get(i).y;
    }
    
    return new Polygon(xPoints, yPoints, nPoints);
  }
  
  /**
   * @return Absolute coordinates.
   */
  public Path2D.Double toPath2DD()
  {
    ArrayList<Vector2D> corners = absCorners();
    Path2D.Double path = new Path2D.Double();
    
    path.moveTo(corners.get(0).x, corners.get(0).y);
    
    for (int i = 1; i < corners.size(); i++)
      path.lineTo(corners.get(i).x, corners.get(i).y);
    
    path.lineTo(corners.get(0).x, corners.get(0).y);
    
    return path;
  }

  /**
   * @return Relatively, clockwise from bottom left
   */
  public ArrayList<Vector2D> absCorners()
  {
    ArrayList<Vector2D> result = relCorners();
    for (Vector2D v : result)
    {
      v.rotateBy(angle);
      v.add(position);
    }
    return result;
  }
  
  private ArrayList<Vector2D> relCorners()
  {
    ArrayList<Vector2D> result = new ArrayList<Vector2D>();
    
    result.add(new Vector2D(xMin, yMin));
    result.add(new Vector2D(xMin, yMax));
    result.add(new Vector2D(xMax, yMax));
    result.add(new Vector2D(xMax, yMin));

    return result;
  }
  
  /**
   * 
   * @return An absolute position that will be somewhere inside
   * (or on relative -x and -y edge of) this.
   */
  public Vector2D randomInteriorPos()
  {
    Vector2D dx = new Vector2D(
        Math.random() * (xMax - xMin) + xMin,
        Math.random() * (yMax - yMin) + yMin);
    dx.rotateBy(angle);
    return position.plus(dx);
  }
  
  /**
   * 
   * @param other
   * @return A vector that describes how {@code other}
   * must be translated in order to remain inside {@code this}
   * <P>
   * Returns a zero vector if {@code other} can't fit inside {@code this},
   * or possibly if {@code this.position} isn't inside {@code this}
   */
  public Vector2D calcContainment(BoundingBox other)
  {
    Vector2D trans = new Vector2D();
    
    List<Vector2D> corners = other.absCorners();
    for (Vector2D c : corners)
      c.set(toRelative(c)); // relative to this
    
    for (Vector2D c : corners)
    {
      // x
      if (c.x > xMax)
      {
        if (trans.x > 0)
          return new Vector2D();
        if (xMax - c.x < trans.x)
          trans.x = xMax - c.x;
      }
      else if (c.x < xMin)
      {
        if (trans.x < 0)
          return new Vector2D();
        if (xMin - c.x > trans.x)
          trans.x = xMin - c.x;
      }
      
      // y
      if (c.y > yMax)
      {
        if (trans.y > 0)
          return new Vector2D();
        if (yMax - c.y < trans.y)
          trans.y = yMax - c.y;
      }
      else if (c.y < yMin)
      {
        if (trans.y < 0)
          return new Vector2D();
        if (yMin - c.y > trans.y)
          trans.y = yMin - c.y;
      }
    }
    
    return toAbs(trans);
  }
}
