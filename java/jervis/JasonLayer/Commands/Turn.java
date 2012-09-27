package jervis.JasonLayer.Commands;

import jervis.CommonTypes.MyDir;

public class Turn extends Command {

	public Turn(MyDir dir) {
		this.actionType = Action.turn;
		this.params = new int[]{dir.ordinal()};		
	}

}
