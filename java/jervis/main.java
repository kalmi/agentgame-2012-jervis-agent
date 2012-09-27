package kalmi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.awt.Point;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import jason.asSemantics.*;
import jason.asSyntax.*;


public class main extends DefaultInternalAction{
	private static final long serialVersionUID = -6271003270174978200L;	
	static Controller controller = new Controller();
	
	public main() {
		
	}

	public Object execute(TransitionSystem ts, final Unifier un, final Term[] arg) throws Exception {
		ArrayList<Point> visibleFoods = new ArrayList<Point>();
		Point mypos = null;
		MyDir mydir = null;
		int internalId = -1; 
		
		jason.asSemantics.Agent agent = ts.getAg();
		Iterator<Literal> iter = agent.getBB().iterator();
		while (iter.hasNext()) {
			Literal item = iter.next();
			LiteralType type = LiteralType.valueOf(item.getFunctor());
			switch (type) {
			case food:
				ListTerm listTerm = (ListTerm) item.getTerm(0);
				for (Term term : listTerm.getAsList()) {
					ListTerm tmp = (ListTerm)term;
					String x = tmp.get(2).toString();
					String y = tmp.get(3).toString();
					Integer x_int = Integer.parseInt(x);
					Integer y_int = Integer.parseInt(y);
					Point food = new Point(x_int, y_int);
					visibleFoods.add(food);
				}
				break;
			case mypos:
				Integer x = Integer.parseInt(item.getTerm(0).toString());
				Integer y = Integer.parseInt(item.getTerm(1).toString());
				mypos = new Point(x,y);
				break;
			case mydir:
				mydir = MyDir.values()[Integer.parseInt(item.getTerm(0).toString())];
			case myname:
				String myname_str = item.getTerm(0).toString();
				if(myname_str == "jervis_"){
					internalId = 0;
				} else {
					internalId = myname_str.charAt(myname_str.length()-1) - '0';
				}
			default:
				break;
			}
		}

		
		Command command  = controller.process(internalId, mypos, mydir, visibleFoods);
		
		
		String commandName = command.actionType.toString();
		ActionExec action = new ActionExec(new LiteralImpl(commandName), new Intention());
		action.getActionTerm().addTerm(ASSyntax.createNumber(command.param1));
		action.getActionTerm().addTerm(ASSyntax.createNumber(command.param2));
		ts.getUserAgArch().act(action, null);
		
		return true;
	}
}