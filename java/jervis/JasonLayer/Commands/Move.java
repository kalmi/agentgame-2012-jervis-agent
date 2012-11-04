package jervis.JasonLayer.Commands;

import java.awt.Point;

import jervis.AI.Agent;
import jervis.AI.Config;
import jervis.AI.State;
import jervis.CommonTypes.MyDir;

public class Move extends Command {
	final public MyDir dir; 
	
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
			
			agent.energy -= 5 * (agent.inwater? Config.waterCostFactor : 1);
      state.seennessManager.report(agent);
		}
	}
	
	@Override
	public Point getDestination(Agent agent){
		int dy = 0, dx = 0;
		switch (dir) {
		case up:
			dy -= 1;
			break;
			
		case right:
			dx += 1;
			break;
			
		case down:
			dy += 1;
			break;
			
		case left:
			dx -= 1;
			break;						
		}
		
		return new Point(agent.position.x+dx, agent.position.y+dy);
	}
}
