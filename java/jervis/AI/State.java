package jervis.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jervis.AI.Utils.CircularArrayList;
import jervis.CommonTypes.Food;
import jervis.CommonTypes.PerceivedAgent;

import jervis.CommonTypes.Perception;


public class State {
	
	
	
	public final Agent[] agentsInOrder;
	public List<PerceivedAgent> enemyAgents;
	public boolean simpleIsAlive = true;
	
	public List<Food> foods = new ArrayList<Food>();
	
	public final CircularArrayList<Integer> last4Consumption = new CircularArrayList<Integer>(4);
	public final CircularArrayList<Integer> last4NewSeen = new CircularArrayList<Integer>(4);

	public final WaterManager waterManager = new WaterManager();
	
	public final int[][] obstacleTimes = new int[60][60];
	
	public boolean isObstacle(Agent me, Point point){
		return isObstacle(me, point.x, point.y);
	}
	
	public boolean isObstacle(Agent me, int x, int y){
		int t = obstacleTimes[x][y];
		int expires = t + 5;
		return  !(expires <= me.time);
	}
	
	public State() {
		this.agentsInOrder = new Agent[Config.numOfJervis];
	}


	public void processVisibleFoods(Agent perceiver, Perception p){
		if(p.visibleFoods != null) {
			for (int i=foods.size()-1;i>=0;i--){
				Point food = foods.get(i);
				if(perceiver.canSee(foods.get(i)) && !p.visibleFoods.contains(food)){
					//System.out.println("-Sir, MY COOOKIE IS GONE! http://www.youtube.com/watch?v=7enjABApKWE");
			    	foods.remove(i);
				}
			}
					
			for (Food food : p.visibleFoods){
				final int posInList = foods.indexOf(food);
				final boolean isAlreadyKnown = (posInList != -1);
				if(isAlreadyKnown){
					foods.get(posInList).value = food.value;
				}else{
					foods.add(food);
					last4NewSeen.insert(perceiver.getInternalTime());
				}
			}
		}
	}
	

	public void processEnemyAgents(Agent agent, Perception p) {
		List<PerceivedAgent> list = new LinkedList<PerceivedAgent>();
		if(p.visibleAgents != null){
			for (PerceivedAgent otherAgent : p.visibleAgents) {
				if(otherAgent.teamId != p.myteam){
					list.add(otherAgent);
					obstacleTimes[otherAgent.x][otherAgent.y] = agent.time;  
					Stat.logSimpleSeen();
				}
			}
		}
		enemyAgents = list;
	}

	public Agent getAgent(int internalId) {
		Agent agent = this.agentsInOrder[internalId];
		return agent;
	}
}
