package jervis.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jervis.AI.RecommendationEngines.EngineFoodLiker;
import jervis.AI.RecommendationEngines.EngineMultiplePathRouter;
import jervis.AI.RecommendationEngines.EngineWallDisliker;
import jervis.AI.RecommendationEngines.RecommendationEngine;
import jervis.CommonTypes.MyDir;


public class Agent {
    public int id;
    public Point position;
    public MyDir direction;
    public int banTurn = 0;
	public int claustrofobicness = 0;

	@SuppressWarnings("serial")
	List<RecommendationEngine> recommendationEngines = new ArrayList<RecommendationEngine>(){{
		//add(new EngineFoodLiker());
		//add(new EngineWallDisliker());
		//add(new EngineMultiplePathRouter());
	}};
	
    public Agent() {
		// TODO Auto-generated constructor stub
	}

	public boolean isFacing(Point pos) {
        int A = position.x + position.y, B = position.y - position.x,
                C = pos.x + pos.y, D = pos.y - pos.x;

        if (direction == MyDir.up) {
            return C <= A && D <= B;
        } else if (direction == MyDir.right) {
            return C >= A && D <= B;
        } else if (direction == MyDir.down) {
            return C >= A && D >= B;
        } else {
            return C <= A && D >= B;
        }
    }

    public boolean canSee(Point pos) {
        if (Math.abs(position.x - pos.x) > 10 || Math.abs(position.y - pos.y) > 10) {
            return false;
        }
        return isFacing(pos); 
    }
}
