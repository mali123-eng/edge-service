package com.example.edgeservice.model;

import lombok.Data;

@Data
public class Item {

	private Long id;

	private String fName;

	private String gender;

	private String email;

	private long dateOfBith;

	private String flag;

	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Object getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public Object getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getDateOfBith() {
		return dateOfBith;
	}

	public void setDateOfBith(long dateOfBith) {
		this.dateOfBith = dateOfBith;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
