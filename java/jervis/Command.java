package kalmi;

public class Command {

    public enum Action {
        step, turn, wait, eat, attack, transfer
    };
    public Action actionType;
    public int param1, param2;

    public Command() {
        actionType = Action.wait;
        param1 = 0;
        param2 = 0;
    }

    public Command(Action actionType, int param1, int param2) {
        this.actionType = actionType;
        this.param1 = param1;
        this.param2 = param2; 
    }
    
    @Override
    public String toString(){
    	return actionType.name() + "(" + param1 + "," + param2 + ")";
    }
}
