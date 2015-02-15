package fr.isima.tp2javapro.ejbs;

import java.util.ArrayList;
import java.util.List;

import com.isima.creationannotation.annotations.PersistenceContext;
import com.isima.creationannotation.annotations.Stateless;
import com.isima.creationannotation.container.EntityManager;

import fr.isima.tp2javapro.datas.Row;

@Stateless
public class MarksManager implements IMarksManager{
	
	@PersistenceContext
	EntityManager mEntityManager;
	
	public int importCVSFile(int filiere, String fileContent){
		Row newRow;
		String[] lines = fileContent.split("\n");
		
		// parsage du fichier .csv
		for(String line : lines){
			String[] fields = line.split(";");
			if(fields.length >= 4){
				String nom = fields[0];
				String prenom = fields[1];
				String matiere = fields[2];
				Double note = Double.parseDouble(fields[3]);
				newRow = new Row(filiere, nom, prenom, matiere, note);
				mEntityManager.persist(filiere, newRow);
				//System.out.println(filiere + " : " + newRow);
			}
		}
		
		return 0;
	}
	
	@Override
	public List<Row> getRows(int filiere) {
		List<Row> res = new ArrayList<Row>();
		List<Object> objs = mEntityManager.get(filiere);
		
		if(objs != null){
			for(Object obj : objs){
				res.add((Row)obj);
			}
		}
		
		return res;
	}
	
	@Override
	public void addRow(int filiere, Row row) {
		mEntityManager.persist(filiere, row);
	}
	
	@Override
	public List<String> getMatieres(int filiere) {
		List<String> res = new ArrayList<String>();
		
		List<Row> rows = getRows(filiere);
		
		for(Row r : rows){
			if(!res.contains(r.getmMatiere())){
				res.add(r.getmMatiere());
			}
		}
		
		return res;
	}
	
	@Override
	public List<String> getEleves(int filiere) {
		List<String> res = new ArrayList<String>();
		
		List<Row> rows = getRows(filiere);
		
		for(Row r : rows){
			String eleve = r.getmPrenom() + " " + r.getmNom();
			if(!res.contains(eleve)){
				res.add(eleve);
			}
		}
		
		return res;
	}
}
