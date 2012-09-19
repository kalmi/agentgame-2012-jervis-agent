+?distance(X1,Y1,X2,Y2,D)
	<-	?abs(X1-X2,Xdiff);
		?abs(Y1-Y2,Ydiff);
		//if(Xdiff==0 | Ydiff==0){
		//	D = Xdiff + Ydiff;
		//}else{
		//	D = Xdiff + Ydiff +1; //include turning in distance
		//}.
		
		D = Xdiff + Ydiff.