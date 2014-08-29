package com.cux.wealthxhendri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.pdfjet.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

public class MainActivity extends Activity {
	private ProgressDialog dialog;
	private Login log;
	private EditText mUser;
	private EditText mPassword;
	private String role;
	private boolean Logged = false;
	private Button btnAdd;
	private Button btnDel;
	private Button btnTakeLeave;
	private Button btnPDF;
	private Employee myEmployee;
	private ArrayList<Employee> lstEmployee;
	private EmployeeAdapter mAdapter;
	ListView listViewToDo;
	/**
	 * Mobile Service Client reference
	 */
	private MobileServiceClient mClient;

	/**
	 * Mobile Service Table used to access data
	 */
	private MobileServiceTable<Login> mLogin;
	private MobileServiceTable<Employee> mEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cux.wealthxhendri.R.layout.activity_main);
        mUser = (EditText) findViewById(com.cux.wealthxhendri.R.id.username);
        mPassword = (EditText) findViewById(com.cux.wealthxhendri.R.id.password);
		try {
			// Create the Mobile Service Client instance, using the provided
			// Mobile Service URL and key
			mClient = new MobileServiceClient(
				      "https://wealthx.azure-mobile.net/",
				      "HVHowPyPFgMXNFboBFtVeGiHPyootp70",
				      this
				).withFilter(new ProgressFilter());

			// Get the Mobile Service Table instance to use tesst
			btnAdd = (Button)findViewById(R.id.add);
			btnTakeLeave = (Button)findViewById(R.id.takeLeave);
			btnPDF = (Button)findViewById(R.id.pdf);
			btnPDF.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					generatePDF();
				}}
				
			);
			btnPDF.setVisibility(View.GONE);
			btnAdd.setVisibility(View.GONE);
			btnTakeLeave.setVisibility(View.GONE);
			mLogin = mClient.getTable(Login.class);
			mEmployee = mClient.getTable(Employee.class);
			// Create an adapter to bind the items with the view
			mAdapter = new EmployeeAdapter(this, R.layout.list_fragment);
			listViewToDo = (ListView) findViewById(R.id.listViewEmployee);
			
			
			listViewToDo.setAdapter(mAdapter);
			
			//	mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);

			// Create an adapter to bind the items with the view
		//	mAdapter = new ToDoItemAdapter(this, R.layout.row_list_to_do);
	//		ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
		//	listViewToDo.setAdapter(mAdapter);
		
			// Load the items from the Mobile Service
		//	refreshItemsFromTable();

		} catch (MalformedURLException e) {
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}

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
	
	private void getEmployee()
	{
		 mEmployee.execute(new TableQueryCallback<Employee>() {
	            public void onCompleted(List<Employee> result, int count,
	                Exception exception, ServiceFilterResponse response) {
	                if (exception == null) {
	                    Logged = true;
	                    
	                    refreshItemsFromTable(result);
	                }
	                else
	                	createAndShowDialog("Failed to connect to server", "Connection Failed");
	                
	                dismissProcess();
	            }
	        });
	}
	
	private void getEmployee(String uname)
	{
		 mEmployee.where().field("user").eq(uname).execute(new TableQueryCallback<Employee>() {
	            public void onCompleted(List<Employee> result, int count,
	                Exception exception, ServiceFilterResponse response) {
	                if (exception == null) {
	                    Logged = true;
	                    myEmployee = result.get(0);
	                    refreshItemsFromTable(result);
	                }
	                else
	                	createAndShowDialog("Failed to connect to server", "Connection Failed");
	                
	                dismissProcess();
	            }
	        });
	}
	
	public void doLogin(View view)
	{
		String pass = mPassword.getText().toString();
		String user = mUser.getText().toString();
	//	mLogin.where().
	//	createAndShowDialog(mLogin.where().field("user").eq(user).and().field("password").eq(pass).toString(), "tet");
		mLogin.where().field("user").eq(user).and().field("password").eq(pass).execute(new TableQueryCallback<Login>() {
			@Override
	            public void onCompleted(List<Login> result, int count,
	                Exception exception, ServiceFilterResponse response) {
	                if (exception == null) {
	                	if(!result.isEmpty())
	                      {
	                		for (Login item : result) {
	                      
	                          log = item;
	                         // createAndShowDialog("Logged in as "+item.toString(), "Success");
	                         // role = item.getmRule();
	                          createAndShowDialog("Logged in as "+item.getmRule(), "Success");
	                          role = item.getmRule();
	                          mAdapter.role = role;
	                          if(item.getmRule().equalsIgnoreCase("ADMIN"))
	                          {
	                        	  btnPDF.setVisibility(View.VISIBLE);
	  	                      	  btnAdd.setVisibility(View.VISIBLE);
		                    	  btnTakeLeave.setVisibility(View.GONE);
		                    	 // btnDel.setVisibility(View.VISIBLE);
	                        	  getEmployee();
	                          }
	                          else
	                          {
	  	                      	 btnAdd.setVisibility(View.INVISIBLE);
	  	                         btnPDF.setVisibility(View.INVISIBLE);
	  	                       //	 btnDel.setVisibility(View.INVISIBLE);
		                       	 btnTakeLeave.setVisibility(View.VISIBLE);
                                 getEmployee(item.getmUser()); 
	                      
	                          }
	                        }
	                      }
	                	else
	                	{
	                		 role = "";
	                		 mAdapter.role = role;
	                		 btnPDF.setVisibility(View.GONE);
 	                      	 btnAdd.setVisibility(View.GONE);
	                       	 btnTakeLeave.setVisibility(View.GONE);
	                       	 mAdapter.clear();
	                		createAndShowDialog("Authentication Failed", "Login Failed");
	                	}
	                }
	                else
	                {
	                	role = "";
	                	 mAdapter.role = role;
	                	 btnPDF.setVisibility(View.GONE);
                      	 btnAdd.setVisibility(View.GONE);
                      	 btnTakeLeave.setVisibility(View.GONE);
                      	mAdapter.clear();
	                	createAndShowDialog(exception.getMessage(), exception.getClass().getCanonicalName());
	                	createAndShowDialog("Failed to connect to server", "Login Failed");
	                }
	                if (dialog.isShowing())
	                	{
	                	    dialog.dismiss();
	                	}
	            }
	        });
		
		dialog = new ProgressDialog(this); 
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Logging in...");
        dialog.setCancelable(false);
        dialog.show();
	}
	
