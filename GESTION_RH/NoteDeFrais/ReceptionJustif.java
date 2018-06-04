package NoteDeFrais;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;

import dao.ConnexionBDD;
import dao.SingletonConnexionBDD;
/**
 * 
 * @author M.AZOUI 
 * @Email m.azouzi@citysystem.ma
 * @Date 17/07/2017
 * @Updated_by R.SABRI
 * @version VDoc 14.2
 * @Object Mail Sender
 *
 */

public class ReceptionJustif extends ConnexionBDD{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings({
        "unchecked", "static-access"
    })
    /*
     * =================================================================================================================
     * ONBEFORESAVE
     * =================================================================================================================
     */
    @Override
    public boolean onBeforeSave() {
        cnx = null;
        st = null;
        rs = null;
        try {
            int count = 0;
            int recept = 0;
            String isReceptionne =null;
            String refVdoc=(String) getWorkflowInstance().getValue("sys_Reference");
            String query ="select COUNT(DateModifJustifO_NF) from NotesFrais where Ref_NF =?";
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            st = cnx.prepareStatement(query);
            st.setString(1, refVdoc);
            rs= st.executeQuery();
            while (rs.next()) count = rs.getInt(1);
            // Date system
            Date date = new Date();
            GregorianCalendar calendar = new GregorianCalendar(); 
            calendar.setTime(date); // jj/mm/aaaa 
            String dateString = (calendar.get(calendar.DATE)+2)+""
                    + "/"+(calendar.get(calendar.MONTH)+1)+""
                    + "/"+calendar.get(calendar.YEAR);  
            ArrayList<ILinkedResource> listeNDF = (ArrayList<ILinkedResource>) getWorkflowInstance().getLinkedResources("NDF_TabValidationJutifsOriginaux");
            
            
            
            String msg = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>"+
            		"<tbody>"+
            			"<tr>"+
            				"<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Rappel d'envoi des justifs des note de frais manquants :"+ refVdoc+"</td>"+
            			"</tr>"+
            			"<tr>"+
            				"<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>"+
            				"<p>Vous devez intervenir sur le document suivant : <a href='http://"+getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IPADRESSE")+"/vdoc/portal/default/portal-tab-workspace'> ${sys_Reference}</a>. Vous trouvez ci-dessous quelques informations sur les NDFs non réceptionnées, la date limite de réception est le "+dateString+"</p>"+
            				"<div class='section section'>"+
            				"<table border='0' cellpadding='0' cellspacing='0' width='100%'>"+
            					"<tbody>"+
            						"<tr style='display:none;'>"+
            							"<td class='section-header'>"+
            							"<table border='0' cellpadding='0' cellspacing='0' width='100%'>"+
            								"<tbody>"+
            									"<tr>"+
            										"<td class='text-style1'>&nbsp;</td>"+
            										"<td class='section-help'>&nbsp;</td>"+
            									"</tr>"+
            								"</tbody>"+
            							"</table>"+
            							"</td>"+
            						"</tr>"+
            						"<tr>"+
            							"<td class='section-content'>"+
            							"<table cellpadding='0' cellspacing='0' class='table-border' width='100%'>"+ 
            								"<tbody>"+
            									"<tr>"+
            										"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Processus</td>"+
            										"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Remboursement des dossiers de mutuelle</td>"+
            									"</tr>"+
            									"<tr>"+
            										"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Demandeur</td>"+
            										"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+getWorkflowInstance().getCreatedBy().getFullName()+"</td>"+
            									"</tr>"+
            									"<tr>"+
            										"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Etape</td>"+
            										"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String)getWorkflowInstance().getValue("sys_CurrentSteps")+"</td>"+
            									"</tr>"+
            									"<tr>"+
            										"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>"+
            										"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String)getWorkflowInstance().getValue("sys_Reference")+"</td>"+
            									"</tr>"+
            									"<tr>"+
            										"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Statut en cours</td>"+
            										"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>En attente réception des justifs originaux</td>"+
            									"</tr>"+
            										
												"<tr>"+
													"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Liste des NDFs non réceptionnées</td>"+
													"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>";
													
													
            msg += " <table style='border-collapse: collapse;padding:0px 0px 0px 0px;border:1px solid black;width: 100%;margin: 0px;font-size: small;cellspacing: 0px;cellpadding: 0px;text-align: left;'>"
            		+ " <tr><th colspan='75' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Désignation</th><th colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Montant</th></tr>";
           
            
            
            
            
            for(ILinkedResource ligne : listeNDF){
                isReceptionne = (String) ligne.getValue("NDF_Tab_Receptionne");
                if("Non".equals(isReceptionne)){
                    String designation = (String) ligne.getValue("NDF_Tab_DesignationRJO");
                    float montant      = (float)  ligne.getValue("NDF_Tab_MontantRJO");
                    msg +="<tr><td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+designation+"</td><td colspan='25' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='25%' valign='top' width='25%'>"+montant+" Dhs</td></tr>";
                    recept++;
                }
            }
            msg += "</table>";
													
													
													msg+= "</td>"+
												"</tr>"+
            					"</tbody>"+
            							"</table>"+
            							"</td>"+
            						"</tr>"+
            						"<tr>"+
            							"<td style='display:none'>&nbsp;</td>"+
            						"</tr>"+
            					"</tbody>"+
            				"</table>"+
            				"</div>"+
            				"</td>"+
            			"</tr>"+
            		"</tbody>"+
            	"</table>"+

            	"<div style='text-align:right; margin-top:10px; font-size:10px; font-family: Arial;'>@attijari.ma</div>";
           
            String currentUserMail = getWorkflowModule().getLoggedOnUser().getEmail();
            String receiverUserMail = getWorkflowInstance().getCreatedBy().getEmail(); 
            String subject = "Rappel d'envoi des justifs des notes de frais manquantes :"+ refVdoc;
            String smtpIPadress = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SMTPADRESS");
            int nbrJrDelayBtwEmails = 1;
            int deadLineEmail = 3;
            // Si la date de modification est diffirent de null et au moins un seul note de frais non receptionnée. 
            if (count==0 && recept>0)
            {
                // Envoi mail
                SendRappelEmail sendEmailObject = new SendRappelEmail(nbrJrDelayBtwEmails,deadLineEmail,smtpIPadress,currentUserMail,receiverUserMail,subject,msg);
                sendEmailObject.activateEmailThenStop();
                // Sauvegarde la date d'envoie de mail
                setDateMailSender(refVdoc, date);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS-ERROR IN onBeforeSave  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return super.onBeforeSave();
    }
    /*
     * =================================================================================================================
     * SETDATEMAILSENDER
     * =================================================================================================================
     */
    public void setDateMailSender(String vdocRef, Date dateday) {
        cnx = null;
        st = null;
        try {
            // DÉFINITION DE LA CONNEXION
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            String Query = "UPDATE NotesFrais SET DateModifJustifO_NF= ? WHERE Ref_NF=?";
            st = cnx.prepareStatement(Query);
            java.util.Date utilDate = dateday;
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            st.setDate(1,sqlDate); 
            st.setString(2,vdocRef);
            st.executeUpdate();
        }catch(Exception e) {
            e.getStackTrace();
            log.error("CS-ERROR IN SETDATEMAILSENDER  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
}
