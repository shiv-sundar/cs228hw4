package edu.iastate.cs228.hw4;

/**
 *  
 * @author Shivkarthi Sundar
 *
 */

/**
 * 
 * This class executes two convex hull algorithms: Graham's scan and Jarvis' march, over randomly
 * generated integers as well integers from a file input. It compares the execution times of 
 * these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random; 


public class CompareHullAlgorithms 
{
	/**
	 * Repeatedly take points either randomly generated or read from files. Perform Graham's scan and 
	 * Jarvis' march over the input set of points, comparing their performances.  
	 * 
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException 
	 **/
	public static void main(String[] args) throws InputMismatchException, FileNotFoundException {		
		Scanner scan = new Scanner(System.in);
		ConvexHull[] algorithms = new ConvexHull[2];
		int x = 1;
		while (x != 3) {
			System.out.println("Input points(1) or Random Points(2) or Exit(3): ");
			x = scan.nextInt();
			if (x == 1) {
				System.out.println("What is the name of the file?: ");
				String s = scan.next();
				algorithms[0] = new GrahamScan(s);
				algorithms[1] = new JarvisMarch(s);
				for (ConvexHull ch : algorithms) {
					System.out.println(ch.stats());
					ch.draw();
					System.out.println(ch.toString());
				}
			}

			else if (x == 2) {
				Random rand;
				System.out.println("How many points?: ");
				int numPoints = scan.nextInt();
				rand = new Random();
				Point[] pts = new Point[numPoints];
				pts = generateRandomPoints(numPoints, rand);
				algorithms[0] = new GrahamScan(pts);
				algorithms[1] = new JarvisMarch(pts);

				for (ConvexHull ch : algorithms) {
					System.out.println(ch.stats());
					ch.draw();
					System.out.println(ch.toString());
				}
			}
		}

		scan.close();
		System.out.println("EXIT");
	}


	/**
	 * This method generates a given number of random points.  The coordinates of these points are 
	 * pseudo-random numbers within the range [-50,50] × [-50,50]. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	private static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		if (numPts < 1) {
			throw new IllegalArgumentException();
		}

		Point[] testar = new Point[numPts];
		for (int x = 0; x < testar.length; x++) {
			testar[x] = new Point(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
		}
		return testar; 
	}
}
