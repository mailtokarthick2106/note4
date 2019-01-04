package com.stackroute.keepnote.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * The class "Category" will be acting as the data model for the Category Table in the database. 
 * Please note that this class is annotated with @Entity annotation. 
 * Hibernate will scan all package for any Java objects annotated with the @Entity annotation. 
 * If it finds any, then it will begin the process of looking through that particular 
 * Java object to recreate it as a table in your database.
 */
@Entity
@Table(name = "Category")
public class Category {
	/*
	 * This class should have six fields
	 * (categoryId,categoryName,categoryDescription,
	 * categoryCreatedBy,categoryCreationDate,notes). Out of these six fields, the
	 * field categoryId should be primary key and auto-generated. This class should
	 * also contain the getters and setters for the fields along with the no-arg ,
	 * parameterized constructor and toString method. The value of
	 * categoryCreationDate should not be accepted from the user but should be
	 * always initialized with the system date. annotate notes field with @OneToMany
	 * and @JsonIgnore
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryId")
	private int categoryId;
	@Column(name = "categoryName")
	private String categoryName;
	@Column(name = "categoryDescription")
	private String categoryDescription;
	@Column(name = "categoryCreatedBy")
	private String categoryCreatedBy;
	@Column(name = "categoryCreationDate")
	private Date categoryCreationDate;
	@JsonIgnore
	@OneToMany(mappedBy="category")
	List<Note> notes;
	public Category() {

	}

	public Category(int categoryId, String categoryName, String categoryDescription, 
			Date categoryCreationDate, String categoryCreatedBy, List<Note> notes) {
		this.categoryId=categoryId;
		this.categoryName=categoryName;
		this.categoryDescription=categoryDescription;
		this.categoryCreationDate=categoryCreationDate;
		this.categoryCreatedBy=categoryCreatedBy;
		this.notes=notes;

	}

	public void setCategoryId(int categoryId) {

	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName=categoryName;

	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription=categoryDescription;

	}

	public void setCategoryCreationDate(Date categoryCreationDate) {
		this.categoryCreationDate=categoryCreationDate;

	}

	public void setCategoryCreatedBy(String categoryCreatedBy) {
		this.categoryCreatedBy=categoryCreatedBy;

	}

	public void setNotes(List<Note> notes) {
		this.notes=notes;

	}

	public String getCategoryCreatedBy() {
		return categoryCreatedBy;
	}

	public Date getCategoryCreationDate() {
		return categoryCreationDate;
	}

	public List<Note> getNotes() {
		return notes;
	}
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) return false;
	    if (!(obj instanceof Category))
	        return false;
	    if (obj == this)
	        return true;
	    return this.getCategoryId() == ((Category) obj).getCategoryId();
	}

}