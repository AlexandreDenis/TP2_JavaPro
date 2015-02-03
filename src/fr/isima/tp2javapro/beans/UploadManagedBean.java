package fr.isima.tp2javapro.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import com.isima.creationannotation.annotations.EJB;
import com.isima.creationannotation.container.EJBContainer;

import fr.isima.tp2javapro.ejbs.IMarksManager;

@ManagedBean
public class UploadManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private IMarksManager mManager;
	
	private String filiere;
	private Part file;
	
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
				EJBContainer.getInstance().manage(this);
			}
			
			// on persiste les données importées
			mManager.importCVSFile(fileContent);
			
			return "main.xhtml";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur : ", "Veuillez import un fichier."));
		}
		
		return null;
	}
}
