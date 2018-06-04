package cs.dev.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
/**
 * @author r.sabri
 * @Creation_Date 10/11/2017 10:50 AM
 * @Plateform VDOC14 2.1 
 */
public class ValidationMandat extends BaseDocumentExtension{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ValidationMandat.class);
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *DECLARATION
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    private String ipHost;
    private String DG;
    private String DGA;
    private String SMM; // Responsable staffing
    private String SMI;
    //    @Override
    public boolean onAfterLoad() {
        //        String Email_Collab="";
        //      List<IUser> SMM_role = (List<IUser>) getWorkflowInstance().getValue("GO_Liste_Manager"); // SMM Rôle
        //        for(IUser ligne : SMM_role){
        //            Email_Collab = ligne.getEmail(); // Email users
        //        }
        //        getResourceController().alert(Email_Collab);
        //       SendMail("Le Mandat à été forcément démarrer");
        return super.onAfterLoad();
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ONAFTERSUBMIT
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterSubmit(IAction action) {
        String Commentaire="";
        if (action.getName().equals("GO_ValiderDGMandat")) // Validation Mandat
        {
            Commentaire ="Le mandat à été validé";
            SendMail(Commentaire);
        }
        if (action.getName().equals("GO_ForcerDemarrage_ValidationMandat")) // Forcer Démarrage Mandat
        {
            Commentaire ="Le mandat à été forcément démarrer";
            SendMail(Commentaire);
        }
        return super.onAfterSubmit(action);
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     * ENVOIE NOTIFICATION (mail d'information)
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void SendMail(String Comment)
    {
        try {
            ipHost = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ATTIP");
            DG = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DG");
            String mailContent ="";
            String DG_Email = DG; // i.berrada@attijari.ma
            String Manager_Email="";
            String email_assi = getWorkflowModule().getLoggedOnUser().getEmail();
            mailContent = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + 
                    "<tbody>" + 
                    "<tr>" + 
                    "<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Pour Information</td>" + 
                    "</tr>" + 
                    "<tr>" + 
                    "<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>" + 
                    "<p>Visualisez le document :&nbsp;<a href='"+ipHost+"'/vdoc/portal/default/portal-tab-workspace'>"+(String) getWorkflowInstance().getValue("GO_NomProjet")+"-"+ (String) getWorkflowInstance().getValue("sys_Reference")+"</a></p>" + 
                    "<div class='section section'>" + 
                    "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + 
                    "<tbody>" + 
                    "<tr style='display:none;'>" + 
                    "<td class='section-header'>" + 
                    "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + 
                    "<tbody>" + 
                    "<tr>" + 
                    "<td class='text-style1'>&nbsp;</td>" + 
                    "<td class='section-help'>&nbsp;</td>" + 
                    "</tr>" + 
                    "</tbody>" + 
                    "</table>" + 
                    "</td>" + 
                    "</tr>" + 
                    "<tr>" + 
                    "<td class='section-content'>" + 
                    "<table cellpadding='0' cellspacing='0' class='table-border' width='100%'>" + 
                    "<tbody>" + 
                    "<tr>" + 
                    "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Processus</td>" + 
                    "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>GESTION DES OPERATIONS</td>" + 
                    "</tr>" + 
                    "<tr>" + 
                    "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Etape</td>" + 
                    "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String) getWorkflowInstance().getValue("sys_CurrentSteps")+"</td>" + 
                    "</tr>" + 
                    "<tr>" + 
                    "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Etape</td>" + 
                    "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String) getWorkflowInstance().getValue("GO_NomProjet")+"</td>" + 
                    "</tr>" +
                    "<tr>" + 
                    "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>" + 
                    "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String) getWorkflowInstance().getValue("sys_Reference")+"</td>" + 
                    "</tr>" + 
                    "</tr>" + 
                    "<tr>" + 
                    "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>Commentaire</label></td>" + 
                    "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Comment+"</td>" + 
                    "</tr>" + 
                    "</tbody>" + 
                    "</table>" + 
                    "</td>" + 
                    "</tr>" + 
                    "<tr>" + 
                    "<td style='display:none'>&nbsp;</td>" + 
                    "</tr>" + 
                    "</tbody>" + 
                    "</table>" + 
                    "</div>" + 
                    "</td>" + 
                    "</tr>" + 
                    "</tbody>" + 
                    "</table>" + 
                    "<div style='text-align:right; margin-top:10px; font-size:10px; font-family: Arial;'>@attijari.ma</div>";

            List<IUser> SMM_role = (List<IUser>) getWorkflowInstance().getValue("GO_Liste_Manager"); 
            for(IUser ligne : SMM_role){
                Manager_Email = ligne.getEmail(); // Email users
            }
            // Envoi mail
            sendEmailSMI(email_assi,mailContent, DG_Email,Manager_Email);
            // }  
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    // =========================================================================
    // Send Mail to SMI (Senior Manager International)
    // =========================================================================
    public void sendEmailSMI(String Assisstante, String TexteEmail,String Collaborateurs,String ManagerMail)
    {    
        // Recipient's email ID needs to be mentioned.
        String to = Collaborateurs;

        // Sender's email ID needs to be mentioned
        String from = Assisstante;

        // Assuming you are sending email from host ip adress
        ipHost = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ATTIP");

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", ipHost);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            SMM = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SMM");
            SMI = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SMI");
            DGA = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DGA");
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            Address [] cc = new Address[] {
                    new InternetAddress(ManagerMail),
                    new InternetAddress(SMM),
                    new InternetAddress(SMI),
                    new InternetAddress(DGA)
            };
            message.addRecipients(Message.RecipientType.CC, cc);            

            // Set Subject: header field
            message.setSubject("Validation mandat");

            Multipart multipart = new MimeMultipart();
            BodyPart messagebp = new MimeBodyPart();
            //            messagebp.setText(TexteEmail);
            //            multipart.addBodyPart(messagebp);
            //
            //            message.setContent(multipart);
            //
            //            // Send message
            //            Transport.send(message);
            message.setContent(TexteEmail, "text/html");

            // Send message

            Transport.send(message);

        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CONNEXION AU BASE DE DONNEES 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
        context = getWorkflowModule().getContextByLogin("sysadmin");
        return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(context, Ref_externe);
    }

}
