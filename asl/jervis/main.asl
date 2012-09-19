{include("lib/abs.asl")}
{include("lib/distance.asl")}
{include("lib/senses.asl")}
{include("lib/futility.asl")}
{include("logic.asl")}

turn_banned(0).

+!init: mypos(0,0)
	<-
	.println("Staring from TL.");
	+current_waypoint_id(0);
	+current_waypoint(10,10).

+!init: mypos(59,59)
	<-
	.println("Staring from BR.");
	+current_waypoint_id(2);
	+current_waypoint(49,49).

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



+!eat_at_my_pos <- eat.
	




