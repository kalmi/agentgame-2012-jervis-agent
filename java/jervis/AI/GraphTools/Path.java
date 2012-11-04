package jervis.AI.GraphTools;


import java.awt.Point;
import java.util.LinkedList;



@SuppressWarnings("serial")
public class Path extends LinkedList<Point>{
	/*
	@Override
	public String toString(){
		if(this.size() == 0) return "";
			
		StringBuffer r = new StringBuffer();
		for (Point vertex : this) {
			r.append(vertex.getId());
			r.append('\n');
		}
		r.deleteCharAt(r.lastIndexOf("\n"));
		return r.toString();		
	}
	*/
}
