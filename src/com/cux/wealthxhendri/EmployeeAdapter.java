package com.cux.wealthxhendri;

import android.content.Context;
import android.view.*;
import android.app.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.*;

public class EmployeeAdapter extends ArrayAdapter<Employee> {

	/**
	 * Adapter context
	 */
	Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;
	public String role;
	public EmployeeAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);

		mContext = context;
		mLayoutResourceId = layoutResourceId;
	}

	/**
	 * Returns the view for a specific item on the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		final Employee currentItem = getItem(position);
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}
		
		//row.setTag();
		final TextView textName = (TextView) row.findViewById(com.cux.wealthxhendri.R.id.txtName);
		textName.setText(currentItem.name);
		
		final TextView textEmpID = (TextView) row.findViewById(com.cux.wealthxhendri.R.id.txtEmpId);
		textEmpID.setText( currentItem.emp_id);
		
		final TextView textDept = (TextView) row.findViewById(com.cux.wealthxhendri.R.id.txtDept);
		textDept.setText(currentItem.dept);
		
		final TextView textAL = (TextView) row.findViewById(com.cux.wealthxhendri.R.id.txtAL);
		textAL.setText("AL: " + String.valueOf(currentItem.al));
		
		final TextView textALT = (TextView) row.findViewById(com.cux.wealthxhendri.R.id.txtALT);
		textALT.setText("AL Taken: " + String.valueOf(currentItem.al_taken));
		
		final TextView textMC = (TextView) row.findViewById(com.cux.wealthxhendri.R.id.txtMC);
		textMC.setText("User: " + String.valueOf(currentItem.user));
		
		final Button btnDel = (Button) row.findViewById(com.cux.wealthxhendri.R.id.btndelete);
		if(!role.equalsIgnoreCase("ADMIN"))
		{
			btnDel.setVisibility(View.INVISIBLE);
		}
		else
		{
			btnDel.setVisibility(View.VISIBLE);
		}
			
		
	/*	final CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkEmployee);
		checkBox.setText(currentItem.getText());
		checkBox.setChecked(false);
		checkBox.setEnabled(true);

		checkBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkBox.isChecked()) {
					checkBox.setEnabled(false);
					if (mContext instanceof ToDoActivity) {
						ToDoActivity activity = (ToDoActivity) mContext;
						activity.checkItem(currentItem);
					}
				}
			}
		});
	*/
		row.setTag(position);
		return row;
	}

}
