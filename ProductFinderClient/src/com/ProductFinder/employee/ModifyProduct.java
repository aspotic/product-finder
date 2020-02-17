package com.ProductFinder.employee;

import java.util.LinkedList;

import com.ProductFinder.R;
import com.ProductFinder.dataStructure.ClientItem;
import com.ProductFinder.network.getData;
import com.ProductFinder.network.sendData;
import com.ProductFinder.run.Status;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


/**
 * Activity used for reconfiguring products
 * @author adam knox
 */
public class ModifyProduct extends Activity {
    private Button modifyProduct;
	private Spinner department;
    private Spinner brand;
    private EditText prodName;
    private EditText prodDescription;
    private EditText prodSerial;
    private EditText prodPrice;
    private EditText prodShelf;
    private EditText prodIsle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_product_form);
        
        //Set the form's name
        ((TextView) findViewById(R.id.formTitle)).setText("Modify Product");
        
        ClientItem item = null;
        
		//Get the result from the server
		LinkedList<ClientItem> list = getData.getItemResults("name=" + Uri.encode(getIntent().getExtras().getString("Name")));
		if (list != null) {
			//If there is only one entry, then continue with the operation
			if (list.size() == 1) {
				item = list.getFirst();
			}
		}
        
        
		
		//Populate the window to contain the item's current data
		if (item != null) {
			final String oldName = item.name;
			final String oldBrand = item.brand;
			final String oldDepartment = item.department;
			final long store = item.store;

			
	        //Get screen objects
	        modifyProduct = (Button) findViewById(R.id.createProduct);
	        modifyProduct.setText("Modify Product");
	        
	    	department = (Spinner) findViewById(R.id.prodDepartment);
	        brand = (Spinner) findViewById(R.id.prodBrand);
	        
			prodName = (EditText) findViewById(R.id.prodName);
			prodDescription = (EditText) findViewById(R.id.prodDescription);
			prodSerial = (EditText) findViewById(R.id.prodSerial);
			prodPrice = (EditText) findViewById(R.id.prodPrice);
			prodShelf = (EditText) findViewById(R.id.prodShelf);
			prodIsle = (EditText) findViewById(R.id.prodIsle);
			
			

	        //Populate the departments spinner
	        LinkedList<String> departmentList = getData.getDepartments();
	        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departmentList);
	        department.setAdapter(deptAdapter);
	        department.setOnItemSelectedListener(new OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	            	department.setSelection(position);
	            }

	            public void onNothingSelected(AdapterView<?> parentView) {
	            	department.setSelection(0);
	            }
	        });
	        
	        //Populate the brand spinner
	        LinkedList<String> brandList = getData.getBrands();
	        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brandList);
	        brand.setAdapter(brandAdapter);
	        brand.setOnItemSelectedListener(new OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	            	brand.setSelection(position);
	            }

	            public void onNothingSelected(AdapterView<?> parentView) {
	            	brand.setSelection(0);
	            }
	        });
	        
	        

			//Set the values of the screen objects
	    	department.setSelection(departmentList.indexOf(item.department));
	    	brand.setSelection(brandList.indexOf(item.brand));
			prodName.setText(item.name);
			prodDescription.setText(item.description);
			prodSerial.setText(String.valueOf(item.serial));
			prodPrice.setText(String.valueOf(item.price));
			prodShelf.setText(String.valueOf(item.shelf));
			prodIsle.setText(String.valueOf(item.isle));
			
			
			
			//Create the button listener
	        modifyProduct.setOnClickListener(new OnClickListener () {
				public void onClick(View view) {
					try {		
						String departmentVal = department.getSelectedItem().toString();
						if (departmentVal == null) {
							departmentVal = oldDepartment;
						}
						String brandVal = brand.getSelectedItem().toString();
						if (brandVal == null) {
							brandVal = oldBrand;
						}
						
						
						//Setup the ClientItem to transfer
						ClientItem newItem = new ClientItem(prodName.getText().toString(), 
															departmentVal,
															brandVal,
															Long.parseLong(prodSerial.getText().toString()),
															prodDescription.getText().toString(),
															Double.parseDouble(prodPrice.getText().toString()),
															Long.parseLong(prodIsle.getText().toString()),
															Long.parseLong(prodShelf.getText().toString()),
															0.0,
															0.0);	
						newItem.store = store;

						//Send the object to the server
						boolean response = sendData.sendModifiedItem(newItem, oldName);

						
						if (response) {
							//Send the user a message saying they failed
							//Create new activity
			                Intent doCreationResponse = new Intent(view.getContext(), Status.class);
			                //Pass required variables
			                doCreationResponse.putExtra("message", "The item '" + newItem.name + "' was successfully Modified!");
			                //respond
			                startActivity(doCreationResponse);
						} else {
							//Send the user a message saying they failed
							//Create new activity
			                Intent doCreationResponse = new Intent(view.getContext(), Status.class);
			                //Pass required variables
			                doCreationResponse.putExtra("message", "The item modification failed. If the item was renamed, please make sure the new name is not already in use");
			                //respond
			                startActivity(doCreationResponse);
						}
					} catch (Exception e) {
						//Send the user a message saying they failed
						//Create new activity
		                Intent doCreationResponse = new Intent(view.getContext(), Status.class);
		                //Pass required variables
		                doCreationResponse.putExtra("message", "The item modification failed.");
		                //respond
		                startActivity(doCreationResponse);
					}
				}
	        });
			
		} else {

			//Create new activity
            Intent doShowResponse = new Intent(getBaseContext(), Status.class);
            //Pass the message
            doShowResponse.putExtra("message", "Could not find product");
            //show the response
            startActivity(doShowResponse);
		}
    }
}