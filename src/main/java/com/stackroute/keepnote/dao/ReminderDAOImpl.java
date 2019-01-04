package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository("reminderDAO")
@Transactional
public class ReminderDAOImpl implements ReminderDAO {
	
	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;
	public ReminderDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new reminder
	 */

	public boolean createReminder(Reminder reminder) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			int count=(Integer)session.save(reminder);
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
	 * Update an existing reminder
	 */

	public boolean updateReminder(Reminder reminder) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(reminder);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/*
	 * Remove an existing reminder
	 */
	
	public boolean deleteReminder(int reminderId) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			int count = session.createQuery("DELETE FROM Reminder WHERE reminderId = " + reminderId).executeUpdate();
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
	 * Retrieve details of a specific reminder
	 */
	
	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {
		System.out.println(reminderId + "reminderId check all the level");
		Reminder reminder = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			reminder = (Reminder) session.get(Reminder.class, reminderId);
			if(reminder==null){
				throw new ReminderNotFoundException("Reminder is not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReminderNotFoundException("Reminder is not found");
		} finally {
			
			  if (session != null && session.isOpen()) { //session.flush();
			  session.clear(); session.close(); }
			 
		}
		return reminder;


	}

	/*
	 * Retrieve details of all reminders by userId
	 */
	
	public List<Reminder> getAllReminderByUserId(String userId) {
		return (List<Reminder>) sessionFactory.getCurrentSession().createCriteria(Reminder.class,userId).list();

	}


}
