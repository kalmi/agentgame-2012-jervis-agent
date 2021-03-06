package jervis.AI;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import jervis.AI.Debug.DebugToggle;
import jervis.AI.Debug.WaterDisplay;
import jervis.CommonTypes.Perception;

public class WaterManager {
	FieldState[][] a = new FieldState[60][60];	
	Waters waters = new Waters(); 
	
	WaterDisplay waterDisplay = DebugToggle.GUIENABLED? new WaterDisplay() : null;
	
	Point latestPoint = null;
	
	Point waterTL = null;
	Point waterBR = null;
		
	public boolean pretendThatThereIsNoWater = false;
	
	public double getWaterProbability(int x, int y) {
		if(pretendThatThereIsNoWater)
			return 0;
		return waters.contains(x, y)? 1 : 0;
	}

	int numOfVisitedWaterCells = 0;
	private List<Waters> previousCandidates;
	private Point lastFoundWater;
	
	public void report(Agent agent, Perception p){
		int x = p.mypos.x;
		int y = p.mypos.y;
		
		boolean changed = false;
		
		if(p.inwater){
			
			if(waterTL == null){
				waterTL = new Point(x, y);
				waterBR = new Point(x, y);
			}
			
			if(y < waterTL.y) {
				waterTL = new Point(waterTL.x, y);
			} else if(y > waterBR.y) {
				waterBR = new Point(waterBR.x, y);
			}
			
			if(x < waterTL.x) {
				waterTL = new Point(x, waterTL.y);
			} else if(x > waterBR.x) {
				waterBR = new Point(x, waterBR.y);
			}
		}
		
		if(p.inwater && a[x][y] != FieldState.water){
			latestPoint = new Point(x,y);
			lastFoundWater = latestPoint; 
			changed = !waters.contains(x, y);
			numOfVisitedWaterCells++;
			a[x][y] = FieldState.water;
		}
		
		if(!p.inwater && a[x][y] != FieldState.non_water){
			latestPoint = new Point(x,y);
			changed = waters.contains(x, y);
			a[x][y] = FieldState.non_water;
		}
		
		if(changed){
			agent.replanSceduled = true;
			long startTime = System.currentTimeMillis();
			determineWater();
			long estimatedTime = System.currentTimeMillis() - startTime;
			if(DebugToggle.ENABLED){
				System.out.print("D took: ");
				System.out.println(estimatedTime);
			}
		}
	}
		
