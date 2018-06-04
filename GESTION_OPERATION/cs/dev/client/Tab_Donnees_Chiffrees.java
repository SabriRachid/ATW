package cs.dev.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;

public class Tab_Donnees_Chiffrees extends ConnexionBDD{

    /**
     * @author r.sabri
     * @Creation_Date 01/06/2017 10:50 PM
     * @Plateform VDOC14 2.1 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log =  Logger.getLogger(Tab_Donnees_Chiffrees.class);
    /*------------------------------------------------------------------------------------------------------------------
     *ONAFTERLOAD
     *------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterLoad() {
        try {
            ArrayList<IOption> cOption = new ArrayList<IOption>();
            Calendar calendar = Calendar.getInstance();
            int year=calendar.get( Calendar.YEAR );//=> Année actuel 
            for (int i =1;i<4;i++) {
                cOption.add(getWorkflowModule().createListOption(""+(year-i),""+(year-i)));
            }
            String Action = (String) getWorkflowInstance().getParentInstance().getValue("Actions");
            if (Action.equals("ADD")){
                getWorkflowInstance().setList("DC_Tab_Annees", cOption);
            }else if (Action.equals("MOD")){
                getWorkflowInstance().setList("DC_Tab_Annees_MOD", cOption);
            }
        }
        catch (Exception e)
        {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("CS -ERROR IN ONAFTERLOAD METHOD : " + e.getClass() + " - " + message);
        }
        return super.onAfterLoad();
    }
    /*------------------------------------------------------------------------------------------------------------------
     *ONBEFORESAVE
     *------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onBeforeSave() {
        String year= null;
        String year2= null;
        int i=0;
        try
        {
            String Action = (String) getWorkflowInstance().getParentInstance().getValue("Actions");
            if (Action.equals("ADD")){
                List<ILinkedResource> TableDC = (List<ILinkedResource>) getWorkflowInstance().getParentInstance().getLinkedResources("DonneesChiffrees");
                if (TableDC.size()!=0)
                {
                    for(ILinkedResource ligne : TableDC){
                        year = (String)ligne.getValue("DC_Tab_Annees");
                        year2 = (String)getWorkflowInstance().getValue("DC_Tab_Annees"); 
                        if (year2.equalsIgnoreCase(year))
                        {
                            i++;
                        }
                    }
                    if(i>1)
                    {
                        getResourceController().alert("Les données chiffrées de l'année "+ year + " existe.");
                        return false;
                    }else {
                        return true;
                    }
                }
            }else if (Action.equals("MOD")){
                List<ILinkedResource> tableDC2 = (List<ILinkedResource>) getWorkflowInstance().getParentInstance().getLinkedResources("DonneesChiffrees_MOD");
                if (tableDC2.size()!=0)
                {
                    for(ILinkedResource ligne : tableDC2){
                        year = (String)ligne.getValue("DC_Tab_Annees_MOD");
                        year2 = (String)getWorkflowInstance().getValue("DC_Tab_Annees_MOD"); 
                        if (year2.equalsIgnoreCase(year))
                        {
                            i++;
                        }
                    }
                    if(i>1)
                    {
                        getResourceController().alert("Les données chiffrées de l'année "+ year + " existe.");
                        return false;
                    }else {
                        return true;
                    }
                }  
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("CS Error in CheckYear() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
        return super.onBeforeSave();
    }
}
