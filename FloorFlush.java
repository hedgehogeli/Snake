package test;
import java.util.*;
import java.io.*;

public class FloorFlush {
	
	static int d[] = new int[100];
	
	// x = row#, 0-9; y = col#, 0-9
	// n = y*10 + x; so 0, 1, 2, 3, ..., 9
	// 					10, 11, ..., 19
	static int grid[][] = 
		{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 1, 0, 0, 0, 1, 1, 1, 1},
	 	 {0, 0, 1, 1, 0, 0, 1, 1, 1, 1},
	 	 {0, 0, 0, 1, 0, 0, 0, 1, 1, 0},
		 {0, 0, 0, 1, 0, 1, 0, 1, 1, 0},
		 {0, 0, 0, 0, 0, 1, 0, 1, 1, 0},
		 {0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};  
	
	public static void main(String[] args) {
		Arrays.fill(d, 99); // initialize to max distance
		
		// taking start node, setting d=0 for n
		Scanner scan = new Scanner(System.in);
		int n = scan.nextInt();
//		int dest = scan.nextInt();
		scan.close();
		d[n] = 0;

		// start queue
		Queue<Integer> qu = new LinkedList<>();
		qu.add(n);
		
//		boolean found = false;
		while(!qu.isEmpty()) {
			int cur = qu.poll();
			LinkedList<Integer> kids = genKids(cur);
			if (kids == null) continue;
			else {
				for (int kid : kids) {
					// calculate new distance
					int dist = d[cur]+1;
					
					// if less, update, then add to qu
					if (dist < d[kid]) {
						d[kid] = dist;
						qu.add(kid);
					}
					
					// if destination, terminate
//					if (kid == dest) {
//						found = true;
//						break;
//					}
				}
			}
//			if (found) break;
		}
		
		
		for (int y=0; y<10; ++y) {
			for (int x=0; x<10; ++x) {
				if (d[getn(x,y)] == 99) System.out.print("? ");
				else System.out.print(d[getn(x,y)] + " ");
			}
			System.out.println();
		}

	}

	
	static int gety(int n) {
		return n/10;
	}
	static int getx(int n) {
		return n%10;
	}
	static int getn(int x, int y) {
		// check within bounds
		if (x<0 || y<0 || x>9 || y>9) {
			return -1; }
		else { return y*10 + x; }
	}

	static LinkedList<Integer> genKids(int n) {
		LinkedList<Integer> kids = new LinkedList<>();
		int x = getx(n);// System.out.println(x);
		int y = gety(n);// System.out.println(y);
		
		int up = getn(x, y-1);// System.out.println(up);
		int right = getn(x+1, y);// System.out.println(right);
		int down = getn(x, y+1);// System.out.println(down);
		int left = getn(x-1, y);// System.out.println(left);
		
		// checking bounds and wall
		if (up != -1 && grid[y-1][x]==0) kids.add(up);
		if (right != -1 && grid[y][x+1]==0) kids.add(right);
		if (down != -1 && grid[y+1][x]==0) kids.add(down);
		if (left != -1 && grid[y][x-1]==0) kids.add(left);
		
		return kids;
	}
}





/*


		while(!qu.isEmpty()) {
			int cur = qu.poll();
			LinkedList<Integer> kids = graph.get(cur);
			if (kids == null) continue;
			else {
				for (int kid:kids) {
				if (visited[kid] == false) {
					visited[kid] = true;
					// do wahtever u need to do prollly here
					qu.add(kid);
					}
				}
			}	
			
			
		}// end of while

*/





