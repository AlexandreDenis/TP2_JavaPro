package fr.isima.tp2javapro.ejbs;

import com.isima.creationannotation.annotations.Stateless;

@Stateless
public class MarksManager implements IMarksManager{
	public int importCVSFile(String fileContent){
		System.out.println("file content = " + fileContent);
		return 0;
	}
}
