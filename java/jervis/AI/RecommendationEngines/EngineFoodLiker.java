package jervis.AI.RecommendationEngines;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import jervis.AI.Agent;
import jervis.AI.State;
import jervis.AI.RecommendationEngines.Recommendation.RecommendationType;
import jervis.CommonTypes.MyDir;




public class EngineFoodLiker extends RecommendationEngine {

	public EngineFoodLiker(int strength) {
		super(strength);
	}

		@SuppressWarnings("serial")
		static List<Rectangle> boundPerAgent = new ArrayList<Rectangle>(){{
		
			add(new Rectangle(0,  0, 60, 60));
			add(new Rectangle(0,  0, 60, 60));
			add(new Rectangle(0,  0, 60, 60));
			
			add(new Rectangle(0,  0, 60, 60));
			add(new Rectangle(0,  0, 60, 60));
			add(new Rectangle(0,  0, 60, 60));

					
		}};
	
	public static boolean okForAgent(Point food, Agent agent){
		//ArrayList<Rectangle> bounds = boundListPerAgent.get(agent.id-1);
		Rectangle r = boundPerAgent.get(agent.order);
		boolean inBound = false;
		//for (Rectangle r : bounds) {
			if(r.contains(food))
				inBound = true;
		//}
		return inBound;
		//return true;
	} 
		
	public List<Recommendation> getRecommendation(State state, int myId) {
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		if(state.foods.size() == 0)
			return new ArrayList<Recommendation>();
		
		Agent agent = state.agentsInOrder[myId];
		
		Point closestFood = null;
		int closestDistance = Integer.MAX_VALUE;
		for (Point food : state.foods) {
			
			if(state.enemyAgent != null)
				if(state.enemyAgent.x == food.x && state.enemyAgent.y == food.y)
					continue;
			
			if(!okForAgent(food, agent))
				continue;
			
			
			
			Agent closestToAgent = null;
			int closestDistanceToAgent = Integer.MAX_VALUE;
			
			for (Agent otherAgent : state.agentsInOrder) {
				
				if(otherAgent == null)
					continue;
				
				if(!okForAgent(food, otherAgent))
					continue;
				
				Agent a = otherAgent;
				int d = Math.abs(food.x - a.position.x) + Math.abs(food.y - a.position.y);
				if(closestDistanceToAgent > d){
					closestDistanceToAgent = d;
					closestToAgent = otherAgent;
				}
			}
			
			if(closestToAgent != agent) continue;
			
			 
			if(closestDistanceToAgent < closestDistance){
				closestDistance = closestDistanceToAgent;
				closestFood = food;
			}
		}
		
		if(closestFood == null)
			return new ArrayList<Recommendation>();
		
		if(agent.position.x > closestFood.x)
			result.add(MyDir.left);
		else if (agent.position.x < closestFood.x)
			result.add(MyDir.right);
		
		if(agent.position.y < closestFood.y)
			result.add(MyDir.down);
		else if (agent.position.y > closestFood.y)
			result.add(MyDir.up);
		
		List<Recommendation> r = new ArrayList<Recommendation>();
		for (MyDir myDir : result) {
			r.add(new Recommendation(strength,RecommendationType.move,myDir));
		}
		return r;
	}

}
