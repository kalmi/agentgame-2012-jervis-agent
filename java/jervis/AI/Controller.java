package jervis.AI;

import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;

import java.util.EnumSet;
import java.util.Set;

import jervis.AI.Debug.DebugFrame2;
import jervis.AI.Debug.DebugToggle;
import jervis.AI.RecommendationEngines.RecommendationEngine;
import jervis.CommonTypes.MyDir;
import jervis.CommonTypes.Perception;
import jervis.JasonLayer.Commands.*;





public class Controller {
	
	DebugFrame2 debugFrame;
	
	State state = new State();
	
	int forgasHack = 0;
	
	public Controller(){
		if(DebugToggle.ENABLED){
			debugFrame = new DebugFrame2();
		}
	}
	
	public Command process(Perception p, AgArch agArch) {		

		if(DebugToggle.ENABLED){
			state.debugInfo = new StringBuffer();
		}
		
		Agent agent = state.getAgent(p.internalId);
		
		agent.update(p);		
		state.processVisibleFoods(agent, p);
		state.processEnemyAgents(agent, p);

		Command command = determineAppropiateCommandFor(agent);
		
		ActionExec action = command.toAction();
		agArch.act(action, null);
		
		if(action.getResult() == true){
			command.pretend(agent, state);
		} else {
			System.out.println( command.toString() + " failed." );
		}
		
		
		
		
		
		
		if(DebugToggle.ENABLED){
			state.debugInfo.append("Decision: ");
			state.debugInfo.append(command.toString());
			state.debugInfo.append("\n");
			state.debugInfo.append("\n");
	
			debugFrame.add(new State(state));
		}
		
		
		
		return command;
	}

	private Command determineAppropiateCommandFor(Agent me) {
		if(me.onFood != null){
			return new Eat();		
		} else {
			if(DebugToggle.ENABLED){
				state.debugInfo.append("  Recommendations:\n");
			}
			
			EnumSet<MyDir> recommendation = EnumSet.allOf(MyDir.class);
			MyDir currentBest = null;
			for (RecommendationEngine engine : me.recommendationEngines) {
				Set<MyDir> currentRecommendation = engine.getRecommendation(state, me.id);
				if(currentRecommendation.size() == 0)
					continue;
				else{
					if(DebugToggle.ENABLED){
						state.debugInfo.append("    ");
						state.debugInfo.append(engine.getClass().getName());
						state.debugInfo.append(": ");
					}
					
					EnumSet<MyDir> newRecommendation = recommendation.clone();
					newRecommendation.retainAll(currentRecommendation);
					
					if(DebugToggle.ENABLED){
						state.debugInfo.append(newRecommendation.toString());
						state.debugInfo.append("\n");
					}
					
					if(newRecommendation.size() > 0){
						recommendation = newRecommendation;
						currentBest = recommendation.iterator().next();
						if(recommendation.size() == 1){
							if(DebugToggle.ENABLED){
								state.debugInfo.append("Only one recommendation → We are done here\n");
							}
							break;
						}
					}
				}				
			}
			
			
			if (currentBest == null)
				return new Wait();
			else if (currentBest != me.direction)			
				return new Turn(currentBest);				
			else
				return new Move(currentBest);		
		}
	}
}
