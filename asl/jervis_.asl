{include("jervis/main.asl")}

first_round.

+time(_): first_round
	<-	-first_round;
		!init;
		!time.

+time(_)
	<-	!time.