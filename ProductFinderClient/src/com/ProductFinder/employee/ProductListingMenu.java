package com.ProductFinder.employee;

import com.ProductFinder.R;
import com.ProductFinder.network.sendData;
import com.ProductFinder.run.Status;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Produces listeners for creating a new item and updating an item buttons
 * @author adam knox
 */
public class ProductListingMenu extends Activity {
    private Button createProduct;
    private Button modifyProduct;
    private Button removeProduct;

    /**
     * Activity used for choosing what to do to the product database
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_update_listings_menu);
        
        //Get screen objects
        createProduct = (Button) findViewById(R.id.createProduct);
        modifyProduct = (Button) findViewById(R.id.modifyProduct);
        removeProduct = (Button) findViewById(R.id.removeProduct);
        
        
        //run activity for creating products
        createProduct.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
				//Create new activity
                Intent doCreateProduct = new Intent(view.getContext(), CreateProduct.class);
                //Pass required variables
                doCreateProduct.putExtra("Name", ((EditText) findViewById(R.id.prodName)).getText().toString());
                //start the product creation activity
                startActivity(doCreateProduct);
			}
        });
        
        
        
        //run activity for modifying existing products
        modifyProduct.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
				//Create new activity
                Intent doListModifyProduct = new Intent(view.getContext(), ModifyProduct.class);
                //Pass required variables
                doListModifyProduct.putExtra("Name", ((EditText) findViewById(R.id.prodName)).getText().toString());
                //show the product
                startActivity(doListModifyProduct);
			}
        }); 
        
        
        //remove existing products
        removeProduct.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
				//Attempt to remove the product
				boolean result = sendData.removeItem(((EditText) findViewById(R.id.prodName)).getText().toString());
				
				//Form the user's message
				String message = "The removal of '" + ((EditText) findViewById(R.id.prodName)).getText().toString() + "' was not possible";
				if (result) {
					message = "The removal of '" + ((EditText) findViewById(R.id.prodName)).getText().toString() + "' was successful";
				}
				
				//Create new activity
                Intent doRemoveProductResponse = new Intent(view.getContext(), Status.class);
                //Pass required variables
                doRemoveProductResponse.putExtra("message", message);
                //respond
                startActivity(doRemoveProductResponse);
			}
        });        
    }
}