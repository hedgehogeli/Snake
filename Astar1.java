package test;
import java.util.*;
import java.io.*;

public class Astar1 {

	static int r = 10;
	static int c = 10;
	
	public static class Node implements Comparable<Node> {
	      // Id for readability of result purposes
	      private static int idCounter = 0;
	      public int id;

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
//	            this.id = h;
	            this.id = idCounter++;
	            this.neighbors = new ArrayList<>();
	      }

	      @Override
	      public int compareTo(Node n) {
	            return Double.compare(this.f, n.f);
	      }

	      public void addNeighbor(int node){
	            neighbors.add(node);
	      }

	      public int calculateHeuristic(Node target){
	            return h(this.h, target.h);
	      }
	}
	
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
	
	static Map<Integer, Node> map = new HashMap<Integer, Node>();
	
	public static void main(String[] args) {

		
		for (int row=0; row<r; ++row) {
			for (int col=0; col<c; ++col) {
				int n = getn(row, col);
				if (grid[getx(n)][gety(n)] == 0) {
					Node newnode = new Node(n);
					// gen neighbors, add neighbors
					LinkedList<Integer> kids = new LinkedList<>();
					for (int kid : kids) {
						if (grid[getx(kid)][gety(kid)] == 0) {
							newnode.addNeighbor(kid);
						}
					}
					map.put(n, newnode);
				}
			}
		}
		
		System.out.println("hi");
		System.out.println("hey" + grid[getx(21)][gety(21)]);
		
		int start = 0;
		int dest = 0;
		Node head = map.get(start);
	    head.g = 0;
	    Node target = map.get(dest);

	    Node res = aStar(head, target);
	    System.out.println(res.h);
	    printPath(res);

	}
	
	static int h(int n, int dest) {
		return Math.abs(getx(n)-getx(dest))+Math.abs(gety(n)-gety(dest));
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
	
	public static Node aStar(Node start, Node target){
	    PriorityQueue<Node> closedList = new PriorityQueue<>();
	    PriorityQueue<Node> openList = new PriorityQueue<>();

	    start.f = start.g + start.calculateHeuristic(target);
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
	                m.f = m.g + m.calculateHeuristic(target);
	                openList.add(m);
	            } else {
	                if(totalWeight < m.g){
	                    m.parent = n;
	                    m.g = totalWeight;
	                    m.f = m.g + m.calculateHeuristic(target);

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
	    return null;
	}
	
	public static void printPath(Node target){
	    Node n = target;

	    if(n==null) return;

	    List<Integer> ids = new ArrayList<>();

	    while(n.parent != null){
	        ids.add(n.id);
	        n = n.parent;
	    }
	    ids.add(n.id);
	    Collections.reverse(ids);

	    for(int id : ids){
	        System.out.print(id + " ");
	    }
	    System.out.println("");
	}
	

}

/*
1.  Initialize the open list
2.  Initialize the closed list
    put the starting node on the open 
    list (you can leave its f at zero)

3.  while the open list is not empty
    a) find the node with the least f on 
       the open list, call it "q"

    b) pop q off the open list
  
    c) generate q's 8 successors and set their 
       parents to q
   
    d) for each successor
        i) if successor is the goal, stop search
        
        ii) else, compute both g and h for successor
          successor.g = q.g + distance between 
                              successor and q
          successor.h = distance from goal to 
          successor (This can be done using many 
          ways, we will discuss three heuristics- 
          Manhattan, Diagonal and Euclidean 
          Heuristics)
          
          successor.f = successor.g + successor.h

        iii) if a node with the same position as 
            successor is in the OPEN list which has a 
           lower f than successor, skip this successor

        iV) if a node with the same position as 
            successor  is in the CLOSED list which has
            a lower f than successor, skip this successor
            otherwise, add  the node to the open list
     end (for loop)
  
    e) push q on the closed list
    end (while loop)*/