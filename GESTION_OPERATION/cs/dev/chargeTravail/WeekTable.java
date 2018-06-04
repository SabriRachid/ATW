package cs.dev.chargeTravail;

import com.axemble.vdoc.sdk.interfaces.IProperty;


public class WeekTable extends Operation {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//------------------------------------------------------
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property) {
	    
	    if(property.getName().equals("CT_Tab_NoteLundiSemAct"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteMardiSemAct"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteMercrediSemAct"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteJeudiSemAct"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteVendrediSemAct"))
	    {
	    	return true;
	    }
	    if(property.getName().equals("CT_Tab_NoteSamediSemAct"))
	    {
	    	return true;
	    }
	    return super.isOnChangeSubscriptionOn(property);
	}
	
	public void onPropertyChanged(IProperty property) {
		String fullname = (String)getWorkflowInstance().getValue("CT_Tab_Collaborateur");
	    if(property.getName().equals("CT_Tab_NoteLundiSemAct"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteLundiSemAct");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleLundiAct");
	    	int indexDay =1; //lundi
	    	getWorkflowInstance().setValue("CT_Tab_SommeLundiAct",CalculerSommeTable1(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteMardiSemAct"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteMardiSemAct");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleMardiAct");
	    	int indexDay =2;//mardi
	    	getWorkflowInstance().setValue("CT_Tab_SommeMardiAct",CalculerSommeTable1(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteMercrediSemAct"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteMercrediSemAct");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleMercrediAct");
	    	int indexDay =3;
	    	getWorkflowInstance().setValue("CT_Tab_SommeMercrediAct",CalculerSommeTable1(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteJeudiSemAct"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteJeudiSemAct");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleJeudiAct");
	    	int indexDay =4;
	    	getWorkflowInstance().setValue("CT_Tab_SommeJeudiAct",CalculerSommeTable1(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteVendrediSemAct"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteVendrediSemAct");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleVendrediAct");
	    	int indexDay =5;
	    	getWorkflowInstance().setValue("CT_Tab_SommeVendrediAct",CalculerSommeTable1(fullname,newNote,Cumule,indexDay));
	    }
	    if(property.getName().equals("CT_Tab_NoteSamediSemAct"))
	    {
	    	String newNote =(String)getWorkflowInstance().getValue("CT_Tab_NoteSamediSemAct");
	    	String Cumule =(String)getWorkflowInstance().getValue("CT_Tab_NCumuleSamediAct");
	    	int indexDay =6;
	    	getWorkflowInstance().setValue("CT_Tab_SommeSamediAct",CalculerSommeTable1(fullname,newNote,Cumule,indexDay));
	    }
	    super.onPropertyChanged(property);
	}
}
