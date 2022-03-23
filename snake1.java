package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;


public class snake1 {

	static int r = 10;
	static int c = 10;
	static int[][] grid = new int[r][c];
	static int head = -1;
	static int tail = -1;
	
	static Map<Integer, Node> map = new HashMap<Integer, Node>();

	public static class Node implements Comparable<Node> {
	      // Parent in the path
	      public Node parent = null;

	      public List<Integer> neighbors;
	      
	      public int occupied;

	      // Evaluation functions
	      public int f = Integer.MAX_VALUE;
	      public int g = Integer.MAX_VALUE;
	      // Hardcoded heuristic
	      public int h; 

	      Node(int h){
	            this.h = h;
	            this.occupied = 0; // initialize to not occupied
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
		
		// create nodes, place ALL POSSIBLE NEIGHBORS in nodes 
		for (int row=0; row<c; ++row) {
			for (int col=0; col<r; ++col) {
				int n = getn(row, col);
				Node newnode = new Node(n);
				LinkedList<Integer> kids = genKids(n);
				for (int kid : kids) {
					newnode.addKid(kid);
				}
				map.put(n, newnode);
			}
		}
		
		int spawnR = (int) (Math.random()*r);
		int spawnC = (int) (Math.random()*c);
		map.get(getn(spawnR, spawnC)).occupied = 1;
		grid[spawnR][spawnC] = 1;
		head = getn(spawnR, spawnC);
		tail = head;
		Queue<Integer> snake = new LinkedList<Integer>();
		snake.add(head);
		
		
		// 1 = HEAD = O
		// 2 = BODY = X
// 3 = TAIL = o



		for (int i=0; i<25; ++i) { 
//System.out.println("for i=" + i);
			// SPAWN APPLE
			int apple = genApple();
System.out.println("apple spawned");
System.out.println();
			printGrid();


			// FIND PATH
			Node res = aStar(map.get(head), map.get(apple));
			if (res == null) {
				System.out.println("NO PATH");
				break;
			}
//System.out.println("aStar succesful");
			List<Integer> pathList = path(res);
//System.out.println(pathList);
//System.out.println();
			clearParents(); // remove parents for next iteration
//System.out.println("parents cleared");
			
			// MOVE SNAKE
			for (int mvmt : pathList) {
				if (mvmt == apple) {
					// update grid
					grid[getr(head)][getc(head)] = 2; // head->body
					grid[getr(mvmt)][getc(mvmt)] = 1; // mvmt->head
					// tail untouched, tail grows
					
					// update map nodes
					map.get(mvmt).occupied = 1;
					
					// add head
					head = mvmt;
					snake.add(mvmt);
					
					// tail untouched
				}
				else {
					// update grid
					grid[getr(head)][getc(head)] = 2; // head->body
					grid[getr(mvmt)][getc(mvmt)] = 1; // mvmt->head
					grid[getr(tail)][getc(tail)] = 0; // tail->empty
					
					// update map nodes
					map.get(tail).occupied = 0;
					map.get(mvmt).occupied = 1;
					
					// add head
					head = mvmt;
					snake.add(mvmt); 
					
					// remove tail, move tail up one
					snake.poll(); 
					tail = snake.peek();
					
					
					
				}
				printGrid();
			}
		}
		
		
		
		

	}
	
	public static void clearParents() {
		for (int row=0; row<r; ++row) {
			for (int col=0; col<c; ++col) {
				int n = getn(row, col);
//System.out.println("clearing " + map.get(n).parent);
				map.get(n).parent = null;
			}
		}
	}
	
	public static int genApple() {
		// checks if spot is occupied, 
		// if not creates apple = 4 = a
		// updates grid
		int spawnR = (int) (Math.random()*r);
		int spawnC = (int) (Math.random()*c);
		if (grid[spawnR][spawnC] == 0) {
			grid[spawnR][spawnC] = 4;
			return getn(spawnR, spawnC);
		}
		else return genApple(); // else try again
	}
	
	public static Node aStar(Node start, Node target){
//System.out.println("astar given "+  start.h + " " + target.h);
	    PriorityQueue<Node> closedList = new PriorityQueue<>();
	    PriorityQueue<Node> openList = new PriorityQueue<>();

	    start.f = start.g + start.calcH(target);
	    openList.add(start);

	    while(!openList.isEmpty()){
	        Node n = openList.peek();
//System.out.println("n=" + n.h);
	        if(n == target){
//System.out.println("fuck?");
//System.out.println(n.parent.h);
	            return n;
	        }
	        
//System.out.println("for iter thru " + n.neighbors);
	        for(int i : n.neighbors){
	        	Node m = map.get(i);
	        	
	        	// inelegible neighbor
	        	if (m.occupied == 1) {
	        		continue;
	        	}
	        		
	            int totalWeight = n.g + 1; // edge weight = 1

	            if(!openList.contains(m) && !closedList.contains(m)){
//	            				System.out.println("reach 1, i=" + i);
	                m.parent = n;
	                m.g = totalWeight;
	                m.f = m.g + m.calcH(target);
	                openList.add(m);
	            } else {
	                if(totalWeight < m.g){
//	                			System.out.println("reach 2, i=" + i);
	                    m.parent = n;
	                    m.g = totalWeight;
	                    m.f = m.g + m.calcH(target);

	                    if(closedList.contains(m)){
//	                    		System.out.println("reach 3, i=" + i);
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
	
	public static List<Integer> path(Node target){
	    Node n = target;

	    if(n==null) {
	    	System.out.println("printPath given target null");
	    	return null;
	    }

	    List<Integer> nodes = new ArrayList<>();

	    while(n.parent!=null && n.h!=head){  //System.out.println("idk man " + n.h);
	        nodes.add(n.h);
	        n = n.parent;
	    }
///	    nodes.add(n.h); // don't want start node
	    Collections.reverse(nodes);
	    return nodes;
	}
	
	public static void printGrid() {
		for (int row=0; row<r; ++row) {
			for (int col=0; col<c; ++col) {
				if (grid[row][col] == 0) {
					System.out.print("- ");
				}
				else if(grid[row][col] == 1) {
					System.out.print("O "); //head
				}
				else if(grid[row][col] == 2) {
					System.out.print("X "); //body
				}
				else if(grid[row][col] == 3) {
					System.out.print("o "); //tail
				}
				else if(grid[row][col] == 4) {
					System.out.print("a "); //apple
				}
				else {
					System.out.print(grid[row][col] + " ");
				}
			}
			System.out.println();
	    }
		System.out.println();
	}
	
	static LinkedList<Integer> genKids(int n) {
		LinkedList<Integer> kids = new LinkedList<>();
		int r = getr(n);
		int c = getc(n);
		
		int left = getn(r, c-1);
		int down = getn(r+1, c);
		int right = getn(r, c+1);
		int up = getn(r-1, c);
		// checking bounds and grid emptiness
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
	static int getn(int row, int col) {
		if (row<0 || col<0 || row>=r || col>=c) return -1; // check within bounds
		else return row*10+col;
	}

}
