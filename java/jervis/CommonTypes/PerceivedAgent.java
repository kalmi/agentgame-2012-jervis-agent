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

	
	
	
    private boolean isFacing(int x, int y) {
        int A = this.x + this.y, B = this.y - this.x,
                C = x + y, D = y - x;

        if (dir == MyDir.up) {
            return C <= A && D <= B;
        } else if (dir == MyDir.right) {
            return C >= A && D <= B;
        } else if (dir == MyDir.down) {
            return C >= A && D >= B;
        } else {
            return C <= A && D >= B;
        }
	}
    
	public boolean isFacing(Point pos) {
		return isFacing(pos.x, pos.y);
    }

	public boolean canSee(int x, int y) {
        if (Math.abs(this.x - x) > 10 || Math.abs(this.y - y) > 10) {
            return false;
        }
        return isFacing(x,y); 
	}

	public boolean canSee(Point pos) {
    	return canSee(pos.x, pos.y);
    }
}
