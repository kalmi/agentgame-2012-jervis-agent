//isMovingFutile
+?isMovingFutile(RESULT): time(T) & T>=15000-10 & mypos(Mx,My) & target(Tx,Ty)
	<-	?distance(Tx,Ty,Mx,My,D);		
		if(15000-T <= D){ //azert kisebbegyenlo, mert egyenlon csak elerhetem, de enni mar nem tudok belole
			RESULT = 1;
		}else{
			RESULT = 0;
		}.

+?isMovingFutile(RESULT): time(T) & T>=15000-10 & mypos(Mx,My)
	<-	RESULT = 1.

+?isMovingFutile(RESULT)
	<-	RESULT=0.