package com.attijari.GestionSalarie;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;

import dao.SingletonConnexionBDD;

public class TabRemuneration extends ConnexionBDD {

	
	
	
	
	@Override
	public boolean onAfterLoad() {
		
				getWorkflowInstance().setList("P_GS_TAB_Remunerations_Anne", getExercice());
		
		return super.onAfterLoad();
	}
	
	
	@Override
	public boolean onBeforeSubmit(IAction action) {
		
		String MatirculeUPD = (String) getWorkflowInstance().getParentInstance().getValue("P_GS_Matricule");
		
		List Remuneration = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Remunerations");
		if (Remuneration.size() != 0){
			
			for (Iterator IT = Remuneration.iterator(); IT.hasNext();){
				ILinkedResource AllRemun = (ILinkedResource) IT.next();
							
				Object annee1 = (Object)AllRemun.getValue("P_GS_TAB_Remunerations_Anne");
				float annee2 = Float.parseFloat(annee1.toString());
				int annee = (int) annee2;
				
				
				if(ExistYear(MatirculeUPD).equals(annee)){
					 getResourceController().alert("l'année que vous avez choisi est déjà ajouté ");
					return false;
				}			}		}
		
		return super.onBeforeSubmit(action);
	}
	
	public List<IOption> getExercice()
	{
		List<IOption> listda = new ArrayList<IOption>();
		
		try
		{
			// récupérer l'année courante
			Date d = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			int annee = c.get(Calendar.YEAR) ;
		
			for (int i = annee-10;  i< annee+10; i++)
			{
				listda.add(getWorkflowModule().createListOption(i + "", i + ""));
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listda;
	}
	
	
	public String ExistYear (String Matricule){
		String anneeR = "";
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select anneeRemunetation from Remuneration where Personnelmatricule = ?";
			st=connection.prepareStatement(req1);
			st.setString(1, Matricule);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				anneeR = rs1.getString(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return anneeR;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
