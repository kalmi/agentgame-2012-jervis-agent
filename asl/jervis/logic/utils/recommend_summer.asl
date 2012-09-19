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