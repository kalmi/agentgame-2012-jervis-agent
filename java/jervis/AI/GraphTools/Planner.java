package jervis.AI.GraphTools;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import jervis.AI.Agent;
import jervis.AI.Config;
import jervis.AI.State;
import jervis.AI.GraphTools.Planner.GraphProvider.Edge;
import jervis.AI.GraphTools.Planner.GraphProvider.Vertex;

public class Planner {
	
	static int kifejtve = 0;
	
	final State state;	
	final Point start;
	final Point target;
	final Agent agent;
	public Planner(Point start, Point target, State state, Agent agent){
		this.start = start;
		this.target = target;
		this.state = state;
		this.agent = agent;
		
		if(agent.hightlighted){
			System.out.println();
			System.out.print("Replan for ");
			System.out.println(agent.myName);
		}
		
		if(agent.time % 100 == 0 && agent.order == 0)
			System.out.println((double)kifejtve / agent.time );
	}
	
	public static int compareInt(int x, int y) {
		 return (x < y) ? -1 : ((x == y) ? 0 : 1);
	}
	
	public LinkedList<Point> plan(){
		Set<Vertex> closed =  new HashSet<Vertex>();
		Set<Vertex> open =  new HashSet<Vertex>();
		PriorityQueue<Vertex> openListInProcessingOrder = new PriorityQueue<Vertex>(11, new Comparator<Vertex>() {
			public int compare(Vertex o1, Vertex o2) {
				return compareInt((o1.bestCost+o1.heuristic), (o2.bestCost+o2.heuristic));
			}
		});
		
		GraphProvider g = new GraphProvider(target);
		
		Vertex start = g.getVertex(this.start);		
		start.bestCost = 0;
		openListInProcessingOrder.add(start);
		
		while(!openListInProcessingOrder.isEmpty()){
			Vertex current = openListInProcessingOrder.remove();
			kifejtve++;
			closed.add(current);
			if(target.equals(current)){
				Path path = new Path();
				while(current!=null){
					path.addFirst(current);
					current = current.reachedFrom;
				}
				return path;
			}
			
			List<Vertex> candidates = new ArrayList<Vertex>();
			for (Edge e : current.getEdges()) {
				Vertex v = e.v;
				int possibleNewBestCost = current.bestCost + e.weight;
				if(possibleNewBestCost < v.bestCost){
					v.bestCost = possibleNewBestCost;
					v.reachedFrom = current;
				}
				candidates.add(v);
			}
			
			candidates.removeAll(open);
			candidates.removeAll(closed);
			openListInProcessingOrder.addAll(candidates);
			open.addAll(candidates);
		}
		return null;
	}
	
	
	class GraphProvider{
		final Point target;
		final Vertex[][] generatedVertices = new Vertex[60][60];
		
		public GraphProvider(Point target){
			this.target = target; 
		}
		
		public Vertex getVertex(Point point){
			return getVertex(point.x, point.y);
		}
		
		public Vertex getVertex(int x, int y){
			if(generatedVertices[x][y] == null){
				generatedVertices[x][y] = generateVertex(x,y);
			}
			return generatedVertices[x][y];
		}
		
		private Vertex generateVertex(int x, int y) {
			int heuristic = h(x,y);
			Vertex v = new Vertex(x,y,heuristic);
			return v;
		}

		class Edge {
			public Edge(Vertex v, int d) {
				this.v = v;
				this.weight = d;
			}
			final public Vertex v;
			final public int weight;
		}
		
		@SuppressWarnings("serial")
		class Vertex extends Point{
			public Vertex reachedFrom;
			public int bestCost = Integer.MAX_VALUE - 100000;
			public int possibleNewBestCost;
			
			public final int heuristic;
			public final int myCost;
			
			public Vertex(int x, int y, int heuristic) {
				super(x,y);
				boolean axis = (x == target.x || x == start.x || y == start.y || y == target.y);
				this.heuristic = axis?heuristic:heuristic * 20; 
				
				int localEnergyUsage = state.waterManager.isWater(new Point(x,y)) ? 5*Config.waterCostFactor : 5;				
				boolean enemyPresent = state.isObstacle(agent, x, y);
				boolean edgy = x < 10 || y < 10 || y > 49 || x > 49;
				
				this.myCost = localEnergyUsage*10*20 + (enemyPresent?(60+60)*50*20*21+1:0) + (edgy?1:0);
			}

			private ArrayList<Edge> getEdges() {
				ArrayList<Edge> l =  new ArrayList<Edge>(4);
				if(x-1 >= 0){
					Vertex v = getVertex(x-1,y);
					Edge e = new Edge(v, myCost);
					l.add(e);
				}
				
				if(x+1 < 60){
					Vertex v = getVertex(x+1,y);
					Edge e = new Edge(v, myCost);
					l.add(e);
				}
				
				if(y-1 >= 0){
					Vertex v = getVertex(x,y-1);
					Edge e = new Edge(v, myCost);
					l.add(e);
				}
				
				if(y+1 < 60){
					Vertex v = getVertex(x,y+1);
					Edge e = new Edge(v, myCost);
					l.add(e);
				}
			
				return l;
			}
		}
		
		private int h(int x, int y) {
			return (Math.abs(target.x-x)+Math.abs(target.y-y))*5*10;
		}
	}	
}
