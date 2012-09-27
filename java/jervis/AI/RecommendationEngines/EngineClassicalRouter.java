package kalmi.AI.RecommendationEngines;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import kalmi.AI.Agent;
import kalmi.AI.State;
import kalmi.CommonTypes.MyDir;


public class EngineClassicalRouter implements RecommendationEngine {

	@SuppressWarnings("serial")
	static final List<Point> waypoints = new ArrayList<Point>(){{
		add(new Point(-1,10));
		add(new Point(49,-1));
		add(new Point(-1,49));
		add(new Point(10,-1));
	}}; 
	
	int currentWaypoint = 0;
	
	public Set<MyDir> getRecommendation(State state, int myId) {
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		
		Agent agent = state.agents[myId];
		int x = agent.position.x;
		int y = agent.position.y;
		
		Point waypoint = waypoints.get(currentWaypoint);
		
		if(waypoint.x==-1){
			if(waypoint.y < y)
				result.add(MyDir.up);
			else if(waypoint.y > y)
				result.add(MyDir.down);
		}
		
		if(waypoint.y==-1){
			if(waypoint.x < x)
				result.add(MyDir.left);
			else if(waypoint.x > x)
				result.add(MyDir.right);
		}
		
		if(result.isEmpty()){
			currentWaypoint = (currentWaypoint + 1) % waypoints.size();
			return getRecommendation(state, myId);
		}
			
		
		return result;
			
	}

}
