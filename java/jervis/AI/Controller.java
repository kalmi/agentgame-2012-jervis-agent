package jervis.AI;

import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;

import jervis.AI.Debug.DebugFrame2;
import jervis.AI.Debug.DebugToggle;
import jervis.AI.RecommendationEngines.Recommendation;
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
			state.debugInfo.append("Agent #");
			state.debugInfo.append(p.internalId);
			state.debugInfo.append("\n\n");
		}
		
		Agent agent = state.getAgent(p.internalId);	
		agent.update(p);		
		state.processVisibleFoods(agent, p);
		state.processEnemyAgents(agent, p);
		
		SimpleEnergyWatcher.run(p, state);

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
			
			int[] turnRecommendedness = new int[4];
			int[] moveRecommendedness = new int[4];
						
			for (RecommendationEngine engine : me.recommendationEngines) {
				for (Recommendation r : engine.getRecommendation(state, me.id)) {
					if(DebugToggle.ENABLED){
						state.debugInfo.append("    ");
						state.debugInfo.append(r.toString());
						state.debugInfo.append("\n");
					}
					if(r.recommendationType == Recommendation.RecommendationType.moveOrTurn){
						//turnRecommendedness[r.dir.ordinal()] += r.strength;
						moveRecommendedness[r.dir.ordinal()] += r.strength;
					} else if(r.recommendationType == Recommendation.RecommendationType.turn){
						turnRecommendedness[r.dir.ordinal()] += r.strength;
					}	
				}
			}
			
			int highestMoveRecommendedness = 0;
			MyDir moveDir = null;;
			for (int i = 0; i < moveRecommendedness.length; i++) {
				if(highestMoveRecommendedness < moveRecommendedness[i]){
					highestMoveRecommendedness = moveRecommendedness[i];
					moveDir = MyDir.fromInt(i);
				}
			}
			
			int highestTurnRecommendedness = 0;
			MyDir turnDir = null;
			for (int i = 0; i < turnRecommendedness.length; i++) {
				if(highestTurnRecommendedness < turnRecommendedness[i]){
					highestTurnRecommendedness = turnRecommendedness[i];
					turnDir = MyDir.fromInt(i);
				}
			}
			
			boolean justTurn = ((highestMoveRecommendedness < highestTurnRecommendedness) /*&& turnDir != me.direction*/);
			
			if (highestMoveRecommendedness + highestTurnRecommendedness == 0)
				return new Wait();
			else if (justTurn)
				return new Turn(turnDir);
			else if (moveDir != me.direction)			
				return new Turn(moveDir);				
			else
				return new Move(moveDir);		
		}
	}
}
