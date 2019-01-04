package com.stackroute.keepnote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.dao.CategoryDAO;
import com.stackroute.keepnote.dao.NoteDAO;
import com.stackroute.keepnote.dao.ReminderDAO;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn�t currently 
* provide any additional behavior over the @Component annotation, but it�s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service("noteService")
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteDAO,CategoryDAO,ReminderDAO.
	 * (Use Constructor-based autowiring) Please note that we should not create
	 * any object using the new keyword.
	 */
	@Autowired
	NoteDAO noteDAO;
	
	@Autowired
    ReminderDAO reminderDAO;
	
	@Autowired
    CategoryDAO categoryDAO;
	
	/*
	 * This method should be used to save a new note.
	 */

	public boolean createNote(Note note) throws ReminderNotFoundException, CategoryNotFoundException {
	/*	if(note.getCategory()==null && note.getReminder()!=null){
			throw new ReminderNotFoundException("Remainde was not found");
		}*/
		boolean insertStatus = noteDAO.createNote(note);
		if (insertStatus) {
			/*if(note.getCategory()!=null){
				System.out.println(note.getCategory().getCategoryId());
				Category currentCategory = categoryDAO.getCategoryById(note.getCategory().getCategoryId());
				if (currentCategory == null) {
					throw new CategoryNotFoundException("Category was not found"+note.getCategory().getCategoryId());
				}
			}
			*/
			return insertStatus;
		} else {
			return insertStatus;
		}

	}

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(int noteId)throws NoteNotFoundException {
		Note currentNote=noteDAO.getNoteById(noteId);
		if(currentNote==null){
			throw new NoteNotFoundException("Note was not found");
		}
		return noteDAO.deleteNote(noteId);

	}
	/*
	 * This method should be used to get a note by userId.
	 */

	public List<Note> getAllNotesByUserId(String userId) {
		return noteDAO.getAllNotesByUserId(userId);

	}

	/*
	 * This method should be used to get a note by noteId.
	 */
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		Note currentNote=noteDAO.getNoteById(noteId);
		if(currentNote==null){
			throw new NoteNotFoundException("Note was not found");
		}
		return currentNote;

	}

	/*
	 * This method should be used to update a existing note.
	 */

	public Note updateNote(Note note, int id)
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {
		Note currentNote = noteDAO.getNoteById(id);
		if (currentNote == null) {
			throw new NoteNotFoundException("Note is not found");
		}
		if(note.getReminder()!=null){
			Reminder currentReminder = reminderDAO.getReminderById(note.getReminder().getReminderId());
			if (currentReminder == null) {
				throw new ReminderNotFoundException("Reminder was not found");
			}
		}
		if(note.getCategory()!=null){
			Category currentCategory = categoryDAO.getCategoryById(note.getCategory().getCategoryId());
			if (currentCategory == null) {
				throw new CategoryNotFoundException("Category was not found");
			}
		}
		boolean updateStatus = noteDAO.UpdateNote(note);
		if (!updateStatus) {
			throw new NoteNotFoundException("Note is not found");
		}
		return note;

	}

}
