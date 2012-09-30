package jervis.AI.RecommendationEngines;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import jervis.AI.State;
import jervis.AI.RecommendationEngines.Recommendation.RecommendationType;
import jervis.CommonTypes.MyDir;

public class EngineAxisKeeperHack extends RecommendationEngine {

	public EngineAxisKeeperHack(int strength) {
		super(strength);
	}

	@Override
	public List<Recommendation> getRecommendation(State state, int myId) {
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		
		if(myId==0 || myId==2){
			result.add(MyDir.up);
			result.add(MyDir.down);
		}else if(myId==1 || myId==3){
			result.add(MyDir.left);
			result.add(MyDir.right);
		}


		List<Recommendation> r = new ArrayList<Recommendation>();
		for (MyDir myDir : result) {
			r.add(new Recommendation(strength,RecommendationType.move,myDir));
		}
		return r;
	}

}
