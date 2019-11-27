package edu.iastate.cs228.hw4;

/**
 *  
 * @author Shivkarthi Sundar
 *
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException; 
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 * This class implements construction of the convex hull of a finite number of points. 
 *
 */

public abstract class ConvexHull {
	protected String algorithm;

	protected long time;

	/**
	 * The array points[] holds an input set of Points, which may be randomly generated or 
	 * input from a file.  Duplicates are possible. 
	 */
	private Point[] points;

	/**
	 * Lowest point from points[]; and in case of a tie, the leftmost one of all such points. 
	 * To be set by a constructor. 
	 */
	protected Point lowestPoint; 

	/**
	 * This array stores the same set of points from points[] with all duplicates removed. 
	 * These are the points on which Graham's scan and Jarvis' march will be performed. 
	 */
	protected Point[] pointsNoDuplicate; 

	/**
	 * Vertices of the convex hull in counterclockwise order are stored in the array 
	 * hullVertices[], with hullVertices[0] storing lowestPoint. 
	 */
	protected Point[] hullVertices;

	protected QuickSortPoints quicksorter;

	/**
	 * Constructor over an array of points.  
	 * 
	 *    1) Store the points in the private array points[].
	 *    
	 *    2) Initialize quicksorter. 
	 *    
	 *    3) Call removeDuplicates() to store distinct points from the input in pointsNoDuplicate[].
	 *    
	 *    4) Set lowestPoint to pointsNoDuplicate[0]. 
	 * 
	 * @param pts
	 * @throws IllegalArgumentException  if pts.length == 0
	 */
	public ConvexHull(Point[] pts) throws IllegalArgumentException {
		if (pts.length == 0) {
			throw new IllegalArgumentException();
		}

		points = pts;
		quicksorter = new QuickSortPoints(points);
		removeDuplicates();
		lowestPoint = pointsNoDuplicate[0];
	}


	/**
	 * Read integers from an input file.  Every pair of integers represent the x- and y-coordinates 
	 * of a point.  Generate the points and store them in the private array points[]. The total 
	 * number of integers in the file must be even.
	 * 
	 * You may declare a Scanner object and call its methods such as hasNext(), hasNextInt() 
	 * and nextInt(). An ArrayList may be used to store the input integers as they are read in 
	 * from the file.  
	 * 
	 * Perform the operations 1)-4) described for the previous constructor. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public ConvexHull(String inputFileName) throws FileNotFoundException, InputMismatchException {
		File file = new File(inputFileName);
		ArrayList<Point> ALP = new ArrayList<Point>();
		Scanner scan = new Scanner(file);
		while (scan.hasNextInt()) {
			try {
				int x = scan.nextInt();
				int y = scan.nextInt();
				ALP.add(new Point(x, y));
			}

			catch (NoSuchElementException e) {
				scan.close();
				throw new InputMismatchException();
			}
		}

		scan.close();
		ALP.toArray(points);
		if (points.length == 0) {
			throw new IllegalArgumentException();
		}

		quicksorter = new QuickSortPoints(points);
		removeDuplicates();
		lowestPoint = pointsNoDuplicate[0];
	}

	/**
	 * Construct the convex hull of the points in the array pointsNoDuplicate[]. 
	 */
	public abstract void constructHull(); 

	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <convex hull algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * Graham's scan   1000	  9200867
	 *  
	 * Use the spacing in the sample run in Section 5 of the project description. 
	 * @throws FileNotFoundException 
	 * @throws IllegalStateException 
	 */
	public String stats() throws IllegalStateException, FileNotFoundException {
		long x = System.currentTimeMillis();
		constructHull();
		long y = System.currentTimeMillis();
		String s = new String();
		s += algorithm + "   ";
		s += this.pointsNoDuplicate.length + "   ";
		s += y - x;
		writeHullToFile();
		return s; 
	}


