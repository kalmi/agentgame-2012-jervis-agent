package jervis.AI.RecommendationEngines;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import jervis.AI.Agent;
import jervis.AI.State;
import jervis.AI.RecommendationEngines.Recommendation.RecommendationType;
import jervis.CommonTypes.MyDir;

public class WaterDislikerEngine extends RecommendationEngine {

	public WaterDislikerEngine(int strength){
		super(strength);
	}
	
	@Override
	public List<Recommendation> getRecommendation(State state, int myId) {
		Agent agent = state.agentsInOrder[myId];
		int x = agent.position.x;
		int y = agent.position.y;
		boolean waterToUp = state.waterManager.isWater(new Point(x,y-1));
		boolean waterToDown = state.waterManager.isWater(new Point(x,y+1));
		boolean waterToRight = state.waterManager.isWater(new Point(x+1,y));
		boolean waterToLeft = state.waterManager.isWater(new Point(x-1,y));
		
		EnumSet<MyDir> result = EnumSet.noneOf(MyDir.class);
		
		if(waterToRight){
			result.add(MyDir.left);
			result.add(MyDir.up);
			result.add(MyDir.down);
		}
		if(waterToLeft){
			result.add(MyDir.right);
			result.add(MyDir.up);
			result.add(MyDir.down);
		}
		if(waterToUp){
			result.add(MyDir.left);
			result.add(MyDir.right);
			result.add(MyDir.down);
		}
		if(waterToDown){
			result.add(MyDir.left);
			result.add(MyDir.right);
			result.add(MyDir.up);
		}
		
		List<Recommendation> r = new ArrayList<Recommendation>();
		for (MyDir myDir : result) {
			r.add(new Recommendation(strength,RecommendationType.move,myDir));
			System.out.println(myDir);
		}
		return r;
	}

}
