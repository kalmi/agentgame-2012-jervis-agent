package jervis.CommonTypes;

import java.awt.Point;

public enum MyDir {
	up,
	right,
	down,
	left;
	
	private static final MyDir[] values = MyDir.values();
	
	public MyDir cwNext(){
		return values[(ordinal()+1) % values().length];
	}
	
	public MyDir ccwNext(){
		return values[(ordinal()-1) % values().length];
	}
	
	public static MyDir fromInt(int i){
		return MyDir.values[i];
	}
	
	public static Point movePoint(Point point, MyDir dir){
		int dy = 0, dx = 0;
		switch (dir) {
		case up:
			dy -= 1;
			break;
			
		case right:
			dx += 1;
			break;
			
		case down:
			dy += 1;
			break;
			
		case left:
			dx -= 1;
			break;						
		}
		
		return new Point(point.x+dx, point.y+dy);
	}
}