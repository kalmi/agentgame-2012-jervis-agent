package jervis.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;

import jervis.AI.GraphTools.Planner;
import jervis.CommonTypes.MyDir;
import jervis.CommonTypes.Perception;
import jervis.JasonLayer.Commands.*;

public class Controller {	
	State state = new State();
	
	@SuppressWarnings("serial")
	private final Map<String, Agent> mapping = new HashMap<String, Agent>(){
		@Override
		public Agent get(Object key){
			Agent agent = super.get(key);
			if(agent==null){
				String name = (String)key;
				agent = new Agent(name);
				mapping.put(name, agent);
				state.agentsInOrder[agent.order] = agent;
			}
			return agent;
		}
	};

	int playDeadTill = 0; 
	public void process(Perception p, AgArch agArch) {
		if(p.time > 1000 && state.agentsInOrder[0].jasonId == p.jasonId && playDeadTill < p.time){
			int safetyMargin = 2;
			
			int time_used = 120 - p.myteamtimeleft;
			double time_per_round = ((double)time_used) / p.time;
			
			int rounds_left = 15000 - p.time;			
			int time_left = 120 - time_used;
						
			double estimated_required_time = time_per_round * rounds_left;
			
			if(estimated_required_time + safetyMargin > time_left && p.time > 1000){
				int N = 250;
				System.out.print("Round #");
				System.out.println(p.time);
				System.out.print("Estimated required time: " );
				System.out.println(estimated_required_time);
				System.out.print("We have: " );
				System.out.println(time_left);
				System.out.println("ZZZZZZZZZzzzzzzzzzzzzzz!");
				System.out.println("---");
				playDeadTill = p.time + N;
				
				for (Agent agent : state.agentsInOrder) {
					agent.lastSuccessfulMove += N;
				}
			}
		}
		
		if(playDeadTill > p.time || state.omg__a_jervis_died_or_deadlocked){
			Command command = new Wait();
			ActionExec action = command.toAction();
			agArch.act(action, null);
			return;
		}
		
		Agent agent = mapping.get(p.myname);
		
		agent.update(p);				
		state.waterManager.report(p);		
		state.processVisibleFoods(agent, p);
		state.processEnemyAgents(agent, p);		
		state.simpleIsAlive = SimpleEnergyWatcher.run(p, state);
		
		Command command = determineAppropiateCommandFor(agent);
		
		ActionExec action = command.toAction();
		agArch.act(action, null);
		
		if(action.getResult() == true){
			command.pretend(agent, state);
			agent.lastCommandFailed = false;
			
			if(command instanceof Move){
				agent.plan.removeFirst();
				agent.lastSuccessfulMove = agent.time;
			}
		} else {
			agent.lastCommandFailed = true;
			//System.out.println( "-Sir, this is not good: " + command.toString() + " failed." );
			if(command instanceof Move){
				Move lastMove = (Move)command;
				agent.replanSceduled = true;
				Point destination = lastMove.getDestination(agent);
				state.obstacleTimes[destination.x][destination.y] = agent.time;  
				state.foods.remove(destination);
				
				if(agent.lastSuccessfulMove < agent.time - 500){
					state.omg__a_jervis_died_or_deadlocked = true; 
				}
			}
		}
		
		
		Stat.logCommand(agent, command);
		/*
		if(agent.order >= Config.numOfJervis-1 && p.time == 14999){
			for (String line: Stat.getSummary().split("\n")) {
				System.out.println(line);
			}			
		}*/
		
	}

	int[] waypointIdPerAgent = new int[5];
	
