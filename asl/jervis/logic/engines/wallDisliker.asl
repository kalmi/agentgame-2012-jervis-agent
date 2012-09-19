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
	