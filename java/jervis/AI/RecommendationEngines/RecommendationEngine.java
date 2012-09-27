package kalmi.AI.RecommendationEngines;

import java.util.Set;

import kalmi.AI.State;
import kalmi.CommonTypes.MyDir;


public interface RecommendationEngine {
	public Set<MyDir> getRecommendation(State state, int myId);
}
