package com.mcg.gameoflife;

import java.io.Serializable;

public class Cell implements Serializable{
	private int posX;
	private int posY;
	
	Cell( ){
		posX = 0;
		posY = 0;
	}
	
	Cell(int X){
		posX = X;
	}
	
	Cell(int X, int Y){
		posX = X;
		posY = Y;
	}
	
	public int getX( ){
		return posX;
	}
	
	public int getY( ){
		return posY;
	}
	
	public void setXY(int x, int y){
		posX = x;
		posY = y;
	}
	
	@Override
	public boolean  equals (Object object) {
		boolean result = false;
		Cell temp = (Cell)object;
		if(this.posX == temp.getX() && this.posY == temp.getY())
			return true;
		return result;
	}
	
	
}
