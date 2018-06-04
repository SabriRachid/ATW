package com.attijari.processJourFerie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;

import dao.SingletonConnexionBDD;

public class DefinitionJourFerie extends BaseDocumentExtension
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4269810337595101111L;
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	private String tabJourFerieNS;
	private String idJourFerieNS;
	private String libJourFerieNS;
	private String nbrJourFerieNS;
	private String dateJourFerieNS;
	private String anneeNS;
	private String btnValider;
	
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException
	{
		this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		try
		{
			
			tabJourFerieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEF_JF_TABJRFE");
			libJourFerieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("LIB_JR_FER");
			nbrJourFerieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("NBR_JR");
			idJourFerieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IDJOURFERIE");
			dateJourFerieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DAT_JR_FER");
			anneeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEF_JF_ANNE");
			btnValider = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEF_JR_BTNVALIDER");
			
			
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "SELECT idJoursFerie,libelleJoursFerie,nbr_jours_ferie,JourNormal FROM JourFerie order by JourNormal";
			st = connection.prepareStatement(req);
			ResultSet rs = st.executeQuery();
			List<ILinkedResource> tableJourFerie = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources(tabJourFerieNS);
			if (tableJourFerie != null)
			{
				if (tableJourFerie.isEmpty())
				{
					while (rs.next())
					{
						ILinkedResource ligne = getWorkflowInstance().createLinkedResource(tabJourFerieNS);
						ligne.setValue(libJourFerieNS, rs.getString(2));
						ligne.setValue(nbrJourFerieNS, rs.getInt(3));
						ligne.setValue(idJourFerieNS, rs.getString(1));
						
						if(rs.getDate(4)!=null){
							java.sql.Date datesql = rs.getDate(4);
							Date datejava = new Date(datesql.getTime());
							Calendar cdate = Calendar.getInstance();
							cdate.setTime(datejava);
							Date newDate = new Date();
							Calendar cdate1 = Calendar.getInstance();
							cdate1.setTime(newDate);
							int annee = cdate1.get(cdate1.YEAR);//(String) getWorkflowInstance().getValue(anneeNS);
							cdate.set(cdate.YEAR, annee);
							ligne.setValue(dateJourFerieNS, cdate.getTime());
						}
						
						getWorkflowInstance().addLinkedResource(ligne);
					}
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	public int getAnneeIfExiste(){
		int compteurAnneeExistante = 0;
		try{
			String annee = (String) getWorkflowInstance().getValue(anneeNS);
			String trouveAnneRequete = "select annee from JourFerieAnnuelle where annee = ?";
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			st = connection.prepareStatement(trouveAnneRequete);
			st.setString(1, annee);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				compteurAnneeExistante++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return compteurAnneeExistante;
	}
	
	@SuppressWarnings(
	{
		"unchecked", "static-access"
	})
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		if (property.getName().equals(anneeNS))
		{
			try{
				int compteurAnneeExistante = getAnneeIfExiste();
				if(compteurAnneeExistante>0){
					getResourceController().alert("l'année séléctionnée existe déjà, Veuillez choisir une autre année.");
				}
				else{
					List<ILinkedResource> tableJourFerie = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources(tabJourFerieNS);
					getWorkflowInstance().deleteLinkedResources(tableJourFerie);
					if (tableJourFerie != null)
					{
						if (!tableJourFerie.isEmpty())
						{
							for (ILinkedResource ligne : tableJourFerie)
							{
								ILinkedResource newligne = getWorkflowInstance().createLinkedResource(tabJourFerieNS);
								Date dateJourFerie = (Date) ligne.getValue(dateJourFerieNS);
								if (dateJourFerie != null)
								{
									Calendar ca = Calendar.getInstance();
									ca.setTime(dateJourFerie);
									int anneeNumber = getWorkflowInstance().getValue(anneeNS) !=null ? Integer.parseInt((String) getWorkflowInstance().getValue(anneeNS)) : (new Date().getYear()+1900);
									ca.set(ca.YEAR, anneeNumber);
									newligne.setValue(dateJourFerieNS, ca.getTime());
									
								}
								newligne.setValue(idJourFerieNS, ligne.getValue(idJourFerieNS));
								newligne.setValue(libJourFerieNS, ligne.getValue(libJourFerieNS));
								newligne.setValue(nbrJourFerieNS, ligne.getValue(nbrJourFerieNS));
								getWorkflowInstance().addLinkedResource(newligne);
							}
						}
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
		}
		super.onPropertyChanged(property);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if(action.getName().equals(btnValider)){
			try{
				int compteurAnneeExistante = getAnneeIfExiste();
				if(compteurAnneeExistante>0){
					getResourceController().alert("L'année séléctionnée existe déjà, Veuillez choisir une autre année.");
					return false;
				}
				else{
					List<ILinkedResource> tableJourFerie = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources(tabJourFerieNS);
					int compteurDateNull = 0;
					for (ILinkedResource ligne : tableJourFerie)
					{
						Date date_jourferie = (Date) ligne.getValue(dateJourFerieNS);
						if(date_jourferie==null){
							compteurDateNull++;
						}
					}
					
					if(compteurDateNull>0){
						getResourceController().alert("L'affectation des dates des jours fériés n'est pas terminée.");
						return false;
					}
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return super.onBeforeSubmit(action);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if(action.getName().equals(btnValider)){
			try
			{
				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				List<ILinkedResource> tableJourFerie = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources(tabJourFerieNS);
				for (ILinkedResource ligne : tableJourFerie)
				{
					String id_jourferie = (String) ligne.getValue(idJourFerieNS);
					Date date_jourferie = (Date) ligne.getValue(dateJourFerieNS);
					String insertReq = "insert into  JourFerieAnnuelle(id_jourferie,date_jourferie,annee) values (?,?,?)";
					st = connection.prepareStatement(insertReq);
					st.setString(1, id_jourferie);
					java.sql.Date sqlDate = new java.sql.Date(date_jourferie.getTime());
					st.setDate(2, sqlDate);
					int annee = Integer.parseInt((String) getWorkflowInstance().getValue(anneeNS));
					st.setInt(3, annee);
					st.executeUpdate();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return super.onAfterSubmit(action);
	}
	
}
