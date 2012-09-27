package jervis.AI.RecommendationEngines;

import java.util.EnumSet;
import java.util.Set;

import jervis.AI.Agent;
import jervis.AI.State;
import jervis.CommonTypes.MyDir;



public class EngineWallDisliker implements RecommendationEngine {

	public Set<MyDir> getRecommendation(State state, int myId) {
		Agent agent = state.agents[myId];
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		
		int x = agent.position.x;
		int y = agent.position.y;
		
		if(x < 10)
			result.add(MyDir.right);
		else if (x > 49)
			result.add(MyDir.left);
		
		if(y < 10)
			result.add(MyDir.down);
		else if (y > 49)
			result.add(MyDir.up);
		
		return result;
			
	}

}
