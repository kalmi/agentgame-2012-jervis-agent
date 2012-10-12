package jervis;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jervis.AI.Controller;
import jervis.CommonTypes.Perception;
import jervis.JasonLayer.InternalActionHandler;


public class main extends DefaultInternalAction{
	private static final long serialVersionUID = -6271003270174978200L;
	
	static Controller controller = null;
	static Config config = null;
	
	public main() {
		
	}

	public Object execute(TransitionSystem ts, final Unifier un, final Term[] arg) throws Exception {
		return executeS(ts, un, arg);
	}
	
	public static synchronized Object executeS(TransitionSystem ts, final Unifier un, final Term[] arg) throws Exception {
		jason.asSemantics.Agent agent = ts.getAg();		
		Perception perception = new Perception(agent.getBB());
		
		if(config == null){
			String[] names = InternalActionHandler.getNames(ts, un, agent);
			main.config = new Config(names);
			main.controller = new Controller(config);
		}
		
		controller.process(config, perception, ts.getUserAgArch());
		
		
		return true;
	}
}