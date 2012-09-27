package kalmi;

import jason.stdlib.foreach;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.sun.management.VMOption.Origin;


public class State {
	public Map<Integer,Agent> agents = new HashMap<Integer,Agent>();
	public List<Point> foods = new ArrayList<Point>();
	
	public StringBuffer debugInfo = new StringBuffer();
	
	public State(){}
	
	public State(State s){
		debugInfo.append(s.debugInfo);
		for (Point food : s.foods) {
			foods.add((Point) food.clone());
		}
		
		for (Integer x : s.agents.keySet()) {
			Agent original = s.agents.get(x); 
			Agent agent = new Agent();
			agent.direction = original.direction;
			agent.position = (Point) original.position.clone();
			agent.id = original.id;
			agent.claustrofobicness = original.claustrofobicness;
			agents.put(x, agent);
		}
	}
}
