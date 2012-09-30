package jervis.AI.RecommendationEngines;

import java.util.ArrayList;
import java.util.List;

import jervis.AI.Agent;
import jervis.AI.State;
import jervis.AI.RecommendationEngines.Recommendation.RecommendationType;

public class EngineTurnOnEat extends RecommendationEngine {
	public EngineTurnOnEat(int strength) {
		super(strength);
	}
	
	public List<Recommendation> getRecommendation(State state, int myId) {
		ArrayList<Recommendation> r = new ArrayList<Recommendation>();
		if(state.last4Consumption.isEmpty()) return r;
		
		Agent me = state.getAgent(myId);
		if(!EngineFoodLiker.okForAgent(me.position, me)) return r;
		
		if(!state.last4NewSeen.isEmpty())
			if(state.last4Consumption.getNewest() < state.last4NewSeen.getNewest())
				return r;
		
		int lastConsumedAt = state.last4Consumption.getNewest();
		if(lastConsumedAt >= me.getInternalTime() - 3*6){
			r.add(new Recommendation(strength,RecommendationType.turn,me.direction.cwNext()));
		}
		return r;
	}
}
