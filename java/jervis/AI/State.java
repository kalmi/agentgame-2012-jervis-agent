package jervis.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jervis.AI.Utils.CircularArrayList;
import jervis.CommonTypes.Food;
import jervis.CommonTypes.PerceivedAgent;

import jervis.CommonTypes.Perception;


public class State {
	
	final int numOfAgents = 5;
	
	public Agent[] agents = new Agent[numOfAgents];
	public PerceivedAgent enemyAgent = null;
	
	public List<Food> foods = new ArrayList<Food>();
	
	public CircularArrayList<Integer> last4Consumption = new CircularArrayList<Integer>(4);
	
	public StringBuffer debugInfo = new StringBuffer();
	
	public State(){}
	
	public State(State s){
		debugInfo.append(s.debugInfo);
		for (Food food : s.foods) {
			foods.add(new Food(food));
		}
		
		for (Agent original : s.agents) {
			Agent agent = new Agent();
			agent.direction = original.direction;
			agent.position = (Point) original.position.clone();
			agent.id = original.id;
			agents[original.id] = agent;
		}
	}
	
	
	public void processVisibleFoods(Agent perceiver, Perception p){
		if(p.visibleFoods != null) {
			for (int i=foods.size()-1;i>=0;i--){
				Point food = foods.get(i);
				if(perceiver.canSee(foods.get(i)) && !p.visibleFoods.contains(food))
			    	foods.remove(i);
			}
					
			for (Food food : p.visibleFoods){
				final int posInList = foods.indexOf(food);
				final boolean isAlreadyKnown = (posInList != -1);
				if(isAlreadyKnown){
					foods.get(posInList).value = food.value;
				}else{
					foods.add(food);
				}
			}
		}
	}
	

	public void processEnemyAgents(Agent agent, Perception p) {
		enemyAgent = null;
		if(p.visibleAgents != null){
			for (PerceivedAgent otherAgent : p.visibleAgents) {
				if(otherAgent.teamId != p.myteam){
					enemyAgent = otherAgent;
				}
			}
		}
	}

	public Agent getAgent(int internalId) {
		Agent agent = this.agents[internalId];		
		if(agent == null){
			agent = new Agent();
			this.agents[internalId] = agent;
		}
		return agent;
	}
}
