package cs.dev.budget;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.utils.Logger;

import dao.ConnexionBDD;

public class Tab_Details_Budget extends ConnexionBDD{

    /**
     * Update on 23.01.2018 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log =  Logger.getLogger(Tab_Details_Budget.class);
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ONAFTERLOAD
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterLoad() {
        try {
            ArrayList<IOption> cOption = new ArrayList<IOption>();
            Calendar calendar = Calendar.getInstance();
            int ann=calendar.get( Calendar.YEAR );//=> Année actuel 
            for (int i =0;i<10;i++) {
                cOption.add(getWorkflowModule().createListOption(""+(ann+i),""+(ann+i)));
            }
            getWorkflowInstance().setList("PB_Tab_Annee", cOption);
        }
        catch (Exception e)
        {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            log.error("Error in onAfterLoad method : " + e.getClass() + " - " + message);
        }
        return super.onAfterLoad();
    }
    /*------------------------------------------------------------------------------------------------------------------
     * ONBEFORESAVE 
     *------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onBeforeSave() {
        String year= null;
        String year2= null;
        int i=0;
        try
        {
            List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getParentInstance().getLinkedResources("PB_DetailBudget");
            if (tableListePersonnes.size()!=0)
            {
                for(ILinkedResource ligne : tableListePersonnes){
                    year = (String)ligne.getValue("PB_Tab_Annee");
                    year2 = (String)getWorkflowInstance().getValue("PB_Tab_Annee"); 
                    if (year2.equalsIgnoreCase(year))
                    {
                        i++;
                    }
                }
                if(i>1)
                {
                    getResourceController().alert("Le budget de l'année "+ year + " exist.");
                    return false;
                }else {
                    return true;
                }
            }
        }catch (Exception e) {
            log.error("CS Error in CheckYear() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
        return super.onBeforeSave();
    }
    /*------------------------------------------------------------------------------------------------------------------
     * 
     *------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void onPropertyChanged(IProperty property) {
        if (property.getName().equals("PB_Tab_TotalFixe"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_Tab_TotalFixe");
            if (budget != null)
                Dbudget = Double.parseDouble(budget.toString());

            getWorkflowInstance().setValue("PB_Tab_TotalFixeStr", ConnexionBDD.SeparateurMilliers(Dbudget));
        }

        if (property.getName().equals("PB_Tab_TotalVariable"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_Tab_TotalVariable");
            if (budget != null)
                Dbudget = Double.parseDouble(budget.toString());

            getWorkflowInstance().setValue("PB_Tab_TotalVariableStr", ConnexionBDD.SeparateurMilliers(Dbudget));
        }

        if (property.getName().equals("PB_Tab_TotalAnnee"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_Tab_TotalAnnee");
            if (budget != null)
                Dbudget = Double.parseDouble(budget.toString());

            getWorkflowInstance().setValue("PB_Tab_TotalAnneeStr", ConnexionBDD.SeparateurMilliers(Dbudget));
        }

        if (property.getName().equals("PB_Tab_1erSemestreFixe"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_Tab_1erSemestreFixe");
            if (budget != null)
                Dbudget = Double.parseDouble(budget.toString());

            getWorkflowInstance().setValue("PB_Tab_1erSemestreFixeStr", ConnexionBDD.SeparateurMilliers(Dbudget));
        }

        if (property.getName().equals("PB_Tab_2emeSemestreFixe"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_Tab_2emeSemestreFixe");
            if (budget != null)
                Dbudget = Double.parseDouble(budget.toString());

            getWorkflowInstance().setValue("PB_Tab_2emeSemestreFixeStr", ConnexionBDD.SeparateurMilliers(Dbudget));
        }

        if (property.getName().equals("PB_Tab_1erSemestreVariable"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_Tab_1erSemestreVariable");
            if (budget != null)
                Dbudget = Double.parseDouble(budget.toString());

            getWorkflowInstance().setValue("PB_Tab_1erSemestreVariableStr", ConnexionBDD.SeparateurMilliers(Dbudget));
        }

        if (property.getName().equals("PB_Tab_2emeSemestreVariable"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_Tab_2emeSemestreVariable");
            if (budget != null)
                Dbudget = Double.parseDouble(budget.toString());

            getWorkflowInstance().setValue("PB_Tab_2emeSemestreVariableStr", ConnexionBDD.SeparateurMilliers(Dbudget));
        }
        super.onPropertyChanged(property);
    }
    /*------------------------------------------------------------------------------------------------------------------
     * 
     *------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if (property.getName().equals("PB_Tab_TotalFixe"))
        {
            return true;
        }

        if (property.getName().equals("PB_Tab_TotalVariable"))
        {
            return true;  
        }

        if (property.getName().equals("PB_Tab_TotalAnnee"))
        {
            return true;  
        }

        if (property.getName().equals("PB_Tab_1erSemestreFixe"))
        {
            return true;
        }

        if (property.getName().equals("PB_Tab_2emeSemestreFixe"))
        {
            return true;
        }

        if (property.getName().equals("PB_Tab_1erSemestreVariable"))
        {
            return true;
        }

        if (property.getName().equals("PB_Tab_2emeSemestreVariable"))
        {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }

}
