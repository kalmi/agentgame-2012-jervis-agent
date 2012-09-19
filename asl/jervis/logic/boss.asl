{include("utils/recommend_summer.asl")}
{include("engines/foodLiker.asl")}
{include("engines/wallDisliker.asl")}
{include("engines/wayPointer.asl")}
{include("movement.asl")}

+!eat_at_my_pos <- eat.

+!bossBoss: food(Food) & .min(Food,[0,V,X,Y]) <-
	-target(_,_);
	-known_food(X,Y);
	!eat_at_my_pos.

+!bossBoss
	<-	//debug.DebugAction;
		.abolish(recommend_x(_));
		.abolish(recommend_y(_));
		.abolish(recommend(_,_));
		+recommend_y(0);
		+recommend_x(0);

		!setNextWaypointIfOnWaypoint;

		!askWallDisliker;
		!askWaypointer;
		!askFoodLiker;
		
		//for ( recommend(Dir, Strength)) {
        //	.println(Dir, " - ", Strength);    // print all members of the list
     	//}
     	


		?processRecommendations(R1,R2,R3,R4);

		!go(R1,RESULT1);
		if(RESULT1==error){
			!go(R2,RESULT2);
			if(RESULT2==error){
				!go(R3,RESULT3);
				if(RESULT3==error){
					!go(R4,RESULT4);
					if(RESULT4==error){
						wait;
					}
				}
			}
		}.

		//.println("------------------").

		//.findall(recommend_sum(A,B),recommend_sum(A,B),LIST);
		//.println(LIST);
		//.sort(LIST,SORTED);
		//.reverse()
		//for ( .member(R,SORTED) ) {
        //	.print(R);    // print all members of the list
     	//}

		//TODO: try to comply


+?processRecommendations(R1,R2,R3,R4): recommend_x(XSum) & recommend_y(YSum)
	<-	?abs(XSum,XSumAbs);
		?abs(YSum,YSumAbs);
		//.println("X:", XSum);
		//.println("Y:", YSum);
		if(XSumAbs>YSumAbs){
			if(XSum>0){
				R1=right;
				R4=left;
			}else{
				R1=left;
				R4=right;
			}

			if(YSum>0){
				R2=down;
				R3=up;
			}else{
				R2=up;
				R3=down;
			}
		}else{
			if(YSum>0){
				R1=down;
				R4=up;
			}else{
				R1=up;
				R4=down;
			}

			if(XSum>0){
				R2=right;
				R3=left;
			}else{
				R2=left;
				R3=right;
			}
		}.		