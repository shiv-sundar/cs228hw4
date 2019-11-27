package edu.iastate.cs228.hw4;

/**
 * @author Shivkarthi Sundar
 */

import java.util.Comparator;

/**
 * This class sorts an array of Point objects using a provided Comparator.  For the purpose
 * you may adapt your implementation of quicksort from Project 2.  
 */

public class QuickSortPoints {
	private Point[] points;

	/**
	 * Constructor takes an array of Point objects. 
	 * 
	 * @param pts
	 */
	QuickSortPoints(Point[] pts) {
		points = new Point[pts.length];
		for (int x = 0; x < pts.length; x++) {
			points[x] = pts[x];
		}
	}


	/**
	 * Copy the sorted array to pts[]. 
	 * 
	 * @param pts  array to copy onto
	 */
	void getSortedPoints(Point[] pts) {
		for (int x = 0; x < points.length; x++) {
			pts[x] = points[x];
		}
	}


	/**
	 * Perform quicksort on the array points[] with a supplied comparator. 
	 * 
	 * @param comp
	 */
	public void quickSort(Comparator<Point> comp) {
		quickSortRec(0, points.length - 1, comp);
	}


	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last, Comparator<Point> comp) {
		if (first < last) {
			int x = partition(first, last, comp);
			quickSortRec(first, x - 1, comp);
			quickSortRec(x + 1, last, comp);
		}
	}


	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last, Comparator<Point> comp) {
		Point pivot = points[last]; 
		int i = first - 1;
		for (int j = first; j < last; j++) {
			if (comp.compare(points[j], pivot) == -1) {
				i++;
				Point tmp = points[i];
				points[i] = points[j];
				points[j] = tmp;
			}
		}

		Point tmp = new Point(points[i + 1].getX(), points[i + 1].getY());
		points[i + 1] = new Point(points[last].getX(), points[last].getY());
		points[last] = tmp;
		return i + 1;
	}
}