package kalmi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import kalmi.Command.Action;




public class Controller {
	
	DebugFrame2 debugFrame = new DebugFrame2();

	State state = new State();
	
	int forgasHack = 0;
	
	public Command process(int internalId, Point mypos, MyDir mydir, ArrayList<Point> visibleFoods) {
		
		state.debugInfo = new StringBuffer();
		
		List<Point> foods = state.foods;
		for (int i=foods.size()-1;i>=0;i--){
			Point food = foods.get(i);
			if(Utils.isVisible(mypos, mydir, foods.get(i)) && !visibleFoods.contains(food))
		    	foods.remove(i);
		}
				
		for (Point food : visibleFoods)
			if(!foods.contains(food))
				foods.add(food);
		
		
		Agent agent = state.agents.get(internalId);
		
		if(agent == null){
			agent = new Agent();
			state.agents.put(internalId, agent);
		}
		
		
		
		agent.direction = mydir;
		agent.position = mypos;
		agent.id = internalId;
		
		if( agent.banTurn > 0)
			agent.banTurn--;
		
		if( agent.claustrofobicness > 0)
			agent.claustrofobicness--;
		
		
		state.debugInfo.append("Agent #");
		state.debugInfo.append(internalId);
		state.debugInfo.append("\n");
		state.debugInfo.append("  Pos: ");
		state.debugInfo.append(mypos.x);
		state.debugInfo.append(",");
		state.debugInfo.append(mypos.y);
		state.debugInfo.append("\n");
		state.debugInfo.append("  Dir: ");
		state.debugInfo.append(mydir.name());
		state.debugInfo.append("(");
		state.debugInfo.append(mydir.ordinal());
		state.debugInfo.append(")\n");
		state.debugInfo.append("  Clau: ");
		state.debugInfo.append(agent.claustrofobicness);
		state.debugInfo.append("\n");
		
		

		
		boolean onFood = false;
		for (Point food : foods) {
			if(food.equals(mypos)){
				onFood = true;
				forgasHack = 3*3; //TODO: Csak akkor forogni, ha tényleg elfogy.
				break;
			}
		}
		
				
		Command command = new Command();
		if(onFood){
			command = new Command(Action.eat, 0, 0);			
		} else {
			state.debugInfo.append("  Recommendations:\n");
			EnumSet<MyDir> recommendation = EnumSet.allOf(MyDir.class);
			MyDir currentBest = null;
			for (RecommendationEngine engine : agent.recommendationEngines) {
				Set<MyDir> currentRecommendation = engine.getRecommendation(state, internalId);
				if(currentRecommendation.size() == 0)
					continue;
				else{
					state.debugInfo.append("    ");
					state.debugInfo.append(engine.getClass().getName());
					state.debugInfo.append(": ");
					
					
					EnumSet<MyDir> newRecommendation = recommendation.clone();
					newRecommendation.retainAll(currentRecommendation);
					
					state.debugInfo.append(newRecommendation.toString());
					state.debugInfo.append("\n");
					
					if(newRecommendation.size() > 0){
						recommendation = newRecommendation;
						currentBest = recommendation.iterator().next();
						if(recommendation.size() == 1){
							state.debugInfo.append("Only one recommendation → We are done here\n");
							break;
						}
					}
				}
				
			}
			
			
			if (currentBest != null){
				if(agent.banTurn==0 && currentBest != mydir){
					agent.direction = currentBest;
					command = new Command(Action.turn, currentBest.ordinal(), 0);
					agent.banTurn = 0;
				}else{
					command = new Command(Action.step, currentBest.ordinal(), 0);
				}
			}
			else{
				if(forgasHack!=0){
					command = new Command(Action.turn, (mydir.ordinal()+1)%4,0);
					forgasHack--;
				}
				
				
			}
		}
		
		state.debugInfo.append("Decision: ");
		state.debugInfo.append(command.toString());
		state.debugInfo.append("\n");
		state.debugInfo.append("\n");
		
		state.debugInfo.append("WAITER: ");
		state.debugInfo.append(EngineBevarosPathRouter.waiter);
		state.debugInfo.append("\n");
		state.debugInfo.append("LAUNCH: ");
		state.debugInfo.append(EngineBevarosPathRouter.launch);
		state.debugInfo.append("\n");
		
		//debugFrame.add(new State(state));
		
		

		
		
		return command;
	}
	
	
}
