package jervis.AI;

import java.awt.Point;
import java.util.LinkedList;

import jervis.CommonTypes.Food;
import jervis.CommonTypes.MyDir;
import jervis.CommonTypes.Perception;
import jervis.JasonLayer.Commands.Command;



public class Agent {
	public final String myName;
    public final int order;
    
    public Point position;
    public MyDir direction;   
	public int energy;
	public Food onFood = null;
	public int time = 1;
	public boolean inwater = false;
	public boolean hightlighted;
	
	public Command nextCommand = null;
	
	public boolean otherTeamWasSeenEatingByMeLastTime = false;
	public boolean doIComeAfterEnemy = false;
	
	private static int orderCounter = 0;
	
    public Agent(String myName) {
    	
		this.myName = myName;
		this.order = orderCounter++;
		this.energy = 20000/Config.numOfJervis;
	}
    
    public Agent(Agent original) {
    	this.doIComeAfterEnemy = original.doIComeAfterEnemy;
    	
    	this.direction = original.direction;
    	this.position = (Point) original.position.clone();
    	this.order = original.order;
    	this.myName = original.myName; 
	}

	public int getInternalTime(){
    	return time*Config.numOfJervis + order;
    }

    private boolean debugOutputtedThisRound;
    private boolean silent;
	public boolean lastCommandFailed = false;
	public LinkedList<Point> plan;
	public boolean goingForFood;
	public boolean replanSceduled;
	public int lastSuccessfulMove;
	public int jasonId;
    private void debug(String text){
    	if(!silent){
	    	System.out.println("[" + Integer.toString(order) + ":"+ Integer.toString(time) +"] " + text);
	    	debugOutputtedThisRound = true;
    	}
    }
    
    
    public void update(Perception p){
    	debugOutputtedThisRound = false;
    	time = p.time;
    	silent = !(time > 1);
    	
    	inwater = p.inwater;
    	hightlighted = p.highlighted;
    	
    	this.jasonId = p.jasonId; 
    	/*if(p.idFromName != order){
    		id = p.idFromName;
			debug("-Test complete. Preparing to power down and begin diagnostics..."); 
			debug("-Uh, yeah, tell you what. Do a weather and ATC check, start listening in on ground control."); 
			debug("-Sir, there are still terabytes of calculations required before an actual flight is..."); 
			debug("-Jarvis... sometimes you gotta run before you can walk.");
		}
    	
    	if(p.jasonId!=order){
    		order = p.jasonId;
    		debug("-Sir, I am number " + Integer.toString(order) + " in order.");
    	}*/
    	
    	
		if(!p.mypos.equals(position)){
			debug("-Sir, it appears that we are losing altitude. Adjusting...");
			position = p.mypos;
		}
		
		if(!p.mydir.equals(direction)){
			debug("-Sir, I don't think I actually look the way I think I do. Adjusting...");
			direction = p.mydir;
		}
    	
		//onFood depends on properly set "this.position" (dependency)
		if(p.visibleFoods == null){
			debug("-Sir, we have temporarly lost our \"food\" sensors. ");
		}else{
			boolean shouldBeOnFood = (onFood != null);			
			boolean seemsToBeOnFood = false;
			onFood = null;
			for (Food food : p.visibleFoods) {
				 if (food.equals(position)) {
					 seemsToBeOnFood = true;
					 onFood = food;
				}
			}			
			if(shouldBeOnFood == seemsToBeOnFood){
				if(shouldBeOnFood && !seemsToBeOnFood){
					debug("-Sir, I was anticipating that we would be sitting on a food now, but are in fact not.");
				} else if (shouldBeOnFood && !seemsToBeOnFood){
					debug("-Sir, an unanticipated sudden food appeared under us.");
				}
			}	
		}
		
		if(p.visibleAgents == null){
			debug("-Sir, we have temporarly lost our \"agent\" sensors. ");
		}	
		
		if(p.myenergy != energy){
			debug("-Sir, I am experiencing unexpected energy fluctuations. Adjusting...");
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
		s.append("Agent ");
		s.append(myName);
		s.append(" #");
		s.append(order);
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


	public static int getCollectiveEnergy(Agent[] agents) {
		int energy = 0;
		for (Agent agent : agents) {
      if(agent!=null) //SHOULD NOT HAPPEN (syncronization in main probably solved it)
        energy += agent.energy;
		}
		return energy;
	}
}
