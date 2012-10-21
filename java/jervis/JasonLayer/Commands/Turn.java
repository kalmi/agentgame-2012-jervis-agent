package jervis.JasonLayer.Commands;

import jervis.AI.Agent;
import jervis.AI.Config;
import jervis.AI.State;
import jervis.CommonTypes.MyDir;

public class Turn extends Command {
	final private MyDir dir; 
	
	public Turn(MyDir dir) {
		this.actionType = Action.turn;
		this.params = new int[]{dir.ordinal()};		
		this.dir = dir;
	}

	@Override
	public void pretend(Agent agent, State state) {
		if(agent.energy>= 2){
			agent.direction = dir;	
			agent.energy -= 2 * (agent.inwater? Config.waterCostFactor : 1);
		}
	}

}
