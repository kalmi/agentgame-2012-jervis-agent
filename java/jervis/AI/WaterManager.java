package jervis.AI;

import java.awt.Point;
import java.awt.Rectangle;

import jervis.CommonTypes.Perception;

public class WaterManager {
	//Boolean[][] a = new Boolean[60][60];
	
	Rectangle water = null;
	
	Point waterTL = null;
	Point waterBR = null;
	
	public void report(Perception p){
		//a[p.mypos.x][p.mypos.y] = p.inwater;
		if(p.inwater){
			if(water == null){
				waterTL = new Point(p.mypos);
				waterBR = new Point(p.mypos);
			} else{
				if(p.mypos.y < waterTL.y) {
					waterTL = new Point(waterTL.x, p.mypos.y);
				} else if(p.mypos.y > waterBR.y) {
					waterBR = new Point(waterBR.x, p.mypos.y);
				}
				
				if(p.mypos.x < waterTL.x) {
					waterTL = new Point(p.mypos.x, waterTL.x);
				} else if(p.mypos.x > waterBR.x) {
					waterBR = new Point(p.mypos.x, waterBR.x);
				}
			}
			determineWater();
		}
	}

	private void determineWater() {
		water = new Rectangle(waterTL.x, waterTL.y, waterBR.x-waterTL.x, waterBR.y-waterTL.y);
		System.out.println(water);
	}
	
	public boolean isWater(Point x){
		if(water==null) return false;
		else return water.contains(x);
	}
}
