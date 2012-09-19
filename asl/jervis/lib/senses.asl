{include("isVisible.asl")}


//--------ENEMIES-----------------------------
+!senseEnemies: agent(Agents) & myteam(MyTeam)
	<-	.abolish(enemy(_,_));
		for (.member(Agent,Agents)) {
			.nth(2,Agent,Team);
			if(Team \== MyTeam){
				.nth(4,Agent,Ex);
				.nth(5,Agent,Ey);
				+enemy(Ex,Ey);
			}
		}.




//---------FOODS-----------------------------
//[distance_from_me, food_value, position_x, position_y]

	+!senseFoods: food(Foods)
		<-	for (.member(Food,Foods)) {
				!recordFood(Food);
			}.

	+!recordFood(Food): .nth(2,Food,Fx) & .nth(3,Food,Fy) & known_food(Fx,Fy)
		<-	true. //Already known Foods are handled elsewhere
		
	+!recordFood(Food): .nth(2,Food,Fx) & .nth(3,Food,Fy)
		<-	+known_food(Fx,Fy).	



	//Discard known foods that are IN SIGHT, but in fact are no longer there
		+!discardDisappearedKnownFoods
			<-	for (known_food(Fx,Fy)) {
					?isVisible(Fx,Fy,Visible);
					if(Visible==1){
						!notifyField(Fx,Fy);
					}
				}.

		+!notifyField(X,Y): known_food(X,Y) & food(Foods) & .member([_,_,X,Y],Foods)
			<- true. //Food still there. Not removing it from known_foods.
			
		+!notifyField(X,Y): known_food(X,Y)
			<-	-known_food(X,Y);
				-target(X,Y);
				.println("foodgone").




	//Give up on food on which an enemy is already sitting
		+!discardFoodThatIsBeingEatenByEnemy: agent(Agents) & .member([_,_,_,_,X,Y,_],Agents) & known_food(X,Y)
			<- 	.println("Enemy is eating my food. Abandoining.");
				-known_food(X,Y);
				-target(X,Y).	

		+!discardFoodThatIsBeingEatenByEnemy
			<- true.		