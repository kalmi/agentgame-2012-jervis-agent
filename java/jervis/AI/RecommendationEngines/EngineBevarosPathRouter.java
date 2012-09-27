package kalmi.AI.RecommendationEngines;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import kalmi.AI.Agent;
import kalmi.AI.State;
import kalmi.CommonTypes.MyDir;


public class EngineBevarosPathRouter implements RecommendationEngine {

	@SuppressWarnings("serial")
	List<ArrayList<Point>> waypointsPerAgent = new ArrayList<ArrayList<Point>>(){{
		
		add(new ArrayList<Point>(){{
			add(new Point(10,10));
			add(new Point(10,49));
			add(new Point(49,49));
			add(new Point(49,10));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(10,10));
			add(new Point(10,49));
			add(new Point(49,49));
			add(new Point(49,10));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(10,10));
			add(new Point(10,49));
			add(new Point(49,49));
			add(new Point(49,10));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(10,10));
			add(new Point(10,49));
			add(new Point(49,49));
			add(new Point(49,10));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(30,30));
		}});
		
	}};
	
	
	
	int currentWaypoint = -1;
	
	static public int waiter = 0;
	public boolean amIInWait = false;
	static public int launch = 0;
	
	public Set<MyDir> getRecommendation(State state, int myId) {
		
		ArrayList<Point> waypoints = waypointsPerAgent.get(myId-1);
		
		
		if(currentWaypoint == -1){
			currentWaypoint = myId % waypoints.size();
		}
		Point waypoint = waypoints.get(currentWaypoint);
		
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		
		Agent agent = state.agents[myId];
		int x = agent.position.x;
		int y = agent.position.y;
		
		
		
		if(waypoint.x==-1){
			if(waypoint.y < y)
				result.add(MyDir.up);
			else if(waypoint.y > y)
				result.add(MyDir.down);
		}
		else if(waypoint.y==-1){
			if(waypoint.x < x)
				result.add(MyDir.left);
			else if(waypoint.x > x)
				result.add(MyDir.right);
		}
		else{
			if(waypoint.y < y)
				result.add(MyDir.up);
			else if(waypoint.y > y)
				result.add(MyDir.down);
			if(waypoint.x < x)
				result.add(MyDir.left);
			else if(waypoint.x > x)
				result.add(MyDir.right);
		}
			
		if(result.isEmpty() && waypoints.size() != 1){
			if(!amIInWait){
				waiter++;
				amIInWait = true;
			}
			if(waiter == 4 || launch != 0){
				currentWaypoint = (currentWaypoint + 1) % waypoints.size();
				amIInWait = false;
				launch++;
				waiter--;				
				if(launch==4){
					launch = 0;
				}
				return getRecommendation(state, myId);
			}
		}
			
		
		return result;
			
	}

}
