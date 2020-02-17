package com.ProductFinder.customer;

import java.util.ArrayList;

import com.ProductFinder.R;
import com.ProductFinder.dataStructure.ClientItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Display the item's found from a query of the database
 * @author adam knox
 */
public class Results extends Activity {
	private ListView searchResultsView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_search_results);
        
        //Get required objects
    	searchResultsView = (ListView) findViewById(R.id.searchResults);
	    
		@SuppressWarnings("unchecked")
		final ArrayList<ClientItem> list = (ArrayList<ClientItem>) getIntent().getExtras().get("itemList");
		
		//Get the keys from the item list
		ArrayList<String> keys = new ArrayList<String> ();
		for (ClientItem item : list) {
			keys.add(item.name);
		}
		
		
		//Add the results to the results list view
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter searchResultsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, keys);
	    searchResultsView.setAdapter(searchResultsAdapter);
	    searchResultsView.setFocusableInTouchMode(true);
	    
	    //Switch to activity displaying selected product information
	    searchResultsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	public void onItemClick(@SuppressWarnings("rawtypes") AdapterView adapterView, View view, int index, long arg3) {
				//Create new activity
                Intent doShowItem = new Intent(view.getContext(), DisplayItem.class);
                
                //Pass the list of matched items
                doShowItem.putExtra("item", list.get(index));
                
                //start the product display activity
                startActivity(doShowItem);
	    	}
	    });
    }
}