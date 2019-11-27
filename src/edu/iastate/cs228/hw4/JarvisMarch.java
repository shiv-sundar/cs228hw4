package edu.iastate.cs228.hw4;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;

/**
 * 
 * @author Shivkarthi Sundar
 *
 */
public class JarvisMarch extends ConvexHull {
	private Point highestPoint; 

	private PureStack<Point> leftChain; 

	private PureStack<Point> rightChain; 

	/**
	 * Call corresponding constructor of the super class.  Initialize the variable algorithm 
	 * (from the class ConvexHull). Set highestPoint. Initialize the two stacks leftChain 
	 * and rightChain. 
	 * 
	 * @throws IllegalArgumentException  when pts.length == 0
	 */
	public JarvisMarch(Point[] pts) throws IllegalArgumentException {
		super(pts);
		algorithm = "JARVIS";
		highestPoint = pointsNoDuplicate[pointsNoDuplicate.length - 1];
		leftChain = new ArrayBasedStack<Point>();
		rightChain = new ArrayBasedStack<Point>();
	}


	/**
	 * Call corresponding constructor of the superclass.  Initialize the variable algorithm.
	 * Set highestPoint.  Initialize leftChain and rightChain.  
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public JarvisMarch(String inputFileName) throws FileNotFoundException, InputMismatchException {
		super(inputFileName);
		algorithm = "JARVIS";
		highestPoint = pointsNoDuplicate[pointsNoDuplicate.length - 1];
		leftChain = new ArrayBasedStack<Point>();
		rightChain = new ArrayBasedStack<Point>();
	}

	/**
	 * Calls createRightChain() and createLeftChain().  Merge the two chains stored on the stacks  
	 * rightChain and leftChain into the array hullVertices[].
	 * 
	 * Two degenerate cases below must be handled: 
	 * 
	 *     1) The array pointsNoDuplicates[] contains just one point, in which case the convex
	 *        hull is the point itself. 
	 *     
	 *     2) The array contains collinear points, in which case the hull is the line segment 
	 *        connecting the two extreme points from them.   
	 */
	public void constructHull() {
		if (pointsNoDuplicate.length == 1) {
			hullVertices = pointsNoDuplicate;
		}

		else {
			boolean[] b = new boolean[pointsNoDuplicate.length];
			for (int x = 0; x < b.length - 1; x++) {
				b[x] = (pointsNoDuplicate[x].getX() * pointsNoDuplicate[x + 1].getY()) - (pointsNoDuplicate[x].getY() * pointsNoDuplicate[x + 1].getX()) == 0;
			}

			b[b.length - 1] = (pointsNoDuplicate[pointsNoDuplicate.length - 1].getX() * pointsNoDuplicate[0].getY()) - (pointsNoDuplicate[pointsNoDuplicate.length - 1].getY() * pointsNoDuplicate[0].getX()) == 0;			
			boolean bf = true;
			for (boolean q : b) {
				if (!q) {
					bf = false;
					break;
				}
			}

			if (bf) {
				hullVertices = new Point[2];
				hullVertices[0] = pointsNoDuplicate[0];
				hullVertices[1] = pointsNoDuplicate[pointsNoDuplicate.length - 1];
			}

			else {
				createRightChain();
				createLeftChain();
				int x = rightChain.size() - 1;
				hullVertices = new Point[leftChain.size() + rightChain.size()];
				while (!rightChain.isEmpty()) {
					hullVertices[x] = rightChain.pop();
					x--;
				}
				
				x = hullVertices.length - 1;
				while (!leftChain.isEmpty()) {
					hullVertices[x] = leftChain.pop();
					x--;
				}
			}
		}
	}


	/**
	 * Construct the right chain of the convex hull.  Starts at lowestPoint and wrap around the 
	 * points counterclockwise.  For every new vertex v of the convex hull, call nextVertex()
	 * to determine the next vertex, which has the smallest polar angle with respect to v.  Stop 
	 * when the highest point is reached.  
	 * 
	 * Use the stack rightChain to carry out the operation.  
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void createRightChain() {
		Point p = pointsNoDuplicate[0];
		while (!p.equals(highestPoint)) {
			rightChain.push(p);
			p = nextVertex(p);
		}
	}


	/**
	 * Construct the left chain of the convex hull.  Starts at highestPoint and continues the 
	 * counterclockwise wrapping.  Stop when lowestPoint is reached.  
	 * 
	 * Use the stack leftChain to carry out the operation. 
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void createLeftChain() {
		Point p = pointsNoDuplicate[pointsNoDuplicate.length - 1];
		while (!p.equals(lowestPoint)) {
			leftChain.push(p);
			p = nextVertex(p);
		}
	}

	/**
	 * Return the next vertex, which is less than all other points by polar angle with respect
	 * to the current vertex v. When there is a tie, pick the point furthest from v. Comparison 
	 * is done using a PolarAngleComparator object created by the constructor call 
	 * PolarAngleComparator(v, false).
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param v  current vertex 
	 * @return
	 */
	public Point nextVertex(Point v) {
		PolarAngleComparator pac = new PolarAngleComparator(v, false);
		Point tmp = pointsNoDuplicate[0];
		for (int x = 0; x < pointsNoDuplicate.length; x++) {
			if (!pointsNoDuplicate[x].equals(v)) {
				if (pac.compare(tmp, pointsNoDuplicate[x]) > 0) {
					tmp = pointsNoDuplicate[x];
				}
			}
		}

		return tmp; 
	}
}
