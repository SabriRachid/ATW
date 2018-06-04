package cs.dev.chargeTravail;

import com.axemble.vdoc.sdk.interfaces.IProperty;


public class WeekTable2 extends Operation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//------------------------------------------------------
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property) {
	    
	    if(property.getName().equals("CT_Tab_NoteLundiSemActP1"))
	    {                             
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteMardiSemActP1"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteMercrediSemActP1"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteJeudiSemActP1"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteVendrediSemActP1"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteSamediSemActP1"))
	    {
	    	return true;
	    }
	    return super.isOnChangeSubscriptionOn(property);
	}
	
	public void onPropertyChanged(IProperty property) {
		String fullname = (String)getWorkflowInstance().getValue("CT_Tab_CollaborateurSemActP1");

	    if(property.getName().equals("CT_Tab_NoteLundiSemActP1"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteLundiSemActP1");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleLundiAct1");
	    	int indexDay =1; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeLundiAct1",CalculerSommeTable2(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteMardiSemActP1"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteMardiSemActP1");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleMardiAct1");
	    	int indexDay =2; //mardi
	    	getWorkflowInstance().setValue("CT_Tab_SommeMardiAct1",CalculerSommeTable2(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteMercrediSemActP1"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteMercrediSemActP1");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleMercrediAct1");
	    	int indexDay =3; //mercredi
	    	getWorkflowInstance().setValue("CT_Tab_SommeMercrediAct1",CalculerSommeTable2(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteJeudiSemActP1"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteJeudiSemActP1");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleJeudiAct1");
	    	int indexDay =4; //jeudi
	    	getWorkflowInstance().setValue("CT_Tab_SommeJeudiAct1",CalculerSommeTable2(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteVendrediSemActP1"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteVendrediSemActP1");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleVendrediAct1");
	    	int indexDay =5; //vendredi
	    	getWorkflowInstance().setValue("CT_Tab_SommeVendrediAct1",CalculerSommeTable2(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteSamediSemActP1"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteSamediSemActP1");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleSamediAct1");
	    	int indexDay =6; //samedi
	    	getWorkflowInstance().setValue("CT_Tab_SommeSamediAct1",CalculerSommeTable2(fullname,newNote,Cumule,indexDay));
	    }
	    super.onPropertyChanged(property);
	}


}
