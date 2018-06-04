package cs.dev.chargeTravail;

import com.axemble.vdoc.sdk.interfaces.IProperty;


public class WeekTable4 extends Operation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ==============================================================================================================
    // 
    // ==============================================================================================================
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property) {
	    
	    if(property.getName().equals("CT_Tab_NoteLundiSemActP3"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteMardiSemActP3"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteMercrediSemActP3"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteJeudiSemActP3"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteVendrediSemActP3"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteSamediSemActP3"))
	    {
	    	return true;
	    }
	    return super.isOnChangeSubscriptionOn(property);
	}
	// ==============================================================================================================
	// 
	// ==============================================================================================================
	public void onPropertyChanged(IProperty property) {
		String fullname = (String)getWorkflowInstance().getValue("CT_Tab_CollaborateurSemActP3");
		// Note Lundi ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    if(property.getName().equals("CT_Tab_NoteLundiSemActP3"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteLundiSemActP3");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleLundiAct3");
	    	int indexDay =1; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeLundiAct3",CalculerSommeTable4(fullname,newNote,Cumule,indexDay));
	    }
	    // Note Mardi ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    if(property.getName().equals("CT_Tab_NoteMardiSemActP3"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteMardiSemActP3");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleMardiAct3");
	    	int indexDay =2; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeMardiAct3",CalculerSommeTable4(fullname,newNote,Cumule,indexDay));
	    }
	    // Note Mercredi ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    if(property.getName().equals("CT_Tab_NoteMercrediSemActP3"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteMercrediSemActP3");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleMercrediAct3");
	    	int indexDay =3; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeMercrediAct3",CalculerSommeTable4(fullname,newNote,Cumule,indexDay));
	    }
	    // Note Jeudi ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    if(property.getName().equals("CT_Tab_NoteJeudiSemActP3"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteJeudiSemActP3");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleJeudiAct3");
	    	int indexDay =4; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeJeudiAct3",CalculerSommeTable4(fullname,newNote,Cumule,indexDay));
	    }
	    // Note Vendredi ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    if(property.getName().equals("CT_Tab_NoteVendrediSemActP3"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteVendrediSemActP3");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleVendrediAct3");
	    	int indexDay =5; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeVendrediAct3",CalculerSommeTable4(fullname,newNote,Cumule,indexDay));
	    }
	    // Note Samedi ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    if(property.getName().equals("CT_Tab_NoteSamediSemActP3"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteSamediSemActP3");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleSamediAct3");
	    	int indexDay =6; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeSamediAct3",CalculerSommeTable4(fullname,newNote,Cumule,indexDay));
	    }
	    super.onPropertyChanged(property);
	}


}
