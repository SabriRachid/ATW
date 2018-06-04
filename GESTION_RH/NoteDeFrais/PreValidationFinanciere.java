package NoteDeFrais;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.serviceRH.EncryptionFile;

import NoteDeFrais.FileManager;
import NoteDeFrais.GenererPDF;
import dao.ConnexionBDD;
import dao.SingletonConnexionBDD;

public class PreValidationFinanciere extends ConnexionBDD{

    /* ========================================= *
     * @Plateform :VDoc 14.2
     * @author r.sabri
     * @Creation_date :07/02/2017 15h17
     * ========================================= */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(PreValidationFinanciere.class);
    private static final DecimalFormat DFORMAT = new DecimalFormat("###0.00");
    private static final NumberFormat FORMATTER = new RuleBasedNumberFormat(RuleBasedNumberFormat.SPELLOUT);
    // ---------------------------------------------------------------------------------------------------------------
    // ONAFTERLOAD
    // ---------------------------------------------------------------------------------------------------------------
    public boolean onAfterLoad() {
        getNoteTableValidationFinancier();
        return super.onAfterLoad();
    }
    // ---------------------------------------------------------------------------------------------------------------
    // GETNOTETABLEVALIDATIONFINANCIER
    // ---------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void getNoteTableValidationFinancier()
    {
        cnx =null;
        st = null;
        rs = null;
        try {
            String filiale= null;
            //String NumEmploye = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
            String loginEmploye = getWorkflowModule().getLoggedOnUser().getLogin();
            // Le numéro '458709' => le matricule de a.wafir
            if (loginEmploye.equals(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC"))) {
                filiale="Attijari Finances Corporate";
            }else {
                filiale="Attijari Intermédiation";
            }
            // DÉFINITION DE LA CONNEXION
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            if(((Collection) getWorkflowInstance().getLinkedResources("RNF_Tab_RemboursementNoteFrais")).size()==0)
            {
                String query ="SELECT SUM(Qte_NF*Mt_NF) as 'Montant',Demandeur_NF,Ref_NF,matricule,RIB,Filiale_NF,TypeMission " + 
                        "FROM NotesFrais,Personnel WHERE matricule=PersonnelMatricule " + 
                        "AND Recept_NF='Oui' and Rembourcer_NF='Non' and NotesFrais.Statut ='Validé' and Filiale_NF=?  and DATEDIFF(dd,DateNF,GETDATE())<15 " + 
                        "GROUP BY Demandeur_NF,Ref_NF,matricule,RIB,Filiale_NF,TypeMission";
                st = cnx.prepareStatement(query);
                st.setString(1, filiale);
                rs = st.executeQuery();
                while (rs.next()) {
                    // CRÉATION D'UNE LIGNE 
                    ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource("RNF_Tab_RemboursementNoteFrais");
                    //POSITIONNEMENT DE QUELQUES VALEURS 
                    linkedResource.setValue("RNF_Ref",rs.getString("Ref_NF")); 
                    linkedResource.setValue("RNF_Demandeur",rs.getString("Demandeur_NF"));
                    linkedResource.setValue("RNF_Montant",rs.getFloat("Montant"));  
                    linkedResource.setValue("RNF_Matricule",rs.getString("matricule"));
                    linkedResource.setValue("RNF_RIB",rs.getString("RIB"));
                    linkedResource.setValue("RNF_TypeMission",rs.getString("TypeMission")); 
                    // AJOUT DE LA LIGNE AU TABLEAU
                    getWorkflowInstance().addLinkedResource( linkedResource );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS-ERROR IN GETNOTTABLEVALIDATION  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
           // ConnexionBDD.close(cnx, st,rs);
        }

    }
    // ---------------------------------------------------------------------------------------------------------------
    // ONPROPERTYCHANGED
    // ---------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    @Override
    public void onPropertyChanged(IProperty property) {
        if (property.getName().equals("RNF_Tab_RemboursementNoteFrais"))
        {
            //getResourceController().showBodyBlock("FragOrders", false);
            float total =0f;
            List RNF = (List) getWorkflowInstance().getLinkedResources("RNF_Tab_RemboursementNoteFrais");
            if (RNF.size() != 0)
            {
                //getResourceController().showBodyBlock("FragOrders", true);
//                getResourceController().setHidden("RNF_OrderPaiement", true);
//                getResourceController().setHidden("RNF_OrderVirement", true);
            }else {
//                getResourceController().showBodyBlock("FragOrders", false);
            }
            // INITIALISER LES CHAMPS PIÉCES JOINTE
            List<IAttachment> docOP = (List<IAttachment>) getWorkflowInstance().getValue("RNF_OrderPaiement");
            docOP.clear();
            List<IAttachment> docOV = (List<IAttachment>) getWorkflowInstance().getValue("RNF_OrderVirement");
            docOV.clear();
        }
        super.onPropertyChanged(property);
    }
    // ---------------------------------------------------------------------------------------------------------------
    // ISONCHANGESUBSCRIPTIONON
    // ---------------------------------------------------------------------------------------------------------------
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if (property.getName().equals("RNF_Tab_RemboursementNoteFrais"))
        {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }
    // ---------------------------------------------------------------------------------------------------------------
    // AFTER SUBMIT
    // ---------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterSubmit(IAction action) {
        if(action.getName().equals("vALIDER"))
        {
            cnx = null;
            st = null;
            try {
                String filiale= null;
                //String NumEmploye = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
                String loginEmploye = getWorkflowModule().getLoggedOnUser().getLogin();
                // Le numéro '458709' => le matricule de a.wafir
                if (loginEmploye.equals(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC"))) {
                    filiale="Attijari Finances Corporate";
                }else {
                    filiale="Attijari Intermédiation";
                }
                // MISE À JOURS DES NOTES DE FRAIS REMBOURSÉES
                Collection associ = (Collection) getWorkflowInstance().getLinkedResources("RNF_Tab_RemboursementNoteFrais");
                if (associ.size() != 0) {
                    for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        // DÉFINITION DE LA CONNEXION
                        cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
                        /**String Query = "UPDATE NotesFrais SET Rembourcer_NF= ?\r\n" + 
                                "  WHERE Ref_NF = ? and Recept_NF='Oui' and Rembourcer_NF='Standby' and Filiale_NF=?  and DATEDIFF(dd,DateNF,GETDATE())<15";
                         **/
                        String Query = "UPDATE NotesFrais SET Rembourcer_NF= ?\r\n" + 
                                "  WHERE Ref_NF = ? and Recept_NF='Oui' and Rembourcer_NF='Standby' and Filiale_NF=? and Statut='Validé'";
                        st = cnx.prepareStatement(Query);
                        st.setString(1,"Encours");
                        st.setString(2, (String) asso.getValue("RNF_Ref"));
                        st.setString(3, filiale);
                        st.executeUpdate();
                    }
                }
            }catch(Exception e) {
                e.getStackTrace();
                log.error("CS-ERROR IN ONAFTERSUBMIT  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
            }finally {
                // LIBÉRER RESSOURCES DE LA MÉMOIRE.
                ConnexionBDD.close(cnx, st);
            }
        }
        return super.onAfterSubmit(action);
    }
    // ---------------------------------------------------------------------------------------------------------------
    // GENERERRAPPORTIMPRESSION ORDRE PAIEMENT
    // ---------------------------------------------------------------------------------------------------------------
    public void genererRapportImpressionOP(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource asso) {
        cnx = null;
        st = null;
        try {
            String filiale= null;
            //String NumEmploye = im.getLoggedOnUser().getEmployeeNumber();
            String loginEmploye = im.getLoggedOnUser().getLogin();
            String ref=(String) iw.getValue("sys_Reference");
            cnx=ConnectionDefinition(im).getConnection();
            Map<String, Object> Para = new HashMap<String, Object>();
            FileManager path = new FileManager();
            // Le numéro '458709' => le matricule de a.wafir
            if (loginEmploye.equals(iw.getCatalog().getConfiguration().getStringUserProperty("RH_AFC"))) {
                filiale="Attijari Finances Corporate";
            }else {
                filiale="Attijari Intermédiation";
            }
            float Total = (float) iw.getValue("RNF_MT");
            // MISE À JOURS DES NOTES DE FRAIS REMBOURSÉES
            Collection associ = (Collection) iw.getLinkedResources("RNF_Tab_RemboursementNoteFrais");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    asso = (ILinkedResource) iter1.next();
                    String Query = "UPDATE NotesFrais SET Rembourcer_NF= ?, MT_Ref_Lettre=?\r\n" + 
                            "  WHERE Recept_NF='Oui' and Ref_NF=? and Filiale_NF=? and Statut='Validé'";
                    st = cnx.prepareStatement(Query);
                    float MonT = (float) asso.getValue("RNF_Montant");
                    st.setString(1,"Standby"); // ETAT DE REMBOURSEMENT DES NOTES DE FRAIS
                    st.setString(2,NumToLetter(new BigDecimal(MonT))); // CONVERTION DE MONTANT EN LETTRE 
                    st.setString(3,(String) asso.getValue("RNF_Ref")); // REFERENCE DE NOTE DE FRAIS 
                    st.setString(4, filiale);
                    st.executeUpdate();
                }
            }
            // PARAMÉTRE
            Para.put("Filiale", filiale);
            Para.put("Total", Total);
            Para.put("montantLettre", NumToLetter(new BigDecimal(Total)));
            Para.put("NumOrdre", ref);
            // GÉNÉRER L'ORDRE DE PAIEMENT
            GenererPDF.generer("OrdrePaiement", path.getOutDir(),"Ordre de Paiement"+"_"+ref, Para, cnx, iw); 
            List<IAttachment> docs = (List<IAttachment>) iw.getValue("RNF_OrderPaiement");
            // INITIALISER LE CHAMPS PIÉCE JOINTE
//            docs.clear();
            iw.setValue("RNF_OrderPaiement", new  ArrayList<IAttachment>());
            im.addAttachment(iw, "RNF_OrderPaiement", "Ordre de Paiement"+"_"+ref+".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Paiement"+"_"+ref+".pdf");
            EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Paiement"+"_"+ref+".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Paiement"+"_"+ref+".pdf");
            EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Paiement"+"_"+ref+".pdf");
            //            ir.setHidden("RNF_OrderPaiement", true);
            st.close();
            cnx.close();
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS-ERROR IN genererRapportImpression  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // ---------------------------------------------------------------------------------------------------------------
    // GENERERRAPPORTIMPRESSION ORDRE VIREMENT
    // ---------------------------------------------------------------------------------------------------------------
    public void genererRapportImpressionOV(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource asso) {
        cnx = null;
        st = null;
        try {
            String filiale= null;
            String MembreDir = null;
            String PresiDir = null;
            //String NumEmploye = im.getLoggedOnUser().getEmployeeNumber();
            String login = im.getLoggedOnUser().getLogin();
            String ref=(String) iw.getValue("sys_Reference");
            cnx=ConnectionDefinition(im).getConnection();
            Map<String, Object> Para = new HashMap<String, Object>();
            FileManager path = new FileManager();
            // Le numéro '458709' => le matricule de a.wafir
            if (login.equals(iw.getCatalog().getConfiguration().getStringUserProperty("RH_AFC"))) {
                filiale="Attijari Finances Corporate";
                MembreDir=im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("RH_AFC")).getFullName();
                PresiDir =im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("DG_AFC")).getFullName();
            }else {
                filiale="Attijari Intermédiation";
                MembreDir=im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("RH_ATI")).getFullName();
                PresiDir =im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("DG_ATI")).getFullName();
            }
            float Total = (float) iw.getValue("RNF_MT");
            // MISE À JOURS DES NOTES DE FRAIS REMBOURSÉES
            Collection associ = (Collection) iw.getLinkedResources("RNF_Tab_RemboursementNoteFrais");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    asso = (ILinkedResource) iter1.next();
                    String Query = "UPDATE NotesFrais SET Rembourcer_NF= ?, MT_Ref_Lettre=?\r\n" + 
                            "  WHERE Recept_NF='Oui' and Ref_NF=? and Filiale_NF=? and Statut='Validé'";
                    st = cnx.prepareStatement(Query);
                    float MonT = (float) asso.getValue("RNF_Montant");
                    st.setString(1,"Standby"); // ETAT DE REMBOURSEMENT DES NOTES DE FRAIS
                    st.setString(2,NumToLetter(new BigDecimal(MonT))); // CONVERTION DE MONTANT EN LETTRE 
                    st.setString(3,(    String) asso.getValue("RNF_Ref")); // REFERENCE DE NOTE DE FRAIS 
                    st.setString(4, filiale);
                    st.executeUpdate();
                }
            }
            // PARAMÉTRE
            Para.put("Filiale", filiale);
            Para.put("Rembourser", "Standby");
            Para.put("Total", Total);
            Para.put("MbrDirec", MembreDir);
            Para.put("PresiDirec", PresiDir);
            // GÉNÉRER L'ORDRE DE VIREMENT
            GenererPDF.generer("OrderDeVirement", path.getOutDir(),"Ordre de Virement"+"_"+ref, Para, cnx, iw); 
            List<IAttachment> docs = (List<IAttachment>) iw.getValue("RNF_OrderVirement");
            // INITIALISER LE CHAMPS PIÉCE JOINTE
            iw.setValue("RNF_OrderVirement", new ArrayList<IAttachment>());
            im.addAttachment(iw, "RNF_OrderVirement", "Ordre de Virement"+"_"+ref+".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Virement"+"_"+ref+".pdf");
            EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Virement"+"_"+ref+".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Virement"+"_"+ref+".pdf");
            EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Virement"+"_"+ref+".pdf");
            //            ir.setHidden("RNF_OrderVirement", true);
            st.close();
            cnx.close();
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS-ERROR IN genererRapportImpression  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // ---------------------------------------------------------------------------------------------------------------
    // NUMBER TO LETTER
    // ---------------------------------------------------------------------------------------------------------------
    private static String NumToLetter(BigDecimal num)
    {
        String[] s = DFORMAT.format(num).split(Pattern.quote(String.valueOf(DFORMAT.getDecimalFormatSymbols().getDecimalSeparator())));
        BigInteger intPart = new BigInteger(s[0]);
        if (s.length == 1)
        {
            return FORMATTER.format(intPart);
        }
        else
        {
            BigInteger decPart = new BigInteger(s[1]);
            return FORMATTER.format(intPart)
                    // POUR LES PARTIES FIXES IL FAUDRAIT FAIRE "RESOURCEBUNDLE"
                    + " Dh" + (intPart.intValue() > 1 ? "s" : "") + " et " + FORMATTER.format(decPart) + " ct" + (decPart.intValue() > 1 ? "s" : "");
        }
    }
    // ---------------------------------------------------------------------------------------------------------------
    // AFTER SUBMIT
    // ---------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
	@Override
    public boolean onBeforeSubmit(IAction action)
    {
    	// TODO Auto-generated method stub
    	 if(action.getName().equals("vALIDER"))
         {
    		 List<IAttachment> ordrePaiements = (List<IAttachment>) getWorkflowInstance().getValue("RNF_OrderPaiement");
    		 List<IAttachment> ordreVirements = (List<IAttachment>) getWorkflowInstance().getValue("RNF_OrderVirement");
    		 if(ordreVirements.size()==0||ordrePaiements.size()==0){
    			 getResourceController().alert("Veuillez générer l'ordre de virement!!!");
    			 return false;
    		 }
         }
    	return super.onBeforeSubmit(action);
    }
}
