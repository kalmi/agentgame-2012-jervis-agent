package jervis.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;


import jervis.AI.State.Obstacle;
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
	
	@SuppressWarnings("unused")
	private Random random = new Random();

	
	public void process(Perception p, AgArch agArch) {
		
		if(p.myteamtimeleft<10 || state.omg__a_jervis_died_or_deadlocked){
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

		
		ArrayList<Obstacle> toRemove = new ArrayList<State.Obstacle>();
		for (Obstacle o : state.obstacles) {
			if(o.expires<=agent.time)
				toRemove.add(o);
		}		
		state.obstacles.removeAll(toRemove);
		
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
				Obstacle obstacle = state.new Obstacle(destination, agent.time+5);
				//System.out.println(state.obstacles);
				state.obstacles.add(obstacle);
				//System.out.println(state.obstacles);
				//System.out.println("---");
				
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
		Point target = getCurrentWaypointTarget(me);
		
		if(isWaypointReached(me)){
			waypointIdPerAgent[me.order] = (waypointIdPerAgent[me.order]+1) % waypointsPerAgent.get(me.order).size();
			target = getCurrentWaypointTarget(me);
		}
		
		boolean goingForFood = false;
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
		
		Planner planner = new Planner(me.position, target, state);
		me.plan = planner.plan();
		me.goingForFood = goingForFood;
	}
	
	private void replanEverything(){
		for (Agent me : state.agentsInOrder) {
			if(me==null) continue;
			
			replan(me);
		}
	}
	
	private Command determineAppropiateCommandFor(Agent me) {
		if(me.onFood != null){
			return new Eat(); 
		} else {

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
							state.obstacles.contains(getCurrentWaypointTarget(me)))       )){
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
