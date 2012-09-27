package kalmi.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import kalmi.CommonTypes.Food;


public class State {
	
	final int numOfAgents = 5;
	
	public Agent[] agents = new Agent[numOfAgents];
	public List<Food> foods = new ArrayList<Food>();
	
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
			agent.claustrofobicness = original.claustrofobicness;
			agents[original.id] = agent;
		}
	}
}
