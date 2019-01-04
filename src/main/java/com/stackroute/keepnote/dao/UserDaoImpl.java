package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository("userDAO")
@Transactional
public class UserDaoImpl implements UserDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;

	public UserDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new user
	 */

	public boolean registerUser(User user) {

		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			String userId = (String) session.save(user);
			System.out.println(userId + "Register checked");
			tx.commit();
			if (userId != null) {
				return true;
			}
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
	 * Update an existing user
	 */

	public boolean updateUser(User user) {

		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(user);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//throw e;
		}
		return false;

	}

	/*
	 * Retrieve details of a specific user
	 */
	public User getUserById(String UserId) {

		System.out.println(UserId + "getUserById all the level");
		User user = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			user = session.get(User.class, UserId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (session != null && session.isOpen()) { // session.flush();
				session.clear();
				session.close();
			}

		}
		return user;

	}

	/*
	 * validate an user
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		Session session = null;
		boolean userFound = false;
		try {
			session = sessionFactory.openSession();
			// session.beginTransaction();
			Query<User> query = session
					.createQuery("FROM User WHERE userId = '" + userId + "'  AND userPassword='" + password + "'");
			// session.getTransaction().commit();
			List<User> users = query.list();
			if ((users != null) && (users.size() > 0)) {
				userFound = true;

			} else {
				throw new UserNotFoundException("User is not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserNotFoundException("User is not found");
		} finally {
			if (session != null && session.isOpen()) {
				// session.flush();
				session.clear();
				session.close();
			}
		}
		return userFound;

	}

	/*
	 * Remove an existing user
	 */
	public boolean deleteUser(String userId) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			int count = session.createQuery("DELETE FROM User WHERE userId = '" + userId + "'").executeUpdate();
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

}
