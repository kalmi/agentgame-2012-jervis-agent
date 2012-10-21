package jervis.JasonLayer.Commands;

import jervis.AI.Agent;
import jervis.AI.State;

public class Transfer extends Command {

	final Agent source;
	final Agent target;
	final int amount;
	
	public Transfer(Agent source, Agent target, int amount) {
		this.source = source;
		this.target = target;
		this.amount = amount;
		
		this.actionType = Action.transfer;
		this.params = new int[]{
				target.jasonId,
				amount};		
	}

	@Override
	public void pretend(Agent agent, State state) {
		source.energy -= amount;
		target.energy += amount*0.95;
	}

}
