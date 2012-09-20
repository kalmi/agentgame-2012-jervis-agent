+?distance(X1,Y1,X2,Y2,D)
	<-	?abs(X1-X2,Xdiff);
		?abs(Y1-Y2,Ydiff);
		//if(Xdiff==0 | Ydiff==0){
		//	D = Xdiff + Ydiff;
		//}else{
		//	D = Xdiff + Ydiff +1; //include turning in distance
		//}.
		
		D = Xdiff + Ydiff.

+?distanceAny(X1,Y1,X2,Y2,D)
	<-	
		if(X1 == any | X2 == any){
			?abs(0,Xdiff);
		} else {
			?abs(X1-X2,Xdiff);
		}
		
		if(Y1 == any | Y2 == any){
			?abs(0,Ydiff);
		} else {
			?abs(Y1-Y2,Ydiff);
		}

		//if(Xdiff==0 | Ydiff==0){
		//	D = Xdiff + Ydiff;
		//}else{
		//	D = Xdiff + Ydiff +1; //include turning in distance
		//}.
		
		D = Xdiff + Ydiff.		