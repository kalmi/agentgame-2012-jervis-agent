package jervis.AI;

import java.util.Arrays;
import java.util.List;

public class Config {
	 public static int numOfJervis;
	 public static int numOfEnemy;
	 public static int numOfAll;
	 
	 public static final int waterCostFactor = 10;
	 
	 
	 public static void populate(String[] names){
		 List<String> l = Arrays.asList(names);
		 numOfJervis = 0;
		 for (String name : l) {
			if(name.startsWith("jervis")){
				numOfJervis++;
			}
		 }
		 numOfEnemy = l.size() - numOfJervis;
		 numOfAll = numOfJervis + numOfEnemy;  
	 }
}
