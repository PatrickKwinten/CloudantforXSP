package org.quintessens.model;

import java.io.Serializable;
import java.util.Date;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;

import org.quintessens.app.CloudantController;
import org.quintessens.comments.utils.JSFUtil;

public class Company implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Date created;
	private String _id;
	private String _rev;
	public String get_rev() {
		return _rev;
	}


	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	private String name;
	private String branche;
	public String from;
	private Integer employees;
	
	public Integer getEmployees() {
		return employees;
	}


	public void setEmployees(Integer employees) {
		this.employees = employees;
	}

	private boolean newNote;
	private boolean readOnly;
	
	public Company(){ 
		//System.out.println("Company.java constructor");
		//System.out.println(className());
	}
	
	
	public void clear() {		
		created = null;
		name = null;
		branche = null;
		employees = null;
	}
	
	public void create() throws NotesException{	
		setNewNote(true);
		setReadOnly(false);
		from = JSFUtil.getCurrentUser().getCanonical();
		CloudantController cloudant = new CloudantController();
		cloudant.connect("companies");		
		Date date = new Date();
		this.created = date;		
		cloudant.saveDocument(this);
		this.clear();
	}
	
	public void save(String dbName) throws NotesException {
		try {
			JSFUtil jsfUtil = new JSFUtil();
			Session session = jsfUtil.getSession();
			Database db = session.getDatabase("", dbName);
			Document doc = null;		
			if (newNote) {
				// True means never been saved				
				doc = db.createDocument();
				doc.replaceItemValue("form", "company");
			} else {	
				doc = db.getDocumentByUNID(_id);
			}		

			// Common elements to save
			
			doc.replaceItemValue("From", from);
			doc.replaceItemValue("name", name);
			doc.replaceItemValue("branche", branche);
			doc.computeWithForm(true, true);
			doc.save();
			
			doc.recycle();
			db.recycle();
			readOnly = true;
		}catch (NotesException e) {
		// ??
		}	
	}
	
	public Company load(Document doc) throws NotesException{
		if (null == doc){
			//item not found
			//set valid prop to false or something
		}
		else{
			if (loadValues(doc)){
				////set valid prop to true or something
			}
		}
		
		return this;
	}
	
	public boolean loadValues(Document doc) throws NotesException {
		name = doc.getItemValueString("name");
		branche = doc.getItemValueString("branche");
		employees = doc.getItemValueInteger("employees");		
		return true;
	}

	
	private String className(){
		return this.getClass().getSimpleName().toString();
	}

	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}


	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getBranche() {
		return branche;
	}


	public void setBranche(String branche) {
		this.branche = branche;
	}


	public boolean isNewNote() {
		return newNote;
	}


	public void setNewNote(boolean newNote) {
		this.newNote = newNote;
	}


	public boolean isReadOnly() {
		return readOnly;
	}


	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}
	
	
	
	
}
