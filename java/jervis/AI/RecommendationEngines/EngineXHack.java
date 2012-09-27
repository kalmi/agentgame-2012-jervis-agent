package jervis.AI.RecommendationEngines;

import java.util.EnumSet;
import java.util.Set;

import jervis.AI.State;
import jervis.CommonTypes.MyDir;



public class EngineXHack implements RecommendationEngine {

	public Set<MyDir> getRecommendation(State state, int myId) {
		//Agent agent = state.agents.get(myId);
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		/*
		if(myId < 3){
		result.add(MyDir.right);
		result.add(MyDir.left);
		}*/
		
		return result;
			
	}

}