private void refreshItemsFromTable(List<Employee> empList) {
		if(lstEmployee == null)
		{
			lstEmployee = new ArrayList<Employee>();
		}
	    lstEmployee.clear();
		mAdapter.clear();
		for (Employee item : empList) {
			mAdapter.add(item);
			lstEmployee.add(item);
		}
	}

public void doDelete(View view)
{
	setProcess("Deleting..");
	final int position = listViewToDo.getPositionForView((RelativeLayout)view.getParent());
	//String adp = (String) v.getTag();
	int n = position; //Integer.parseInt(adp);
	Employee emp = lstEmployee.get(n);
	Login log = new Login();
	log.setmUser(emp.user);
	mLogin.where().field("user").eq(emp.user).execute( new TableQueryCallback<Login>(){

		@Override
		public void onCompleted(List<Login> arg0, int arg1, Exception arg2,
				ServiceFilterResponse arg3) {
			Login log = arg0.get(0);
			mLogin.delete(log, new TableDeleteCallback(){

				@Override
				public void onCompleted(Exception arg0,
						ServiceFilterResponse arg1) {
					getEmployee();
					// TODO Auto-generated method stub
				}
				
			});
			// TODO Auto-generated method stub
			
		}
		
	});
}

public void doAdd(View v)
{
	 Intent k = new Intent(MainActivity.this, AddActivity.class);
     startActivityForResult(k, 1);
}


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	getEmployee();
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

