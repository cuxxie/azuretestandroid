package com.cux.wealthxhendri;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;

import java.util.List;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.os.Build;

public class TableActivity extends ActionBarActivity {
	private EmployeeAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// Create an adapter to bind the items with the view
		mAdapter = new EmployeeAdapter(this, R.layout.list_fragment);
		ListView listViewToDo = (ListView) findViewById(R.id.listViewEmployee);
		listViewToDo.setAdapter(mAdapter);
	
		// Load the items from the Mobile Service
		//refreshItemsFromTable();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.table, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_table,
					container, false);
			return rootView;
		}
	}
	
	private void refreshItemsFromTable(List<Employee> empList) {

		
		for (Employee item : empList) {
			mAdapter.add(item);
		}
		// Get the items that weren't marked as completed and add them in the
		// adapter
	/*	mToDoTable.where().field("complete").eq(val(false)).execute(new TableQueryCallback<ToDoItem>() {

			public void onCompleted(List<ToDoItem> result, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {
					mAdapter.clear();

					for (ToDoItem item : result) {
						mAdapter.add(item);
					}

				} else {
					createAndShowDialog(exception, "Error");
				}
			}
		});
		*/
	}
	
}
