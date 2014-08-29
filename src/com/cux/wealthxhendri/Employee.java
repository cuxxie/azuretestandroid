package com.cux.wealthxhendri;

public class Employee {
/*
id	string	
emp_id	string	
name	string	
dept	string	
al	number	
al_taken	number	
mc	number */
	@com.google.gson.annotations.SerializedName("id")
   public String id;
	@com.google.gson.annotations.SerializedName("emp_id")
   public String emp_id;
	@com.google.gson.annotations.SerializedName("name")
   public String name;
	@com.google.gson.annotations.SerializedName("dept")
   public String dept;
	@com.google.gson.annotations.SerializedName("al")
   public int al;
	@com.google.gson.annotations.SerializedName("al_taken")
   public int al_taken;
	@com.google.gson.annotations.SerializedName("mc")
   public int mc;
	@com.google.gson.annotations.SerializedName("user")
	   public String user;
	
	public Employee() {
		super();
		al = 14;
		al_taken = 0;
		mc = 0;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Employee && ((Employee) o).id == id;
	}
}
