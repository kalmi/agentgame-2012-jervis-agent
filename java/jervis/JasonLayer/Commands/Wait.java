package jervis.JasonLayer.Commands;

import jervis.AI.Agent;
import jervis.AI.State;

public class Wait extends Command {
	public Wait(){
		this.actionType = Action.wait;
	}

	@Override
	public void pretend(Agent agent, State state) {
		state.seennessManager.report(agent);
	}
}
