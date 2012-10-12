package jervis.AI;

import jervis.CommonTypes.Perception;


public class SimpleEnergyWatcher {
	static Integer otherTeamCollectiveEnergyLastRound = null;
	static boolean simpleAteLastRound = false;
	
	static boolean run(Perception p, State state){		
		
		if(p.time > 1){
			int otherTeamCollectiveEnergy = getSimpleEnergy(p, state);
			if(otherTeamCollectiveEnergy<100) return false;
			if(p.internalId == 0){
				Agent agent = state.getAgent(p.internalId);
				
				if(p.time > 2 && otherTeamCollectiveEnergyLastRound != null){
					boolean simpleAteThisRound = (otherTeamCollectiveEnergy>otherTeamCollectiveEnergyLastRound+6);
					
					if(simpleAteThisRound) Stat.logSimpleEat();
					
					if(simpleAteLastRound && !simpleAteThisRound){
						state.last4Consumption.insert(agent.getInternalTime()-1);
						Stat.logSimpleEatFinished();
					}
					simpleAteLastRound = simpleAteThisRound;
					
					 
				}
				otherTeamCollectiveEnergyLastRound = otherTeamCollectiveEnergy;
			}
		}
		return true;
	}

	
	private static int getSimpleEnergy(Perception p, State state){
		int myCollectiveEnergy = Agent.getCollectiveEnergy(state.agents);
		int otherTeamCollectiveEnergy = ((int)((double)myCollectiveEnergy/p.ratio)) - myCollectiveEnergy;
		return otherTeamCollectiveEnergy;
	}
}
