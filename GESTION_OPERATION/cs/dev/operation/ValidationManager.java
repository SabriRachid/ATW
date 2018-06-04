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
 * @Creation_Date 15/12/2016 11:50 AM
 * @Plateform VDOC14 2.1 
 */
public class ValidationManager extends BaseDocumentExtension{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ValidationManager.class);
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *DECLARATION
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    private String ipHost;
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ONAFTERSUBMIT
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterSubmit(IAction action) {
        if (action.getName().equals("GO_ValiderMan"))
        {
            ModifyDocument();
            //SendMail();
        }
        return super.onAfterSubmit(action);
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *AFFECTATION DE MANAGER POUR UNE OPERATION CREER 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void ModifyDocument() {
        try {
            connection=ConnectionDefinition("Ref_Attijari").getConnection();
            String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); // RÉFÉRENCE VDOC
			String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
			String RefFormater = NomProjet+"-"+RefDossier;
            // Manager connecter
            IUser manager = getWorkflowModule().getLoggedOnUser();
            String FullName = manager.getFullName();
            String Query = "Update Dossiers set Manager= ? where RefDossier = ?";
            st = connection.prepareStatement(Query);
            st.setString(1, FullName);
            st.setString(2, RefFormater);
            st.executeUpdate();
            
        }catch(Exception e)
        {
            log.error("CS Error : problème dans la méthode ModifyDocument - [ValidationManager]" + e.getMessage());
        }
    }
    
    
    /*
     * 
     */
    public void SendMail()
    {
        try {
          //String refVdoc=(String) getWorkflowInstance().getValue("sys_Reference");
            String mailContent ="";
            String Email_Collab ="i.berrada@attijari.ma";
            //List<IUser> SMM_role = (List<IUser>) getWorkflowInstance().getValue("GO_SeniorManagerMaroc"); // SMM Rôle
            //for(IUser ligne : SMM_role){
                //Email_Collab = ligne.getEmail(); // Email users
                String email_assi = getWorkflowModule().getLoggedOnUser().getEmail();
                mailContent = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + 
                        "<tbody>" + 
                        "<tr>" + 
                        "<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Pour Information</td>" + 
                        "</tr>" + 
                        "<tr>" + 
                        "<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>" + 
                        "<p>Visualisez le document :&nbsp; : <b>"+(String) getWorkflowInstance().getValue("GO_NomProjet")+"-"+ (String) getWorkflowInstance().getValue("sys_Reference")+"</b></p>" + 
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
                        "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>" + 
                        "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String) getWorkflowInstance().getValue("sys_Reference")+"</td>" + 
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
                // Envoi mail
                //sendEmailSMI(email_assi,mailContent, Email_Collab);
           // }  
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
 // =========================================================================
    // Send Mail to SMI (Senior Manager International)
    // =========================================================================
    public void sendEmailSMI(String Assisstante, String TexteEmail,String Collaborateurs)
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
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            // SMM + SMI + DGA
            Address [] cc = new Address[] {
                   new InternetAddress("d.nejjar@attijari.ma"),
                   new InternetAddress("f.squalli@attijari.ma"),
                   new InternetAddress("m.berrada@attijari.ma")
            };
            message.addRecipients(Message.RecipientType.CC, cc);            
            
            // Set Subject: header field
            message.setSubject("Informations sur le document GESTION DES OPERATIONS");

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
