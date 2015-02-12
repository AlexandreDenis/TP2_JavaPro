package fr.isima.tp2javapro.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import com.isima.creationannotation.annotations.EJB;
import com.isima.creationannotation.container.EJBContainer;

import fr.isima.tp2javapro.datas.Row;
import fr.isima.tp2javapro.ejbs.IMarksManager;

@ManagedBean
@ViewScoped
public class DataTableManagedBean implements Serializable{
	private static final long serialVersionUID = 1L;

	@EJB
	private IMarksManager mManager;
	
	// données
	private List<Row> mRows;
	private String filiere;
	
	// données pour l'ajout d'une ligne
	private String Nom;
	private String Prenom;
	private String Matiere;
	private String Note;
	
	private boolean NomValid = true;
	
	public List<Row> getmRows() {
		return mRows;
	}
	public void setmRows(List<Row> mRows) {
		this.mRows = mRows;
	}
	public String getFiliere() {
		return filiere;
	}
	public void setFiliere(String filiere) {
		this.filiere = filiere;
	}
	
	public String getNom() {
		return Nom;
	}
	public void setNom(String nom) {
		Nom = nom;
	}
	public String getPrenom() {
		return Prenom;
	}
	public void setPrenom(String prenom) {
		Prenom = prenom;
	}
	public String getMatiere() {
		return Matiere;
	}
	public void setMatiere(String matiere) {
		Matiere = matiere;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}

	public boolean NomValid() {
		return NomValid;
	}
	public void setNomValid(boolean isNomValid) {
		this.NomValid = isNomValid;
	}
	/**
	 * Recharge les données du tableau
	 */
	public String loadData(){
		int filiereToLoad;
		
		FacesContext context = FacesContext.getCurrentInstance();
		UploadManagedBean uploadBean 
			= (UploadManagedBean) context.getApplication()
				.evaluateExpressionGet(context,
						"#{uploadManagedBean}",
						UploadManagedBean.class);
		
		try {
			EJBContainer.getInstance().manage(this);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		if(filiere != null){
			filiereToLoad = Integer.parseInt(filiere);
		} else {
			String filiereFromUploadBean = uploadBean.getFiliere();
			if(filiereFromUploadBean != null && !filiereFromUploadBean.equals("")){
				filiereToLoad = Integer.parseInt(filiereFromUploadBean);
			} else {
				filiereToLoad = 1;	// filiere chargée par défaut
			}
		}
		
		filiere = Integer.toString(filiereToLoad);
		
		mRows = mManager.getRows(filiereToLoad);
		
		return "";
	}
	
	public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Ligne éditée", "id : " + Integer.toString(((Row) event.getObject()).getmId()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edition annulée", "id : " + Integer.toString(((Row) event.getObject()).getmId()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public String addRow(){
    	double valNote = 0;
    	
    	if(Nom == "" || Prenom == "" || Matiere == "" || Note == ""){
    		FacesMessage msg = new FacesMessage("Erreur", "Veuillez renseigner tous les champs");
            FacesContext.getCurrentInstance().addMessage(null, msg);
    	} else {
    		try{
    			valNote = Double.parseDouble(Note);
    		} catch(NumberFormatException e){
    			FacesMessage msg = new FacesMessage("Erreur", "La note renseignée doit être un nombre");
                FacesContext.getCurrentInstance().addMessage(null, msg);
    		}
    		
    		// tout s'est bien passé
    		// on peut effectuer l'enregistrement
    		Row newRow = new Row(Integer.parseInt(filiere), Nom, Prenom, Matiere, valNote);
    		
    		try {
    			EJBContainer.getInstance().manage(this);
    		} catch (Exception e){
    			e.printStackTrace();
    		}
    		
    		mManager.addRow(Integer.parseInt(filiere), newRow);
    		
    		// on remet les champs à vide
    		Nom = "";
    		Prenom = "";
    		Matiere = "";
    		Note = "";
    	}
    	
    	return loadData();
    }
}
