package jervis.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;

import jervis.AI.GraphTools.Planner;
import jervis.CommonTypes.Food;
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
			
			int time_used = 150 - p.myteamtimeleft;
			double time_per_round = ((double)time_used) / p.time;
			
			int rounds_left = 15000 - p.time;			
			int time_left = 150 - time_used;
						
			double estimated_required_time = time_per_round * rounds_left;
			
			if(time_left < 50 && estimated_required_time + safetyMargin > time_left && p.time > 1000){
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
		
		Agent agent = mapping.get(p.myname);
		
		if((playDeadTill > p.time && !agent.goingForFood)){
			Command command = new Wait();
			ActionExec action = command.toAction();
			agArch.act(action, null);
			return;
		}
		
		agent.update(p);				
		state.waterManager.report(agent, p);
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
				if(agent.plan!=null)
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
				state.obstacleTimes[destination.x][destination.y] = agent.getInternalTime();  
				state.foods.remove(destination);
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
			add(new Point(9,10));
			add(new Point(28,10));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(10,50));
			add(new Point(10,31));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(50,49));
			add(new Point(31,49));
		}});
		
		add(new ArrayList<Point>(){{
			add(new Point(49,9));
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
		Food targetFood = null;
		if(helper == me){
			target = agentInNeed.position;
		} else {
			
			target = null;
			
			if(state.foods.size() != 0){
				Food closestFood = null;
				int closestDistance = Integer.MAX_VALUE;
				for (Food food : state.foods){
					 
					if(food.unreachableAt != null && SimpleEnergyWatcher.simpleIsWaitingSince!=null && food.unreachableAt>=SimpleEnergyWatcher.simpleIsWaitingSince){
						continue;
					}
					
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
					targetFood = closestFood;
					goingForFood = true;
				}
			}
		}
		me.plan = null;
		if(target!=null){
			Planner planner = new Planner(me.position, target, state, me);
			me.plan = planner.plan();
		}
		if(targetFood!=null && me.plan == null){
			targetFood.unreachableAt = me.getInternalTime();
		}
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
	int pretendNotificationDone = Integer.MIN_VALUE;
	
	private Command determineAppropiateCommandFor(Agent me) {
		if(me.onFood != null){
			return new Eat(); 
		} else {
			int lastConsumedAt = state.last4Consumption.isEmpty()? Integer.MIN_VALUE : state.last4Consumption.getNewest();
			int lastNewSeen = state.last4NewSeen.isEmpty()? Integer.MIN_VALUE : state.last4NewSeen.getNewest();
			boolean doWeGetToEat = lastConsumedAt+50*4 > me.getInternalTime();
			state.waterManager.pretendThatThereIsNoWater =  !doWeGetToEat && lastConsumedAt != Integer.MIN_VALUE;
			
			/*if(me.time % 100 == 0 && me.order == 0){
				System.out.print(doWeGetToEat);
				System.out.print(' ');
				System.out.print(lastConsumedAt);
				System.out.print(' ');
				System.out.print(me.getInternalTime());
				System.out.print(' ');
				System.out.println(state.waterManager.pretendThatThereIsNoWater);
				
			}*/
			
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
			
			
			if(lastConsumedAt >= me.getInternalTime() - Config.numOfAll){
				replanNeeded  = true;
			}
			
			
			if(lastNewSeen == me.getInternalTime()){
				replanNeeded = true;
			}
			
			if(isWaypointReached(me)){
				replanNeeded  = true;
			}
			
			if(replanNeeded){
				replanEverything();
			}
			
			/*if(!me.goingForFood){
				if(lastConsumedAt >= me.getInternalTime() - 3*6){
					return new Turn(me.direction.cwNext());
				}
			}*/			
			
			if(state.waterManager.pretendThatThereIsNoWater && pretendNotificationDone + 100 < me.time){
				System.out.println("IAMAJESUS" + Integer.toString(me.time));
				pretendNotificationDone = me.time;
			}
			if(me.plan != null && me.plan.size() >=2){
				return getNextStepOfPlan(me);
			}else{
				
				if(me.plan != null)
					me.plan = null;
				
				int max = Integer.MIN_VALUE;
				Command best = new Wait();
				
				Point closestLand = null;
				
				if(me.inwater){
					int distanceCheck = 0;
					boolean bug = false;
					while(closestLand==null || bug){
						bug = true;
						distanceCheck++;
						for (MyDir dir : MyDir.values()) {
							int dy = 0, dx = 0;
							switch (dir) {
							case up:
								dy -= distanceCheck;
								break;
								
							case right:
								dx += distanceCheck;
								break;
								
							case down:
								dy += distanceCheck;
								break;
								
							case left:
								dx -= distanceCheck;
								break;						
							}
							int x = me.position.x + dx;
							int y = me.position.y + dy;
							if(x>=60 || y>=60 || x<0 || y<0)
								continue;
							
							bug = false;
							
							if(state.waterManager.getWaterProbability(x,y)==0 && !state.isObstacle(me,x,y)){
								closestLand = new Point(x,y);
								Planner planner = new Planner(me.position, closestLand, state, me);
								me.plan = planner.plan();
								return getNextStepOfPlan(me);
							}
						}
					}
					System.out.println("No land!?"); 
				}
				

				
				
				for (MyDir dir : MyDir.values()) {
					Move m = new Move(dir);
					Point dest = m.getDestination(me);
					
					if(dest.x>=60 || dest.y>=60 || dest.x<0 || dest.y<0)
						continue;
				
					
					if(state.isObstacle(me, dest))
						continue;

					int x = state.seennessManager.getAvgSeennessFor(dest.x, dest.y, me.direction, me.getInternalTime(), state);
					
					if(state.waterManager.getWaterProbability(dest.x,dest.y)==1)
						x*=0.1;
					
					if(x>max){
						max = x;
						best = m;
					}
				}
				
				if(state.waterManager.getWaterProbability(me.position.x,me.position.y)!=1){
				for (MyDir dir : MyDir.values()) {
					int x = state.seennessManager.getAvgSeennessFor(me.position.x, me.position.y, dir, me.getInternalTime(), state);
					if(x>max){
						max = x;
						best = new Turn(dir);
					}
				}}
				
				
				//System.out.println(best.toString());
				return best;
			}
		}
	}

	private Command getNextStepOfPlan(Agent me) {
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
