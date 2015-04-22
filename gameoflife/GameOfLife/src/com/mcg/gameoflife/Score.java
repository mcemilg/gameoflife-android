package com.mcg.gameoflife;

public class Score implements Comparable<Score>{
	private String userName;
	private int score;
	
	Score(String n, int s){
		userName = n;
		score = s;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public int getScore(){
		return score;
	}

	@Override
	public int compareTo(Score another) {
		//return 0 if equal
	    //1 if passed greater than this
	    //-1 if this greater than passed
	    return another.score > score? 1 : another.score < score? -1 : 0;
	}
	
	public String getScoreText()
	{
	    return userName+" - "+score;
	}
	

}
