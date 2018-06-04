package cs.dev.chargeTravail;

import com.axemble.vdoc.sdk.interfaces.IProperty;


public class WeekTable3 extends Operation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//------------------------------------------------------
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property) {
	    
	    if(property.getName().equals("CT_Tab_NoteLundiSemActP2"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteMardiSemActP2"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteMercrediSemActP2"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteJeudiSemActP2"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteVendrediSemActP2"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteSamediSemActP2"))
	    {
	    	return true;
	    }
	    return super.isOnChangeSubscriptionOn(property);
	}
	
	public void onPropertyChanged(IProperty property) {
		String fullname = (String)getWorkflowInstance().getValue("CT_Tab_CollaborateurSemActP2");
		
	    if(property.getName().equals("CT_Tab_NoteLundiSemActP2"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteLundiSemActP2");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleLundiAct2");
	    	int indexDay =1; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeLundiAct2",CalculerSommeTable3(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteMardiSemActP2"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteMardiSemActP2");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleMardiAct2");
	    	int indexDay =2; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeMardiAct2",CalculerSommeTable3(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteMercrediSemActP2"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteMercrediSemActP2");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleMercrediAct2");
	    	int indexDay =3; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeMercrediAct2",CalculerSommeTable3(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteJeudiSemActP2"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteJeudiSemActP2");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleJeudiAct2");
	    	int indexDay =4; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeJeudiAct2",CalculerSommeTable3(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteVendrediSemActP2"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteVendrediSemActP2");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleVendrediAct2");
	    	int indexDay =5; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeVendrediAct2",CalculerSommeTable3(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteSamediSemActP2"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteSamediSemActP2");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleSamediAct2");
	    	int indexDay =6; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeSamediAct2",CalculerSommeTable3(fullname,newNote,Cumule,indexDay));
	    }
	    super.onPropertyChanged(property);
	}


}
