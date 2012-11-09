package jervis.AI;

import jervis.CommonTypes.Food;
import jervis.CommonTypes.Perception;


public class SimpleEnergyWatcher {
	static Integer otherTeamCollectiveEnergyLastRound = null;
	public static Integer simpleIsWaitingSince = null;
	private static int simpleIsWaitingCounter = 0;
	
	static boolean run(Perception p, State state){		
		
		if(p.time > 1){
			int otherTeamCollectiveEnergy = getOtherTeamEnergy(p, state);
			if(otherTeamCollectiveEnergy<(Config.numOfEnemy*100)) return false;
			
			Agent agent = state.getAgent(p.idFromName);
			if(otherTeamCollectiveEnergyLastRound != null){
				boolean simpleAteThisRound = (otherTeamCollectiveEnergy>otherTeamCollectiveEnergyLastRound+6);
				boolean simpleIsWaitingSinceLastAgent = !simpleAteThisRound && otherTeamCollectiveEnergy>otherTeamCollectiveEnergyLastRound-4;
				
				if(simpleIsWaitingSinceLastAgent){
					simpleIsWaitingCounter++;
				} else {
					simpleIsWaitingCounter = 0;
				}
				
				if(simpleIsWaitingCounter>Config.numOfAll){
					if(simpleIsWaitingSince == null){
						simpleIsWaitingSince = agent.getInternalTime();
						//System.out.println("B");
					}
				} else if(simpleIsWaitingSince != null){
					simpleIsWaitingSince = null;
					for (Food food : state.foods) {
						food.unreachableAt = null;
					}
					//System.out.println("E");
				}
				
				if(simpleAteThisRound) {
					Stat.logSimpleEat();
					agent.doIComeAfterEnemy = true; 
				}
				
				if(agent.otherTeamWasSeenEatingByMeLastTime && !simpleAteThisRound){
					state.last4Consumption.insert(agent.getInternalTime()-1);
					Stat.logSimpleEatFinished();
				}
				agent.otherTeamWasSeenEatingByMeLastTime = simpleAteThisRound;
				
				 
			}
			otherTeamCollectiveEnergyLastRound = otherTeamCollectiveEnergy;
		
		}
		return true;
	}
	
	private static int getOtherTeamEnergy(Perception p, State state){
		int myCollectiveEnergy = Agent.getCollectiveEnergy(state.agentsInOrder);
		int otherTeamCollectiveEnergy = ((int)((double)myCollectiveEnergy/p.ratio)) - myCollectiveEnergy;
		return otherTeamCollectiveEnergy;
	}
}
