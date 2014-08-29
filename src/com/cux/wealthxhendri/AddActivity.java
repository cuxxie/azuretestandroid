package com.cux.wealthxhendri;


import java.net.MalformedURLException;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Build;

public class AddActivity extends ActionBarActivity {
	private MobileServiceClient mClient;

	/**
	 * Mobile Service Table used to access data
	 */
	private ProgressDialog dialog;
	private MobileServiceTable<Login> mLogin;
	private MobileServiceTable<Employee> mEmployee;
	private EditText txtName;
	private EditText txtEmp;
	private EditText txtDept;
	private EditText txtUser;
	private Button btnSave;
	Login usr;
	Employee emp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		btnSave = (Button) findViewById(R.id.btnSave);
		
		btnSave.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		            //Do stuff here
		    	doSave();
		    }
		});
		dialog = new ProgressDialog(this);
		txtName = (EditText) findViewById(R.id.txtAddName);
		txtDept = (EditText) findViewById(R.id.txtAddDept);
		txtEmp = (EditText) findViewById(R.id.txtAddEmpId);
		txtUser = (EditText) findViewById(R.id.txtAddUser);
		try {
			mClient = new MobileServiceClient(
				      "https://wealthx.azure-mobile.net/",
				      "HVHowPyPFgMXNFboBFtVeGiHPyootp70",
				      this
				).withFilter(new ProgressFilter());
			
			mLogin = mClient.getTable(Login.class);
			mEmployee = mClient.getTable(Employee.class);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
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
	
	void doSave()
	{
		setProcess("Saving..");
		String Username = txtUser.getText().toString();
		usr = new Login(Username, "123456789", "USER");
		emp = new Employee();
		emp.name = txtName.getText().toString();
		emp.dept = txtDept.getText().toString();
		emp.emp_id = txtEmp.getText().toString();
		emp.user = txtUser.getText().toString().toUpperCase();
		
		if(Username == "" || emp.name == "" || emp.dept == "" || emp.emp_id == "")
		{
			createAndShowDialog("Please Complete all input!", "Incomplete");
			return;
		}
		
		mLogin.insert(usr, new TableOperationCallback<Login> (){

			@Override
			public void onCompleted(Login Log, Exception exc,
					ServiceFilterResponse arg2) {
				// TODO Auto-generated method stub
				if(exc == null)
				{
					mEmployee.insert(emp, new TableOperationCallback<Employee>(){

						@Override
						public void onCompleted(Employee arg0, Exception arg1,
								ServiceFilterResponse arg2) {
							// TODO Auto-generated method stub
							success();
							
						}
						
					});
				}
				else
				{
					dismissProcess();
					createAndShowDialog(exc, "Add Failed");
				}
			}
			
		});
		
	}
	public void setProcess(String text)
	{
		if (!dialog.isShowing())
		{
			dialog = new ProgressDialog(this); 
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage(text);
			dialog.setCancelable(false);
			dialog.show();
		}
	}

	public void dismissProcess()
	{
		if (dialog.isShowing())
	 	{
	 	    dialog.dismiss();
	 	}
	}
	private void success()
	{
		dismissProcess();
		finish();
	}
	/**
	 * Creates a dialog and shows it
	 * 
	 * @param exception
	 *            The exception to show in the dialog
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if(exception.getCause() != null){
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
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
			View rootView = inflater.inflate(R.layout.fragment_add, container,
					false);
			return rootView;
		}
	}
	
	
private class ProgressFilter implements ServiceFilter {
		
		@Override
		public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
				final ServiceFilterResponseCallback responseCallback) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
				//	if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
				}
			});
			
			nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {
				
				@Override
				public void onResponse(ServiceFilterResponse response, Exception exception) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
					//		if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
						}
					});
					
					if (responseCallback != null)  responseCallback.onResponse(response, exception);
				}
			});
		}
	}
}
