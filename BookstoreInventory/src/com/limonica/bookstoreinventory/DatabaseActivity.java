package com.limonica.bookstoreinventory;

import java.util.List;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

public class DatabaseActivity extends ListActivity {
	
	private BooksDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		
		dataSource = new BooksDataSource(this);
		dataSource.open();
		
		List<Book> values = dataSource.getAllBooks();
		
		//SimpleCursorAdapter to show elements in listview
		ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	public void onClick(View view) {
		ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) getListAdapter();
		Book book = null;
		if (view.getId() == R.id.add) {
            IntentIntegrator.initiateScan(DatabaseActivity.this, R.layout.capture,
                    R.id.viewfinder_view, R.id.preview_view, true);
		}
		else if (view.getId() == R.id.delete) {
			int dbSize = getListAdapter().getCount();
			if(getListAdapter().getCount() > 0) {
				book = (Book) getListAdapter().getItem(dbSize-1);
				dataSource.deleteBook(book);
				adapter.remove(book);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database, menu);
		return true;
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) getListAdapter();
		Book book = null;
		
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                	dataSource.open();
                	book = dataSource.createBook(result.toString());
                    adapter.add(book);
                    dataSource.close();
                }
                break;
            default:
        }
    }

}
