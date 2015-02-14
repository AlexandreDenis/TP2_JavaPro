package fr.isima.tp2javapro.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.isima.creationannotation.annotations.EJB;
import com.isima.creationannotation.container.EJBContainer;

import fr.isima.tp2javapro.datas.Row;
import fr.isima.tp2javapro.ejbs.IMarksManager;

@ManagedBean
@ViewScoped
public class StatManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private IMarksManager mManager;
	
	// données
	private List<Row> mRows;
	private String filiere;
	private BarChartModel moyenneModel;
	private BarChartModel moyenneEleveModel;
	
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

	public BarChartModel getMoyenneModel() {
		return moyenneModel;
	}

	public void setMoyenneModel(BarChartModel moyenneModel) {
		this.moyenneModel = moyenneModel;
	}

	public BarChartModel getMoyenneEleveModel() {
		return moyenneEleveModel;
	}

	public void setMoyenneEleveModel(BarChartModel moyenneEleveModel) {
		this.moyenneEleveModel = moyenneEleveModel;
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
		
		// récupère la liste des notes pour la filière donnée
		mRows = mManager.getRows(filiereToLoad);
		
		// créé le graphique des moyennes par matière
		initBarModels();
		createBarModels();
		
		return "";
	}
	
	private void initBarModels(){
		// moyenne par matière
		moyenneModel = new BarChartModel();
		HashMap<String, Double> moyennes = getMoyennes();
		
		ChartSeries moyennesChart = new ChartSeries();
		moyennesChart.setLabel("Moyennes par matière");
		
		Set cles = moyennes.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
		   String cle = (String)it.next();
		   Double currMoyenne = (Double)moyennes.get(cle);
		   moyennesChart.set(cle, currMoyenne);
		}
		
		moyenneModel.addSeries(moyennesChart);
		
		// moyenne par élève
		moyenneEleveModel = new BarChartModel();
		HashMap<String, Double> moyennesEleve= getMoyennesEleve();
		
		ChartSeries moyennesEleveChart = new ChartSeries();
		moyennesEleveChart.setLabel("Moyennes par élève");
		
		Set cles2 = moyennesEleve.keySet();
		Iterator it2 = cles2.iterator();
		while (it2.hasNext()){
		   String cle2 = (String)it2.next();
		   Double currMoyenne = (Double)moyennesEleve.get(cle2);
		   moyennesEleveChart.set(cle2, currMoyenne);
		}
		
		moyenneEleveModel.addSeries(moyennesEleveChart);
	}
	
	private HashMap<String, Double> getMoyennes(){
		HashMap<String, Double> res = new HashMap<String, Double>();
		
		HashMap<String, Double> sum = new HashMap<String, Double>();
		HashMap<String, Integer> nb = new HashMap<String, Integer>();
		
		// on récupère le sum et le nb
		for(Row row : mRows){
			String matiere = row.getmMatiere();
			
			if(!sum.containsKey(matiere)){
				sum.put(matiere, Double.valueOf(0));
			}
			
			if(!nb.containsKey(matiere)){
				nb.put(matiere, Integer.valueOf(0));
			}
			
			// sum
			sum.put(matiere, sum.get(matiere) + row.getmNote());
			
			// nb
			nb.put(matiere, nb.get(matiere) + 1);
		}
		
		// calcul des moyennes
		Set cles = sum.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
		   String cle = (String)it.next();
		   Double currSum = (Double)sum.get(cle);
		   Integer currNb = (Integer)nb.get(cle);
		   Double currMoyenne = currSum / currNb;
		   
		   res.put(cle, currMoyenne);
		}
		
		return res;
	}
	
	private HashMap<String, Double> getMoyennesEleve(){
		HashMap<String, Double> res = new HashMap<String, Double>();
		
		HashMap<String, Double> sum = new HashMap<String, Double>();
		HashMap<String, Integer> nb = new HashMap<String, Integer>();
		
		// on récupère le sum et le nb
		for(Row row : mRows){
			String eleve = row.getmPrenom() + " " + row.getmNom();
			
			if(!sum.containsKey(eleve)){
				sum.put(eleve, Double.valueOf(0));
			}
			
			if(!nb.containsKey(eleve)){
				nb.put(eleve, Integer.valueOf(0));
			}
			
			// sum
			sum.put(eleve, sum.get(eleve) + row.getmNote());
			
			// nb
			nb.put(eleve, nb.get(eleve) + 1);
		}
		
		// calcul des moyennes
		Set cles = sum.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
		   String cle = (String)it.next();
		   Double currSum = (Double)sum.get(cle);
		   Integer currNb = (Integer)nb.get(cle);
		   Double currMoyenne = currSum / currNb;
		   
		   res.put(cle, currMoyenne);
		}
		
		return res;
	}
	
	private void createBarModels(){
		// moyennes par matière
        moyenneModel.setTitle("Moyennes par matière");
        moyenneModel.setLegendPosition("ne");
         
        Axis xAxis = moyenneModel.getAxis(AxisType.X);
        xAxis.setLabel("Matière");
         
        Axis yAxis = moyenneModel.getAxis(AxisType.Y);
        yAxis.setLabel("Note");
        yAxis.setMin(0);
        yAxis.setMax(20);
        
        // moyenne par élève
        moyenneEleveModel.setTitle("Moyennes par élève");
        moyenneEleveModel.setLegendPosition("ne");
         
        Axis xAxis2 = moyenneEleveModel.getAxis(AxisType.X);
        xAxis2.setLabel("Elève");
         
        Axis yAxis2 = moyenneEleveModel.getAxis(AxisType.Y);
        yAxis2.setLabel("Note");
        yAxis2.setMin(0);
        yAxis2.setMax(20);
	}
}
