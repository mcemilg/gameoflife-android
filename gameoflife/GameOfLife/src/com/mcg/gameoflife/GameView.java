package com.mcg.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;


public class GameView extends View {
    private static final int BOX_SIZE = 20;			    
        
    GameView(Context c){
    	super(c);
    }

    //draw method
    @Override
    protected void onDraw(Canvas canvas) {	
        Paint cell = new Paint();
    	Paint background = new Paint();			
        
        background.setColor(getResources().getColor(R.color.game_background));	
        
        cell.setColor(getResources().getColor(R.color.cellcolor));
        
        // draw background
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        
        // draw cells
    	for (Cell c : GameActivity.getLivingCells() ) {
        	
            canvas.drawRect(c.getX() * BOX_SIZE, c.getY() * BOX_SIZE, 
                (c.getX() * BOX_SIZE) +BOX_SIZE, (c.getY() * BOX_SIZE) +BOX_SIZE, cell);
    		}
            	
        }
        
        
        
}


