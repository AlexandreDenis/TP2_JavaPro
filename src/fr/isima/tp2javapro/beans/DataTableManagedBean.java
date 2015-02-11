package fr.isima.tp2javapro.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;
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
     
    public void onCellEdit(CellEditEvent event) {
        /*Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }*/
    }
}
