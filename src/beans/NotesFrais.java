package beans;

import java.util.Collection;
import java.util.Date;

import com.axemble.vdoc.sdk.interfaces.IAttachment;

public class NotesFrais {
	/* ========================================= *
	 * @Plateform :VDoc 15.0
	 * @author r.sabri
	 * @Creation_date :07/02/2017 15h17
	 * ========================================= */
	
	// Les attrubuts de tableaux note de frais
	String Designation;
	float Montant;
	float Qte;
	Date dateJustif;
	Collection<IAttachment> PJ;
	Float Total;
	String Validate;
	String Reception;
	String Statut;
	/**
     * @return the reception
     */
    public String getReception() {
        return Reception;
    }
    /**
     * @param reception the reception to set
     */
    public void setReception(String reception) {
        Reception = reception;
    }
    /**
     * @return the validate
     */
    public String getValidate() {
        return Validate;
    }
    /**
     * @param validate the validate to set
     */
    public void setValidate(String validate) {
        Validate = validate;
    }
    // Getters & Setters 
	public String getDesignation() {
		return Designation;
	}
	public void setDesignation(String designation) {
		Designation = designation;
	}
	public float getMontant() {
		return Montant;
	}
	public void setMontant(float montant) {
		Montant = montant;
	}
	public float getQte() {
		return Qte;
	}
	public void setQte(float qte) {
		Qte = qte;
	}
	public Date getDateJustif() {
		return dateJustif;
	}
	public void setDateJustif(Date dateJustif) {
		this.dateJustif = dateJustif;
	}
	public Collection<IAttachment> getPJ() {
		return PJ;
	}
	public void setPJ(Collection<IAttachment> pJ) {
		PJ = pJ;
	}
	public Float getTotal() {
		return Total;
	}
	public void setTotal(Float total) {
		Total = total;
	}
    /**
     * @return the statut
     */
    public String getStatut() {
        return Statut;
    }
    /**
     * @param statut the statut to set
     */
    public void setStatut(String statut) {
        Statut = statut;
    }
	
}
