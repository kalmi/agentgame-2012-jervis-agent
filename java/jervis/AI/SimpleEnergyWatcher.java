package jervis.AI;

import jervis.CommonTypes.Perception;

public class SimpleEnergyWatcher {
	static Integer otherTeamCollectiveEnergyLastRound = null;
	static Integer simpleId = null;
	static boolean simpleAteLastRound = false;
	
	static void run(Perception p, State state){		
		if(simpleId == null && p.time > 1){
			simpleId = determineSimpleId(state);
		}
					
		if(p.time > 1){
			
			if(p.internalId != (simpleId+1) % 5)
				return;
			
			int myCollectiveEnergy = Agent.getCollectiveEnergy(state.agents);
			int otherTeamCollectiveEnergy = ((int)((double)myCollectiveEnergy/p.ratio)) - myCollectiveEnergy;
			
			if(p.time > 2){
				boolean simpleAteThisRound = (otherTeamCollectiveEnergy>otherTeamCollectiveEnergyLastRound+4);
				if(simpleAteLastRound && !simpleAteThisRound){
					state.last4Consumption.insert(p.time);					
				}
				simpleAteLastRound = simpleAteThisRound;
			}
			
			otherTeamCollectiveEnergyLastRound = otherTeamCollectiveEnergy;
		}
	}

	private static int determineSimpleId(State state) {
		int id = 5;
		for (Agent agent : state.agents) {
			if(agent.order>agent.id){
				id = agent.order-1;
				break;
			}
		}
		return id;
	}
}
