package jervis.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;

import jervis.AI.Debug.DebugFrame2;
import jervis.AI.Debug.DebugToggle;
import jervis.AI.RecommendationEngines.Recommendation;
import jervis.AI.RecommendationEngines.RecommendationEngine;
import jervis.AI.RecommendationEngines.Recommendation.RecommendationType;
import jervis.CommonTypes.MyDir;
import jervis.CommonTypes.PerceivedAgent;
import jervis.CommonTypes.Perception;
import jervis.JasonLayer.Commands.*;





public class Controller {
	
	DebugFrame2 debugFrame;	
	State state;
	private Random random = new Random();
	
	private final Map<String, Agent> mapping = new HashMap<String, Agent>();
	
	public Controller() {
		if(DebugToggle.GUIENABLED){
			debugFrame = new DebugFrame2();
		}
		state = new State();
	}

	public void process(Perception p, AgArch agArch) {		
		if(DebugToggle.GUIENABLED){
			state.debugInfo = new StringBuffer();
			state.debugInfo.append("Agent #");
			state.debugInfo.append(p.idFromName);
			state.debugInfo.append("\n\n");
		}
		
		if(!mapping.containsKey(p.myname)){
			Agent agent = new Agent(p.myname);
			mapping.put(p.myname, agent);
			state.agentsInOrder[agent.order] = agent;
		}
		
		Agent agent = mapping.get(p.myname);
		
		agent.update(p);		
		
		/*System.out.println();
		System.out.println(agent.myName);
		System.out.println(agent.order);*/
		
		state.processVisibleFoods(agent, p);
		state.processEnemyAgents(agent, p);
		
		
		final boolean simpleAlive = SimpleEnergyWatcher.run(p, state);
		
		if(!simpleAlive && state.simpleIsAlive){
			System.out.println("-Sir, our best friend is dead.");
		} else if (simpleAlive && !state.simpleIsAlive) {
			System.out.println("-Sir, I'm sensing a zombie.");
		}
		state.simpleIsAlive = simpleAlive;

		Command command = determineAppropiateCommandFor(agent);
		
		ActionExec action = command.toAction();
		agArch.act(action, null);
		
		if(action.getResult() == true){
			command.pretend(agent, state);
		} else {
			System.out.println( "-Sir, this is not good: " + command.toString() + " failed." );
		}
		
		
		
		
		
		
		if(DebugToggle.GUIENABLED){
			state.debugInfo.append("Decision: ");
			state.debugInfo.append(command.toString());
			state.debugInfo.append("\n");
			state.debugInfo.append("\n");
	
			debugFrame.add(new State(state));
		}
		
		Stat.logCommand(agent, command);
		
		if(agent.order >= Config.numOfJervis-1 && p.time == 14999){
			for (String line: Stat.getSummary().split("\n")) {
				System.out.println(line);
			}
			
		}
		
	}

