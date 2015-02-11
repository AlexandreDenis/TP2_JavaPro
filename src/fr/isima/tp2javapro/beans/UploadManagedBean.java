package fr.isima.tp2javapro.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import com.isima.creationannotation.annotations.EJB;
import com.isima.creationannotation.container.EJBContainer;

import fr.isima.tp2javapro.datas.Row;
import fr.isima.tp2javapro.ejbs.IMarksManager;
import fr.isima.tp2javapro.ejbs.MarksManager;

@ManagedBean
public class UploadManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private IMarksManager mManager;
	
	// données
	private String filiere;
	private List<Row> mRows;
	
	private Part file;
	
	@PostConstruct
	private void init(){
		try {
			EJBContainer.createEJBContainer(this.getClass().getClassLoader());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// getters et setters
	public String getFiliere() {
		return filiere;
	}

	public void setFiliere(String filiere) {
		this.filiere = filiere;
	}
	
	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}
	
	public List<Row> getmRows() {
		return mRows;
	}

	public void setmRows(List<Row> mRows) {
		this.mRows = mRows;
	}

	/**
	 * Upload d'un fichier .csv
	 * @return le nom de la page à laquelle accéder
	 */
	public String uploadFile() throws Exception{
		System.out.println("UPLOADFILE()");
		String fileContent = "";
		
		if(file != null){
			try {
				// on récupère le contenu du fichier uploadé
				fileContent = new Scanner(file.getInputStream())
				  .useDelimiter("\\A").next();
			} catch (IOException e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Erreur : ",
								"Problème de lecture du fichier uploadé."));
				
				e.printStackTrace();
			}
			
			if(mManager == null){
				try {
					EJBContainer.createEJBContainer(MarksManager.class.getClassLoader());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				EJBContainer.getInstance().manage(this);
			}
			
			// on persiste les données importées
			mManager.importCVSFile(Integer.parseInt(filiere), fileContent);
			
			mRows = mManager.getRows(Integer.parseInt(filiere));
			
			return "main.xhtml";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur : ", "Veuillez importer un fichier."));
		}
		
		return null;
	}
	
	/**
	 * Recharge les données du tableau
	 */
	public String loadData(){
		int filiereToLoad;
		
		try {
			EJBContainer.getInstance().manage(this);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		if(filiere != null){
			filiereToLoad = Integer.parseInt(filiere);
		} else {
			filiereToLoad = 1;	// filiere chargée par défaut
		}
		
		mRows = mManager.getRows(filiereToLoad);
		
		return "";
	}
}