	/**
	 * The string displays the convex hull with vertices in counterclockwise order starting at  
	 * lowestPoint.  When printed out, it will list five points per line with three blanks in 
	 * between. Every point appears in the format "(x, y)".  
	 * 
	 * For illustration, the convex hull example in the project description will have its 
	 * toString() generate the output below: 
	 * 
	 * (-7, -10)   (0, -10)   (10, 5)   (0, 8)   (-10, 0)   
	 * 
	 * lowestPoint is listed only ONCE. 
	 *  
	 * Called only after constructHull().  
	 */
	public String toString() {
		String s = new String();
		int ppl = 0;
		for (int x = 0; x < pointsNoDuplicate.length; x++) {
			if (ppl == 5) {
				s += '\n';
				ppl = 0;
			}

			else {
				s += pointsNoDuplicate[x].toString() + "   ";
				ppl++;
			}
		}

		return s; 
	}


	/** 
	 * 
	 * Writes to the file "hull.txt" the vertices of the constructed convex hull in counterclockwise 
	 * order.  These vertices are in the array hullVertices[], starting with lowestPoint.  Every line
	 * in the file displays the x and y coordinates of only one point.  
	 * 
	 * For instance, the file "hull.txt" generated for the convex hull example in the project 
	 * description will have the following content: 
	 * 
	 *  -7 -10 
	 *  0 -10
	 *  10 5
	 *  0  8
	 *  -10 0
	 * 
	 * The generated file is useful for debugging as well as grading. 
	 * 
	 * Called only after constructHull().  
	 * 
	 * 
	 * @throws IllegalStateException  if hullVertices[] has not been populated (i.e., the convex 
	 *                                   hull has not been constructed)
	 * @throws FileNotFoundException 
	 */
	public void writeHullToFile() throws IllegalStateException, FileNotFoundException {
		if (hullVertices.length == 0) {
			throw new IllegalStateException();
		}

		for (Point x : hullVertices) {
			if (x == null) {
				throw new IllegalStateException();
			}
		}

		File file = new File("hullVertices.txt");
		PrintWriter pw = new PrintWriter(file);
		for (int x = 0; x < hullVertices.length; x++) {
			pw.println(hullVertices[x].toString());
		}

		pw.close();
	}


	/**
	 * Draw the points and their convex hull.  This method is called after construction of the 
	 * convex hull.  You just need to make use of hullVertices[] to generate a list of segments 
	 * as the edges. Then create a Plot object to call the method myFrame().  
	 */
	public void draw() {		
		int numSegs = hullVertices.length; 
		Segment[] segments = new Segment[numSegs];
		for (int x = 0; x < hullVertices.length - 1; x++) {
			segments[x] = new Segment(hullVertices[x], hullVertices[x + 1]);
		}

		segments[segments.length - 1] = new Segment(hullVertices[0], hullVertices[hullVertices.length - 1]); 
		Plot.myFrame(pointsNoDuplicate, segments, getClass().getName());
	}


	/**
	 * Sort the array points[] by y-coordinate in the non-decreasing order.  Have quicksorter 
	 * invoke quicksort() with a comparator object which uses the compareTo() method of the Point 
	 * class. Copy the sorted sequence onto the array pointsNoDuplicate[] with duplicates removed.
	 *     
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void removeDuplicates() {
		class PointComparator implements Comparator<Point> {
			@Override
			public int compare(Point x, Point y) {
				return x.compareTo(y);
			}
		}

		PointComparator comp = new PointComparator();
		quicksorter.quickSort(comp);
		quicksorter.getSortedPoints(points);
		ArrayList<Point> ALP = new ArrayList<Point>();
		ALP.add(points[0]);
		for (int x = 1; x < points.length; x++) {
			if (!points[x].equals(ALP.get(ALP.size() - 1))) {
				ALP.add(points[x]);
			}
		}
		
		pointsNoDuplicate = new Point[ALP.size()];
		ALP.toArray(pointsNoDuplicate);
	}
}
