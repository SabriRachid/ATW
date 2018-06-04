package cs.dev.chargeTravail;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IProperty;

public class SaisieNotes extends Operation{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public boolean onAfterLoad ()
    {
        //String dossier = (String) getWorkflowInstance().getValue("CT_Dossier");

        //recuperer le premier jour de la semain
        getWorkflowInstance().setValue("CT_Date", getFirstDayOfWeek());
        //  if (dossier != null)
        // {
       // if(ifExiste())
        //{
           // getResourceController().alert("Vous avez déja saisi les notes de cette semaine.");
       // }else
       // {
            //full list 
            getWorkflowInstance().setList("CT_Dossier", getDossier()); // modefier setValue par setList
            //		//recuperer le premier jour de la semain
            //		getWorkflowInstance().setValue("CT_Date", getFirstDayOfWeek());
        //}
        // }
        return super.onAfterLoad();
    }
    //------------------------------------------------------
    @Override
    public void onPropertyChanged(IProperty property) {
        if(property.getName().equals("CT_Dossier"))
        {
            // Création méthode onPropertyChanged
            //get value of list
            String refDossier = (String) getWorkflowInstance().getValue("CT_Dossier");
            getWorkflowInstance().setValue("CT_Semaine_1", null);
            getWorkflowInstance().setValue("CT_Semaine_2", null);
            getWorkflowInstance().setValue("CT_Semaine_3", null);

            //full first table 
            fullFirstTable(refDossier);

            //full second table
            fullSecondTable(refDossier);

            //full thrid table
            fullThirdTable(refDossier);

            //full thrid table
            fullFourthTable(refDossier);
        }
        if(property.getName().equals("CT_NotesSemaineActuelle"))
        {
            modiferCumuleTable2();
        }
        if(property.getName().equals("CT_NotesSemaineActuellePlus1"))
        {
            modiferCumuleTable3();
        }
        if(property.getName().equals("CT_NotesSemaineActuellePlus2"))
        {
            modiferCumuleTable4();
        }
        super.onPropertyChanged(property);
    }
    //------------------------------------------------------
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        // Création méthode onPropertyChanged
        if(property.getName().equals("CT_Dossier"))
        {
            return true;
        }
        if(property.getName().equals("CT_NotesSemaineActuelle"))
        {
            return true;
        }
        if(property.getName().equals("CT_NotesSemaineActuellePlus1"))
        {
            return true;
        }
        if(property.getName().equals("CT_NotesSemaineActuellePlus2"))
        {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }

    public boolean onBeforeSubmit(IAction action)
    {
        if(action.getName().equals("CT_PourNotification"))
        {
            try {

                if(IsEmpty()) {
                    getResourceController().alert("Veuillez remplir tous les tableaux !");
                    return false;
                }else {
                    //get value of list
                    String refDossier = (String) getWorkflowInstance().getValue("CT_Dossier");
                    // Object x = getWorkflowInstance().getValue("CT_Semaine_1");
                    if(getWorkflowInstance().getValue("CT_Semaine_1")==null||getWorkflowInstance().getValue("CT_Semaine_2")==null||getWorkflowInstance().getValue("CT_Semaine_3")==null)
                    {
                        saveFirstNotesTable(refDossier);
                        saveSecondNotesTable(refDossier);
                        saveThirdNotesTable(refDossier);

                    }else {
                        int week1 = 0;
                        int week2 = 0;
                        int week3 = 0;
                        //get the value of first week
                        if(getWorkflowInstance().getValue("CT_Semaine_1") instanceof Integer) {
                            week1 = (int)getWorkflowInstance().getValue("CT_Semaine_1");
                        }else {
                            week1 = (int)(float)getWorkflowInstance().getValue("CT_Semaine_1");
                        }

                        //get the value of second week
                        if(getWorkflowInstance().getValue("CT_Semaine_2") instanceof Integer) {
                            week2 = (int)getWorkflowInstance().getValue("CT_Semaine_2");
                        }else {
                            week2 = (int)(float)getWorkflowInstance().getValue("CT_Semaine_2");
                        }
                        //get the value of third week
                        if(getWorkflowInstance().getValue("CT_Semaine_3") instanceof Integer) {
                            week3 = (int)getWorkflowInstance().getValue("CT_Semaine_3");
                        }else {
                            week3 = (int)(float)getWorkflowInstance().getValue("CT_Semaine_3");
                        }

                        if(ifWeekExist(week1)){
                            updateFirstTable(refDossier,week1);
                        }else {
                            log.error("the week1 don't exist");
                        }

                        if(ifWeekExist(week2)){
                            updateSecondTable(refDossier,week2);
                        }else {
                            log.error("the week2 don't exist");
                        }

                        if(ifWeekExist(week3)){
                            updateThirdTable(refDossier, week3);
                        }else {
                            log.error("the week3 don't exist");
                        }
                    }
                    saveFourthNotesTable(refDossier);
                }
                //get path
                //String path = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("path");
                //Creation des deux fichier json
                //createJsonFileEvent(path);
                //createJsonFileResources(path);
            } catch (Exception e) {
                log.error("error in onBeforeSubmit method "+e.getMessage()+" _ "+e.getClass()+" _ "+ e.getStackTrace()[0].getLineNumber() );
                e.getStackTrace();
            }
        }
        return super.onBeforeSubmit(action);
    }
}
