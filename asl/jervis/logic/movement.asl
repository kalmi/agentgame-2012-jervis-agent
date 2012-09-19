turn_banned(0).

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