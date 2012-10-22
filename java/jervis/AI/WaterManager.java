package jervis.AI;

import java.awt.Point;
import java.awt.Rectangle;

import jervis.CommonTypes.Perception;

public class WaterManager {
	//Boolean[][] a = new Boolean[60][60];
	
	private Rectangle water = null;// new Rectangle(11, 6, 20, 100);
	
	Point waterTL = null;
	Point waterBR = null;
	
	public boolean pretendThatThereIsNoWater = false;
	
	public Rectangle getWater() {
		if(pretendThatThereIsNoWater)
			return null;
		else
			return water;
	}

	public void report(Agent agent, Perception p){
		//a[p.mypos.x][p.mypos.y] = p.inwater;
		if(p.inwater){
			if(water == null){
				waterTL = new Point(p.mypos);
				waterBR = new Point(p.mypos);
			} else{
				if(p.mypos.y < waterTL.y) {
					waterTL = new Point(waterTL.x, p.mypos.y);
					agent.replanSceduled = true;
				} else if(p.mypos.y > waterBR.y) {
					waterBR = new Point(waterBR.x, p.mypos.y);
					agent.replanSceduled = true;
				}
				
				if(p.mypos.x < waterTL.x) {
					waterTL = new Point(p.mypos.x, waterTL.y);
					agent.replanSceduled = true;
				} else if(p.mypos.x > waterBR.x) {
					waterBR = new Point(p.mypos.x, waterBR.y);
					agent.replanSceduled = true;
				}
			}
			determineWater();
		}
	}

	private void determineWater() {
		water = new Rectangle(waterTL.x, waterTL.y, waterBR.x-waterTL.x+1, waterBR.y-waterTL.y+1);
		//System.out.println(water);
	}
	
	public boolean isWater(Point x){
		if(water==null) return false;
		else return water.contains(x);
	}
}
