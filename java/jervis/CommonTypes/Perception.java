package jervis.CommonTypes;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import jason.asSyntax.Literal;
import jason.bb.BeliefBase;
import jervis.JasonLayer.BeliefParser;
import jervis.JasonLayer.LiteralType;

public class Perception {

	public final int internalId;
	public final Point mypos;
	public final MyDir mydir;
	public final List<Food> visibleFoods;
	public final List<PerceptedAgent> visibleAgents;
	public final double ratio;
	 
	
	public Perception(BeliefBase bb) {
	
		Integer internalId = null;
		Point mypos = null;
		MyDir mydir = null;
		List<Food> visibleFoods = null;
		List<PerceptedAgent> visibleAgents = null;
		Double ratio = null;
		
		Iterator<Literal> iter = bb.iterator();
		while (iter.hasNext()) {
			Literal item = iter.next();
			LiteralType type = LiteralType.valueOf(item.getFunctor());
			
			switch (type) {
			
			case myname:
				internalId = BeliefParser.parseNameAndGetId(item);
				break;
				
			case mypos:
				mypos = BeliefParser.parseMyPos(item);
				break;
				
			case mydir:
				mydir = BeliefParser.parseMyDir(item);
				break;
				
			case food:
				visibleFoods = BeliefParser.parseFoods(item);
				break;

			case agent:
				visibleAgents = BeliefParser.parseAgents(item);
				break;

			case myteamratio:
				ratio = BeliefParser.parseMyTeamRatio(item);
				
			default:
				break;
			}
		}
		
		this.internalId = internalId;
		this.mypos = mypos;
		this.mydir = mydir;
		this.visibleFoods = visibleFoods;
		this.visibleAgents = visibleAgents;
		this.ratio = ratio;
		

	}

}