	void determineWater(){
		if(waterTL == null) return;
		
		int initial_x = waterTL.x;
		int initial_y = waterTL.y;
		
		int end_x = waterBR.x;
		int end_y = waterBR.y;
		
		int range_limit = (end_x - initial_x) + (end_y - initial_y);
		if(DebugToggle.ENABLED){
			System.out.print("limit: ");
			System.out.println(range_limit);
		}
		
		List<Waters> candidates = new ArrayList<Waters>();
		
		Waters initial = new Waters();
		initial.rectangles[0] = new Rectangle(lastFoundWater.x, lastFoundWater.y, 1, 1);
		candidates.add(initial);
		
		int range = 0;
		while(range <= range_limit){
			for (int i = 0; i < range; i++) {
				int y = initial_y + range - i;
				int x = initial_x + i;
				
				if(x>=60) continue;
				if(y>=60) continue;
				if(x<0) continue;
				if(y<0) continue;
				
				if(a[x][y] == FieldState.water){
					ArrayList<Waters> newCandidates = new ArrayList<Waters>(); 
					for (Waters waters : candidates) {
						newCandidates.addAll(waters.splitWithAdd(x, y));
					}
					
					candidates.clear();
					for (Waters waters : newCandidates) {
						if(!candidates.contains(waters))
							candidates.add(waters);
					}
				}
			}
			range++;
		}
		
		final int expectedSize = 60*60*Config.waterCoveragePercent/100;
		double bestDiff = Double.MAX_VALUE;
		Waters bestWaters = null;
		
		if(DebugToggle.ENABLED){
			System.out.print("NumOfPW:");
			System.out.println(candidates.size());
		}
		
		for (Waters waters : candidates) {
			double diff = Math.abs(expectedSize - waters.getAreaSum());
			if(diff < bestDiff){
				bestWaters = waters;
				bestDiff = diff;
			}
		}

		if(DebugToggle.ENABLED){
			System.out.print("BestDiff: ");
		}
		if(bestWaters!=null){
			if(DebugToggle.ENABLED){
				System.out.println(bestDiff);
			}
			this.waters = bestWaters;
			this.previousCandidates = candidates;
			
			int preExpandArea = waters.getAreaSum();
			this.waters = this.waters.expand(2);
			int postExpandArea = waters.getAreaSum();
			if(DebugToggle.ENABLED){
				System.out.print("ExDiff: ");
				System.out.println(postExpandArea - preExpandArea);
			}
			if(waterDisplay!=null)
				waterDisplay.draw(this);
		}else{
			System.out.println("bug (keeping previous belief)");
			System.out.println(latestPoint);
			if(this.previousCandidates!=null){
				for (Waters ww : previousCandidates) {
					for (Rectangle rec : ww.rectangles) {
						System.out.println(rec);
					}
					System.out.println();
				}
			}
			
			System.out.println("Known waters:");
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[i].length; j++) {
					if(a[i][j]==FieldState.water){
						System.out.print(i);
						System.out.print(',');
						System.out.println(j);
					}
				}
			}
			System.out.println();
			System.out.println("Known lands:");
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[i].length; j++) {
					if(a[i][j]==FieldState.non_water){
						System.out.print(i);
						System.out.print(',');
						System.out.println(j);
					}
				}
			}
		}
	}
	
	enum FieldState {
		water,
		non_water
	}
	
	class Waters{
		public Rectangle[] rectangles = new Rectangle[Config.numOfWaters];
		public boolean contains(int x, int y){
			for (Rectangle r : rectangles) {
				if(r!=null && r.contains(x,y))
					return true;
			}
			return false;
		}
		public int getAreaSum(){
			if(rectangles.length!=2)
				throw new UnsupportedOperationException();
			int areaSum = 0;
			
			if(rectangles[0] != null)
				areaSum += rectangles[0].width*rectangles[0].height;
			
			if(rectangles[1] != null)
				areaSum += rectangles[1].width*rectangles[1].height;
			
			if(rectangles[0] != null && rectangles[1] != null){
				Rectangle intersection = rectangles[0].intersection(rectangles[1]);
				areaSum -= intersection.width*intersection.height;
			}
			
			return areaSum;
		}
		public List<Waters> splitWithAdd(int x, int y){
			List<Waters> result = new ArrayList<Waters>();
			outer:
			for (int i = 0; i < waters.rectangles.length; i++) {
				Waters w = new Waters();
				
				for (int j = 0; j < waters.rectangles.length; j++) {
					
					Rectangle newRectangle = null;
					if(rectangles[j]!=null)
						newRectangle = new Rectangle(rectangles[j]);
					
					if(i==j){
						if(newRectangle == null)
							newRectangle = new Rectangle(x,y,1,1);
						else
							newRectangle.add(new Rectangle(x,y,1,1));
					}
					
					w.rectangles[j] = newRectangle;
					
					if(newRectangle!=null){
						int x_max = newRectangle.x + newRectangle.width;
						int y_max = newRectangle.y + newRectangle.height;
						
						for (int _x = newRectangle.x; _x < x_max; _x++) {
							for (int _y = newRectangle.y; _y < y_max; _y++) {
								if(a[_x][_y]==FieldState.non_water)
									continue outer;
							}
						}
					}
					
				}

				for (Waters r : result) {
					if(r.equals(w)) continue outer;
				}
				result.add(w);
			}
			return result;
		} 
				
		public Waters expand(int maxAmountInEveryDirection) {
			Waters newWaters = new Waters();
			for (int i = 0; i < rectangles.length; i++) {
				if(rectangles[i]!=null)
					newWaters.rectangles[i] = new Rectangle(rectangles[i]);
			}
			
			for (int i = 0; i < maxAmountInEveryDirection; i++) {
				for (Rectangle rectangle : newWaters.rectangles) {				
					if(rectangle==null)
						continue;
					
					if(canWaterRectangleBeExpandedUpwards(rectangle)){
						rectangle.setLocation(rectangle.x, rectangle.y-1);
						rectangle.setSize(rectangle.width, rectangle.height+1);
					}
					
					if(canWaterRectangleBeExpandedDownwards(rectangle)){
						rectangle.setLocation(rectangle.x, rectangle.y);
						rectangle.setSize(rectangle.width, rectangle.height+1);
					}
					
					if(canWaterRectangleBeExpandedLeftwards(rectangle)){
						rectangle.setLocation(rectangle.x-1, rectangle.y);
						rectangle.setSize(rectangle.width+1, rectangle.height);
					}
					
					if(canWaterRectangleBeExpandedRightwards(rectangle)){
						rectangle.setLocation(rectangle.x, rectangle.y);
						rectangle.setSize(rectangle.width+1, rectangle.height);
					}
				}
			}
			return newWaters;
		}

		boolean canWaterRectangleBeExpandedUpwards(Rectangle r){
			if(r.y == 0) return false;
			
			for (int j = 0; j < r.width; j++) {
				if( a[r.x+j][r.y-1] == FieldState.non_water ){
					return false;
				}
			}
			return true;
		}
		
		boolean canWaterRectangleBeExpandedDownwards(Rectangle r){
			final int bottom = r.y + r.height - 1; 
			if(bottom == 59) return false;
			
			for (int j = 0; j < r.width; j++) {
				if( a[r.x+j][bottom+1] == FieldState.non_water ){
					return false;
				}
			}
			return true;
		}
		
		boolean canWaterRectangleBeExpandedLeftwards(Rectangle r){
			if(r.x == 0) return false;
			
			for (int j = 0; j < r.height; j++) {
				if( a[r.x-1][r.y+j] == FieldState.non_water ){
					return false;
				}
			}
			return true;
		}
		
		boolean canWaterRectangleBeExpandedRightwards(Rectangle r){
			final int rightside = r.x + r.width - 1; 
			if(rightside == 59) return false;
			
			for (int j = 0; j < r.height; j++) {
				if( a[rightside+1][r.y+j] == FieldState.non_water ){
					return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean equals(Object other){
		    if (other == null) return false;
		    if (other == this) return true;
		    if (!(other instanceof Waters))return false;
		    Waters otherInstance = (Waters)other;
		    if(otherInstance.rectangles.length != this.rectangles.length) return false;
		    for (int i = 0; i < rectangles.length; i++) {
		    	if(rectangles[i] == null && otherInstance.rectangles[i] == null)
		    		continue;
		    	
		    	if(rectangles[i] == null || otherInstance.rectangles[i] == null)
		    		return false;
		    	
		    	if(!rectangles[i].equals(otherInstance.rectangles[i]))
					return false;
			}
		    return true;
		}
	}
}
