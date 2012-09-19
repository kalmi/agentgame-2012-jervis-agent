+!eat_at_my_pos <- eat.
	





+!setNextWaypointIfOnWaypoint: current_waypoint(Fx,Fy) & mypos(Mx,My) & (Mx == Fx | Fx == any) & (My == Fy | Fy == any)
	<-	!setNextWaypoint.

+!setNextWaypointIfOnWaypoint
	<-	true.
	
+!setNextWaypoint <-
	!setNextWaypointId;
	!updateCurrentWaypoint.
	
+!setNextWaypointId: current_waypoint_id(W_id) <-
	-current_waypoint_id(_);
	+current_waypoint_id((W_id+1) mod 4).

+!updateCurrentWaypoint: current_waypoint_id(W_id) <-
	-current_waypoint(_,_);
	.nth((W_id),[[any,10],[49,any],[any,49],[10,any]],W);
	.nth(0,W,Nx);
	.nth(1,W,Ny);
	+current_waypoint(Nx,Ny).




+!ban_turn: time(T)
	<-	.abolish(turn_banned(_));
		+turn_banned(T).


//TODO: detect TURN loops and force move without turning
+!go(up,Result): mydir(D) & D\==0 & time(T) & turn_banned(Tb) & Tb+6<T & mypos(Mx, My) & My > 9+1
	<- !ban_turn; turn(0); Result = ok.

+!go(right,Result): mydir(D) & D\==1 & time(T) & turn_banned(Tb) & Tb+6<T & mypos(Mx, My) & Mx < 50-1
	<- !ban_turn; turn(1); Result = ok.

+!go(down,Result): mydir(D) & D\==2 & time(T) & turn_banned(Tb) & Tb+6<T & mypos(Mx, My) & My < 50-1
	<- !ban_turn; turn(2); Result = ok.

+!go(left,Result): mydir(D) & D\==3 & time(T) & turn_banned(Tb) & Tb+6<T & mypos(Mx, My) & Mx > 9+1
	<- !ban_turn; turn(3); Result = ok.


+!go(up,Result): mypos(Mx, My) & enemy(Mx,My-1) <-
	.println("Requested: up | CANNOT COMPLY | ENEMY ON FIELD");
	Result = error.

+!go(right,Result,Result): mypos(Mx, My) & enemy(Mx+1,My) <-
	.println("Requested: right | CANNOT COMPLY | ENEMY ON FIELD");
	Result = error.

+!go(down,Result): mypos(Mx, My) & enemy(Mx,My+1) <-
	.println("Requested: down | CANNOT COMPLY | ENEMY ON FIELD");
	Result = error.

+!go(left,Result): mypos(Mx, My) & enemy(Mx-1,My) <-
	.println("Requested: left | CANNOT COMPLY | ENEMY ON FIELD");
	Result = error.

+!go(up,Result)
	<- step(0); Result = ok.

+!go(right,Result)
	<- step(1); Result = ok.

+!go(down,Result)
	<- step(2); Result = ok.

+!go(left,Result)
	<- step(3); Result = ok.


+recommend(Dir, Strength): recommend_y(Sum) & (Dir == up | Dir == down)
	<-	.abolish(recommend_y(_));
		if(Dir == up){
			+recommend_y(Sum-Strength);
		}else{
			+recommend_y(Sum+Strength);
		}.

+recommend(Dir, Strength): recommend_x(Sum) & (Dir == right | Dir == left)
	<-	.abolish(recommend_x(_));
		if(Dir == right){
			+recommend_x(Sum+Strength);
		}else{
			+recommend_x(Sum-Strength);
		}.
		
-recommend(Dir, Strength): recommend_y(Sum) & (Dir == up | Dir == down)
	<-	.abolish(recommend_y(_));
		if(Dir == up){
			+recommend_y(Sum+Strength);
		}else{
			+recommend_y(Sum-Strength);
		}.

