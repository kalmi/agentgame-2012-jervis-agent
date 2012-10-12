package jervis.CommonTypes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jason.asSyntax.Literal;
import jason.bb.BeliefBase;
import jervis.JasonLayer.BeliefParser;
import jervis.JasonLayer.LiteralType;

public class Perception {

	public final String myname;
	public final int idFromName;
	public final Point mypos;
	public final MyDir mydir;
	public final List<Food> visibleFoods;
	public final List<PerceivedAgent> visibleAgents;
	public final double ratio;
	public final int myenergy;
	public final int myteam;
	public final int time;
	public final int jasonId;
	public final boolean inwater;
	 
	static boolean enemyUsedBroadcast = false;
	
	public Perception(BeliefBase bb) {
	
		String myname = null;
		Integer internalId = null;
		Point mypos = null;
		MyDir mydir = null;
		List<Food> visibleFoods = null;
		List<PerceivedAgent> visibleAgents = null;
		Double ratio = null;
		Integer myenergy = null;
		Integer myTeamId = null;
		Integer time = null;
		Integer jasonId = null;
		boolean inwater = false;
		
		
		Iterator<Literal> iter = bb.iterator();
		List<Literal> toBeRemoved = new ArrayList<Literal>();
		while (iter.hasNext()) {
			Literal item = iter.next();
			LiteralType type;
			try{
				type = LiteralType.valueOf(item.getFunctor());
			} catch (IllegalArgumentException e){
				enemyUsedBroadcast = true;
				toBeRemoved.add(item);
				//System.out.println("-Sir, I cannot recognize the following belief: " + item.getFunctor());
				continue;
			}

			
			switch (type) {
			
			case myid:
				jasonId = BeliefParser.parseJasonId(item);
				break;
				
			
			case myname:
				myname = BeliefParser.parseName(item);
				internalId = BeliefParser.parseNameAndGetId(item);
				break;
				
			case mypos:
				mypos = BeliefParser.parseMyPos(item);
				break;
				
			case mydir:
				mydir = BeliefParser.parseMyDir(item);
				break;

			case myenergy:
				myenergy = BeliefParser.parseMyEnergy(item);
				break;				
				
			case food:
				visibleFoods = BeliefParser.parseFoods(item);
				break;

			case agent:
				visibleAgents = BeliefParser.parseAgents(item);
				break;

			case myteam:
				myTeamId = BeliefParser.parseMyTeam(item);
				break;				
				
			case myteamratio:
				ratio = BeliefParser.parseMyTeamRatio(item);
				break;
				
			case time:
				time = BeliefParser.parseTime(item);
				break;				

			case inwater:
				inwater = true;
				break;
				
			default:
				break;
			}
		}
		
		for (Literal literal : toBeRemoved) {
			bb.remove(literal);
		}
		
		this.myname = myname;
		this.jasonId = jasonId;
		this.idFromName = internalId;
		this.mypos = mypos;
		this.mydir = mydir;
		this.visibleFoods = visibleFoods;
		this.visibleAgents = visibleAgents;
		this.ratio = ratio;
		this.myenergy = myenergy;
		this.myteam = myTeamId;
		this.time = time;
		this.inwater = inwater;
		

	}

}
