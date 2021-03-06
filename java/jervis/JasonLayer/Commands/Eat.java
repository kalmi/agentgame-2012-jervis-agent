package jervis.JasonLayer.Commands;

import jervis.AI.Agent;
import jervis.AI.Stat;
import jervis.AI.State;
import jervis.CommonTypes.Food;



public class Eat extends Command {

	public Eat() {
		this.actionType = Action.eat;
	}

	@Override
	public void pretend(Agent agent, State state) {
		Food food = agent.onFood;
		
		int change;
		if(food.value > 20){
			change = 20;
		} else {
			change = food.value;
		}
		
		agent.energy += change;		
		agent.onFood.value -= change;		
		agent.onFood.value -= 5;
		
		if(agent.onFood.value <= 0){
			agent.onFood = null;
			state.foods.remove(food);
			state.last4Consumption.insert(agent.getInternalTime());
			Stat.logEatFinshed(agent);
		}
		state.seennessManager.report(agent);
	}

}