	private Command determineAppropiateCommandFor(Agent me) {
		if(me.onFood != null){
			return new Eat();		
		} else {
			if(me.nextCommand != null){
				Command tmp = me.nextCommand;
				me.nextCommand = null;
				return tmp;
			}
			
			if(DebugToggle.ENABLED){
				state.debugInfo.append("  Recommendations:\n");
			}
			
			List<Recommendation> turnRecommendedness = new ArrayList<Recommendation>();
			List<Recommendation> moveOrTurnRecommendedness = new ArrayList<Recommendation>();
			List<Recommendation> moveRecommendedness = new ArrayList<Recommendation>();
			
			for (MyDir d : MyDir.values()) {
				turnRecommendedness.add(new Recommendation(0, RecommendationType.turn, d));
				moveOrTurnRecommendedness.add(new Recommendation(0, RecommendationType.moveOrTurn, d));
				moveRecommendedness.add(new Recommendation(0, RecommendationType.move, d));
			}
			
			
						
			for (RecommendationEngine engine : me.recommendationEngines) {
				for (Recommendation r : engine.getRecommendation(state, me.order)) {
					if(DebugToggle.ENABLED){
						state.debugInfo.append("    ");
						state.debugInfo.append(r.toString());
						state.debugInfo.append("\n");
					}
					if(r.recommendationType == Recommendation.RecommendationType.moveOrTurn){
						//turnRecommendedness[r.dir.ordinal()] += r.strength;
						moveOrTurnRecommendedness.get(r.dir.ordinal()).add(r);
						moveRecommendedness.get(r.dir.ordinal()).add(r, 0.5);
						turnRecommendedness.get(r.dir.ordinal()).add(r, 0.5);
					} else if(r.recommendationType == Recommendation.RecommendationType.turn){
						turnRecommendedness.get(r.dir.ordinal()).add(r);
					} else if(r.recommendationType == Recommendation.RecommendationType.move){
						moveRecommendedness.get(r.dir.ordinal()).add(r);
						moveOrTurnRecommendedness.get(r.dir.ordinal()).add(r, 0.5);
					}	
				}
			}
		
			
			List<Recommendation> recommendations = new ArrayList<Recommendation>();
			recommendations.addAll(moveOrTurnRecommendedness);
			recommendations.addAll(turnRecommendedness);
			recommendations.addAll(moveRecommendedness);
			
			Collections.sort(recommendations);
			Collections.reverse(recommendations);
			
			for (Recommendation r : recommendations) {
				if(r.getStrength() <= 2) break;
				
				if(r.recommendationType == RecommendationType.turn){
					if(r.dir == me.direction){
						System.out.println("-Sir, you are dumb. We are already facing that way.");
						continue;
					} else {
						return new Turn(r.dir);
					}
				} else if (r.recommendationType == RecommendationType.moveOrTurn || r.recommendationType == RecommendationType.move) {
					Command move = new Move(r.dir);
					boolean blocking_detected = false;
					for (PerceivedAgent e : state.enemyAgents) {
						if(move.getDestination(me).equals(e))
							blocking_detected = true;
					}
					if (!blocking_detected){
						if(r.dir == me.direction ||  r.recommendationType == RecommendationType.move)
							return move;
						else
							return new Turn(r.dir);
					} else {
						Command currentCommand;
						if(me.position.x == 0 && (r.dir == MyDir.up || r.dir == MyDir.down)){
							currentCommand = new Move(MyDir.right);
							me.nextCommand = new Move(r.dir);
						} else
						if(me.position.x == 59 && (r.dir == MyDir.up || r.dir == MyDir.down)){
							currentCommand = new Move(MyDir.left);
							me.nextCommand = new Move(r.dir);
						} else
						if(me.position.y == 0 && (r.dir == MyDir.left || r.dir == MyDir.right)){
							currentCommand = new Move(MyDir.down);
							me.nextCommand = new Move(r.dir);
						} else
						if(me.position.y == 59 && (r.dir == MyDir.left || r.dir == MyDir.right)){
							currentCommand = new Move(MyDir.up);
							me.nextCommand = new Move(r.dir);
						}else
						if (r.dir == MyDir.up || r.dir == MyDir.down){
							MyDir firstDir;
							if(random.nextBoolean()){
								firstDir = MyDir.left;
							} else {
								firstDir = MyDir.right;
							}
							
							currentCommand = new Move(firstDir);
							me.nextCommand = new Move(r.dir);
						}else
						if (r.dir == MyDir.left || r.dir == MyDir.right){
							MyDir firstDir;
							if(random.nextBoolean()){
								firstDir = MyDir.up;
							} else {
								firstDir = MyDir.down;
							}
							
							currentCommand = new Move(firstDir);
							me.nextCommand = new Move(r.dir);
						}else{
							throw new RuntimeException();
						}
						return currentCommand;
					}
				}
			}
			
			return new Wait();

		}
	}
}
