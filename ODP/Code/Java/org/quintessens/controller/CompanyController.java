package org.quintessens.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lotus.domino.NotesException;

import org.quintessens.app.CloudantController;
import org.quintessens.model.Company;

public class CompanyController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Company> companies = new ArrayList<Company>();
	
	public List<Company> getCompanies() throws NotesException{
		//System.out.println(jsfUtil.getClass() + " getCompanies");
		CloudantController cloudant = new CloudantController();
		cloudant.connect("companies");	
		companies = (List<Company>) cloudant.findAllDocuments(Company.class);
		//System.out.println("number of companies found:" + companies.size());
		return companies;		
	}
	
	public void remove(String id){
		//System.out.println("remove request for id :" + id);
		CloudantController cloudant = new CloudantController();
		cloudant.connect("companies");	
		Company company = (Company) cloudant.findDocumentByID(Company.class, id);
			
		
		//System.out.println("company found, id:" + company.get_id());
		cloudant.removeDocument(company);	
	}
	
	public void update(String id, String newName, Integer newEmployees){
		System.out.println("update");
		CloudantController cloudant = new CloudantController();
		cloudant.connect("companies");	
		Company company = (Company) cloudant.findDocumentByID(Company.class, id);
		System.out.println("company found:" + company.getName());
		company.setName(newName);
		company.setEmployees(newEmployees);
		cloudant.updateDocument(company);
		System.out.println("updated");
	}
}
