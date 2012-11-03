package jervis.JasonLayer;

import jason.asSemantics.Agent;
import jason.asSemantics.InternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

public class InternalActionHandler {
	public static String[] getNames(TransitionSystem ts, final Unifier un, Agent agent) throws Exception {
		Literal literal = ASSyntax.createVar();
		InternalAction ia = agent.getIA(".all_names");
		ia.execute(ts, un, new Term[]{literal});
		literal.apply(un);
		ListTerm list = (ListTerm) literal;
		
		int length = 0;
		for (@SuppressWarnings("unused") Term term : list) {
			length++;
		}
		String[] r = new String[length];
		int i = 0;
		for (Term term : list) {
			r[i] = term.toString();
			i++;
		}
		return r;
	}
	
	public static String getMyName(TransitionSystem ts, final Unifier un, Agent agent) throws Exception {
		Literal literal = ASSyntax.createVar();
		InternalAction ia = agent.getIA(".my_name");
		ia.execute(ts, un, new Term[]{literal});
		literal.apply(un);
		StringTerm my_name = (StringTerm) literal;
		return my_name.toString();
	}
}
