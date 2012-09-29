package jervis.JasonLayer.Commands;

import java.awt.Point;
import java.util.Arrays;

import jason.asSemantics.ActionExec;
import jason.asSemantics.Intention;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.LiteralImpl;
import jervis.AI.Agent;
import jervis.AI.State;

public abstract class Command {

    public enum Action {
        step, turn, wait, eat, attack, transfer
    };
    
    public Action actionType;
    public int[] params = new int[0];
    
    @Override
    public String toString(){
    	return actionType.name() + Arrays.toString(params);
    }
    
    public ActionExec toAction(){
		ActionExec action = new ActionExec(new LiteralImpl(actionType.name()), new Intention());
		for (int x : params) {
			action.getActionTerm().addTerm(ASSyntax.createNumber(x));
		}
		return action;
    }

	public abstract void pretend(Agent agent, State state);
	
	public Point getDestination(Agent agent){
		return agent.position;
	}
}
