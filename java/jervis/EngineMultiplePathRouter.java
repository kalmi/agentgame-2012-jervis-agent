package kalmi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class EngineMultiplePathRouter implements RecommendationEngine {

	@SuppressWarnings("serial")
	List<ArrayList<Point>> waypointsPerAgent = new ArrayList<ArrayList<Point>>(){{
		
		add(new ArrayList<Point>(){{
			add(new Point(10,10));
			add(new Point(28,10));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(10,31));
			add(new Point(10,49));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(31,49));
			add(new Point(49,49));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(49,10));
			add(new Point(49,28));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(30,30));
			/*add(new Point(25,-1));
			add(new Point(-1,35));
			add(new Point(35,-1));
			add(new Point(-1,25));*/
		}});
		
		/*
		add(new ArrayList<Point>(){{
			add(new Point(-1,10));
			add(new Point(49,-1));
			add(new Point(-1,49));
			add(new Point(10,-1));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(-1,10+2));
			add(new Point(49-2,-1));
			add(new Point(-1,49-2));
			add(new Point(10+2,-1));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(-1,10+4));
			add(new Point(49-4,-1));
			add(new Point(-1,49-4));
			add(new Point(10+4,-1));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(-1,10+7));
			add(new Point(49-7,-1));
			add(new Point(-1,49-7));
			add(new Point(10+7,-1));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(-1,10+10));
			add(new Point(49-10,-1));
			add(new Point(-1,49-7));
			add(new Point(10+10,-1));
		}});*/
		
	}};
	
	
	
	int currentWaypoint = 0;
	
	public Set<MyDir> getRecommendation(State state, int myId) {
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		
		Agent agent = state.agents.get(myId);
		int x = agent.position.x;
		int y = agent.position.y;
		
		ArrayList<Point> waypoints = waypointsPerAgent.get(myId-1);
		Point waypoint = waypoints.get(currentWaypoint);
		
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
			currentWaypoint = (currentWaypoint + 1) % waypoints.size();
			return getRecommendation(state, myId);
		}
			
		
		return result;
			
	}

}
