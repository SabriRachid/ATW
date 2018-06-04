package NoteDeFrais;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

/**
 Run a simple task once every second, starting 3 seconds from now.
 Cancel the task after 20 seconds.
*/
public class SendRappelEmail {
  
  /** Run the example. */
	
	
 
  
  public static void sendEmail(String Assisstante, String Collaborateur, String MsgEmail,String subject,String server)
  {    
     // Recipient's email ID needs to be mentioned.
     String to = Collaborateur;

 	// Sender's email ID needs to be mentioned
		String from = Assisstante;
		
		// Assuming you are sending email from 192.168.1.22
		String host = server;
		
     // Get system properties
     Properties props = System.getProperties();

     // Setup mail server
     props.put("mail.smtp.host", host);
 
     // Get the default Session object.
     Session session = Session.getDefaultInstance(props);

     try{
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

        // Set Subject: header field
        message.setSubject(subject+" "+new Date().toGMTString());


        Multipart multipart = new MimeMultipart();
        BodyPart messagebp = new MimeBodyPart();
        messagebp.setContent(MsgEmail, "text/html");
        multipart.addBodyPart(messagebp);
        
        message.setContent(multipart);
       
        
        // Send message

        Transport.send(message);
        
     }catch (MessagingException mex) {
        mex.printStackTrace();
     }
  }
  
  SendRappelEmail(int mailDelayRappel,int deadLineBtwEmails,String server, String sender, String receiver, String subject, String msg){
	
    fInitialDelay = 1;
    fDelayBetweenRuns = mailDelayRappel * 24 * 60 * 60;
    fDeadLineEmail = deadLineBtwEmails * 24 * 60 * 60;
    fScheduler = Executors.newScheduledThreadPool(NUM_THREADS);  
    this.sender = sender;
    this.server = server;
    this.receiver = receiver;
    this.subject = subject;
    this.msg = msg;
  }
  
  /** Sound the alarm for a few seconds, then stop. */
  void activateEmailThenStop(){
    Runnable soundEmailTask = new SoundEmailTask();
    ScheduledFuture<?> soundEmailFuture = fScheduler.scheduleWithFixedDelay(
      soundEmailTask, fInitialDelay, fDelayBetweenRuns, TimeUnit.SECONDS
    );
    Runnable stopEmail = new StopEmailTask(soundEmailFuture);
    fScheduler.schedule(stopEmail, fDeadLineEmail, TimeUnit.SECONDS);
  }

  // PRIVATE 
  private  ScheduledExecutorService fScheduler;
  private  long fInitialDelay;
  private  long fDelayBetweenRuns;
  private  long fDeadLineEmail;
  private  String sender;
  private  String server;
  private  String receiver;
  private  String subject;
  private  String msg;
  
 

  /** If invocations might overlap, you can specify more than a single thread.*/ 
  private   int NUM_THREADS = 60;
  private   boolean DONT_INTERRUPT_IF_RUNNING = false;
  private  ExecutorService emailExecutor;
  
  private  class SoundEmailTask implements Runnable {
    @Override public void run() {
    	try{
//    		 ++fCount;
    	      emailExecutor = Executors.newSingleThreadExecutor();
    	      emailExecutor.execute(new Runnable() {
    	            @Override
    	            public void run() {
    	                try {
    	                	
    	                	SendRappelEmail.sendEmail(sender,receiver,msg,subject,server);
    	                } catch (Exception e) {
    	                    e.printStackTrace();
    	                }
    	            }
    	        });
    	        emailExecutor.shutdown();
    	     
    	    
    	}catch(Exception e){
    		e.printStackTrace();
    	}
     }
//    private int fCount;
  }
  
  private  class StopEmailTask implements Runnable {
    StopEmailTask(ScheduledFuture<?> aSchedFuture){
      fSchedFuture = aSchedFuture;
    }
    @Override public void run() {
      fSchedFuture.cancel(DONT_INTERRUPT_IF_RUNNING);
      /* 
       Note that this Task also performs cleanup, by asking the 
       scheduler to shutdown gracefully. 
      */
      fScheduler.shutdown();
    }
    private ScheduledFuture<?> fSchedFuture;
  }
} 