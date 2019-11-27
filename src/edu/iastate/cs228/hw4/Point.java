 package edu.iastate.cs228.hw4;

/**
 *  
 * @author Shivkarthi Sundar
 *
 */

public class Point implements Comparable<Point>
{
	private int x; 
	private int y;
	
	public Point() {
		x = 0;
		y = 0;
	}
	
	public Point(int x, int y) {
		this.x = x;  
		this.y = y;   
	}
	
	public Point(Point p) { 
		x = p.getX();
		y = p.getY();
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass())
		{
			return false;
		}
    
		Point other = (Point) obj;
		return x == other.x && y == other.y;   
	}

	/**
	 * Compare this point with a second point q in the bottom-up order. 
	 * 
	 * IMPORTANT: Compare y-coordinates first, and in case of a tie, compare x-coordinates.
	 * 
	 * @param 	q 
	 * @return  -1  if this.y < q.y || (this.y == q.y && this.x < q.x)
	 * 		    0   if this.y == q.y && this.x == q.x 
	 * 			1	otherwise
	 */
	public int compareTo(Point q) {
		if (y == q.y && x == q.x) {
			return 0;
		}
		
		if (this.y < q.y || (this.y == q.y && this.x < q.x)) {
			return -1;
		}
				
		return 1;  
	}
	
	
	/**
	 * 
	 * Output a point in the standard form (x, y). 
	 */
	@Override
    public String toString() {
		String s = new String();
		s += "(" + x + ", " + y + ")";
		return s;
	}
}