	@SuppressWarnings("serial")
	List<ArrayList<Point>> waypointsPerAgent = new ArrayList<ArrayList<Point>>(){{
		
		add(new ArrayList<Point>(){{
			add(new Point(10,10));
			add(new Point(28,10));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(10,49));
			add(new Point(10,31));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(49,49));
			add(new Point(31,49));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(49,10));
			add(new Point(49,28));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(30,30));
		}});
		
	}};	
	
	private Point getCurrentWaypointTarget(Agent me){
		return waypointsPerAgent.get(me.order).get(waypointIdPerAgent[me.order]);
	}
	
	private boolean isWaypointReached(Agent me){
		Point target = getCurrentWaypointTarget(me);
		return (target.equals(me.position) || (me.lastCommandFailed && me.position.distance(target) < 2.1));
	}
	
	private void replan(Agent me){
		boolean goingForFood = false;
		
		Point target;
		if(helper == me){
			target = agentInNeed.position;
		} else {
			
			target = getCurrentWaypointTarget(me);
			
			if(isWaypointReached(me)){
				waypointIdPerAgent[me.order] = (waypointIdPerAgent[me.order]+1) % waypointsPerAgent.get(me.order).size();
				target = getCurrentWaypointTarget(me);
			}
			
			if(state.foods.size() != 0){
				Point closestFood = null;
				int closestDistance = Integer.MAX_VALUE;
				for (Point food : state.foods){
					Agent closestToAgent = null;
					int closestDistanceToAgent = Integer.MAX_VALUE;
					
					for (Agent otherAgent : state.agentsInOrder) {
						if(otherAgent == null)
							continue;
						
						Agent a = otherAgent;
						int d = Math.abs(food.x - a.position.x) + Math.abs(food.y - a.position.y);
						if(closestDistanceToAgent > d){
							closestDistanceToAgent = d;
							closestToAgent = otherAgent;
						}
					}
					
					if(closestToAgent != me)
						continue;
					
					if(closestDistanceToAgent < closestDistance){
						closestDistance = closestDistanceToAgent;
						closestFood = food;
					}
				}
				if(closestFood!=null){
					target = closestFood;
					goingForFood = true;
				}
			}
		}	
		Planner planner = new Planner(me.position, target, state, me);
		me.plan = planner.plan();
		me.goingForFood = goingForFood || helper == me;
	}
	
	private void replanEverything(){
		for (Agent me : state.agentsInOrder) {
			if(me==null) continue;
			
			replan(me);
		}
	}
	
	private Agent getHighestEnergyAgent(){
		Agent agentWithHighestEnergy = null;
		for (Agent agent : state.agentsInOrder) {
			if(agentWithHighestEnergy == null || agent.energy>agentWithHighestEnergy.energy){
				agentWithHighestEnergy = agent;
			}
		}
		return agentWithHighestEnergy;
	}
	
	Agent agentInNeed = null;
	Agent helper = null;
	
	private Command determineAppropiateCommandFor(Agent me) {
		if(me.onFood != null){
			return new Eat(); 
		} else {

			if(helper == me && me.position.equals(agentInNeed.position)){
				Command command = new Transfer(me, agentInNeed, me.energy/2);
				agentInNeed.replanSceduled = true;
				helper = null;
				agentInNeed = null;
				System.out.println("Thou shalt have my own!");
				return command;
			}
			
			if(me.energy < 5*Config.waterCostFactor){
				if(agentInNeed==null){
					agentInNeed = me;
					System.out.println("AGENT IN NEED!");
				}
				return new Wait(); //don't move! help is coming...
			}
			
			if(me.nextCommand != null){
				me.replanSceduled = true;
				Command tmp = me.nextCommand;
				me.nextCommand = null;
				return tmp;
			}
						
			boolean replanNeeded = false;
			if(me.replanSceduled){
				me.replanSceduled = false;
				replanNeeded  = true;
			}
			
			if(agentInNeed != null && helper == null && getHighestEnergyAgent() == me){
				helper = me;
				replanNeeded = true;
			}
			
			if(me.time <= 5){
				replanNeeded  = true;
			}
			
			int lastConsumedAt = state.last4Consumption.isEmpty()? Integer.MIN_VALUE : state.last4Consumption.getNewest();
			if(lastConsumedAt >= me.getInternalTime() - Config.numOfAll){
				replanNeeded  = true;
			}
			
			if(isWaypointReached(me)){
				replanNeeded  = true;
			}
			
			if(replanNeeded){
				replanEverything();
			}
			
			if(!me.goingForFood){
				if(lastConsumedAt >= me.getInternalTime() - 3*6){
					return new Turn(me.direction.cwNext());
				}
			}			
						
			if(me.plan == null || me.plan.size() == 1 ||
					(isWaypointReached(me) &&
							(state.enemyAgents.contains(getCurrentWaypointTarget(me)) ||
							state.isObstacle(me, getCurrentWaypointTarget(me)))       )){
				return new Wait();
			} else {
				Point nextPoint = me.plan.get(1);
				
				/*if(state.enemyAgents.contains(nextPoint)){
					replanNeeded = true;
					return new Wait();
				}*/

				MyDir dir = null;
				if(nextPoint.x>me.position.x){
					dir = MyDir.right;
				}
				if(nextPoint.x<me.position.x){
					dir = MyDir.left;
				}
				if(nextPoint.y>me.position.y){
					dir = MyDir.down;
				}
				if(nextPoint.y<me.position.y){
					dir = MyDir.up;
				}
				
				if(me.goingForFood){
					return new Move(dir);
				} else {
					if(me.direction != dir)
						return new Turn(dir);
					else
						return new Move(dir);
				}
				
				
			}
		}
	}
	
}