public void doTakeLeave(View v)
{
	AlertDialog.Builder alert = new AlertDialog.Builder(this);

	alert.setTitle("Take Leave");
	alert.setMessage("Number of Days:");

	// Set an EditText view to get user input 
	final EditText input = new EditText(this);
	input.setHint("Taken Annual Leave");
	input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
	alert.setView(input);

	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	public void onClick(DialogInterface dialog, int whichButton) {
	  int value = Integer.parseInt(input.getText().toString());
	  
	  if(myEmployee.al_taken + value <= myEmployee.al )
        {
		  myEmployee.al_taken = myEmployee.al_taken + value;
        
	      mEmployee.update(myEmployee,new TableOperationCallback<Employee>() {

		@Override
		public void onCompleted(Employee emp, Exception exc,
				ServiceFilterResponse fltr) {
			getEmployee(emp.user);
			
		}
          });
	  setProcess("Saving..");
	  //  mEmployee.insert(element, callback);
	  // Do something with value!
	  }
	  else
	  {
		  createAndShowDialog("Cannot Take leave more than allowed annual leave", "Warning");
	  }
	}
	});

	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	  public void onClick(DialogInterface dialog, int whichButton) {
	    // Canceled.
	  }
	});

	alert.show();
}



void generatePDF()
{
	File exportDir;
	float currentRow = 60f;
	float rowSpace = 20f;
	String state = Environment.getExternalStorageState();
	//check if the external directory is availabe for writing
	if (!Environment.MEDIA_MOUNTED.equals(state)) {
	return;
	}
	else {
	 exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	}
	
	
	//if the external storage directory does not exists, we create it
	if (!exportDir.exists()) {
	exportDir.mkdirs();
	}
	File file;
	file = new File(exportDir, "Report.pdf");
	
	//PDF is a class of the PDFJET library
	PDF pdf;
	try {
		pdf = new PDF(new FileOutputStream(file));
		
		//first we create a page with portrait orientation
		Page page = new Page(pdf, Letter.PORTRAIT);

		//font of the title
		Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
		f1.setSize(14.0f);
	    
		Font f2 = new Font(pdf, CoreFont.HELVETICA);
		f1.setSize(12.0f);
		
		//title: font f1 and color blue
		TextLine title;
		
		title = new TextLine(f1, "Employee Report");
		title.setFont(f1);
		title.setColor(Color.blue);
		
		title.setPosition(page.getWidth()/2 - title.getWidth()/2, 40f);
		//center the title horizontally on the page
		//title.setPosition(page.getWidth()/2);

		//draw the title on the page
		title.drawOn(page);
		
		for (Employee item : lstEmployee) {
			currentRow = currentRow + rowSpace;
			title = new TextLine(f1, "Name: "+item.name);
			title.setFont(f2);
			title.setColor(Color.black);
			title.setPosition(40f, currentRow);
			title.drawOn(page);
			currentRow = currentRow + rowSpace;
			title = new TextLine(f1, "Employee ID: "+item.emp_id);
			title.setFont(f2);
			title.setColor(Color.black);
			title.setPosition(40f, currentRow);
			title.drawOn(page);
			currentRow = currentRow + rowSpace;
			title = new TextLine(f1, "Department: "+item.dept);
			title.setFont(f2);
			title.setColor(Color.black);
			title.setPosition(40f, currentRow);
			title.drawOn(page);
			currentRow = currentRow + rowSpace;
			title = new TextLine(f1, "Username: "+item.user);
			title.setFont(f2);
			title.setColor(Color.black);
			title.setPosition(40f, currentRow);
			title.drawOn(page);
			currentRow = currentRow + rowSpace;
			title = new TextLine(f1, "Annual Leave: "+ String.valueOf(item.al));
			title.setFont(f2);
			title.setColor(Color.black);
			title.setPosition(40f, currentRow);
			title.drawOn(page);
			currentRow = currentRow + rowSpace;
			title = new TextLine(f1, "Annual leave taken : "+ String.valueOf(item.al_taken));
			title.setFont(f2);
			title.setColor(Color.black);
			title.setPosition(40f, currentRow);
			title.drawOn(page);
			currentRow = currentRow + rowSpace;
			if((currentRow + 120f) > page.getHeight())
			{
				currentRow = 60f;
				page = new Page(pdf, Letter.PORTRAIT);
			}
		}
		
		pdf.flush();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Intent intent = new Intent();
	intent.setAction(android.content.Intent.ACTION_VIEW);
	intent.setDataAndType(Uri.fromFile(file), "application/pdf");
	try
	{
    	startActivity(intent); 
	}
	catch(Exception ex)
	{
		
	}//instructions to create the pdf file content

	
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
