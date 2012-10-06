package jervis.JasonLayer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import jervis.CommonTypes.Food;
import jervis.CommonTypes.MyDir;
import jervis.CommonTypes.PerceivedAgent;

public class BeliefParser {
	static Map<String, Integer> mapping = new HashMap<String, Integer>();
	static Integer largestId = -1;
	
	public static String parseName(Literal item){
		return item.getTerm(0).toString();
	}
	
	public static int parseNameAndGetId(Literal item){
		String myname_str = item.getTerm(0).toString();
		if(mapping.containsKey(myname_str)){
			return mapping.get(myname_str);
		}else{
			largestId++;
			mapping.put(myname_str, largestId);
			return largestId;
		}
	}	
	
	public static Point parseMyPos(Literal item) {
		int x = Integer.parseInt(item.getTerm(0).toString());
		int y = Integer.parseInt(item.getTerm(1).toString());
		return new Point(x,y);
	}
	
	public static MyDir parseMyDir(Literal item) {
		return MyDir.fromInt(Integer.parseInt(item.getTerm(0).toString()));
	}
	
	public static List<Food> parseFoods(Literal literal){
		ArrayList<Food> foods = new ArrayList<Food>();
		ListTerm listTerm = (ListTerm) literal.getTerm(0);
		for (Term term : listTerm.getAsList()) {
			ListTerm tmp = (ListTerm)term;
			
			String val_str = tmp.get(1).toString();
			Integer val = Integer.parseInt(val_str);
			
			String x_str = tmp.get(2).toString();
			Integer x = Integer.parseInt(x_str);
			
			String y_str = tmp.get(3).toString();
			Integer y = Integer.parseInt(y_str);
			
			Food food = new Food(x, y, val);
			
			foods.add(food);
		}
		return foods;
	}

	//        0 1  2 3 4 5 6
	//agent([[D,Id,T,E,X,Y,O],...])
	public static List<PerceivedAgent> parseAgents(Literal literal){
		List<PerceivedAgent> agents = new ArrayList<PerceivedAgent>();
		ListTerm listTerm = (ListTerm) literal.getTerm(0);
		for (Term term : listTerm.getAsList()) {
			ListTerm tmp = (ListTerm)term;
			
			String id_str = tmp.get(1).toString();
			byte id = Byte.parseByte(id_str);
			
			String team_str = tmp.get(2).toString();
			byte teamId = Byte.parseByte(team_str);
			
			String energy_str = tmp.get(3).toString();
			int energy = Integer.parseInt(energy_str);
			
			String x_str = tmp.get(4).toString();
			int x = Integer.parseInt(x_str);
			
			String y_str = tmp.get(5).toString();
			int y = Integer.parseInt(y_str);
			
			String dir_str = tmp.get(6).toString();
			MyDir dir = MyDir.fromInt(Integer.parseInt(dir_str));;
			
			PerceivedAgent agent = new PerceivedAgent(id, x, y, dir, energy, teamId);
			agents.add(agent);
		}
		return agents;
	}
	
	
	public static double parseMyTeamRatio(Literal item) {
		return Double.parseDouble(item.getTerm(0).toString()); 
	}

	public static int parseMyEnergy(Literal item) {
		return Integer.parseInt(item.getTerm(0).toString()); 
		
	}

	public static int parseTime(Literal item) {
		return Integer.parseInt(item.getTerm(0).toString()); 
	}

	public static int parseMyTeam(Literal item) {
		return Integer.parseInt(item.getTerm(0).toString()); 
	}

	public static int parseJasonId(Literal item) {
		return Integer.parseInt(item.getTerm(0).toString());
	}

	


}
