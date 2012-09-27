package kalmi.CommonTypes;

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
}