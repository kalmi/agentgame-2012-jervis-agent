+!waypointerInit: mypos(0,0)
	<-
	.println("Staring from TL.");
	+current_waypoint_id(0);
	+current_waypoint(10,10).

+!waypointerInit: mypos(59,59)
	<-
	.println("Staring from BR.");
	+current_waypoint_id(2);
	+current_waypoint(49,49).



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