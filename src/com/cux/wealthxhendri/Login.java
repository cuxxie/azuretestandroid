package com.cux.wealthxhendri;


public class Login {
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	@com.google.gson.annotations.SerializedName("user")
	private String mUser;
	
	public String getmUser() {
		return mUser;
	}

	public void setmUser(String mUser) {
		this.mUser = mUser;
	}

	@com.google.gson.annotations.SerializedName("password")
	private String mPassword;
	
	public String getmPassword() {
		return mPassword;
	}

	public void setmPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public String getmRule() {
		return mRule;
	}

	public void setmRule(String mRule) {
		this.mRule = mRule;
	}

	@com.google.gson.annotations.SerializedName("role")
	private String mRule;

	@Override
	public String toString() {
		return "Login [mUser=" + mUser + ", mPassword=" + mPassword
				+ ", mRule=" + mRule + "]";
	}
	
	public Login()
	{
		
	}
	
	public Login(String mUser, String mPassword, String mRule) {
		super();
		this.mUser = mUser;
		this.mPassword = mPassword;
		this.mRule = mRule;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Login && ((Login) o).mId == mId;
	}
	
}
