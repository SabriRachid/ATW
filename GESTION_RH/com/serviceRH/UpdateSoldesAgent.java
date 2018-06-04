package com.serviceRH;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.axemble.vdoc.sdk.agent.base.BaseAgent;
import com.axemble.vdoc.sdk.interfaces.IUser;

public class UpdateSoldesAgent extends BaseAgent
{
	
	@Override
	protected void execute()
	{
		// TODO Auto-generated method stub
		
		ServiceRH srv = new ServiceRH();
		List<IUser> allusers =  (List<IUser>) (getDirectoryModule().getUsers(getWorkflowModule().getSysadminContext()));
		float droitAquis = 0;
		String loginVdoc = "";
		for(IUser user : allusers){
			loginVdoc = user.getLogin();
			 
			float soldeConsomme = srv.getDroitAcquis(loginVdoc);
			droitAquis = soldeConsomme;
			float oldSoldeAnterieur = srv.getSoldeAnterieur(loginVdoc);
			float oldReliquatEnCours = srv.getSoldeEnCours(loginVdoc);
			
			float nvSoldeEnCours = 0;
			float nvSoldeAnterieur = 0;
			float diff = 0;
			
			if(oldReliquatEnCours==droitAquis){
				nvSoldeAnterieur= oldSoldeAnterieur+soldeConsomme;
				nvSoldeEnCours = droitAquis;
			}
			else if(oldReliquatEnCours<droitAquis){
				
				if(oldReliquatEnCours+soldeConsomme>droitAquis){
					diff = oldReliquatEnCours+soldeConsomme - droitAquis;
					nvSoldeEnCours = droitAquis;
					nvSoldeAnterieur = diff;
					
				}
				if(oldReliquatEnCours+soldeConsomme==droitAquis){
					nvSoldeEnCours = droitAquis;
					nvSoldeAnterieur = diff;
				}
				if(oldReliquatEnCours+soldeConsomme<droitAquis){
					nvSoldeEnCours = oldReliquatEnCours+soldeConsomme;
					nvSoldeAnterieur = diff;
				}
			}
			
			
			srv.setSoldeEnCours(loginVdoc, nvSoldeEnCours);
			srv.setSoldeAnterieur(loginVdoc, nvSoldeAnterieur);
		}
		
	}
	
}
