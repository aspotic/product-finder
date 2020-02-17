package com.ProductFinder.customer;

import java.util.LinkedList;

import com.ProductFinder.dataStructure.ClientItem;
import com.ProductFinder.network.getData;
import com.ProductFinder.run.Status;
import com.ProductFinder.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;	
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 * Performs a search of the product database
 * @author adam knox
 */
public class Search extends Activity {
    private EditText searchTerm;
	private Spinner department;
    private Spinner brand;
    private EditText highestPrice;
    private CheckBox advancedSearch;
    private Button search;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_search);
        
        //Get screen objects
    	searchTerm = (EditText) findViewById(R.id.searchTerm);
    	department = (Spinner) findViewById(R.id.deptSpinner);
        brand = (Spinner) findViewById(R.id.brandSpinner);
        highestPrice = (EditText) findViewById(R.id.highPrice);
        advancedSearch = (CheckBox) findViewById(R.id.advancedSearch);
        search = (Button) findViewById(R.id.search);
        
        
        //Populate the departments spinner
        LinkedList<String> departments = getData.getDepartments();
        if (departments == null) {
        	departments = new LinkedList<String>();
        }
        departments.add("All Departments");
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departments);
        department.setAdapter(deptAdapter);
        department.setSelection(departments.size()-1);
        department.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	department.setSelection(position);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            	department.setSelection(0);
            }
        });
        
        
        //Populate the brand spinner
        LinkedList<String> brands = getData.getBrands();
        if (brands == null) {
        	brands = new LinkedList<String>();
        }
        brands.add("All Brands");
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brands);
        brand.setAdapter(brandAdapter);
        brand.setSelection(brands.size()-1);
        brand.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	brand.setSelection(position);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            	brand.setSelection(0);
            }
        });
        
        
        //Check for highest price format or magnitude errors
        highestPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
        	public void onFocusChange(View arg0, boolean arg1) {
        	     double price = 0;
 				//Make sure the content is a double
        		try {
        			price = Double.parseDouble(highestPrice.getText().toString());
        		} catch (NumberFormatException e) {
        			highestPrice.setText(getString(R.string.highprice));
            		highestPrice.selectAll();
        		}
				//Make sure the price is positive
        		if (price < 0) {
        			highestPrice.setText(getString(R.string.highprice));
            		highestPrice.selectAll();
        		}
			}
        });
        
        
        //Hide all advanced search items by default
        department.setVisibility(View.GONE);
        brand.setVisibility(View.GONE);
        highestPrice.setVisibility(View.GONE);


        //Show/Hide advanced search using advancedSearch check box
        advancedSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				//Show Advanced Search Items
				if (advancedSearch.isChecked()) {
			        department.setVisibility(View.VISIBLE);
			        brand.setVisibility(View.VISIBLE);
			        highestPrice.setVisibility(View.VISIBLE);
			        //Hide Advanced Search Items
				} else {
			        department.setVisibility(View.GONE);
			        brand.setVisibility(View.GONE);
			        highestPrice.setVisibility(View.GONE);
				}
			}
        });
    
        
        //Request Search Results From Server
        search.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
				//Create filter string
				String filter = "";
				
				//Only use advanced parameters if advanced search is selected
				if (advancedSearch.isChecked()) {
					//Clear unused filters
					String departmentString = department.getSelectedItem().toString();
					if (departmentString.equals("All Departments")) {
						departmentString = "";
					}
					String brandString = brand.getSelectedItem().toString();
					if (brandString.equals("All Brands")) {
						brandString = "";
					}
					String maxPriceString = highestPrice.getText().toString();
					if (maxPriceString.equals("All Brands")) {
						maxPriceString = "Highest Price";
					}
					
					//Create the filter
					filter = "department=" + Uri.encode(departmentString) + 
							 "&brand=" + Uri.encode(brandString) + 
							 "&MaxPrice=" + Uri.encode(highestPrice.getText().toString()) + 
							 "&name=" + Uri.encode(searchTerm.getText().toString());
				} else {
					filter = "name=" + Uri.encode(searchTerm.getText().toString());
				}
				
				//Get the results list from the server and pass it to the next activity
				LinkedList<ClientItem> list = getData.getItemResults(filter);
				if (list != null) {
					//Did not find the item so notify the user
					if (list.size() == 0) {
						//Create new activity
		                Intent doShowResponse = new Intent(view.getContext(), Status.class);
		                //Pass the message
		                doShowResponse.putExtra("message", "Could not find product");
		                //show the response
		                startActivity(doShowResponse);

					//If there is only one entry, then go directly to the display page
					} else if (list.size() == 1) {
						//Create new activity
		                Intent doShowResponse = new Intent(view.getContext(), DisplayItem.class);
		                //Pass the item
		                doShowResponse.putExtra("item", list.getFirst());
		                //show the item
		                startActivity(doShowResponse);

					//found multiple results so go to the item list activity
					} else {
						//Create new activity
		                Intent doShowResults = new Intent(view.getContext(), Results.class);
		                //Pass the list of matched items
		                doShowResults.putExtra("itemList", list);
		                //start the product listing activity
		                startActivity(doShowResults);
					}
					
				//Did not find the item so notify the user
				} else {
					//Create new activity
	                Intent doShowResponse = new Intent(view.getContext(), Status.class);
	                //Pass the message
	                doShowResponse.putExtra("message", "Could not find product");
	                //show the response
	                startActivity(doShowResponse);
				}
			}
        });
    }
}