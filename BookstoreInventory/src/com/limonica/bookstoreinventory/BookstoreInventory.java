package com.limonica.bookstoreinventory;

import java.util.ArrayList;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BookstoreInventory extends Activity {
	private Handler  handler = new Handler();
    private TextView txtScanResult;
    private ArrayList<String> newBooks = new ArrayList<String>();
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookstore_inventory);
        
        txtScanResult = (TextView) findViewById(R.id.scan_result);
        View btnScan = findViewById(R.id.scan_button);
        View btnSend = findViewById(R.id.send_database);

        // Scan button
        btnScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the last parameter to true to open front light if available
                IntentIntegrator.initiateScan(BookstoreInventory.this, R.layout.capture,
                        R.id.viewfinder_view, R.id.preview_view, true);
            }
        });
        
        // Send to database button     
        btnSend.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent showList = new Intent(BookstoreInventory.this, DatabaseActivity.class);
        		startActivity(showList);
        	}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bookstore_inventory, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                	newBooks.add(result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            txtScanResult.setText(newBooks.toString());
                        }
                    });
                }
                break;
            default:
        }
    }
}
