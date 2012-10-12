package jervis;

import java.util.Arrays;
import java.util.List;

public class Config {
	 public final int numOfJervis;
	 public final int numOfEnemy;
	 public final int numOfAll;
	 
	 public final int waterCostFactor = 10;
	 
	 
	 Config(String[] names){
		 List<String> l = Arrays.asList(names);
		 int numOfJervis = 0;
		 for (String name : l) {
			if(name.startsWith("jervis")){
				numOfJervis++;
			}
		 }
		 this.numOfJervis = numOfJervis;
		 this.numOfEnemy = l.size() - numOfJervis;
		 this.numOfAll = this.numOfJervis + this.numOfEnemy;  
	 }
}
