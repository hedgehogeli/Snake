package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Astar2 {
	
	// THIS WORKS. 
	
/*	static int grid[][] = 
		{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 1, 0, 0, 0, 1, 1, 1, 1},
	 	 {0, 0, 1, 1, 0, 0, 1, 1, 1, 1},
	 	 {0, 0, 0, 1, 0, 0, 0, 1, 1, 0},
		 {0, 0, 0, 1, 0, 1, 0, 1, 1, 0},
		 {0, 0, 0, 0, 0, 1, 0, 1, 1, 0},
		 {0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};  */
	
	static int grid[][] = 
		{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}}; 
	
	static Map<Integer, Node> map = new HashMap<Integer, Node>();

	public static class Node implements Comparable<Node> {
	      // Parent in the path
	      public Node parent = null;

	      public List<Integer> neighbors;

	      // Evaluation functions
	      public int f = Integer.MAX_VALUE;
	      public int g = Integer.MAX_VALUE;
	      // Hardcoded heuristic
	      public int h; 

	      Node(int h){
	            this.h = h;
	            this.neighbors = new ArrayList<>();
	      }

	      @Override
	      public int compareTo(Node n) {
	            return Double.compare(this.f, n.f);
	      }

	      public void addKid(int node){
	            neighbors.add(node);
	      }

	      public int calcH(Node target){
	    	    return Math.abs(getr(this.h)-getr(target.h))+
	    	    		Math.abs(getc(this.h)-getc(target.h));
	      }
	}
	
	public static void main(String[] args) {

		for (int row=0; row<10; ++row) {
			for (int col=0; col<10; ++col) {
				int n = getn(row, col);
				if (grid[getr(n)][getc(n)] == 0) {
					Node newnode = new Node(n);
					// gen neighbors, add neighbors
					LinkedList<Integer> kids = genKids(n);
					for (int kid : kids) {
						if (grid[getr(kid)][getc(kid)] == 0) {
							newnode.addKid(kid);
						}
					}
					map.put(n, newnode);
				}
			}
		}
		
		int start = 14;
		int dest = 89;
		Node head = map.get(start);
	    head.g = 0;
	    Node target = map.get(dest);

	    Node res = aStar(head, target);
	//    System.out.println(res.h);
	    printPath(res);

	    for (int row=0; row<10; ++row) {
			for (int col=0; col<10; ++col) {
				if (grid[row][col] == 1) {
					System.out.print("X ");
				}
				else if(grid[row][col] == 0) {
					System.out.print("- ");
				}
				else {
					System.out.print(grid[row][col] + " ");
				}
			}
			System.out.println();
	    }
	    
	}
	
	public static Node aStar(Node start, Node target){
	    PriorityQueue<Node> closedList = new PriorityQueue<>();
	    PriorityQueue<Node> openList = new PriorityQueue<>();

	    start.f = start.g + start.calcH(target);
	    openList.add(start);

	    while(!openList.isEmpty()){
	        Node n = openList.peek();
	        if(n == target){
	            return n;
	        }

	        for(int i : n.neighbors){
	        	Node m = map.get(i);
	            int totalWeight = n.g + 1; // edge weight 1???

	            if(!openList.contains(m) && !closedList.contains(m)){
	                m.parent = n;
	                m.g = totalWeight;
	                m.f = m.g + m.calcH(target);
	                openList.add(m);
	            } else {
	                if(totalWeight < m.g){
	                    m.parent = n;
	                    m.g = totalWeight;
	                    m.f = m.g + m.calcH(target);

	                    if(closedList.contains(m)){
	                        closedList.remove(m);
	                        openList.add(m);
	                    }
	                }
	            }
	        }

	        openList.remove(n);
	        closedList.add(n);
	    }
	    System.out.println("astar f'n ret null");
	    return null;
	}
	
	public static void printPath(Node target){
	    Node n = target;

	    if(n==null) {
	    	System.out.println("printPath given target null");
	    	return;
	    }
	        

	    List<Integer> nodes = new ArrayList<>();

	    while(n.parent != null){
	    	grid[getr(n.h)][getc(n.h)] = 8;
	        nodes.add(n.h);
	        n = n.parent;
	    }
	    grid[getr(n.h)][getc(n.h)] = 8;
	    nodes.add(n.h); // start node
	    Collections.reverse(nodes);

	    for(int node : nodes){
	        System.out.print(node + " ");
	    }
	    System.out.println("");
	}
	
	static LinkedList<Integer> genKids(int n) {
		LinkedList<Integer> kids = new LinkedList<>();
		int r = getr(n);// System.out.println(x);
		int c = getc(n);// System.out.println(y);
		
		int left = getn(r, c-1);// System.out.println(up);
		int down = getn(r+1, c);// System.out.println(right);
		int right = getn(r, c+1);// System.out.println(down);
		int up = getn(r-1, c);// System.out.println(left);
		
		// checking bounds and wall
		if (up != -1 && grid[r-1][c]==0) kids.add(up);
		if (right != -1 && grid[r][c+1]==0) kids.add(right);
		if (down != -1 && grid[r+1][c]==0) kids.add(down);
		if (left != -1 && grid[r][c-1]==0) kids.add(left);
		
		return kids;
	}
	
	static int getr(int n) {
		return n/10;
	}
	static int getc(int n) {
		return n%10;
	}
	static int getn(int r, int c) {
		if (r<0 || c<0 || r>9 || c>9) return -1; // check within bounds
		else return r*10+c;
	}

}
