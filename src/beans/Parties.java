package beans;

import java.util.Collection;
import java.util.Date;

public class Parties {

    String DesignationP ;
    String Tache;
    Date DTE;
    String user;
    Collection equipe;
    /**
     * @return the designationP
     */
    public String getDesignationP() {
        return DesignationP;
    }
    /**
     * @param designationP the designationP to set
     */
    public void setDesignationP(String designationP) {
        DesignationP = designationP;
    }
    /**
     * @return the tache
     */
    public String getTache() {
        return Tache;
    }
    /**
     * @param tache the tache to set
     */
    public void setTache(String tache) {
        Tache = tache;
    }
    /**
     * @return the dTE
     */
    public Date getDTE() {
        return DTE;
    }
    /**
     * @param dTE the dTE to set
     */
    public void setDTE(Date dTE) {
        DTE = dTE;
    }
    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }
    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * @return the equipe
     */
    public Collection getEquipe() {
        return equipe;
    }
    /**
     * @param equipe the equipe to set
     */
    public void setEquipe(Collection equipe) {
        this.equipe = equipe;
    }
    
    
}
