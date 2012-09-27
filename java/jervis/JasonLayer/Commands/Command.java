package kalmi.JasonLayer.Commands;

import java.util.Arrays;

import jason.asSemantics.ActionExec;
import jason.asSemantics.Intention;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.LiteralImpl;

public abstract class Command {

    protected enum Action {
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
}
