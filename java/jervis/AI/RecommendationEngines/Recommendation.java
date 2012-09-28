package jervis.AI.RecommendationEngines; 

import jervis.CommonTypes.MyDir;



public class Recommendation {
	
	public enum RecommendationType{
		moveOrTurn, turn
	}
	
	public final int strength;
	public final RecommendationType recommendationType;
	public final MyDir dir;
	public Recommendation(int strength, RecommendationType recommendationType, MyDir dir) {
		this.strength = strength;
		this.recommendationType = recommendationType;
		this.dir = dir;
	}
	
	public String toString(){
		return Integer.toString(strength) + ", " + recommendationType.name() + "," + dir.name(); 
	}
}
