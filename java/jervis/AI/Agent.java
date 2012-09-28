package jervis.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jervis.AI.RecommendationEngines.*;
import jervis.CommonTypes.Food;
import jervis.CommonTypes.MyDir;
import jervis.CommonTypes.Perception;


public class Agent {
    public int id;
    public Point position;
    public MyDir direction;
	public int energy = 4000;
	public int time = 1;	
	public Food onFood = null;	
	
	@SuppressWarnings("serial")
	List<RecommendationEngine> recommendationEngines = new ArrayList<RecommendationEngine>(){{
		add(new EngineFoodLiker());
		add(new EngineWallDisliker());
		add(new EngineMultiplePathRouter());
	}};
	
    public Agent() {
		// TODO Auto-generated constructor stub
	}

    private boolean debugOutputtedThisRound;
    private void debug(String text){
    	System.out.println("[" + Integer.toString(id) + ":"+ Integer.toString(time) +"] " + text);
    	debugOutputtedThisRound = true;
    }
    
    
    public void update(Perception p){
    	debugOutputtedThisRound = false;
    	
    	if(p.internalId != id){
			System.out.println("All systems online.");
			id = p.internalId;
		}
    	
		if(!p.mypos.equals(position)){
			debug(" - Sir, it appears that we are losing altitude. Adjusting...");
			position = p.mypos;
		}
		
		if(!p.mydir.equals(direction)){
			debug(" - Sir, I don't think I actually look the way I think I do. Adjusting...");
			direction = p.mydir;
		}
    	
		//onFood depends on properly set "this.position" (dependency)
		if(p.visibleFoods == null){
			debug(" - Sir, we have temporarly lost our \"food\" sensors. ");
		}else{
			boolean shouldBeOnFood = (onFood != null);			
			boolean seemsToBeOnFood = false;
			for (Food food : p.visibleFoods) {
				 if (food.equals(position)) {
					 seemsToBeOnFood = true;
					 onFood = food;
				}
			}			
			if(shouldBeOnFood == seemsToBeOnFood){
				if(shouldBeOnFood && !seemsToBeOnFood){
					debug(" - Sir, I was anticipating that we would be sitting on a food now, but are in fact not.");
				} else if (shouldBeOnFood && !seemsToBeOnFood){
					debug(" - Sir, an unanticipated sudden food appeared under us.");
				}
			}	
		}
		
		if(p.visibleAgents == null){
			debug(" - Sir, we have temporarly lost our \"agent\" sensors. ");
		}	
		
		if(p.myenergy != energy){
			debug(" - Sir, I am experiencing unexpected energy fluctuations. Adjusting...");
			energy = p.myenergy;
		}
		
		if(debugOutputtedThisRound)
			System.out.println("---");
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
    
    public String toString(){
    	StringBuilder s = new StringBuilder();
		s.append("Agent #");
		s.append(id);
		s.append("\n");
		s.append("  Pos: ");
		s.append(position.x);
		s.append(",");
		s.append(position.y);
		s.append("\n");
		s.append("  Dir: ");
		s.append(direction.name());
		s.append("(");
		s.append(direction.ordinal());
		s.append(")\n");
		return s.toString();
    }
}