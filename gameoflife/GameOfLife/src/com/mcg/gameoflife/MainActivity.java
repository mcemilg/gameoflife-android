package com.mcg.gameoflife;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText edit = null;
	private static String userName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
	
		
		//Start button
		Button bStart = (Button) findViewById(R.id.button1);
        bStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		edit = (EditText) findViewById(R.id.editText1);
        		userName = edit.getText().toString();
        		
            	if(userName.trim().equals("")){			//user name error
            		Toast.makeText(getApplicationContext(),
        					getString(R.string.null_username),
        					Toast.LENGTH_SHORT).show();
            	}else{
            		Intent intent = new Intent(MainActivity.this, GameActivity.class);	
                    startActivity(intent);		//start game activity
            	}
            
            }
        });
        
		//High Score button
		Button bHighScore = (Button) findViewById(R.id.button2);
        bHighScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent highIntent = new Intent(MainActivity.this, HighScoreActivity.class);
            	startActivity(highIntent);		//start highscore activity
            }
        });
		
	}
	
	public static String getUserName(){
		return userName;
	}
}
