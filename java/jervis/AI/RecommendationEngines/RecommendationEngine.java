package jervis.AI.RecommendationEngines;

import java.util.Set;

import jervis.AI.State;
import jervis.CommonTypes.MyDir;



public interface RecommendationEngine {
	public Set<MyDir> getRecommendation(State state, int myId);
}
