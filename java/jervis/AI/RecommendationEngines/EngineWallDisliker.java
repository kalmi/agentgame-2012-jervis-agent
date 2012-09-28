package jervis.AI.RecommendationEngines;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import jervis.AI.Agent;
import jervis.AI.State;
import jervis.AI.RecommendationEngines.Recommendation.RecommendationType;
import jervis.CommonTypes.MyDir;



public class EngineWallDisliker extends RecommendationEngine {

	public EngineWallDisliker(int strength) {
		super(strength);
	}

	public List<Recommendation> getRecommendation(State state, int myId) {
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
		
		List<Recommendation> r = new ArrayList<Recommendation>();
		for (MyDir myDir : result) {
			r.add(new Recommendation(strength,RecommendationType.moveOrTurn,myDir));
		}
		return r;
			
	}

}
