package com.ProductFinder.run;

import com.ProductFinder.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


/**
 * Activity that notifies the user of their procedure status
 * @author adam knox
 */
public class Status extends Activity {
    private Button okay;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_job_status);
        okay = (Button) findViewById(R.id.okay);
        
        //Get the passed in message
        String message  = this.getIntent().getStringExtra("message");
        
        //Set the message
        ((TextView) findViewById(R.id.status)).setText(message);
        
        okay.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
				try {
				//Go to given window
	            Intent doBack = new Intent(view.getContext(), Main.class);
	            startActivity(doBack);
			} catch (Exception e) {}
			}
        });
    }
}