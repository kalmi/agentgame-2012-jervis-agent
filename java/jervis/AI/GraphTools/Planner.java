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
	
	final State state;	
	final Point start;
	final Point target;
	final Agent agent;
	public Planner(Point start, Point target, State state, Agent agent){
		this.start = start;
		this.target = target;
		this.state = state;
		this.agent = agent;
	}
		
	
	public LinkedList<Point> plan(){
		Set<Vertex> closed =  new HashSet<Vertex>();
		Set<Vertex> open =  new HashSet<Vertex>();
		PriorityQueue<Vertex> openListInProcessingOrder = new PriorityQueue<Vertex>(11, new Comparator<Vertex>() {
			public int compare(Vertex o1, Vertex o2) {
				return Double.compare((o1.bestCost+o1.heuristic), (o2.bestCost+o2.heuristic));
			}
		});
		
		GraphProvider g = new GraphProvider(target);
		
		Vertex start = g.getVertex(this.start);		
		start.bestCost = 0;
		openListInProcessingOrder.add(start);
		
		while(!openListInProcessingOrder.isEmpty()){
			Vertex current = openListInProcessingOrder.remove();
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
				double possibleNewBestCost = current.bestCost + e.weight;
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
			double heuristic = h(x,y);
			Vertex v = new Vertex(x,y,heuristic);
			return v;
		}

		class Edge {
			public Edge(Vertex v, double d) {
				this.v = v;
				this.weight = d;
			}
			final public Vertex v;
			final public double weight;
		}
		
		@SuppressWarnings("serial")
		class Vertex extends Point{
			public Vertex reachedFrom;
			public double bestCost = Integer.MAX_VALUE;
			public double possibleNewBestCost;
			
			public final double heuristic;
			public final int localEnergyUsage;
			
			public Vertex(int x, int y, double heuristic) {
				super(x,y);
				this.heuristic = heuristic; 
				localEnergyUsage = state.waterManager.isWater(new Point(x,y)) ? 5*Config.waterCostFactor : 5;
			}

			private ArrayList<Edge> getEdges() {
				ArrayList<Edge> l =  new ArrayList<Edge>();
				if(x-1 >= 0){
					Point p = new Point(x-1,y);
					Vertex v = getVertex(p);
					boolean enemyPresent = state.isObstacle(agent, p);
					boolean edgy = p.x < 10 || p.y < 10 || p.y > 49 || p.x > 49;
					Edge e = new Edge(v, v.localEnergyUsage + (enemyPresent?1000:0) + (edgy?0.1:0));
					l.add(e);
				}
				
				if(x+1 < 60){
					Point p = new Point(x+1,y);
					Vertex v = getVertex(p);
					boolean enemyPresent = state.isObstacle(agent, p);
					boolean edgy = p.x < 10 || p.y < 10 || p.y > 49 || p.x > 49;
					Edge e = new Edge(v, v.localEnergyUsage + (enemyPresent?1000:0) + (edgy?0.1:0));
					l.add(e);
				}
				
				if(y-1 >= 0){
					Point p = new Point(x,y-1);
					Vertex v = getVertex(p);
					boolean enemyPresent = state.isObstacle(agent, p);
					boolean edgy = p.x < 10 || p.y < 10 || p.y > 49 || p.x > 49;
					Edge e = new Edge(v, v.localEnergyUsage + (enemyPresent?1000:0) + (edgy?0.1:0));
					l.add(e);
				}
				
				if(y+1 < 60){
					Point p = new Point(x,y+1);
					Vertex v = getVertex(p);
					boolean enemyPresent = state.isObstacle(agent, p);
					boolean edgy = p.x < 10 || p.y < 10 || p.y > 49 || p.x > 49;
					Edge e = new Edge(v, v.localEnergyUsage + (enemyPresent?1000:0) + (edgy?0.1:0));
					l.add(e);
				}
			
				return l;
			}
		}
		
		private double h(int x, int y) {
			return (Math.abs(target.x-x)+Math.abs(target.y-y))*4.99;
		}
	}	
}
