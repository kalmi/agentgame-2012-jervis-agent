package jervis;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jervis.AI.Controller;
import jervis.CommonTypes.Perception;


public class main extends DefaultInternalAction{
	private static final long serialVersionUID = -6271003270174978200L;	
	static Controller controller = new Controller();
	
	public main() {
		
	}

	public Object execute(TransitionSystem ts, final Unifier un, final Term[] arg) throws Exception {
		return executeS(ts, un, arg);
	}
	
	public static synchronized Object executeS(TransitionSystem ts, final Unifier un, final Term[] arg) throws Exception {
		jason.asSemantics.Agent agent = ts.getAg();		
		Perception perception = new Perception(agent.getBB());
		
		controller.process(perception, ts.getUserAgArch());

		return true;
	}
}