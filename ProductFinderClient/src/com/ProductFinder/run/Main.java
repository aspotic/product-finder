package com.ProductFinder.run;
import com.ProductFinder.R;
import com.ProductFinder.customer.Search;
import com.ProductFinder.employee.ProductListingMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//TODO
//FUTURE IDEAS
//Map
//Multiple Stores (Semi implemented already)
//Modify stores/departments/brands on app under a third permissions level
//create permissions for groups like management, floor employee, customer
//Integrate with an online store/web interface
//Make the server-client ineraction transactional


/**
 * The main program activity. what is seen when the program starts
 * @author Adam Knox
 */
public class Main extends Activity {
    private Button search;
    private Button updateLog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Get screen objects
        search = (Button) findViewById(R.id.search);
        updateLog = (Button) findViewById(R.id.updateLog);

        
        //run activity to perform a product search
        search.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
                Intent doSearch = new Intent(view.getContext(), Search.class);
                startActivity(doSearch);
			}
        });

        //run activity to perform an update on the product database
        updateLog.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
                Intent doUpdateProduct = new Intent(view.getContext(), ProductListingMenu.class);
                startActivity(doUpdateProduct);
			}
        });
    }
}