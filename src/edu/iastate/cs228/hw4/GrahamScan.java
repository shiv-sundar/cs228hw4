package edu.iastate.cs228.hw4;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;

public class GrahamScan extends ConvexHull
{
	/**
	 * Stack used by Graham's scan to store the vertices of the convex hull of the points 
	 * scanned so far.  At the end of the scan, it stores the hull vertices in the 
	 * counterclockwise order. 
	 */
	private PureStack<Point> vertexStack;  

	/**
	 * Call corresponding constructor of the super class.  Initialize two variables: algorithm 
	 * (from the class ConvexHull) and vertexStack. 
	 * 
	 * @throws IllegalArgumentException  if pts.length == 0
	 */
	public GrahamScan(Point[] pts) throws IllegalArgumentException {
		super(pts);
		algorithm = "GRAHAM";
		vertexStack = new ArrayBasedStack<Point>();
	}


	/**
	 * Call corresponding constructor of the super class.  Initialize algorithm and vertexStack.  
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public GrahamScan(String inputFileName) throws FileNotFoundException, InputMismatchException {
		super(inputFileName); 
		algorithm = "GRAHAM";
		vertexStack = new ArrayBasedStack<Point>();
	}

	/**
	 * This method carries out Graham's scan in several steps below: 
	 * 
	 *     1) Call the private method setUpScan() to sort all the points in the array 
	 *        pointsNoDuplicate[] by polar angle with respect to lowestPoint.    
	 *        
	 *     2) Perform Graham's scan. To initialize the scan, push pointsNoDuplicate[0] and 
	 *        pointsNoDuplicate[1] onto vertexStack.  
	 * 
	 *     3) As the scan terminates, vertexStack holds the vertices of the convex hull.  Pop the 
	 *        vertices out of the stack and add them to the array hullVertices[], starting at index
	 *        vertexStack.size() - 1, and decreasing the index toward 0.    
	 *        
	 * Two degenerate cases below must be handled: 
	 * 
	 *     1) The array pointsNoDuplicates[] contains just one point, in which case the convex
	 *        hull is the point itself. 
	 *     
	 *     2) The array contains only collinear points, in which case the hull is the line segment 
	 *        connecting the two extreme points.   
	 */
	public void constructHull() {
		if (pointsNoDuplicate.length == 1) {
			hullVertices = new Point[1];
			hullVertices[0] = pointsNoDuplicate[0];
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
				setUpScan();
				vertexStack.push(pointsNoDuplicate[0]);
				vertexStack.push(pointsNoDuplicate[1]);
				vertexStack.push(pointsNoDuplicate[2]);
				for (int x = 3; x < pointsNoDuplicate.length; x++) {
					Point p = vertexStack.pop();
					PolarAngleComparator pac = new PolarAngleComparator(vertexStack.peek(), true);
					if (pac.compare(p, pointsNoDuplicate[x]) == -1) {
						vertexStack.push(p);
						vertexStack.push(pointsNoDuplicate[x]);
					}
					
					else {
						x--;
					}
				}
				
				hullVertices = new Point[vertexStack.size()];
				int x = vertexStack.size() - 1;
				while (!vertexStack.isEmpty()) {
					hullVertices[x] = vertexStack.pop();
					x--;
				}
			}
		}
	}


	/**
	 * Set the variable quicksorter from the class ConvexHull to sort by polar angle with respect 
	 * to lowestPoint, and call quickSort() from the QuickSortPoints class on pointsNoDuplicate[]. 
	 * The argument supplied to quickSort() is an object created by the constructor call 
	 * PolarAngleComparator(lowestPoint, true).       
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 *
	 */
	public void setUpScan()	{
		quicksorter = new QuickSortPoints(pointsNoDuplicate);
		quicksorter.quickSort(new PolarAngleComparator(lowestPoint, true));
		quicksorter.getSortedPoints(pointsNoDuplicate);
	}
}
