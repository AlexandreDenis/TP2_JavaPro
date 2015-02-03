package fr.isima.tp2javapro.datas;

import java.io.Serializable;

public class Row implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// attributs
	private String mId;
	private int mFiliere;
	private String mNom;
	private String mPrenom;
	private String mMatiere;
	private double mNote;
	
	// constructeur
	public Row(int mFiliere, String mNom, String mPrenom, String mMatiere, double mNote) {
		super();
		this.mFiliere = mFiliere;
		this.mNom = mNom;
		this.mPrenom = mPrenom;
		this.mMatiere = mMatiere;
		this.mNote = mNote;
	}

	// getters et setters
	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public int getmFiliere() {
		return mFiliere;
	}

	public void setmFiliere(int mFiliere) {
		this.mFiliere = mFiliere;
	}

	public String getmNom() {
		return mNom;
	}

	public void setmNom(String mNom) {
		this.mNom = mNom;
	}

	public String getmPrenom() {
		return mPrenom;
	}

	public void setmPrenom(String mPrenom) {
		this.mPrenom = mPrenom;
	}

	public String getmMatiere() {
		return mMatiere;
	}

	public void setmMatiere(String mMatiere) {
		this.mMatiere = mMatiere;
	}

	public double getmNote() {
		return mNote;
	}

	public void setmNote(double mNote) {
		this.mNote = mNote;
	}
}
