package com.ProductFinder.employee;

import com.ProductFinder.dataStructure.ClientItem;
import com.ProductFinder.network.getData;
import com.ProductFinder.network.sendData;
import com.ProductFinder.run.Status;
import com.ProductFinder.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


/**
 * Activity used for adding a new product to the the product database
 * @author adam knox
 */
public class CreateProduct extends Activity {
    private Button createProduct;
	private Spinner department;
    private Spinner brand;
    private EditText name;
    private String prodName;
    private String prodBrand;
    private String prodDepartment;
    private String prodDescription;
    private String prodSerial;
    private String prodPrice;
    private String prodShelf;
    private String prodIsle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_product_form);
        
        
        //Get screen objects
        createProduct = (Button) findViewById(R.id.createProduct);
    	department = (Spinner) findViewById(R.id.prodDepartment);
        brand = (Spinner) findViewById(R.id.prodBrand);
        name = (EditText) findViewById(R.id.prodName);
        
        //Set the product name
        name.setText(getIntent().getStringExtra("Name"));

        //Populate the departments spinner
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getData.getDepartments());
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
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getData.getBrands());
        brand.setAdapter(brandAdapter);
        brand.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	brand.setSelection(position);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            	brand.setSelection(0);
            }
        });
        
        
        
        //send the new product to the server
        createProduct.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
				try {
					//Get the form input data
			        prodName = ((EditText) findViewById(R.id.prodName)).getText().toString();
			        prodBrand = ((Spinner) findViewById(R.id.prodBrand)).getSelectedItem().toString();
			        prodDepartment = ((Spinner) findViewById(R.id.prodDepartment)).getSelectedItem().toString();
			        prodDescription = ((EditText) findViewById(R.id.prodDescription)).getText().toString();
			        prodSerial = ((EditText) findViewById(R.id.prodSerial)).getText().toString();
			        prodPrice = ((EditText) findViewById(R.id.prodPrice)).getText().toString();
			        prodShelf = ((EditText) findViewById(R.id.prodShelf)).getText().toString();
			        prodIsle = ((EditText) findViewById(R.id.prodIsle)).getText().toString();
			        
					//Create the item object
					ClientItem item = new ClientItem(prodName, prodDepartment, prodBrand, Long.parseLong(prodSerial), prodDescription, Double.parseDouble(prodPrice), Integer.parseInt(prodIsle), Integer.parseInt(prodShelf), 0.0, 0.0);
					
					//Send the object to the server
					boolean response = sendData.sendNewItem(item);
					
					if (response) {
						//Send the user a message saying they failed
						//Create new activity
		                Intent doCreationResponse = new Intent(view.getContext(), Status.class);
		                //Pass required variables
		                doCreationResponse.putExtra("message", "The item '" + item.name + "' was successfully created!");
		                //respond
		                startActivity(doCreationResponse);
					} else {
						//Send the user a message saying they failed
						//Create new activity
		                Intent doCreationResponse = new Intent(view.getContext(), Status.class);
		                //Pass required variables
		                doCreationResponse.putExtra("message", "The item creation failed.");
		                //respond
		                startActivity(doCreationResponse);
					}
				} catch (Exception e) {
					//Send the user a message saying they failed
					//Create new activity
	                Intent doCreationResponse = new Intent(view.getContext(), Status.class);
	                //Pass required variables
	                doCreationResponse.putExtra("message", "Please retry your item creation operation.  Some of your data was entered in the wrong format.");
	                //respond
	                startActivity(doCreationResponse);
				}
			}
        });
    }
}