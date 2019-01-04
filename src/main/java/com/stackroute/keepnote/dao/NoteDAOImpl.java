package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Note;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository("noteDAO")
@Transactional
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;
	public NoteDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new note
	 */
	
	public boolean createNote(Note note) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			int count=(Integer)session.save(note);
			tx.commit();
			if (count > 0) {
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
	 * Remove an existing note
	 */
	
	public boolean deleteNote(int noteId) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			int count = session.createQuery("DELETE FROM Note WHERE noteId = " + noteId).executeUpdate();
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
	 * Retrieve details of all notes by userId
	 */
	
	@SuppressWarnings("deprecation")
	public List<Note> getAllNotesByUserId(String userId) {
		return (List<Note>) sessionFactory.getCurrentSession().createCriteria(Note.class,userId).list();

	}

	/*
	 * Retrieve details of a specific note
	 */
	
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		System.out.println(noteId + "check all the level");
		Note note = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			note = (Note) session.get(Note.class, noteId);
			if(note==null){
				throw new NoteNotFoundException("Note was not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NoteNotFoundException("Note was not found");
		} finally {
			
			  if (session != null && session.isOpen()) { //session.flush();
			  session.clear(); session.close(); }
			 
		}
		return note;

	}

	/*
	 * Update an existing note
	 */

	public boolean UpdateNote(Note note) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(note);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}


}
