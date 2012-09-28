package jervis.JasonLayer.Commands;

import jervis.AI.Agent;
import jervis.AI.State;
import jervis.CommonTypes.MyDir;

public class Move extends Command {
	final private MyDir dir; 
	
	public Move(MyDir dir){
		this.actionType = Action.step;
		this.params = new int[]{dir.ordinal()};
		this.dir = dir;
	}

	@Override
	public void pretend(Agent agent, State state) {
		if(agent.energy>=5){
			switch (dir) {
			case up:
				agent.position.y -= 1;
				break;
				
			case right:
				agent.position.x += 1;
				break;
				
			case down:
				agent.position.y += 1;
				break;
				
			case left:
				agent.position.x -= 1;
				break;						
			}
			
			agent.energy -= 5;
		}
	}
}
