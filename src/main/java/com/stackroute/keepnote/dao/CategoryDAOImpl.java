package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository("categoryDAO")
@Transactional
public class CategoryDAOImpl implements CategoryDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;
	public CategoryDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new category
	 */
	public boolean createCategory(Category category) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			int count=(Integer)session.save(category);
			if (count > 0) {
				return true;
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

			if (session != null && session.isOpen()) { // session.flush();
				session.clear();
				session.close();
			}

		}
		return false;


	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			int count = session.createQuery("DELETE FROM Category WHERE categoryId = " + categoryId).executeUpdate();
			session.getTransaction().commit();
			if (count > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				// session.flush();
				session.clear();
				session.close();
			}
		}
		return false;


	}
	/*
	 * Update an existing category
	 */

	public boolean updateCategory(Category category) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(category);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	/*
	 * Retrieve details of a specific category
	 */

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {
		System.out.println(categoryId + "<<<>>>check all the level");
		Category category = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			category = (Category) session.get(Category.class, categoryId);
			if(category==null){
				throw new CategoryNotFoundException("Category is not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CategoryNotFoundException("Category is not found");
			
		} finally {
			
			  if (session != null && session.isOpen()) { //session.flush();
			  session.clear(); session.close(); }
			 
		}
		return category;

	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		return (List<Category>) sessionFactory.getCurrentSession().createCriteria(Category.class,userId).list();

	}

}
