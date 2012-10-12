package jervis.AI;

import jervis.CommonTypes.Perception;


public class SimpleEnergyWatcher {
	static Integer otherTeamCollectiveEnergyLastRound = null;
	
	static boolean run(Perception p, State state){		
		
		if(p.time > 1){
			int otherTeamCollectiveEnergy = getOtherTeamEnergy(p, state);
			if(otherTeamCollectiveEnergy<(state.config.numOfEnemy*100)) return false;
			
			Agent agent = state.getAgent(p.idFromName);
			if(otherTeamCollectiveEnergyLastRound != null){
				boolean simpleAteThisRound = (otherTeamCollectiveEnergy>otherTeamCollectiveEnergyLastRound+6);
				
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
