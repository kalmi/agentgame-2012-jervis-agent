package jervis.AI;

import jervis.AI.Debug.DebugToggle;
import jervis.JasonLayer.Commands.Command;

public class Stat {
	private static int simpleAte;
	private static int simpleAteFinished;
	private static int simpleSeen;
	
	private static int[] ate = new int[5];
	private static int[] ateFinished = new int[5];
	private static int[] moved = new int[5];
	private static int[] turned = new int[5];
	private static int[] waited = new int[5];

	public static void logCommand(Agent agent, Command command){
		if(DebugToggle.ENABLED){
			String commandName = command.getClass().getSimpleName();
			int id = agent.order;
			if(commandName.endsWith("Wait")){
				waited[id]++;
			} else if(commandName.endsWith("Turn")){
				turned[id]++;
			} else if(commandName.endsWith("Move")){
				moved[id]++;
			} else if(commandName.endsWith("Eat")){
				ate[id]++;
			} else {
				System.out.println("- Sir, something is of unknown class: "+ commandName);
			}
		}
	}
	
	public static void logEatFinshed(Agent agent){
		if(DebugToggle.ENABLED){
			ateFinished[agent.order]++;
		}
	}
	
	public static void logSimpleSeen(){
		if(DebugToggle.ENABLED){
			simpleSeen++;
		}
	}
	
	public static void logSimpleEat(){
		if(DebugToggle.ENABLED){
			simpleAte++;
		}
	}
	
	public static void logSimpleEatFinished(){
		if(DebugToggle.ENABLED){
			simpleAteFinished++;
		}
	}

	public static String getSummary(){
		String r = "";
		if(DebugToggle.ENABLED){
			r += "Stats:\n";
			
			r += "  JervisAll:\n";
			r += "    Eat: " + Integer.toString(sum(ate))+ "\n";
			r += "    EatFinished: " + Integer.toString(sum(ateFinished))+ "\n";
			r += "    15000/EatFinished: " + Double.toString((15000.0/sum(ateFinished))) + "\n";
			r += "    Move: " + Integer.toString(sum(moved))+ "\n";
			r += "    Turn: " + Integer.toString(sum(turned))+ "\n";
			r += "    Wait: " + Integer.toString(sum(waited))+ "\n\n";
			
			for (int i = 0; i < 5; i++) {
				r += "  Jervis" + Integer.toString(i) + ":\n";
				r += "    Eat: " + Integer.toString(ate[i])+ "\n";
				r += "    EatFinished: " + Integer.toString(ateFinished[i])+ "\n";
				r += "    15000/EatFinished: " + Double.toString((15000.0/ateFinished[i])) + "\n";
				r += "    Move: " + Integer.toString(moved[i])+ "\n";
				r += "    Turn: " + Integer.toString(turned[i])+ "\n";
				r += "    Wait: " + Integer.toString(waited[i])+ "\n\n";
			}
			
			r += "  Simple:\n";
			r += "    Eat: " + Integer.toString(simpleAte) + "\n";
			r += "    EatFinished: " + Integer.toString(simpleAteFinished) + "\n";
			r += "    15000/EatFinished: " + Double.toString((15000.0/simpleAteFinished)) + "\n";
			r += "    SimpleSeen: " + Integer.toString(simpleSeen) + "\n\n";
		}
		return r;
	}
	
	private static int sum(int[] a){
		int sum = 0;
		for (int i : a) {
			sum+=i;
		}
		return sum;
	}
}
