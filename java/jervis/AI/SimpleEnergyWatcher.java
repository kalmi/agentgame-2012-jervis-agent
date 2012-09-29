package jervis.AI;

import jervis.CommonTypes.Perception;

public class SimpleEnergyWatcher {
	static Integer otherTeamCollectiveEnergyLastRound = null;
	static Integer simpleId = null;
	static boolean simpleAteLastRound = false;
	
	static boolean run(Perception p, State state){		
		if(simpleId == null && p.time > 1){
			simpleId = determineSimpleId(state);
		}
		
		if(p.time > 1){
			int otherTeamCollectiveEnergy = getSimpleEnergy(p, state);
			if(otherTeamCollectiveEnergy<100) return false;
			if(p.internalId == (simpleId+1) % 5){
				Agent agent = state.getAgent(p.internalId);
				
				if(p.time > 2){
					boolean simpleAteThisRound = (otherTeamCollectiveEnergy>otherTeamCollectiveEnergyLastRound+6);
					if(simpleAteLastRound && !simpleAteThisRound){
						state.last4Consumption.insert(agent.getInternalTime()-1);					
					}
					simpleAteLastRound = simpleAteThisRound;
					
					 
				}
				otherTeamCollectiveEnergyLastRound = otherTeamCollectiveEnergy;
			}
		}
		return true;
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
	
	public static boolean isSimpleAlive(Perception p, State state){
		return !(getSimpleEnergy(p, state) < 100);
	}
	
	private static int getSimpleEnergy(Perception p, State state){
		int myCollectiveEnergy = Agent.getCollectiveEnergy(state.agents);
		int otherTeamCollectiveEnergy = ((int)((double)myCollectiveEnergy/p.ratio)) - myCollectiveEnergy;
		return otherTeamCollectiveEnergy;
	}
}
