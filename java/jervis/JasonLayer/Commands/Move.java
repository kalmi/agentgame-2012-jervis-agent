package kalmi.JasonLayer.Commands;

import kalmi.CommonTypes.MyDir;

public class Move extends Command {
	public Move(MyDir dir){
		this.actionType = Action.step;
		this.params = new int[]{dir.ordinal()};		
	}
}
