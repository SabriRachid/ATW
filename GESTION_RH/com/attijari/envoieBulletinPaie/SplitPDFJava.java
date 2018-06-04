package com.attijari.envoieBulletinPaie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadPendingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.Splitter;
import org.apache.turbine.services.servlet.TurbineServlet;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.serviceRH.EncryptionFile;

import dao.SingletonConnexionBDD;

public class SplitPDFJava
{
	public void readPDF(String chemin,IWorkflowInstance iw)
	{
		try
		{
			String ref = (String) iw.getValue("sys_Reference" );
			String mois = (String) iw.getValue("EBP_MOIS");
			String emailRh = (String) iw.getValue("EBP_EmailRH");
			EncryptionFile.decrypter(chemin, chemin);
			PDDocument document = PDDocument.load(new File(chemin));
			EncryptionFile.crypter(chemin, chemin);
			if (!document.isEncrypted())
			{
				PDFTextStripper stripper = new PDFTextStripper();
				String text = stripper.getText(document);
				for(String matricule : matricules.keySet()){
					if(text.contains(matricule)){
						
						sendEmailBulletin(emailRh, matricules.get(matricule),mois, ref,chemin,iw);
						continue;
					}
				}
				
			}
			document.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void sendEmailBulletin(String Assisstante, String Collaborateur, String mois, String ref,String filename,IWorkflowInstance iw)
	{
		// Recipient's email ID needs to be mentioned.
		String to = Collaborateur;
		
		// Sender's email ID needs to be mentioned
		String from = Assisstante;
		
		// Assuming you are sending email from 192.168.1.22
		String host = iw.getCatalog().getConfiguration().getStringUserProperty("SMTPADRESS");
		
		// Get system properties
		Properties properties = System.getProperties();
		
		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		
		try
		{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			message.setSubject("Bulletion de paie du mois "+mois);
			
			Multipart multipart = new MimeMultipart();
			BodyPart messagebp = new MimeBodyPart();
			String MsgEmail = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + "<tbody>" + "<tr>"
					+ "<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Bulletion de paie du mois "+mois+"</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>"
					+ "<p>Vous trouverez ci-joint votre bulletion de paie du mois "+mois+"</p>"
					+ "<div class='section section'>"
					+ "<table border='0' cellpadding='0' cellspacing='0' width='100%'>"
					+ "<tbody>"
					+ "<tr style='display:none;'>"
					+ "<td class='section-header'>"
					+ "<table border='0' cellpadding='0' cellspacing='0' width='100%'>"
					+ "<tbody>"
					+ "<tr>"
					+ "<td class='text-style1'>&nbsp;</td>"
					+ "<td class='section-help'>&nbsp;</td>"
					+ "</tr>"
					+ "</tbody>"
					+ "</table>"
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td class='section-content'>"
					+ "<table cellpadding='0' cellspacing='0' class='table-border' width='100%'>"
					+ "<tbody>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Processus</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Envoi de Bulletin de paie</td>"
					+ "</tr>"
					
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>Bulletin du mois</label></td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ mois
					+ "</td>"
					+ "</tr>"
					
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ ref
					+ "</td>"
					+ "</tr>"
					
					
					+ "</table>" +
					
					"<div style='text-align:right; margin-top:10px; font-size:10px; font-family: Arial;'>@attijari.ma</div>";
					
					//message.setContent(MsgEmail, "text/html");
					messagebp.setContent(MsgEmail, "text/html");
					multipart.addBodyPart(messagebp);
					multipart.addBodyPart(addPJ(filename));
					message.setContent(multipart);
			// Send message
			
			Transport.send(message);
			EncryptionFile.crypter(filename, filename);
			EncryptionFile.deleteFile(filename);
			
		}
		catch (MessagingException mex)
		{
			EncryptionFile.crypter(filename, filename);
			EncryptionFile.deleteFile(filename);
			mex.printStackTrace();
		}
	}
	
	
	public  BodyPart addPJ(String filename){
		BodyPart messagebp = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        try {
			messagebp.setDataHandler(new DataHandler(source));
			EncryptionFile.decrypter(filename, filename);
			messagebp.setFileName(filename.replace(TurbineServlet.getRealPath("BulletinsPaie")+"\\", ""));
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return messagebp;
	}
	
	
	@SuppressWarnings("unchecked")
	public  String copyPJToVDocWar(IWorkflowInstance iWorkflowInstance, String sysnamePJ, String personnelMatricule, String repertoirePJ) throws IOException
	{
		try
		{
			
			List<IAttachment> pjs = (List<IAttachment>) iWorkflowInstance.getValue(sysnamePJ);
			
			String filename = null;
			int i = 0;
			for(IAttachment pj : pjs)
			{
				filename =  pj.getShortName().contains(personnelMatricule)?i+pj.getShortName():personnelMatricule+ i +"_"+pj.getShortName();
				File newFile = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//BulletinsPaie//"+repertoirePJ+"//" + filename);
				newFile.createNewFile();
				InputStream is = pj.getInputStream();
				OutputStream os = new FileOutputStream(newFile);
				byte[] buffer = new byte[is.available()];
				int length;
				while ((length = is.read(buffer)) > 0)
				{
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
				
				EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//BulletinsPaie//"+repertoirePJ+"//" + filename, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//BulletinsPaie//"+repertoirePJ+"//" + filename);
				i++;
			}
			
			return filename;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public  void splitPDF(String chemin,String filename,IWorkflowInstance iw)
	{
		try
		{
			EncryptionFile.decrypter(chemin+filename, chemin+filename);
			File file = new File(chemin+filename);
			
			PDDocument document = PDDocument.load(file);
			
			// Instantiating Splitter class
			Splitter splitter = new Splitter();
			
			// splitting the pages of a PDF document
			List<PDDocument> Pages = splitter.split(document);
			EncryptionFile.crypter(chemin+filename, chemin+filename);
			// Creating an iterator
			Iterator<PDDocument> iterator = Pages.listIterator();
			
			// Saving each page as an individual document
			int i = 0;
			while (iterator.hasNext())
			{
				PDDocument pd = iterator.next();
				i++;
				pd.save(chemin+ filename+i + ".pdf");
				EncryptionFile.crypter(chemin+ filename+i + ".pdf", chemin+ filename+i + ".pdf");
				readPDF(chemin+ filename+i + ".pdf",iw);
				
				
			}
			document.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	Map<String,String> matricules = new HashMap<String,String>();
	
	public void getMatricules(String filiale){
		try{
			Connection connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(Modules.getWorkflowModule(),Modules.getPortalModule()).getConnection();
			String req = " select matricule,Email from personnel where FilialeIdFiliale = ? ";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, filiale);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()){
				matricules.put(rs.getString(1),rs.getString(2));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
