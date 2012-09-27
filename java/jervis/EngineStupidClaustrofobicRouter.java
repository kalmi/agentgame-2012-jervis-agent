package kalmi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class EngineStupidClaustrofobicRouter implements RecommendationEngine {

	@SuppressWarnings("serial")
	static final List<Point> waypoints = new ArrayList<Point>(){{
		add(new Point(-1,10));
		add(new Point(49,-1));
		add(new Point(-1,49));
		add(new Point(10,-1));
	}}; 
	
	@SuppressWarnings("serial")
	static final List<Point> referenceWaypoints = new ArrayList<Point>(){{
		add(new Point(10,10));
		add(new Point(49,10));
		add(new Point(49,49));
		add(new Point(10,49));
	}}; 
	
	int currentWaypoint = 0;
	
	public Set<MyDir> getRecommendation(State state, int myId) {
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		
		Agent agent = state.agents.get(myId);
		int x = agent.position.x;
		int y = agent.position.y;
		
		Point waypoint = waypoints.get(currentWaypoint);
		
		
		if(agent.claustrofobicness == 0){
			for (Entry<Integer, Agent> entry : state.agents.entrySet()) {
				if(entry.getKey() == agent.id) continue;
				Agent friendly = entry.getValue();
				Point friendlyPos = friendly.position;
				if(friendlyPos.distance(agent.position)<15 && friendly.claustrofobicness == 0){
					Point target = referenceWaypoints.get(currentWaypoint);
					Point start = referenceWaypoints.get((currentWaypoint+referenceWaypoints.size()-1) % referenceWaypoints.size());
					if(start.distance(agent.position) > target.distance(agent.position)){
						currentWaypoint = (currentWaypoint + 1) % waypoints.size();
						agent.claustrofobicness = 20;
						break;
					}
				}
			}
		}
		
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
