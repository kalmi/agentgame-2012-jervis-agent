package jervis;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jervis.AI.Config;
import jervis.AI.Controller;
import jervis.CommonTypes.Perception;
import jervis.JasonLayer.InternalActionHandler;
import jervis.JasonLayer.Commands.Command;
import jervis.JasonLayer.Commands.Wait;


public class main extends DefaultInternalAction{
	private static final long serialVersionUID = -6271003270174978200L;
	
	static Controller controller = null;
	
	public main() {
		
	}

	public Object execute(TransitionSystem ts, final Unifier un, final Term[] arg) throws Exception {
		return executeS(ts, un, arg);
	}
	
	public static synchronized Object executeS(TransitionSystem ts, final Unifier un, final Term[] arg) throws Exception {
		
		if(controller != null && controller.skipTick>0){
			controller.skipTick--;
			Command command = new Wait();
			ActionExec action = command.toAction();
			ts.getUserAgArch().act(action, null);
			return true;
		}
		
		jason.asSemantics.Agent agent = ts.getAg();		
		Perception perception = new Perception(agent.getBB());
		
		if(controller == null){
			String[] names = InternalActionHandler.getNames(ts, un, agent);
			Config.populate(names);
			main.controller = new Controller();
		}
		
		/*if(InternalActionHandler.getMyName(ts, un, agent).startsWith("jervis")){
			controller.process(perception, ts.getUserAgArch());
		} else {
			System.out.println("-Sir, enemy used mind control. It isn't very effective.");
			System.out.println(agent.getBB().toString());
		}*/
		
		controller.process(perception, ts.getUserAgArch());
		
		return true;
	}
}