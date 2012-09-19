{include("lib/abs.asl")}
{include("lib/distance.asl")}
{include("lib/senses.asl")}
{include("lib/futility.asl")}
{include("logic/logic.asl")}

+!init
	<- !waypointerInit.

+!time: food(Foods) & agent(Agents)
	<-	!senseEnemies;
		!senseFoods;
		!discardDisappearedKnownFoods;
		!discardFoodThatIsBeingEatenByEnemy;	
		
		?isMovingFutile(FUTILITY);
		if(FUTILITY==1){
			.println("The game is ending. No reachable food in sight. I guess i will rest.");
			wait;
		}else{
			!bossBoss;
		}.

+!time
	<-	.println("WTF... JASON BUG");
		wait.