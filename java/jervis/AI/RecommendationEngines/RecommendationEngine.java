package jervis.AI.RecommendationEngines;

import java.util.List;
import jervis.AI.State;


public abstract class  RecommendationEngine {
	public abstract List<Recommendation> getRecommendation(State state, int myId);
	
	protected int strength;
	public RecommendationEngine(int strength){
		this.strength = strength;
	}
}
