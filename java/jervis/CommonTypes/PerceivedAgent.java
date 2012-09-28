package jervis.CommonTypes;

import java.awt.Point;

@SuppressWarnings("serial")
public class PerceivedAgent extends Point {
	public final int energy;
	public final MyDir dir;
	public final byte teamId;
	public final byte id;
	
	public PerceivedAgent(PerceivedAgent other) {
		super(other);
		id = other.id;
		dir = other.dir;
		energy = other.energy;
		teamId = other.teamId;
		
		
	}

	public PerceivedAgent(byte id, int x, int y, MyDir dir, int energy, byte teamId) {
		super(x, y);
		this.dir = dir;
		this.energy = energy;
		this.teamId = teamId;
		this.id = id;
	}

}
