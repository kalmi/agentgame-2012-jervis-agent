package jervis.CommonTypes;

import java.awt.Point;

@SuppressWarnings("serial")
public class PerceptedAgent extends Point {
	final int energy;
	final MyDir dir;
	final byte teamId;
	final byte id;
	
	public PerceptedAgent(PerceptedAgent other) {
		super(other);
		id = other.id;
		dir = other.dir;
		energy = other.energy;
		teamId = other.teamId;
		
		
	}

	public PerceptedAgent(byte id, int x, int y, MyDir dir, int energy, byte teamId) {
		super(x, y);
		this.dir = dir;
		this.energy = energy;
		this.teamId = teamId;
		this.id = id;
	}

}
