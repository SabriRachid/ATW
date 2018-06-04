package com.attijari.envoieBulletinPaie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.serviceRH.EncryptionFile;

public class EnvoieBulletinPaie extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */


	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		IUser rh = getWorkflowModule().getLoggedOnUser();
		getWorkflowInstance().setValue("EBP_EmailRH", rh.getEmail());
		if(rh.getOrganization().getName().equals("Attijarifinancescorp")){
			getWorkflowInstance().setValue("EBP_REP", "AFC");
		}
		else{
			getWorkflowInstance().setValue("EBP_REP", "ATI");
		}
		return super.onAfterLoad();
	}
	
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if(action.getName().equals("Envoyer")){
			SplitPDFJava spj = new SplitPDFJava();
			String rep = (String) getWorkflowInstance().getValue("EBP_REP");
			IUser rh = getWorkflowModule().getLoggedOnUser();
			spj.getMatricules(rh.getOrganization().getName());
			String filename = "";
			try
			{
				filename = spj.copyPJToVDocWar(getWorkflowInstance(), "EBP_PJBP", rh.getEmployeeNumber(), rep);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			spj.splitPDF("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\BulletinsPaie\\"+rep+"\\",filename,getWorkflowInstance());
			EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\BulletinsPaie\\"+rep+"\\"+filename);
		}
		return super.onAfterSubmit(action);
	}
}
