package kalmi;

import java.util.Set;

public interface RecommendationEngine {
	public Set<MyDir> getRecommendation(State state, int myId);
}