-recommend(Dir, Strength): recommend_x(Sum) & (Dir == right | Dir == left)
	<-	.abolish(recommend_x(_));
		if(Dir == right){
			+recommend_x(Sum-Strength);
		}else{
			+recommend_x(Sum+Strength);
		}.








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
		//!askRounder;
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


+!askWallDisliker
	<-	!askWallDisliker_x;
		!askWallDisliker_y.

+!askWallDisliker_x: mypos(Mx,My) & Mx <= 9
	<-	+recommend(right,(10-Mx)*100)[source(wallDisliker)].

+!askWallDisliker_x: mypos(Mx,My) & Mx >= 50
	<-	+recommend(left,(Mx-49)*100)[source(wallDisliker)].

+!askWallDisliker_y: mypos(Mx,My) & My <= 9
	<-	+recommend(down,(10-My)*100)[source(wallDisliker)].

+!askWallDisliker_y: mypos(Mx,My) & My >= 50
	<-	+recommend(up,(My-49)*100)[source(wallDisliker)].

+!askWallDisliker_x
	<- true.

+!askWallDisliker_y
	<- true.
	


/* +!askRounder: mypos(Mx,My) & Mx <= 29 & My <= 29 //bal felul
	<-	+recommend(left,1)[source(rounder)];
		+recommend(down,1)[source(rounder)].

+!askRounder: mypos(Mx,My) & Mx <= 29 & My > 29 //bal alul
	<-	+recommend(down,1)[source(rounder)];
		+recommend(right,1)[source(rounder)].


+!askRounder: mypos(Mx,My) & Mx > 29 & My > 29 //jobb alul
	<-	+recommend(right,1)[source(rounder)];
		+recommend(up,1)[source(rounder)].

+!askRounder: mypos(Mx,My) & Mx > 29 & My <= 29 //jobb felul
	<-	+recommend(left,1)[source(rounder)];
		+recommend(up,1)[source(rounder)]. */


+!askWaypointer
	<-	!askWaypointer_x;
		!askWaypointer_y.

+!askWaypointer_x: current_waypoint(Fx,Fy) & mypos(Mx,My) & Fx \== any & Mx < Fx
	<-	+recommend(right,Fx-Mx)[source(waypointer)].

+!askWaypointer_x: current_waypoint(Fx,Fy) & mypos(Mx,My) & Fx \== any & Mx > Fx
	<-	+recommend(left,Mx-Fx)[source(waypointer)].

+!askWaypointer_y: current_waypoint(Fx,Fy) & mypos(Mx,My) & Fy \== any  & My < Fy
	<-	+recommend(down,Fy-My)[source(waypointer)].

+!askWaypointer_y: current_waypoint(Fx,Fy) & mypos(Mx,My)  & Fy \== any  & My > Fy
	<-	+recommend(up,My-Fy)[source(waypointer)].

+!askWaypointer_x
	<-	true.

+!askWaypointer_y
	<-	true.


+!askFoodLiker: mypos(Mx,My) & known_food(_,_)
	<-	for( known_food(X,Y) ){
			?distance(X,Y,Mx,My,D);
			+known_food_d(D,X,Y);
		}
		
		.findall(known_food_d(A,B,C),known_food_d(A,B,C),LD);
		//.println(LD);
		.min(LD,known_food_d(BestD, BestX, BestY));

		if(Mx < BestX){
			+recommend(right,10000)[source(foodLiker)];
		}
		if(Mx > BestX){
			+recommend(left,10000)[source(foodLiker)];
		}
		if(My < BestY){
			+recommend(down,10000)[source(foodLiker)];
		}
		if(My > BestY){
			+recommend(up,10000)[source(foodLiker)];
		};
		.abolish(known_food_d(_,_,_)).

+!askFoodLiker
	<- true. //nofood



+!bossBoss: true <-
	.print("time unhandled");
	wait.