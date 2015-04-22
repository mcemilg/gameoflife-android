package com.mcg.gameoflife;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class GameActivity extends ActionBarActivity implements Runnable{

	private static final int BOX_SIZE = 20;	
	private static int WIDTH ;		
	private static int HEIGHT ;		
	
	private static ArrayList<Cell> livingCells = new ArrayList<Cell>(0);
    private ArrayList<Cell> tempCells = new ArrayList<Cell>(0);

	private Thread game;		//main game thread
	private boolean isPlaying = false;
	private boolean next = false;		//for next move
	private GameView gView;		//main game view
	
	private int score = 0;
	private int neighbours = 0; 
	private Cell tempCell = null;
	
	private SharedPreferences savePrefs;
	public static final String SAVE_PREFS = "HighScore";
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    gView = new GameView(this);
	    setContentView(gView);			//setting gameview on game activity
	    
	    savePrefs = getSharedPreferences(SAVE_PREFS, 0);
	    

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.game, menu);
	    
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_next){		//next move
			if(isPlaying){	
				game.interrupt();
				isPlaying = false;
				return true;
			}
			
			next = true;
			isPlaying = false;
			game = new Thread(this);
			game.start();
			game.interrupt();

			return true;
		}
		if (id == R.id.action_play){		//play
			if(isPlaying)
				return true;
			else{
				game = new Thread(this);
				game.start();
			}
			return true;
		}
		if (id == R.id.action_pause){		//pause
			if(isPlaying){
				game.interrupt();
				isPlaying = false;
				return true;
			}else
				return true;
			
		}

		if (id == R.id.action_reset){		//reset
			if(isPlaying){
				game.interrupt();
				isPlaying = false;
			}
			
			reset();
			updateBoard();	
			gView.postInvalidate();

			return true;
		}
		if (id == R.id.action_save){		//save
			if(isPlaying){
				game.interrupt();
				isPlaying = false;
			}
			String fileName = "_save";
			
			try {
				//filename username + _save
				FileOutputStream fos = openFileOutput(MainActivity.getUserName() + fileName, Context.MODE_PRIVATE);
				ObjectOutputStream os = new ObjectOutputStream(fos);
				os.writeObject(livingCells);
				os.close();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		Toast.makeText(getApplicationContext(),"Game Saved",Toast.LENGTH_SHORT).show();


			return true;
		}
		if (id == R.id.action_load){
			if(isPlaying){
				game.interrupt();
				isPlaying = false;
			}
			//waiting for interrupting
		    try {
		        Thread.sleep(200);		//slow dpwn
		    } catch (InterruptedException ex) { 
		    	
		    }
			String fileName = "_save";

			FileInputStream fis;
			try {
				fis = openFileInput(MainActivity.getUserName()+fileName);
				ObjectInputStream is = new ObjectInputStream(fis);
				reset();
				livingCells = (ArrayList<Cell>) is.readObject();
				
				is.close();
				fis.close();
			} catch (FileNotFoundException e) {		
	    		Toast.makeText(getApplicationContext(),"Saved game not found",Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

	
			updateBoard();	
			gView.postInvalidate();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean con = false;  
		
		if(isPlaying){
			game.interrupt();
			isPlaying = false;
			con = true;
			//waiting for interrupting
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(livingCells.contains(new Cell((int)(event.getX() / BOX_SIZE), (int)(event.getY() / BOX_SIZE) - 11)))		// -11 for actionbar size
			livingCells.remove(new Cell((int)(event.getX() / BOX_SIZE), (int)(event.getY() / BOX_SIZE) - 11));
		else
			livingCells.add(new Cell((int)(event.getX() / BOX_SIZE), (int)(event.getY() / BOX_SIZE) - 11));
		
		updateBoard();	
		gView.postInvalidate();


		return super.onTouchEvent(event);
	}

	@Override
	public void onDestroy() {
		String fileName = "highscore";
		FileOutputStream outputStream;

		if(isPlaying){
			game.interrupt();
			isPlaying = false;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(MainActivity.getUserName() + score);
		setHighScore();
		reset();
		super.onDestroy();
	}
	
	private void setHighScore(){
		if(score > 0){			
			
			SharedPreferences.Editor scoreEdit = savePrefs.edit();
			String scores = savePrefs.getString("highScores", "");
			
			if(scores.length() > 0){//we have scores
			    
				List<Score> scoreList = new ArrayList<Score>();
				String[] pastScores = scores.split("\\|");
				
				for(String sc : pastScores){
				
					String[] parts = sc.split(" - ");
				    scoreList.add(new Score(parts[0], Integer.parseInt(parts[1])));
				
				}		
				
				Score newScore = new Score(MainActivity.getUserName(), score);
				scoreList.add(newScore);
				Collections.sort(scoreList);
				
				StringBuilder scoreBuild = new StringBuilder("");
				
				for(int s=0; s<scoreList.size(); s++){
				    if(s>=10) break;//only want ten
				    if(s>0) scoreBuild.append("|");//pipe separate the score strings
				    scoreBuild.append(scoreList.get(s).getScoreText());
				}
				
				//write to prefs
				scoreEdit.putString("highScores", scoreBuild.toString());
				scoreEdit.commit();
				
			}
			else{//we dont have scores
				
				//write to prefs
				scoreEdit.putString("highScores", ""+MainActivity.getUserName()+" - "+score);
				scoreEdit.commit();
			}
			
		}
	}

	public static ArrayList<Cell> getLivingCells() {
	    return livingCells;
	}

	public static int getWidth(){
		return WIDTH;
	}

	public static int getHeight(){
		return HEIGHT;
	}

	public void reset() {
	    livingCells.clear();
	}

	private int countLiveNeighbours(int i ,int j){
		int c = 0;		
		
		if (livingCells.contains(new Cell(i-1,j-1))) 
	    	c++; 
		if (livingCells.contains(new Cell(i-1,j))) 
	    	c++; 
		if (livingCells.contains(new Cell(i-1,j+1))) 
	    	c++; 
		if (livingCells.contains(new Cell(i,j-1))) 
	    	c++; 
		if (livingCells.contains(new Cell(i,j+1))) 
	    	c++; 
		if (livingCells.contains(new Cell(i+1,j-1))) 
	    	c++; 
		if (livingCells.contains(new Cell(i+1,j))) 
	    	c++; 
		if (livingCells.contains(new Cell(i+1,j+1))) 
	    	c++; 
	    
		return c;
		
	}

	@Override
	public void run() {
		isPlaying = true;

		while(isPlaying){
		    for (int i= 0; i < HEIGHT + 30  ; i++) {			
		        for (int j= 0; j < WIDTH +30; j++) {
		        	
		            neighbours = countLiveNeighbours( i, j);
	            	tempCell = new Cell(i,j);                    	
	
		            if (livingCells.contains(tempCell)) {//live cell
	
		            	if ((neighbours == 2) || (neighbours == 3)) {
		                    tempCells.add(tempCell);
		                } 
		            } else { //dead cell
		                
		                if (neighbours == 3) {
		                    tempCells.add(tempCell);
		                }
		            }
		        }
		    }
		    
		    reset();
		    livingCells.addAll(tempCells);
			updateBoard();
		    gView.postInvalidate();
		    tempCells.clear();
		    
		    //update score
		    if(score <= livingCells.size())
		    	score = livingCells.size();

		    //check next flag
		    if(next){
		    	next = false;
				isPlaying = false;
		    	break;
		    }
		    


		    try {
		        Thread.sleep(200);		//slow dpwn
		    } catch (InterruptedException ex) { 
		    	
		    }

			
	   }
		
	}
	
	private void updateBoard(){
		int tempX =0 , tempY = 0;
		
		for(int i = 0; i < livingCells.size() ; ++i){
			
			if(livingCells.get(i).getX() >= tempX )
				tempX = livingCells.get(i).getX();
			
			if(livingCells.get(i).getY() >= tempY)
				tempY = livingCells.get(i).getY();
		}
		WIDTH = tempX  ;
		HEIGHT = tempY ;

	}

}