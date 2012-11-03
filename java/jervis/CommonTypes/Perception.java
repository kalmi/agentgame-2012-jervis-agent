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
	public final int myteamtimeleft;
	public final boolean highlighted;
	 
	static boolean broadcastWarningDone = false;
	static boolean sneakAttackWarningDone = false; 
	
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
		Integer myteamtimeleft = null;
		Integer jasonId = null;		
		boolean inwater = false;
		boolean highlighted = false;
		
		
		Iterator<Literal> iter = bb.iterator();
		List<Literal> toBeRemoved = new ArrayList<Literal>();
		
		
		while (iter.hasNext()) {
			Literal item = iter.next();
			LiteralType type;
			try{
				type = LiteralType.valueOf(item.getFunctor());
			} catch (IllegalArgumentException e){
				toBeRemoved.add(item);
				if(broadcastWarningDone == false){
					System.out.println("-Sir, the enemy is not playing fair. He used broadcast. Functor: " + item.getFunctor());
					broadcastWarningDone = true;
				}
				continue;
			}
			
			if(!(item.getSources().size()==1 && item.getSources().get(0).toString() == "percept")){
				toBeRemoved.add(item);
				
				if(sneakAttackWarningDone == false){
					System.out.println("-Sir, enemy used mind control. Functor: " + item.getFunctor());
					sneakAttackWarningDone = true;
				}
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
				
			case myteamtimeleft:
				myteamtimeleft = BeliefParser.parseTime(item);
				break;

			case inwater:
				inwater = true;
				break;
				
			case highlighted:
				highlighted = true;
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
		this.myteamtimeleft = myteamtimeleft;
		this.inwater = inwater;
		this.highlighted = highlighted;
		

	}

}
