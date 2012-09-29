package jervis.AI.RecommendationEngines; 

import jervis.CommonTypes.MyDir;



public class Recommendation implements Comparable<Recommendation>{
	
	public enum RecommendationType{
		moveOrTurn, turn, move
	}
	
	private int strength;
	public final RecommendationType recommendationType;
	public final MyDir dir;
	
	public int getStrength() {
		return strength;
	}

	public Recommendation(int strength, RecommendationType recommendationType, MyDir dir) {
		this.strength = strength;
		this.recommendationType = recommendationType;
		this.dir = dir;
	}
	
	public String toString(){
		return Integer.toString(strength) + ", " + recommendationType.name() + "," + dir.name(); 
	}

	public int compareTo(Recommendation other) {
		return this.strength - other.strength;
	}
	
	public void add(Recommendation r){
		//if(this.dir!=r.dir || this.recommendationType != r.recommendationType) throw new UnsupportedOperationException();
		this.strength += r.strength;
	}

	public void add(Recommendation r, double d) {
		//if(this.dir!=r.dir || this.recommendationType != r.recommendationType) throw new UnsupportedOperationException();
		this.strength += r.strength*d;
	}
}
