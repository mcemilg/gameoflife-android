package com.mcg.gameoflife;

import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HighScoreActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high_score);
		setContentView(R.layout.activity_high_score);
		
		TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
		SharedPreferences scorePrefs = getSharedPreferences(GameActivity.SAVE_PREFS, 0);
		
		String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
		StringBuilder scoreBuild = new StringBuilder("");
		
		int i = 0;
		
		for(String score : savedScores){
			++i;
			scoreBuild.append(i + ". " +score+"\n");
		}
		
		scoreView.setText(scoreBuild.toString());
	}

}
