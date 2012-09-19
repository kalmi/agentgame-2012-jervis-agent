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
