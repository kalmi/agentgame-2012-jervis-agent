package jervis.AI;

import java.awt.Point;
import java.util.ArrayList;

import jervis.CommonTypes.MyDir;

public class SeennessManager {

	final int[][] lastSeen= new int[60][60]; //contains internalTime
	
	public void report(Agent me){
		int internalTime = me.getInternalTime();
		for (Point p : getAllVisibleFields(me.position.x,me.position.y,me.direction)) {
			lastSeen[p.x][p.y] = internalTime;
		}
	}
	
	
	public int getAvgSeennessFor(int x, int y, MyDir dir, int currentInternalTime, State state){
		int i = 0;
		int sum = 0;
		int possible_number_of_foods = 0;
		for (Point p : getAllVisibleFields(x,y,dir)) {
			i++;
			sum += currentInternalTime - lastSeen[p.x][p.y];
			
			int possible_number_of_foods_here = 0;
			for (Integer consumption : state.last4Consumption.buf) {
				if(consumption==null)
					consumption = 0;
								
				if(lastSeen[p.x][p.y]<consumption)
					possible_number_of_foods_here++;
				
			}
			possible_number_of_foods += possible_number_of_foods_here;
		}		
		return possible_number_of_foods*1000 + sum;
	}
	
	
	private ArrayList<Point> getAllVisibleFields(int x, int y, MyDir dir){
		@SuppressWarnings("serial")
		ArrayList<Point> result = new ArrayList<Point>(){
			@Override
			public boolean add(Point p){
				if(p.x>=60 || p.y>=60 || p.x<0 || p.y<0)
					return false;
				else
					return super.add(p);
			}
		};
		result.add(new Point(x, y));
		switch (dir) {
		case down:
			for (int i = 1; i < 11; i++) {
				for (int j = 1; j < i; j++) {
					result.add(new Point(x+j, y+i));
					result.add(new Point(x-j, y+i));
				}
			}
			break;
			
		case up:
			for (int i = 1; i < 11; i++) {
				for (int j = 1; j < i; j++) {
					result.add(new Point(x+j, y-i));
					result.add(new Point(x-j, y-i));
				}
			}
			break;
			
		case left:	
			for (int i = 1; i < 11; i++) {
				for (int j = 1; j < i; j++) {
					result.add(new Point(x-i, y+j));
					result.add(new Point(x-i, y-j));
				}
			}
			break;
			
		case right:
			for (int i = 1; i < 11; i++) {
				for (int j = 1; j < i; j++) {
					result.add(new Point(x+i, y+j));
					result.add(new Point(x+i, y-j));
				}
			}
			break;

		}
		return result;
	}
}
