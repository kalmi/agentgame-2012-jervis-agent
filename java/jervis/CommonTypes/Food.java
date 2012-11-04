package jervis.CommonTypes;

import java.awt.Point;

@SuppressWarnings("serial")
public class Food extends Point {
	
	public int value = -1; 
	public Integer unreachableAt = null;
	
	public Food() {
		
	}

	public Food(Food x) {
		super(x);
		value = x.value;
	}

	public Food(int x, int y, int value) {
		super(x, y);
		this.value = value;  
	}

}
