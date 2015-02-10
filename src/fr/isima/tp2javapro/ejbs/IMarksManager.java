package fr.isima.tp2javapro.ejbs;

import java.util.List;

import fr.isima.tp2javapro.datas.Row;

public interface IMarksManager {
	int importCVSFile(int filiere, String fileContent);
	List<Row> getRows(int filiere);
}
