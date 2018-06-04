package cs.dev.livrables;


import java.util.ArrayList;
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

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.utils.Logger;
import com.ibm.icu.text.SimpleDateFormat;

import dao.ConnexionBDD;
/** ===================================
 * @author r.sabri
 * @date 17/01/2018 12:35 PM
 * ==================================== */
public class CreationLivrables extends ConnexionBDD{

    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(CreationLivrables.class);

    private String ipHost;

    // -----------------------------------------------------------------------------------------------------------------
    // En AfterSubmit
    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterSubmit(IAction action) {
        if (action.getName().equals("GL_Cloturer"))
        {
            SendMail();
        }
        return super.onAfterSubmit(action);
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Mail
    // -----------------------------------------------------------------------------------------------------------------
    public String MailContent()
    {
        ipHost = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ATTIP");
        String Date_Livrable = new SimpleDateFormat("dd/MM/yyyy").format(getWorkflowInstance().getValue("GL_DateLivrable"));
        String RefDeal = (String) getWorkflowInstance().getParentInstance().getValue("GO_NomProjet")+"-"+ (String) getWorkflowInstance().getParentInstance().getValue("sys_Reference");
        String SysRef = (String) getWorkflowInstance().getParentInstance().getValue("sys_Reference");
        String Livrable = (String) getWorkflowInstance().getValue("GL_DesignationLivrable");
        String Project = (String) getWorkflowInstance().getParentInstance().getValue("GO_NomProjet");
        String Customer = (String) getWorkflowInstance().getParentInstance().getValue("GO_CodeClients");
        String mailContent ="";
        mailContent = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + 
                "<tbody>" + 
                "<tr>" + 
                "<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Pour Information</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>" + 
                "<p>Visualisez le document :&nbsp; :<a href='"+ipHost+"'/vdoc/portal/default/portal-tab-workspace'>"+RefDeal+"</a></p>" + 
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
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>GESTION DES OPERATIONS (Préparation Livrable)</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+SysRef+"</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>Projet</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Project+"</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>Client</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Customer+"</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>D&eacute;signation Livrable</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Livrable+"</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>Date</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Date_Livrable+"</td>" + 
                "</tr>" +
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
        return mailContent;
    }
    // -----------------------------------------------------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings({ "unchecked", "unused" })
    public String PartieLivrable_Content()
    {
        ipHost = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ATTIP");
        String Date_Livrable = new SimpleDateFormat("dd/MM/yyyy").format(getWorkflowInstance().getValue("GL_DateLivrable"));
        String RefDeal = (String) getWorkflowInstance().getParentInstance().getValue("GO_NomProjet")+"-"+ (String) getWorkflowInstance().getParentInstance().getValue("sys_Reference");
        String SysRef = (String) getWorkflowInstance().getParentInstance().getValue("sys_Reference");
        String Livrable = (String) getWorkflowInstance().getValue("GL_DesignationLivrable");
        String Project = (String) getWorkflowInstance().getParentInstance().getValue("GO_NomProjet");
        String Customer = (String) getWorkflowInstance().getParentInstance().getValue("GO_CodeClients");
        String mailContent = "";
        String designation = "";
        String chef_equipe = "";
        String taches = "";
        List<String> equipe = new ArrayList();
        String commentaire = "";
        mailContent = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + 
                "<tbody>" + 
                "<tr>" + 
                "<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Pour Information</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>" + 
                "<p>Visualisez le document :&nbsp; :<a href='http://"+ipHost+"'/vdoc/portal/default/portal-tab-workspace'>"+RefDeal+"</a></p>" + 
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
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>GESTION DES OPERATIONS (Préparation Livrable)</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+SysRef+"</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>Projet</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Project+"</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>Client</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Customer+"</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>D&eacute;signation Livrable</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Livrable+"</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>Date</label></td>" + 
                "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+Date_Livrable+"</td>" + 
                "</tr>" +
                "</tr>" + 
                "</tbody>" + 
                "</table>" + 
                "</td>" + 
                "</tr>" + 
                "<tr>" + 
                "<td style='display:none'>&nbsp;</td>" + 
                "</tr>" + 
                "</tbody>" + 
                "</table>" ;
        // ----------------------------------------------------------------------------------------------------
        String IsPartie = (String) getWorkflowInstance().getValue("GL_CreerParties");
        if (IsPartie.equals("Oui"))
        {
            ArrayList<ILinkedResource> Tab_SP_Parties = (ArrayList<ILinkedResource>) getWorkflowInstance().getLinkedResources("GL_TSP_Parties");
            if (Tab_SP_Parties.size()!=0)
            {
                mailContent += "<table border='0' cellpadding='0' cellspacing='4' width='100%'>"+
                        "<tbody>"+
                        "<tr style='display:none;'>"+
                        "<table style='border-collapse: collapse;padding:0px 0px 0px 0px;border:1px solid black;width: 100%;margin: 0px;font-size: small;cellspacing: 0px;cellpadding: 0px;text-align: left;'>"+
                        "<th colspan='100' style='background-color:#658b9c; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border: 1px solid #d0d5d7' width='100%'><center>Tableau des parties</center></th>"+
                        "<tr>"+
                        "<th colspan='30' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border: 1px solid #d0d5d7' valign='top' width='30%'>Désignation</th>"+
                        "<th colspan='30' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border: 1px solid #d0d5d7' valign='top' width='30%'>Chef d'équipe</th>"+
                        "<th colspan='20' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border: 1px solid #d0d5d7' valign='top' width='20%'>Equipe</th>"+
                        "<th colspan='20' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border: 1px solid #d0d5d7' valign='top' width='20%'>Tâche</th>"+
                        "</tr>";

                for(ILinkedResource ligne : Tab_SP_Parties){
                    designation = (String) ligne.getValue("CP_DesignationPartie");
                    chef_equipe = (String) ligne.getValue("CP_ChefEquipe_Selector");
                    taches = (String) ligne.getValue("CP_Taches");
                    equipe = (List<String>) ligne.getValue("CP_Equipe_Partie");
                    //commentaire = (String) ligne.getValue("");
                    mailContent +="<tr>"+
                            "<td colspan='30' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border: 1px solid #d0d5d7' valign='top' width='30%'>"+designation+"</td>"+
                            "<td colspan='30' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border: 1px solid #d0d5d7' valign='top' width='30%'>"+chef_equipe+"</td>"+
                            "<td colspan='20' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border: 1px solid #d0d5d7' valign='top' width='20%'>"+
                            "<ul style='list-style-type:square'>";
                    for (String Personne : equipe)
                    {
                        mailContent +="<li>"+Personne+"</li>";
                    }
                    mailContent +="</ul>";
                    mailContent +="</td>"+
                            "<td colspan='20' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border: 1px solid #d0d5d7' valign='top' width='20%'>"+taches+"</td>"+
                            "</tr>";
                }

                mailContent +="</table>"+
                        "</tr>"+
                        "</tbody>"+
                        "</table>";
            }
        }
        // ----------------------------------------------------------------------------------------------------
        mailContent += "</div>" + 
                "</td>" + 
                "</tr>" + 
                "</tbody>" + 
                "</table>" + 
                "<div style='text-align:right; margin-top:10px; font-size:10px; font-family: Arial;'>@attijari.ma</div>";
        return mailContent;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Mail
    // -----------------------------------------------------------------------------------------------------------------
    public void sendEmailSMI(String Assisstante, String TexteEmail)
    {   


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
            //List<String> EmailUsers = new ArrayList<String>();
            for (String EmailUsers : MailUsers())
            {
                //message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
                Address [] To = new Address[] {
                        new InternetAddress(EmailUsers)
                };
                message.addRecipients(Message.RecipientType.TO, To);   
            }

            // Set Subject: header field
            message.setSubject("Préparation Livrable");

            Multipart multipart = new MimeMultipart();
            BodyPart messagebp = new MimeBodyPart();

            message.setContent(TexteEmail, "text/html");

            // Send message

            Transport.send(message);

        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // ENVOIE NOTIFICATION (mail d'information)
    // -----------------------------------------------------------------------------------------------------------------
    public void SendMail()
    {
        try {
            String email_assi = getWorkflowModule().getLoggedOnUser().getEmail();
            //PartieLivrable_Content();
            // Envoi mail
             sendEmailSMI(email_assi,PartieLivrable_Content());
            //sendEmailSMI(email_assi,"Test");
            // }  
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Liste des emails utilisteurs
    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public List<String> MailUsers()
    {
        List<String> eMailuser = null;
        eMailuser = new ArrayList<>();
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            ArrayList<String> UserFullName =  ((ArrayList<String>) getWorkflowInstance().getValue("GL_EquipeSelector"));
            String UserLogin = null;
            for (String username : UserFullName)
            {
                // RÉCUPÉRER ID D'UTILISATEUR SÉLÉCTINNÉ
                String query = "SELECT DISTINCT [Login] as 'UserID' FROM Collaborateur WHERE [Fullname] like ?";
                st = cnx.prepareStatement(query);
                st.setString(1,username);
                rs = st.executeQuery();
                while(rs.next())
                {
                    UserLogin = rs.getString("UserID");
                    // Récupérer les Emails des utilisateurs
                    String utilisateurs = getWorkflowModule().getUserByLogin(UserLogin).getEmail();  
                    eMailuser.add(utilisateurs);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return eMailuser;
    }

}
