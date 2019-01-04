package com.stackroute.keepnote.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * The class "User" will be acting as the data model for the User Table in the database. 
 * Please note that this class is annotated with @Entity annotation. 
 * Hibernate will scan all package for any Java objects annotated with the @Entity annotation. 
 * If it finds any, then it will begin the process of looking through that particular 
 * Java object to recreate it as a table in your database.
 */
@Entity
@Table(name = "User")
public class User {

	/*
	 * This class should have five fields (userId,userName,
	 * userPassword,userMobile,userAddedDate). Out of these five fields, the field
	 * userId should be the primary key. This class should also contain the getters
	 * and setters for the fields, along with the no-arg , parameterized constructor
	 * and toString method.The value of userAddedDate should not be accepted from
	 * the user but should be always initialized with the system date
	 */
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId")
	private String userId;
	@Column(name = "userName")
	private String userName;
	@Column(name = "userPassword")
	private String userPassword;
	@Column(name = "userMobile")
	private String userMobile;
	@Column(name = "userAddedDate")
	private Date userAddedDate;

	public User() {

	}

	public User(String userId, String userName, String userPassword,
			String userMobile , Date userAddedDate) {
		this.userId=userId;
		this.userName=userName;
		this.userPassword=userPassword;
		this.userMobile=userMobile;
		this.userAddedDate=userAddedDate;
				

	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
	this.userId=userId;

	}

	public void setUserName(String userName) {
		this.userName=userName;

	}

	public String getUserPassword() {
		return this.userPassword;

	}

	public void setUserPassword(String userPassword) {
		this.userPassword=userPassword;

	}

	public String getUserName() {
		return userName;
	}

	public Date getUserAddedDate() {
		return userAddedDate;
	}

	public String getUserMobile() {
		return userMobile;

	}

	public void setUserMobile(String userMobile) {
		this.userMobile=userMobile;

	}

	public void setUserAddedDate(Date userAddedDate) {
		this.userAddedDate=userAddedDate;

	}
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) return false;
	    if (!(obj instanceof User))
	        return false;
	    if (obj == this)
	        return true;
	    return this.getUserId() == ((User) obj).getUserId();
	}}
