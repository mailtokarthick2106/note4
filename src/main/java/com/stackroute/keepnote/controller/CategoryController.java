package com.stackroute.keepnote.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.service.CategoryService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class CategoryController {

	/*
	 * Autowiring should be implemented for the CategoryService. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword
	 */
	@Autowired
	CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;

	}

	/*
	 * Define a handler method which will create a category by reading the
	 * Serialized category object from request body and save the category in
	 * category table in database. Please note that the careatorId has to be
	 * unique and the loggedIn userID should be taken as the categoryCreatedBy
	 * for the category. This handler method should return any one of the status
	 * messages basis on different situations: 1. 201(CREATED - In case of
	 * successful creation of the category 2. 409(CONFLICT) - In case of
	 * duplicate categoryId 3. 401(UNAUTHORIZED) - If the user trying to perform
	 * the action has not logged in.
	 * 
	 * This handler method should map to the URL "/category" using HTTP POST
	 * method".
	 */
	@PostMapping(value = "/category")
	public ResponseEntity<Void> registerCategory(@RequestBody Category category) {
		Category currentCategory;

		try {
			currentCategory = categoryService.getCategoryById(category.getCategoryId());

			if (currentCategory != null) {
				System.out.println("A currentCategory with name " + currentCategory.getCategoryId() + " already exist");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}

			boolean insertStatus = categoryService.createCategory(category);
			if (!insertStatus) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	/*
	 * Define a handler method which will delete a category from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the category deleted successfully
	 * from database. 2. 404(NOT FOUND) - If the category with specified
	 * categoryId is not found. 3. 401(UNAUTHORIZED) - If the user trying to
	 * perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/category/{id}" using HTTP
	 * Delete method" where "id" should be replaced by a valid categoryId
	 * without {}
	 */
	@DeleteMapping("/category/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
		System.out.println("Fetching & Deleting category with id " + id);
		Category category;

		try {
			category = this.categoryService.getCategoryById(id);

			if (category == null) {
				System.out.println("Unable to delete. category with id " + id + " not found");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
			boolean status = this.categoryService.deleteCategory(id);
			if (!status) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Void>(HttpStatus.OK);

	}

	/*
	 * Define a handler method which will update a specific category by reading
	 * the Serialized object from request body and save the updated category
	 * details in a category table in database handle CategoryNotFoundException
	 * as well. please note that the loggedIn userID should be taken as the
	 * categoryCreatedBy for the category. This handler method should return any
	 * one of the status messages basis on different situations: 1. 200(OK) - If
	 * the category updated successfully. 2. 404(NOT FOUND) - If the category
	 * with specified categoryId is not found. 3. 401(UNAUTHORIZED) - If the
	 * user trying to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/category/{id}" using HTTP PUT
	 * method.
	 */
	@PutMapping("/category/{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category,
			HttpSession session) {
		Category currentCategory = null;
		try {
			currentCategory = categoryService.getCategoryById(id);

			if (currentCategory == null) {
				System.out.println("User with note id " + id + " not found");
				return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
			}
			if (session != null) {
				String userId = (String) session.getAttribute("loggedInUserId");

				if (currentCategory.getCategoryCreatedBy().equalsIgnoreCase(userId)) {
					category.setCategoryCreationDate(new Date());
					currentCategory = categoryService.updateCategory(category, id);
				} else {
					//if (currentCategory == null)
						return new ResponseEntity<Category>(HttpStatus.UNAUTHORIZED);
				}
			}
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Category>(currentCategory, HttpStatus.OK);

	}

	/*
	 * Define a handler method which will get us the category by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the category found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not
	 * logged in.
	 * 
	 * 
	 * This handler method should map to the URL "/category" using HTTP GET
	 * method
	 */
	@GetMapping("/category")
	public ResponseEntity<List<Category>> getCategory(HttpSession session) {
		if (session == null) {
			return new ResponseEntity<List<Category>>(HttpStatus.UNAUTHORIZED);
		}
		List<Category> categorys = null;
		String userId = (String) session.getAttribute("loggedInUserId");
		categorys = categoryService.getAllCategoryByUserId(userId);
		if (categorys == null) {
			return new ResponseEntity<List<Category>>(HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<List<Category>>(categorys, HttpStatus.OK);
	}

}