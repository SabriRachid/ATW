package com.attijari.processAnnulationConge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IOrganization;
import com.axemble.vdoc.sdk.interfaces.IProject;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IViewController;
import com.axemble.vdoc.sdk.interfaces.IWorkflow;
import com.axemble.vdoc.sdk.interfaces.IWorkflowContainer;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IDirectoryModule;
import com.axemble.vdoc.sdk.structs.Period;

import dao.SingletonConnexionBDD;

public class _1DemandeAnnulationConge extends BaseDocumentExtension
{
	int cpt = 0;
	private String rhNS;
	private String nbrJoursSoldesInitialNS;
	private String codeVDOC;
	private String supHieNS;
	private String demandeur;
	private String btnenvoyerNS;
	private String naturecongeNS;
	private String typecongeNS;
	private String nbjrpredefNS;
	private String momentsortieNS;
	private String momententreeNS;
	private String dateCongeDebNS;
	private String dateCongeFinNS;
	private String dateCongeDebSpeNS;
	private String dateCongeFinSpeNS;
	private String periodecongenorNS;
	private String nbrjrouvrNS;
	private String nbrjrcongdispoNS;
	private String justifabsNS;
	private String typecongespeNS;
	private String nbjrpredefspeNS;
	private String periodecongespeNS;
	private String justifabsspeNS;
	private String frag_conge_combineNS;
	private String frag_nbrjrpredefNS;
	private String matinApmidisortieNS;
	private String matinApmidientreeNS;
	private String msgErreurPeriodeCongeNS;
	private String msgErreurPeriodeCongeSpeNS;
	private String nbrjrouvrSpeNS;
	private String matinApmidisortieSpeNS;
	private String matinApmidientreeSpeNS;
	private String momentsortieSpeNS;
	private String momententreeSpeNS;
	private String nbrJrSoldeNS;
	private String nbrJrNonSoldeNS;
	private String droitAnnCongNS;
	private String motifNorNS;
	private String motifSpeNS;
	private String fragmotifNorNS;
	private String fragmotifSpeNS;
	private String frag_remplacantNS;
	private String resteJoursCongeNS;
	private String commentaireNS;
	private String alerteNS;
	private String fragAlerteNS;
	private String fragCongeNS;
	private String codeCongeNS;
	private String fragIdCongeNorNS;
	private String fragIdCongeSpeNS;
	private String idCongeNorNS;
	private String idCongeSpeNS;
	private String lieuNS;
	private String operationNS;
	private float nbrJoursCongeAvantModif;
	private String remplacantNS;
	private String resteDispoApresNS;
	private String nbrjourscongenorinitNS;
	private String nbrjourscongespeinitNS;
	private String nbrjourscongeouvrablestotalInitNS; 
	private String annulerCreerNS; 
	private static final long serialVersionUID = 4269810337595101111L;
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	
	
	private String droitAnnuelleNS;
	private String reliquatDroitAnnuelleNS;
	private String soldeAnterieurNS;
	private String reliquatSoldeAnterieurNS;
	private String soldeanneEncoursNS;
	private String reliquatSoldeanneEncoursNS;
	
	
	private String droitAnnuelleInitNS;
	
	
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
			// get nom systeme des champ ADC_FRAG_CONG
			String ipAdresse = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IPADRESSE");
			getWorkflowInstance().setValue("URI", "http://"+ipAdresse+"/vdoc"+getWorkflowInstance().getURI());
			supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
			rhNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DIRECTEUR_RESSOURCES_HUMAINES");
			remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
			nbrJoursSoldesInitialNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_nbrJoursSoldesInitial");
			idCongeNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_IdCongeNor");
			idCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_IdCongeSpe");
			fragIdCongeNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_IDCONGNOR");
			fragIdCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_IDCONGSPE");
			codeCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DemandeConge");
			fragCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_CONG");
			demandeur = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Demandeur");
			fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_ALERTE");
			resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
			commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Commentaire");
			frag_remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_REMPLACANT");
			fragmotifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_MOTIFSPE");
			fragmotifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_MOTIFNOR");
			motifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MotifsSpe");
			motifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Motifs");
			dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DateReprise");
			dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DateEntree");
			dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DateRepriseSpe");
			dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DateEntreeSpe");
			alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Alerte");
			droitAnnCongNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DroitAnnuelConge");
			nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPrisConge");
			nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsSpeConge");
			remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
			// nbrJrTotalSoldeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");
			// nbrJrResteSoldeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
			momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortieSpe");
			momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntreeSpe");
			matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortieSpe");
			matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntreeSpe");
			nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsOuvrablesSpe");
			msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MsgErrPerSpe");
			supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
			// remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
			btnenvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Envoyer");
			naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_CongeNormalCombine");
			typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_TypeConge");
			nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPredefinis");
			momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortie");
			momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntree");
			// periodecongenorNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONG");
			nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsOuvrables");
			nbrjrcongdispoNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsDispoConge");
			justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_JustifAbscence");
			typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_TypeCongeSpecial");
			nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPredefinisSpe");
			// periodecongespeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGSPE");
			justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_JustifAbscenceSpe");
			frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_DONNE_CONG_COMBINE");
			// frag_nbrjrpredefNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
			matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
			matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
			msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MsgErrPerConge");
			operationNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_TypeOperation");
			lieuNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_LieuConge");
			remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
			
			nbrjourscongenorinitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsOuvrablesNorInit");
			nbrjourscongespeinitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsOuvrablesSpeInit");
			nbrjourscongeouvrablestotalInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPrisCongeInit");
			annulerCreerNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_AnnCreDemande"); 
			
			
			droitAnnuelleInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NBRJRCONGEINIT");
			droitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DroitAnnuelConge");
			reliquatDroitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsDispoConge");
			soldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
			reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_RESTESOLDEDISPOAPRES");
			soldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SOLDEANNENCOURS");
			reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SOLDEANNENCOURSAPMODIF");
			
			
			
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			codeVDOC = null;
			// cacher fragement
			getResourceController().showBodyBlock(fragIdCongeNorNS, false);
			getResourceController().showBodyBlock(fragIdCongeSpeNS, false);
			String codeCongex = (String) getWorkflowInstance().getValue(codeCongeNS);
			if (codeCongex == null)
			{
				getResourceController().showBodyBlock(fragCongeNS, false);
			}
			else
			{
				getResourceController().showBodyBlock(fragCongeNS, true);
				setCongeData();
				
				
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	
	public void setCalcule(){
//		float oldDroitAnnuelle = (float) getWorkflowInstance().getValue(droitAnnuelleNS);
		float oldSoldeAnterieur = (float) getWorkflowInstance().getValue(soldeAnterieurNS);
		float oldSoldeEnCours = (float) getWorkflowInstance().getValue(soldeanneEncoursNS);
		float droitAnnuelleInit = (float) getWorkflowInstance().getValue(droitAnnuelleInitNS);
		float nbrJrCongeOuvr = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
		
		if(oldSoldeEnCours<droitAnnuelleInit){
			
			float nvSANC = 0;
			float nvSANT = 0;
			if(nbrJrCongeOuvr+oldSoldeEnCours>droitAnnuelleInit){
				nvSANC = droitAnnuelleInit;
				nvSANT = nvSANT+nbrJrCongeOuvr-droitAnnuelleInit+oldSoldeEnCours;
			}
			else{
				nvSANC = nbrJrCongeOuvr+oldSoldeEnCours;
			}
			
			getWorkflowInstance().setValue(reliquatSoldeAnterieurNS, nvSANT);
			getWorkflowInstance().setValue(reliquatSoldeanneEncoursNS, nvSANC);
			
		}
		else if(oldSoldeEnCours==droitAnnuelleInit){
			float nvSANT = oldSoldeAnterieur + nbrJrCongeOuvr;
			getWorkflowInstance().setValue(reliquatSoldeAnterieurNS, nvSANT);
			getWorkflowInstance().setValue(reliquatSoldeanneEncoursNS, droitAnnuelleInit);
		}
		
	}
	
	
	public void controleAffichageMotif(String typecongeNSM, String fragmotifNSM, String motifNSM, String justifNSM, String dateCongeFinNSM)
	{
//		String typecongem = (String) getWorkflowInstance().getValue(typecongeNSM);
//		
//		if ("Maladie".equals(typecongem))
//		{
//			getResourceController().setMandatory(justifNSM, true);
//		}
//		else
//		{
//			getResourceController().setMandatory(justifNSM, false);
//		}
//		
//		if ("Divers".equals(typecongem))
//		{
//			getResourceController().showBodyBlock(fragmotifNSM, true);
//			getResourceController().setMandatory(motifNSM, true);
//			getResourceController().setMandatory(justifNSM, true);
//		}
//		else
//		{
//			getResourceController().showBodyBlock(fragmotifNSM, false);
//			getResourceController().setMandatory(motifNSM, false);
//			getResourceController().setMandatory(justifNSM, false);
//		}
		
//		if ("Maladie".equals(typecongem) || "Normal payé".equals(typecongem))
//		{
//			getResourceController().setEditable(dateCongeFinNSM, true);
//		}
//		else
//		{
//			getResourceController().setEditable(dateCongeFinNSM, false);
//		}
	}
	
	@SuppressWarnings(
	{
			"unchecked", "unused"
	})
	private void getValueFromOtherProcess(String refPro, String nsChampsDest, String nsChampsSource)
	{
		try
		{
			IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
			IContext processContext = iDirectoryModule.getContextByLogin("sysadmin");
			IOrganization organization = getDirectoryModule().getOrganization(processContext, "DefaultOrganization");
			IProject project = getProjectModule().getProject(processContext, "APPATTIJARI", organization);
			ICatalog catalog = getWorkflowModule().getCatalog(processContext, "GROUPE_DE_PROCESSUS_DE_GESTION_DE_RESSOURCES_HUMAINES", project);
			IWorkflow workflow = getWorkflowModule().getWorkflow(processContext, catalog, "DEM_CON_1.0");
			IViewController viewController = getWorkflowModule().getViewController(processContext);
			Collection<IWorkflowInstance> cWorkflowInstance = viewController.evaluate(workflow);
				for (IWorkflowInstance iWorkflowInstance : cWorkflowInstance)
				{
					// VERSION DE PROCESS 'GESTION DES OPERATIONS'
					String workflowName = iWorkflowInstance.getWorkflow().getName();
					if (workflowName.equalsIgnoreCase("DEM_CON_1.0"))
					{
						if (iWorkflowInstance.getValue(IProperty.System.REFERENCE) != null)
						{
							String ref = (String) iWorkflowInstance.getValue(IProperty.System.REFERENCE);
							if (ref.trim().equalsIgnoreCase(refPro.trim()))
							{
								// String motif = (String) iWorkflowInstance.getValue(nsChampsSource);
								getWorkflowInstance().setValue(nsChampsDest, iWorkflowInstance.getValue(nsChampsSource));
							}
						}
					}
				}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setLectureEcriture(boolean lecture)
	{
//		try
//		{
//			String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
//			String op = (String) getWorkflowInstance().getValue(operationNS);
//			if("Annulation".equals(op)){
//				getResourceController().setEditable(naturecongeNS, false);
//				getResourceController().setEditable(lieuNS, false);
//				getResourceController().setEditable(typecongeNS, false);
//				getResourceController().setEditable(typecongespeNS, false);
//				getResourceController().setEditable(dateCongeDebNS, false);
//				
//				getResourceController().setEditable(momentsortieNS, false);
//				//getResourceController().setEditable(momentsortieSpeNS, false);
//				getResourceController().setEditable(motifNorNS, false);
//				getResourceController().setEditable(motifSpeNS, false);
//				
//				getResourceController().setEditable(justifabsNS, false);
//				getResourceController().setEditable(justifabsspeNS, false);
//			}
//			else{
//				getResourceController().setEditable(naturecongeNS, true);
//				getResourceController().setEditable(lieuNS, true);
//				getResourceController().setEditable(typecongeNS, true);
//				getResourceController().setEditable(typecongespeNS, true);
//				
//				
//				getResourceController().setEditable(dateCongeDebNS, true);
//				
//				getResourceController().setEditable(momentsortieNS, true);
////				getResourceController().setEditable(momentsortieSpeNS, true);
//				
//				getResourceController().setEditable(justifabsNS, true);
//				getResourceController().setEditable(justifabsspeNS, true);
//			}
//			
//			
//			
//			
//			
//			
//			
//			String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
//			
//			if (("Maladie".equals(typeconge) || "Normal payé".equals(typeconge)) && "Annulation".equals(op))
//			{
//				getResourceController().setEditable(dateCongeFinNS, false);
//				getResourceController().setEditable(momententreeNS, false);
//			}
//			else if (("Maladie".equals(typeconge) || "Normal payé".equals(typeconge)) && !"Annulation".equals(op))
//			{
//				getResourceController().setEditable(dateCongeFinNS, true);
//				getResourceController().setEditable(momententreeNS, true);
//			}
//			
//			String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
//			
//			if (("Maladie".equals(typecongespe) || "Normal payé".equals(typecongespe)) && "Annulation".equals(op))
//			{
//				getResourceController().setEditable(dateCongeFinSpeNS, false);
//				getResourceController().setEditable(momententreeSpeNS, false);
//			}
//			else if (("Maladie".equals(typecongespe) || "Normal payé".equals(typecongespe)) && !"Annulation".equals(op))
//			{
//				getResourceController().setEditable(dateCongeFinSpeNS, true);
//				getResourceController().setEditable(momententreeSpeNS, true);
//			}
//			
//			
//			
//			
//			
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	
	
	public List<IUser> getSuperieurOf(String matriculeUser){
		List<IUser> superieurs = new ArrayList<IUser>();
		try{
			String loginVdocOfUser = matriculeUser;
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select p.loginVdoc "
					+ "from Superieur s , Personnel p , Personnel sup  "
					+ "where sup.loginVdoc = ? "
					+ "and s.PersonnelMatricule=sup.matricule "
					+ "and s.SupMatricule=p.matricule";
			st = connection.prepareStatement(req);
			st.setString(1, loginVdocOfUser);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				String loginSuperieur = rs.getString(1);
				IUser superieur = getWorkflowModule().getUserByLogin(loginSuperieur);
				superieurs.add(superieur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return superieurs;
	}
	
	public void setCongeData()
	{
		try
		{
			String codeDemandeConge = (String) getWorkflowInstance().getValue(codeCongeNS);
			
			// Préparer la requette sql
			String query = "SELECT CodeVdocDemandeConge,DateDemandeConge,IdConge,c.TypeConge,DateDeb,DateFin,NbrJoursOuvrables,MomSortie,MomEntre,Lieu,t.nbrJrDef,c.Personnelmatricule,"
					+ "c.supMatricule,c.remplacantMatricule,c.natureConge,c.maladieComptabilise "
					+ "FROM Conge c, TypeConge t where CodeVdocDemandeConge = ? " + "and c.TypeConge = t.TypeConge " + "order by DateDeb";
			st = connection.prepareStatement(query);
			st.setString(1, codeDemandeConge);
			ResultSet rs = st.executeQuery();
			java.sql.Date datesql = new java.sql.Date(0);
			int i = 0;
			
			float nbrJrNor = 0;
			float nbrJrSpe = 0;
			while (rs.next())
			{
				String natureConge = rs.getString(15);
				if (natureConge.equals("Non combiné"))
				{
					getResourceController().showBodyBlock(frag_conge_combineNS, false);
				}
				else
				{
					getResourceController().showBodyBlock(frag_conge_combineNS, true);
				}
				
				
				// get nombre dispo du congé
				
				String loginVdocOfUser = getWorkflowModule().getLoggedOnUser().getLogin();
				String req = "SELECT NombreJoursConge,NombrJoursDispo,reliquatAnneEnCours,FilialeIdFiliale,NombreJoursCongeAnnuel FROM Personnel where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setString(1, loginVdocOfUser);
				ResultSet rsx = st.executeQuery();
				String filiale = "";
				while (rsx.next())
				{
					getWorkflowInstance().setValue(droitAnnuelleNS, rsx.getFloat(1));
					getWorkflowInstance().setValue(soldeAnterieurNS, rsx.getFloat(2));
					getWorkflowInstance().setValue(soldeanneEncoursNS, rsx.getFloat(3));
					getWorkflowInstance().setValue(droitAnnuelleInitNS, rsx.getFloat(5)); 
					filiale = rsx.getString(4);
				}
				
				
				
				
				if ("attijariintermediation".equals(filiale))
				{
					getResourceController().showBodyBlock(frag_remplacantNS, true);
					IUser remplacant = getWorkflowModule().getUserByLogin(rs.getString(14));
					List<IUser> users = new ArrayList<>();
					users.add(remplacant);
					getWorkflowInstance().setValue(remplacantNS, users);
					
					
					// get superieur hierarchique
//					IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
//					users = new ArrayList<>();
//					users.add(sup);
					getWorkflowInstance().setValue(supHieNS, getSuperieurOf(loginVdocOfUser));
					getWorkflowInstance().save(supHieNS);
//					getResourceController().showBodyBlock("FRAG_SUP", false);
//					getWorkflowInstance().setValue("ADC_EmailNejjar", "");
					// get rh
					IUser rh = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_ATI"));
					users = new ArrayList<>();
					users.add(rh);
					getWorkflowInstance().setValue(rhNS, users);
					getWorkflowInstance().save(rhNS);
					
				}
//				else if ("attijariwb".equals(filiale))
//				{
//					getResourceController().showBodyBlock(frag_remplacantNS, true);
//					IUser remplacant = getWorkflowModule().getUserByLogin(rs.getString(14));
//					List<IUser> users = new ArrayList<>();
//					users.add(remplacant);
//					getWorkflowInstance().setValue(remplacantNS, users);
//					
//					
//					// get superieur hierarchique
////					IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
////					users = new ArrayList<>();
////					users.add(sup);
//					getWorkflowInstance().setValue(supHieNS, getSuperieurOf(loginVdocOfUser));
//					getWorkflowInstance().save(supHieNS);
////					getResourceController().showBodyBlock("FRAG_SUP", false);
////					getWorkflowInstance().setValue("ADC_EmailNejjar", "");
//					// get rh
//					IUser rh = getWorkflowModule().getUserByLogin("m.oirrach");
//					users = new ArrayList<>();
//					users.add(rh);
//					getWorkflowInstance().setValue(rhNS, users);
//					getWorkflowInstance().save(rhNS);
//					
//				}
				else
				{
					getResourceController().showBodyBlock(frag_remplacantNS, false);
					getWorkflowInstance().setValue(remplacantNS, null);
					
					// get superieur hierarchique
//					IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
//					IUser sup1 = getWorkflowModule().getLoggedOnUser().getManager();
					List<IUser> users = new ArrayList<>();
//					users.add(sup);
//					if(sup1 !=null)
//					users.add(sup1);
////					IUser sup2 = getWorkflowModule().getUserByLogin("d.nejjar");
////					users.add(sup2);
//					IUser connectedUser = getWorkflowModule().getLoggedOnUser();
//					if(!connectedUser.getLogin().equals(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DG_AFC"))){
//						IUser sup3 = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DG_AFC"));
//						users.add(sup3);
//						
//					}
					getWorkflowInstance().setValue(supHieNS, getSuperieurOf(loginVdocOfUser));
					getWorkflowInstance().save(supHieNS);
					
//					getResourceController().showBodyBlock("FRAG_SUP", true);
//					getWorkflowInstance().setValue("ADC_EmailNejjar", getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("EMAIL_SNI"));
					
					IUser rh = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC"));
					users = new ArrayList<>();
					users.add(rh);
					getWorkflowInstance().setValue(rhNS, users);
					getWorkflowInstance().save(rhNS);
				}
				
				
				if (i == 0)
				{
					
					IUser user = getWorkflowModule().getUserByLogin(rs.getString(12));
					IUser sup = getWorkflowModule().getUserByLogin(rs.getString(13));
					List<IUser> users = new ArrayList<>();
					users.add(sup);
					getWorkflowInstance().setValue(demandeur, user);
//					getWorkflowInstance().setValue(supHieNS, users);
					getWorkflowInstance().setValue(idCongeNorNS, rs.getString(3));
					getWorkflowInstance().setValue(typecongeNS, rs.getString(4));
					datesql = rs.getDate(5);
					 getWorkflowInstance().setValue(dateCongeDebNS, new Date(datesql.getTime()));
					datesql = rs.getDate(6);
					 getWorkflowInstance().setValue(dateCongeFinNS, new Date(datesql.getTime()));
					getWorkflowInstance().setValue(nbrjrouvrNS, rs.getFloat(7));
					getWorkflowInstance().setValue(nbrjourscongenorinitNS, rs.getFloat(7));
					getWorkflowInstance().setValue(momentsortieNS, rs.getString(8));
					getWorkflowInstance().setValue(momententreeNS, rs.getString(9));
					getWorkflowInstance().setValue(lieuNS, rs.getString(10));
					getWorkflowInstance().setValue(nbjrpredefNS, rs.getFloat(11));
					getWorkflowInstance().setValue(naturecongeNS, "Non combiné");
//					getValueFromOtherProcess(rs.getString(1), motifNorNS, "DEMCON_MOTIFNOR");
//					getValueFromOtherProcess(rs.getString(1), justifabsNS, "DEMCON_JUSTABSC");
					codeVDOC = rs.getString(1);
					//getValueFromOtherProcess(rs.getString(1), remplacantNS, remplacantNS);
					//getValueFromOtherProcess(rs.getString(1), dateCongeDebNS, "DEMCON_DATEREPRISE");
					//getValueFromOtherProcess(rs.getString(1), dateCongeFinNS, "DEMCON_DATEENTREE");
					//getValueFromOtherProcess(rs.getString(1), nbrjrouvrNS, "DEMCON_NBRJROUV");
//					getValueFromOtherProcess(rs.getString(1), nbrJrSoldeNS, "DEMCON_NBRJRSOLDCONG");
//					getValueFromOtherProcess(rs.getString(1), nbrJrNonSoldeNS, "DEMCON_NBRJRNSOLDCONG");
//					getValueFromOtherProcess(rs.getString(1), droitAnnCongNS, "DEMCON_DROITANNCONG");
//					getValueFromOtherProcess(rs.getString(1), nbrjrcongdispoNS, "DEMCON_NBRJRCONGDISPO");
					
					String typeCongeNor = rs.getString(4);
					boolean comptaNor = rs.getBoolean(16);
					if((typeCongeNor.equals("Divers")||typeCongeNor.equals("Maladie"))&&comptaNor==true){
						
						getResourceController().showBodyBlock("FRAGCOMPTASPE", true);
						getWorkflowInstance().setValue("ADC_COMPTASPE", "comptabilisé");
						nbrJrNor += rs.getFloat(7);
					}
					else if((typeCongeNor.equals("Divers")||typeCongeNor.equals("Maladie"))&&comptaNor==false){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("ADC_COMPTASPE", "non comptabilisé");
						nbrJrSpe += rs.getFloat(7);
					}
					else if(typeCongeNor.equals("Normal payé")){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("ADC_COMPTASPE", "comptabilisé");
						nbrJrNor += rs.getFloat(7);
					}
					else if(!(typeCongeNor.equals("Divers")||typeCongeNor.equals("Maladie")||typeCongeNor.equals("Normal payé"))&&comptaNor==false){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("ADC_COMPTASPE", "non comptabilisé");
						nbrJrSpe += rs.getFloat(7);
					}
					
					
					nbrJoursCongeAvantModif = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
					getWorkflowInstance().setValue(nbrJoursSoldesInitialNS, nbrJoursCongeAvantModif);
					
					
					
					
//					String remp = rs.getString(14);
//					if (remp == null)
//					{
//						getResourceController().showBodyBlock(frag_remplacantNS, false);
//					}
//					else
//					{
//						getResourceController().showBodyBlock(frag_remplacantNS, true);
//					}
					
					if("Normal payé".equals(rs.getString(4))){
						getWorkflowInstance().setValue(nbrjourscongeouvrablestotalInitNS, rs.getString(7));
					}
					else{
						getWorkflowInstance().setValue(nbrjourscongeouvrablestotalInitNS,0);
					}
					
					controleAffichageMotif(typecongeNS, fragmotifNorNS, motifNorNS, justifabsNS, dateCongeFinNS);
				}
				else if (i == 1 && natureConge.equals("Combiné"))
				{
					getWorkflowInstance().setValue(idCongeSpeNS, rs.getString(3));
					getWorkflowInstance().setValue(typecongespeNS, rs.getString(4));
					datesql = rs.getDate(5);
					getWorkflowInstance().setValue(dateCongeDebSpeNS, new Date(datesql.getTime()));
					datesql = rs.getDate(6);
					getWorkflowInstance().setValue(dateCongeFinSpeNS, new Date(datesql.getTime()));
					getWorkflowInstance().setValue(nbrjrouvrSpeNS, rs.getFloat(7));
					getWorkflowInstance().setValue(nbrjourscongespeinitNS, rs.getFloat(7));
					getWorkflowInstance().setValue(momentsortieSpeNS, rs.getString(8));
					getWorkflowInstance().setValue(momententreeSpeNS, rs.getString(9));
					getWorkflowInstance().setValue(nbjrpredefspeNS, rs.getFloat(11));
					getWorkflowInstance().setValue(naturecongeNS, "Combiné");
//					getValueFromOtherProcess(rs.getString(1), remplacantNS, remplacantNS);
					//getValueFromOtherProcess(rs.getString(1), dateCongeDebSpeNS, "DEMCON_DATEREPRISESPE");
					//getValueFromOtherProcess(rs.getString(1), dateCongeFinSpeNS, "DEMCON_DATEENTREESPE");
//					getValueFromOtherProcess(rs.getString(1), motifSpeNS, "DEMCON_MOTIFSPE");
//					getValueFromOtherProcess(rs.getString(1), justifabsspeNS, "DEMCON_JUSTABSCSPE");
					//getValueFromOtherProcess(rs.getString(1), nbrjrouvrSpeNS, "DEMCON_NBRJROUVSPE");
					
					
					
					if("Normal payé".equals(rs.getString(4))){
						getWorkflowInstance().setValue(nbrjourscongeouvrablestotalInitNS, rs.getString(7));
					}
					else{
						getWorkflowInstance().setValue(nbrjourscongeouvrablestotalInitNS,0);
					}
					
					String typeCongeSpe = rs.getString(4);
					boolean comptaNor = rs.getBoolean(16);
					if((typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie"))&&comptaNor==true){
						
						getResourceController().showBodyBlock("FRAGCOMPTASPE", true);
						getWorkflowInstance().setValue("ADC_COMPTASPE", "comptabilisé");
						nbrJrNor += rs.getFloat(7);
					}
					else if((typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie"))&&comptaNor==false){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("ADC_COMPTASPE", "non comptabilisé");
						nbrJrSpe += rs.getFloat(7);
					}
					else if(typeCongeSpe.equals("Normal payé")){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("ADC_COMPTASPE", "comptabilisé");
						nbrJrNor += rs.getFloat(7);
					}
					else if(!(typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie")||typeCongeSpe.equals("Normal payé"))&&comptaNor==false){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("ADC_COMPTASPE", "non comptabilisé");
						nbrJrSpe += rs.getFloat(7);
					}
					
					
//					controleAffichageMotif(typecongespeNS, fragmotifSpeNS, motifSpeNS, justifabsspeNS, dateCongeFinSpeNS);
				}
				
				i++;
			}
			
			getWorkflowInstance().setValue(nbrJrSoldeNS, nbrJrNor);
			getWorkflowInstance().setValue(nbrJrNonSoldeNS, nbrJrSpe);
			setCalcule();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		try
		{
			if (property.getName().equals(codeCongeNS))
			{
				String codeCongex = (String) getWorkflowInstance().getValue(codeCongeNS);
				if (codeCongex == null)
				{
					getResourceController().showBodyBlock(fragCongeNS, false);
				}
				else
				{
					getResourceController().showBodyBlock(fragCongeNS, true);
					setCongeData();
//					String op = (String) getWorkflowInstance().getValue(operationNS);
//					if ("Annulation".equals(op))
//					{
//						setLectureEcriture(false);
//						
//					}
//					else
//					{
//						setLectureEcriture(true);
//					}
					
					String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
					if(natureConge.equals("Non combiné")){
						getWorkflowInstance().setValue(idCongeSpeNS, null);
						getWorkflowInstance().setValue(typecongespeNS,null);
						getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
						getWorkflowInstance().setValue(momentsortieSpeNS,"Matin");
						getWorkflowInstance().setValue(momententreeSpeNS,"Matin");
						getWorkflowInstance().setValue(nbjrpredefspeNS, 0f);
						getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
						getWorkflowInstance().setValue(dateCongeFinSpeNS,null);
						getWorkflowInstance().setValue(motifSpeNS, null);
						getWorkflowInstance().setValue(justifabsspeNS, null);
						getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
					}
				}
				
			}
			
//			if (property.getName().equals(operationNS))
//			{
//				String op = (String) getWorkflowInstance().getValue(operationNS);
//				if (op.equals("Annulation"))
//				{
//					setCongeData();
////					setLectureEcriture(false);
//					
//				}
//				else
//				{
//					setLectureEcriture(true);
//				}
//			}
			
			String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
			String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
			getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
			getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
			if (property.getName().equals(naturecongeNS))
			{
				if (natureconge.equals("Non combiné"))
				{
					getResourceController().showBodyBlock(frag_conge_combineNS, false);
//					getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
				}
				else
				{
//					Date datefin1 = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
//					getWorkflowInstance().setValue(dateCongeDebSpeNS, datefin1);
					getResourceController().showBodyBlock(frag_conge_combineNS, true);
					//Period periodCongeNor = (Period) getWorkflowInstance().getValue(periodecongenorNS);
				//	Date dateFin = periodCongeNor.getEndDate();
				//	Period periodCongeSpe = new Period(dateFin, dateFin);
					//getWorkflowInstance().setValue(periodecongespeNS, periodCongeSpe);
					
//					if("Maladie".equals(typecongespe)||"Normal payé".equals(typecongespe)){
//						getResourceController().setEditable(momententreeSpeNS, true);
//					}
//					else{
//						getResourceController().setEditable(momententreeSpeNS, false);
//					}
				}
			}
			
			
			if (property.getName().equals(typecongespeNS))
			{
				
//				getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
//				getWorkflowInstance().setValue(dateCongeFinSpeNS, null);
//				getValueFromOtherProcess(codeVDOC, dateCongeDebSpeNS, "DEMCON_DATEREPRISESPE");
//				getValueFromOtherProcess(codeVDOC, dateCongeFinSpeNS, "DEMCON_DATEENTREESPE");
				
				//String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS); 
//				if("Maladie".equals(typecongespe)||"Normal payé".equals(typecongespe)){
//					getResourceController().setEditable(momententreeSpeNS, true);
//				}
//				else{
//					getResourceController().setEditable(momententreeSpeNS, false);
//				}
//				
//				if(natureconge.equals("Combiné")){
//					Date datefin1 = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
//					getWorkflowInstance().setValue(dateCongeDebSpeNS, datefin1);
//				}
//				
//				getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
//				
//				
//				
//				
//			
//				
//				if ("Maladie".equals(typecongespe) )
//				{
//					//getResourceController().setMandatory(justifabsspeNS, true);
//				}
//				else
//				{
//					//getResourceController().setMandatory(justifabsspeNS, false);
//				}
//				
				
//				if ("Maladie".equals(typecongespe)  || "Normal payé".equals(typecongespe))
//				{
//					getResourceController().setEditable(dateCongeFinSpeNS, true);
//					//getResourceController().setEditable(momententreeSpeNS, false);
//					//getResourceController().setEditable(momentsortieSpeNS, true);
//				}
//				else
//				{
//					getResourceController().setEditable(dateCongeFinSpeNS, false);
//					//getResourceController().setEditable(momententreeSpeNS, false);
//					//getResourceController().setEditable(momentsortieSpeNS, false);
//				}
				
				// get nombre prédéfini du type du congé
				
				String req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
				st = connection.prepareStatement(req);
				st.setString(1, typecongespe);
				ResultSet rs = st.executeQuery();
				while (rs.next())
				{
					getWorkflowInstance().setValue(nbjrpredefspeNS, rs.getFloat(1));
				}
				
				// Period periodCongeNor = (Period) getWorkflowInstance().getValue(periodecongenorNS);
				// Date dateFin = periodCongeNor.getEndDate();
				// Period periodCongeSpe = new Period(dateFin, dateFin);
				// getWorkflowInstance().setValue(periodecongespeNS,periodCongeSpe);
			}
			
			if (property.getName().equals(typecongeNS))
			{
//				getWorkflowInstance().setValue(dateCongeDebNS, null);
//				getWorkflowInstance().setValue(dateCongeFinNS, null);
//				getValueFromOtherProcess(codeVDOC, dateCongeDebNS, "DEMCON_DATEREPRISE");
//				getValueFromOtherProcess(codeVDOC, dateCongeFinNS, "DEMCON_DATEENTREE");
				
				
//				getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
				String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
//				
//				if ("Normal payé".equals(typeconge) || typeconge == null || "Maladie".equals(typeconge))
//				{
//					getResourceController().showBodyBlock(frag_nbrjrpredefNS, false);
//					getResourceController().setEditable(momententreeNS, true);
//					getWorkflowInstance().setValue(momententreeNS, getWorkflowInstance().getValue(momentsortieNS));
//					
//				}
//				else
//				{
//					getResourceController().showBodyBlock(frag_nbrjrpredefNS, true);
//					getResourceController().setEditable(momententreeNS, false);
//					getWorkflowInstance().setValue(momententreeNS, getWorkflowInstance().getValue(momentsortieNS));
//				}
//				
//				if ("Maladie".equals(typeconge) )
//				{
//					//getResourceController().setMandatory(justifabsNS, true);
//				}
//				else
//				{
//					//getResourceController().setMandatory(justifabsNS, false);
//				}
//				
//				
//				if ("Maladie".equals(typeconge) || "Normal payé".equals(typeconge))
//				{
//					getResourceController().setEditable(dateCongeFinNS, true);
//				}
//				else
//				{
//					getResourceController().setEditable(dateCongeFinNS, false);
//				}
//				
				// get nombre prédéfini du type du congé
				
				String req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
				st = connection.prepareStatement(req);
				st.setString(1, typeconge);
				ResultSet rs = st.executeQuery();
				while (rs.next())
				{
					getWorkflowInstance().setValue(nbjrpredefNS, rs.getFloat(1));
				}
			}
			
//			String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
//			if ("Maladie".equals(typeconge) || "Normal payé".equals(typeconge))
//			{
//				Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
//				getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
//				if (property.getName().equals(dateCongeDebNS) || property.getName().equals(dateCongeFinNS))
//				{
//					Date dateFin = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
//					if (dateDeb != null && dateFin != null)
//					{
//						
//						
//						if(natureconge.equals("Combiné")){
//							getWorkflowInstance().setValue(dateCongeDebSpeNS, getWorkflowInstance().getValue(dateCongeFinNS));
//						}
//						else{
//							getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
//						}
//						if (dateDeb.after(dateFin))
//						{
//							getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "La date de reprise est antérieur à la date d'entrée");
//							getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
//						}
//						else
//						{
//							matinApmidisortieNS = "Matin";
//							matinApmidientreeNS = "Matin";
//							getWorkflowInstance().setValue(momententreeNS, "Matin");
//							getWorkflowInstance().setValue(momentsortieNS, "Matin");
//							float days = 0;
//							if (dateDeb != null && dateFin != null)
//							{
//								String typedeconge = (String) getWorkflowInstance().getValue(typecongeNS);
//								if("Maternité".equals(typedeconge)){
//									days = nbJours(dateDeb, dateFin, true, true, true, true, true, true, true, true);
//								}
//								else{
//									days = nbJours(dateDeb, dateFin, false, true, true, true, true, true, false, false);
//								}
//							}
//							
//							String momentSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
//							String momentEntree = (String) getWorkflowInstance().getValue(momententreeNS);
//							// if(!matinApmidisortie.equals(momentSortie)){
//							if ("Après midi".equals(momentSortie))
//								days -= 0.5;
//							// }
//							
//							// if(!matinApmidientree.equals(momentEntree)){
//							if ("Après midi".equals(momentEntree))
//								days += 0.5;
//							// }
//							
//							getWorkflowInstance().setValue(nbrjrouvrNS, (float)days);
//							matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
//							matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
//							float nbrCongeDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
//							float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
//							if (nbrCongeDispo < nbrJrCongeOuvrable)
//							{
//								getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé dépasse le solde de votre congé.");
//							}
//							else
//							{
//								getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
//							}
//							
//							float nbrCongePredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
//							if (nbrCongePredef != nbrJrCongeOuvrable && nbrCongePredef != 0)
//							{
//								getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
//								getResourceController().alert("Le nombre de jours demandé n'est pas authentique au solde séléctionnée");
//							}
//							else if (nbrCongePredef != 0)
//							{
//								getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
//							}
//							
//						}
//					}
//					else{
//						getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
//					}
//				}
//			}
//			else if (!("Maladie".equals(typeconge) || "Normal payé".equals(typeconge)))
//			{
//				if (property.getName().equals(dateCongeDebNS) /*|| property.getName().equals(dateCongeFinNS)*/)
//				{
//					
//					float nbJoursPredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
//					GregorianCalendar calendar = new java.util.GregorianCalendar();
//					Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
//					
//					
//					if(dateDeb !=null ){
//						
//						if(natureconge.equals("Combiné")){
//							getWorkflowInstance().setValue(dateCongeDebSpeNS, getWorkflowInstance().getValue(dateCongeFinNS));
//						}
//						else{
//							getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
//						}
//						
//						matinApmidisortieNS = "Matin";
//						matinApmidientreeNS = "Matin";
//						getWorkflowInstance().setValue(momententreeNS, "Matin");
//						getWorkflowInstance().setValue(momentsortieNS, "Matin");
//						calendar.setTime(dateDeb);
//						// RÉCUPÉRATION DES JOURS FÉRIÉS
//						List<Date> joursFeries = new ArrayList<Date>();
//						int yeardeb = calendar.get(Calendar.YEAR);
//						int yearfin = calendar.get(Calendar.YEAR);
//						for (int i = yeardeb - 1; i <= yearfin; i++)
//						{
//							joursFeries.addAll(getJourFeries(i));
//						}
//						
//						
//						boolean priseCompteDimanche = false;
//						boolean priseCompteLundi = true;
//						boolean priseCompteMardi = true;
//						boolean priseCompteMercredi = true;
//						boolean priseCompteJeudi = true;
//						boolean priseCompteVendredi = true;
//						boolean priseCompteSamedi = false;
//						
//						Boolean[] joursPrisEncompte = new Boolean[]
//								{
//										priseCompteDimanche, priseCompteLundi, priseCompteMardi, priseCompteMercredi, priseCompteJeudi, priseCompteVendredi, priseCompteSamedi
//								};
//						
//						
//						Date dateCalcul = dateDeb;
//						GregorianCalendar calendarDateCalcul = new GregorianCalendar();
//						calendarDateCalcul.setTime(dateCalcul);
//						calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, -1);
//						for(int i=0;i<=nbJoursPredef;i++){
//							
//							calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, 1);
//							dateCalcul = calendarDateCalcul.getTime();
//							GregorianCalendar dateToCompare  = new GregorianCalendar();
//							boolean test = false;// joursFeries.contains(date1);
//							for (Date ligne : joursFeries)
//							{
//								dateToCompare.setTime(ligne);
//								boolean testyear = calendarDateCalcul.get(calendarDateCalcul.YEAR) == dateToCompare.get(dateToCompare.YEAR);
//								boolean testmonth = calendarDateCalcul.get(calendarDateCalcul.MONTH) == dateToCompare.get(dateToCompare.MONTH);
//								boolean testday = calendarDateCalcul.get(calendarDateCalcul.DATE) == dateToCompare.get(dateToCompare.DATE);
//								
//								if (testyear == true && testmonth == true && testday == true)
//								{
//									test = true;
//								}
//							}
//							
//							if (test == false)
//							{
//								int chaine  = calendarDateCalcul.get(calendarDateCalcul.DAY_OF_WEEK) - 1;
//								if (joursPrisEncompte[chaine]==false){
//									nbJoursPredef++;
//								}
//									
//							}
//							else{
//								nbJoursPredef++;
//							}
//							
//						}
//						
//						getWorkflowInstance().setValue(dateCongeFinNS, dateCalcul);
//						getWorkflowInstance().setValue(nbrjrouvrNS, (float) getWorkflowInstance().getValue(nbjrpredefNS));
//						}
//					else{
//						getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
//					}
//					
//					
//				}
//				
//			}
//			
//		
//			//String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
//			if ("Maladie".equals(typecongespe) || "Normal payé".equals(typecongespe))
//			{
//				
//				Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
//				getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
//				if (property.getName().equals(dateCongeDebSpeNS) || property.getName().equals(dateCongeFinSpeNS))
//				{
//					Date dateFin = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
//					if (dateDeb != null && dateFin != null)
//					{
//						if(natureconge.equals("Combiné")){
//							getWorkflowInstance().setValue(dateCongeDebSpeNS, getWorkflowInstance().getValue(dateCongeFinNS));
//						}
//						else{
//							getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
//						}
//						
//						if (dateDeb.after(dateFin))
//						{
//							getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "La date de reprise est antérieur à la date d'entrée");
//							getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
//						}
//						else
//						{
//							matinApmidisortieSpeNS = "Matin";
//							matinApmidientreeSpeNS = "Matin";
//							getWorkflowInstance().setValue(momententreeSpeNS, getWorkflowInstance().getValue(momententreeNS));
//							getWorkflowInstance().setValue(momentsortieSpeNS, getWorkflowInstance().getValue(momententreeNS));
//							float days = 0;
//							if (dateDeb != null && dateFin != null)
//							{
//								String typedecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
//								if("Maternité".equals(typedecongespe)){
//									days = nbJours(dateDeb, dateFin, true, true, true, true, true, true, true, true);
//								}
//								else{
//									days = nbJours(dateDeb, dateFin, false, true, true, true, true, true, false, false);
//								}
//							}
//							
//							String momentSortie = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
//							String momentEntree = (String) getWorkflowInstance().getValue(momententreeSpeNS);
//							// if(!matinApmidisortie.equals(momentSortie)){
//							if ("Après midi".equals(momentSortie))
//								days -= 0.5;
//							// }
//							
//							// if(!matinApmidientree.equals(momentEntree)){
//							if ("Après midi".equals(momentEntree))
//								days += 0.5;
//							// }
//							
//							getWorkflowInstance().setValue(nbrjrouvrSpeNS, (float)days);
//							matinApmidisortieSpeNS = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
//							matinApmidientreeSpeNS = (String) getWorkflowInstance().getValue(momententreeSpeNS);
//							float nbrCongeDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
//							float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
//							if (nbrCongeDispo < nbrJrCongeOuvrable)
//							{
//								getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé dépasse le solde de votre congé.");
//							}
//							else
//							{
//								getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
//							}
//							
//							float nbrCongePredef = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
//							if (nbrCongePredef != nbrJrCongeOuvrable && nbrCongePredef != 0)
//							{
//								getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
//								getResourceController().alert("Le nombre de jours demandé n'est pas authentique au solde séléctionnée");
//							}
//							else if (nbrCongePredef != 0)
//							{
//								getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
//							}
//							
//						}
//					}
//					else{
//						getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
//					}
//				}
//			}
//			else if (!("Maladie".equals(typecongespe) || "Normal payé".equals(typecongespe)))
//			{
//				if (property.getName().equals(dateCongeDebSpeNS) /*|| property.getName().equals(dateCongeFinNS)*/)
//				{
//					float nbJoursPredef = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
//					GregorianCalendar calendar = new java.util.GregorianCalendar();
//					Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
//					if(dateDeb!=null){
//						matinApmidisortieSpeNS = "Matin";
//						matinApmidientreeSpeNS = "Matin";
//						getWorkflowInstance().setValue(momententreeSpeNS, (String) getWorkflowInstance().getValue(momententreeNS));
//						getWorkflowInstance().setValue(momentsortieSpeNS, (String) getWorkflowInstance().getValue(momententreeNS));
//						calendar.setTime(dateDeb);
//						// RÉCUPÉRATION DES JOURS FÉRIÉS
//						List<Date> joursFeries = new ArrayList<Date>();
//						int yeardeb = calendar.get(Calendar.YEAR);
//						int yearfin = calendar.get(Calendar.YEAR);
//						for (int i = yeardeb - 1; i <= yearfin; i++)
//						{
//							joursFeries.addAll(getJourFeries(i));
//						}
//						
//						
//						boolean priseCompteDimanche = false;
//						boolean priseCompteLundi = true;
//						boolean priseCompteMardi = true;
//						boolean priseCompteMercredi = true;
//						boolean priseCompteJeudi = true;
//						boolean priseCompteVendredi = true;
//						boolean priseCompteSamedi = false;
//						
//						Boolean[] joursPrisEncompte = new Boolean[]
//								{
//										priseCompteDimanche, priseCompteLundi, priseCompteMardi, priseCompteMercredi, priseCompteJeudi, priseCompteVendredi, priseCompteSamedi
//								};
//						
//						
//						Date dateCalcul = dateDeb;
//						GregorianCalendar calendarDateCalcul = new GregorianCalendar();
//						calendarDateCalcul.setTime(dateCalcul);
//						calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, -1);
//						for(int i=0;i<=nbJoursPredef;i++){
//							
//							calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, 1);
//							dateCalcul = calendarDateCalcul.getTime();
//							GregorianCalendar dateToCompare  = new GregorianCalendar();
//							boolean test = false;// joursFeries.contains(date1);
//							for (Date ligne : joursFeries)
//							{
//								dateToCompare.setTime(ligne);
//								boolean testyear = calendarDateCalcul.get(calendarDateCalcul.YEAR) == dateToCompare.get(dateToCompare.YEAR);
//								boolean testmonth = calendarDateCalcul.get(calendarDateCalcul.MONTH) == dateToCompare.get(dateToCompare.MONTH);
//								boolean testday = calendarDateCalcul.get(calendarDateCalcul.DATE) == dateToCompare.get(dateToCompare.DATE);
//								
//								if (testyear == true && testmonth == true && testday == true)
//								{
//									test = true;
//								}
//							}
//							
//							if (test == false)
//							{
//								int chaine  = calendarDateCalcul.get(calendarDateCalcul.DAY_OF_WEEK) - 1;
//								if (joursPrisEncompte[chaine]==false){
//									nbJoursPredef++;
//								}
//									
//							}
//							else{
//								nbJoursPredef++;
//							}
//							
//						}
//						
//						getWorkflowInstance().setValue(dateCongeFinSpeNS, dateCalcul);
//						getWorkflowInstance().setValue(nbrjrouvrSpeNS, (float) getWorkflowInstance().getValue(nbjrpredefspeNS));
//					}
//					else{
//						getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
//					}
//					
//					
//				}
//				
//			}
//			
//			if (property.getName().equals(momententreeNS) || property.getName().equals(momentsortieNS))
//			{
//				String momentSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
//				String momentEntree = (String) getWorkflowInstance().getValue(momententreeNS);
//				
//				float days = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
//				Date datedeb1 = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
//				Date datefin1 = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
//				
//				if(natureconge.equals("Combiné")){
//					getWorkflowInstance().setValue(momentsortieSpeNS, momentEntree);
//					getWorkflowInstance().setValue(momententreeSpeNS, momentEntree);
//					
//					if("Maladie".equals(typeconge)||"Normal payé".equals(typeconge)){
////						getResourceController().setEditable(momententreeNS, true);
//						if (!matinApmidisortieNS.equals(momentSortie) && days != 0 && !datedeb1.equals(datefin1))
//						{
//							if ("Après midi".equals(momentSortie))
//								days -= 0.5f;
//							else
//								days += 0.5f;
//						}
//						
//						if (!matinApmidientreeNS.equals(momentEntree) && days != 0 && !datedeb1.equals(datefin1))
//						{
//							if ("Après midi".equals(momentEntree))
//								days += 0.5f;
//							else
//								days -= 0.5f;
//						}
//					}
//					else{
////						getResourceController().setEditable(momententreeNS, false);
//						if(property.getName().equals(momentsortieNS)){
//						getWorkflowInstance().setValue(momententreeNS, getWorkflowInstance().getValue(momentsortieNS));
//						}
//					}
//				}else{
////					getResourceController().setEditable(momententreeNS, true);
//					if("Maladie".equals(typeconge)||"Normal payé".equals(typeconge)){
//						if (!matinApmidisortieNS.equals(momentSortie) && days != 0 && !datedeb1.equals(datefin1))
//						{
//							if ("Après midi".equals(momentSortie))
//								days -= 0.5f;
//							else
//								days += 0.5f;
//						}
//						
//						if (!matinApmidientreeNS.equals(momentEntree) && days != 0 && !datedeb1.equals(datefin1))
//						{
//							if ("Après midi".equals(momentEntree))
//								days += 0.5f;
//							else
//								days -= 0.5f;
//						}
//						
//					}else{
//						//if(natureconge.equals("Combiné")){
////						getResourceController().setEditable(momententreeNS, false);
//						getWorkflowInstance().setValue(momententreeNS, getWorkflowInstance().getValue(momentsortieNS));
//						//}
//					}
//					
//				}
//				
//				
//				
//				if(datedeb1!=null&&datefin1!=null){
//					Calendar dateDebC = Calendar.getInstance();
//					dateDebC.setTime(datedeb1);
//					Calendar dateFinC = Calendar.getInstance();
//					dateFinC.setTime(datefin1);
//					if(dateDebC.get(dateDebC.YEAR) == dateFinC.get(dateFinC.YEAR) && dateDebC.get(dateDebC.MONTH) == dateFinC.get(dateFinC.MONTH) && dateDebC.get(dateDebC.DATE) == dateFinC.get(dateFinC.DATE)){
//						if (!matinApmidisortieNS.equals(momentSortie) )
//						{
//							if ("Après midi".equals(momentSortie))
//								days -= 0.5f;
//							else
//								days += 0.5f;
//						}
//						
//						if (!matinApmidientreeNS.equals(momentEntree) )
//						{
//							if ("Après midi".equals(momentEntree))
//								days += 0.5f;
//							else
//								days -= 0.5f;
//						}
//						
//						if ("Après midi".equals(momentSortie)&&"Matin".equals(momentEntree) && property.getName().equals(momentsortieNS)){
//							days += 0.5f;
//						}
//						else if ("Matin".equals(momentSortie)&&"Matin".equals(momentEntree) && property.getName().equals(momentsortieNS)){
//							days -= 0.5f;
//						}	
//						
//						if(("Après midi".equals(momentSortie)&&"Matin".equals(momentEntree))||
//						   ("Après midi".equals(momentSortie)&&"Après midi".equals(momentEntree)) ||
//						   ("Matin".equals(momentSortie)&&"Matin".equals(momentEntree))){
//							days=0;
//						}
//						
////						if ("Après midi".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
////							days += 0.5f;
////						}
////						else if ("Matin".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
////							days -= 0.5f;
////						}	
//					}
//				}
//				
//				
//				getWorkflowInstance().setValue(nbrjrouvrNS,(float) days);
//				matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
//				matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
//				// float nbrCongeDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
//				float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
//				// if(nbrCongeDispo<nbrJrCongeOuvrable){
//				// getWorkflowInstance().setValue(msgErreurPeriodeCongeNS,"Le nombre de jours demandé dépasse le solde de votre congé.");
//				// }
//				// else{
//				// getWorkflowInstance().setValue(msgErreurPeriodeCongeNS,"");
//				// }
//				
//				float nbrCongePredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
//				if (nbrCongePredef != nbrJrCongeOuvrable && nbrCongePredef != 0)
//				{
//					getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
//					getResourceController().alert("Le nombre de jours demandé n'est pas authentique au solde séléctionnée");
//				}
//				else
//				{
//					getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
//				}
//				
//				
//				
//			}
//			
//			if (property.getName().equals(momententreeSpeNS) || property.getName().equals(momentsortieSpeNS))
//			{
//				String momentSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
//				String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
//				float days = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
//				if (!matinApmidisortieSpeNS.equals(momentSortieSpe) && days != 0 && "Combiné".equals(natureconge))
//				{
//					if ("Après midi".equals(momentSortieSpe))
//						days -= 0.5f;
//					else
//						days += 0.5f;
//				}
//				
//				if (!matinApmidientreeSpeNS.equals(momentEntreeSpe) && days != 0 && "Combiné".equals(natureconge))
//				{
//					if ("Après midi".equals(momentEntreeSpe))
//						days += 0.5f;
//					else
//						days -= 0.5f;
//				}
//				Date datedebSpe1 = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
//				Date datefinSpe1 = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
//				if(datedebSpe1!=null&&datefinSpe1!=null)
//				if(datedebSpe1.equals(datefinSpe1)){
//					if ("Après midi".equals(momentEntreeSpe)&& "Matin".equals(momentSortieSpe)){
//						days += 0.5f;
//					}
//				}
//				
//				getWorkflowInstance().setValue(nbrjrouvrSpeNS, (float)days);
//				matinApmidisortieSpeNS = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
//				matinApmidientreeSpeNS = (String) getWorkflowInstance().getValue(momententreeSpeNS);
//				
//				float nbrCongeDispoSpe = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
//				float nbrJrCongeOuvrableSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
//				if (nbrCongeDispoSpe < nbrJrCongeOuvrableSpe && natureconge.equals("Combiné"))
//				{
//					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé dépasse le solde de votre congé.");
//				}
//				else
//				{
//					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
//				}
//				
//				float nbrCongePredefSpe = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
//				if (nbrCongePredefSpe != nbrJrCongeOuvrableSpe && nbrCongePredefSpe != 0 && natureconge.equals("Combiné"))
//				{
//					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
//					getResourceController().alert("Le nombre de jours demandé n'est pas authentique au solde séléctionnée");
//				}
//				else
//				{
//					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
//				}
//			}
//			
//			if (property.getName().equals(naturecongeNS) || property.getName().equals(momententreeSpeNS) || property.getName().equals(momentsortieSpeNS) || property.getName().equals(typecongeNS)
//					|| property.getName().equals(typecongespeNS) || property.getName().equals(resteJoursCongeNS) || property.getName().equals(dateCongeDebNS)
//					|| property.getName().equals(dateCongeDebSpeNS) || property.getName().equals(nbrjrouvrNS) || property.getName().equals(dateCongeFinNS)
//					|| property.getName().equals(dateCongeFinSpeNS) || property.getName().equals(nbrjrouvrSpeNS) || property.getName().equals(momententreeNS)
//					|| property.getName().equals(momentsortieNS))
//			{
//				// calculate nombre congé soldé
//				typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
//				String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
//				float nbrJourDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
//				float nbrJour = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
//				float nbrJourSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
//				if (typeconge != null)
//				{
//					if (!typeconge.equals(typecongeSpe))
//					{
//						getWorkflowInstance().setValue(nbrJrSoldeNS, 0f);
//						getWorkflowInstance().setValue(nbrJrNonSoldeNS, 0f);
//						if ("Combiné".equals(natureconge) && typeconge != null)
//						{
//							if ("Normal payé".equals(typeconge) )
//							{
//								getWorkflowInstance().setValue(nbrJrSoldeNS, (float)nbrJour);
//								
//								if ("Normal payé".equals(typecongeSpe) )
//								{
//									
//									getWorkflowInstance().setValue(nbrJrSoldeNS, (float)(nbrJourSpe + nbrJour));
//								}
//								else
//								{
//									getWorkflowInstance().setValue(nbrJrNonSoldeNS,(float) nbrJourSpe);
//								}
//							}
//							else
//							{
//								getWorkflowInstance().setValue(nbrJrNonSoldeNS, nbrJour);
//								
//								if ("Normal payé".equals(typecongeSpe) )
//								{
//									
//									getWorkflowInstance().setValue(nbrJrSoldeNS, (float)nbrJourSpe);
//								}
//								else
//								{
//									getWorkflowInstance().setValue(nbrJrNonSoldeNS, (float)(nbrJourSpe + nbrJour));
//								}
//							}
//							
//						}
//						
//						else
//						{
//							if ("Normal payé".equals(typeconge) )
//							{
//								getWorkflowInstance().setValue(nbrJrSoldeNS, (float)nbrJour);
//							}
//							else
//							{
//								getWorkflowInstance().setValue(nbrJrNonSoldeNS,(float) nbrJour);
//							}
//							if (("Normal payé".equals(typecongeSpe) ) && "Combiné".equals(natureconge))
//							{
//								getWorkflowInstance().setValue(nbrJrSoldeNS, 0f + (float)nbrJour);
//							}
//							else if (!("Normal payé".equals(typecongeSpe)) && "Combiné".equals(natureconge))
//							{
//								getWorkflowInstance().setValue(nbrJrNonSoldeNS, 0f + (float)nbrJour);
//							}
//						}
//						
//						if ("Maladie".equals(typeconge))
//						{
//							float resteConge = nbrJourDispo - (nbrJourSpe + nbrJourSpe);
//							if (resteConge < 0)
//							{
//								getResourceController().setMandatory(commentaireNS, true);
//							}
//							else
//							{
//								getResourceController().setMandatory(commentaireNS, false);
//							}
//						}
//						else
//						{
//							getResourceController().setMandatory(commentaireNS, false);
//						}
//						
//					}
//				}
//				else
//				{
//					getWorkflowInstance().setValue(nbrJrSoldeNS, 0f);
//					getWorkflowInstance().setValue(nbrJrNonSoldeNS, 0f);
//				}
//				
//			}
			
			
			float reste = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
			if (reste < 0)
			{
				getResourceController().setMandatory(commentaireNS, true);
				getWorkflowInstance().setValue(alerteNS, "Le reste des jours de congé est inférieur à 0");
				getResourceController().showBodyBlock(fragAlerteNS, true);
			}
			else
			{
				getResourceController().setMandatory(commentaireNS, false);
				getWorkflowInstance().setValue(alerteNS, "");
				getResourceController().showBodyBlock(fragAlerteNS, false);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		super.onPropertyChanged(property);
	}
	
	@SuppressWarnings("static-access")
	public float nbJours(Date d1, Date d2, boolean notionJourFerie, boolean priseCompteLundi, boolean priseCompteMardi, boolean priseCompteMercredi, boolean priseCompteJeudi,
			boolean priseCompteVendredi, boolean priseCompteSamedi, boolean priseCompteDimanche)
	{
		
		if (d2.compareTo(d1) <= 0)
			return 0;
		
		// Tableau des jours a prendre en compte
		Boolean[] joursPrisEncompte = new Boolean[]
		{
				priseCompteDimanche, priseCompteLundi, priseCompteMardi, priseCompteMercredi, priseCompteJeudi, priseCompteVendredi, priseCompteSamedi
		};
		
		Calendar date1 = Calendar.getInstance();
		date1.setTime(d1);
		Calendar date2 = Calendar.getInstance();
		date2.setTime(d2);
		
		// Récupération des jours fériés
		List<Date> joursFeries = new ArrayList<Date>();
		int yeardeb = date1.get(Calendar.YEAR);
		int yearfin = date2.get(Calendar.YEAR);
		for (int i = yeardeb - 1; i <= yearfin; i++)
		{
			joursFeries.addAll(getJourFeries(i));
		}
		
		// Calcul du nombre de jour
		Calendar dateToCompare = Calendar.getInstance();
		
		float nbJour = 0;
		while (date1.before(date2) || (date1.get(date1.YEAR) == date2.get(date2.YEAR) && date1.get(date1.MONTH) == date2.get(date2.MONTH) && date1.get(date1.DATE) == date2.get(date2.DATE)))
		{
			boolean test = false;// joursFeries.contains(date1);
			for (Date ligne : joursFeries)
			{
				dateToCompare.setTime(ligne);
				boolean testyear = date1.get(date1.YEAR) == dateToCompare.get(dateToCompare.YEAR);
				boolean testmonth = date1.get(date1.MONTH) == dateToCompare.get(dateToCompare.MONTH);
				boolean testday = date1.get(date1.DATE) == dateToCompare.get(dateToCompare.DATE);
				if (testyear == true && testmonth == true && testday == true)
				{
					test = true;
				}
			}
			
			if (test == false)
			{
				
				if (joursPrisEncompte[date1.get(Calendar.DAY_OF_WEEK) - 1])
					nbJour++;
			}
			
			date1.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return nbJour - 1;
	}
	
	public static GregorianCalendar addDays(String date, Integer days)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar calendar = null;
		try
		{
			Date df = sdf.parse(date);
			calendar = new java.util.GregorianCalendar();
			calendar.setTime(df);
			calendar.add(Calendar.DAY_OF_MONTH, days);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar;
	}
	
	@SuppressWarnings("static-access")
	public List<Date> getJourFeries(int annee)
	{
		
		List<Date> datesFeries = new ArrayList<Date>();
		try
		{
			
			String req = "SELECT jfa.date_jourferie,jf.libelleJoursFerie,jf.nbr_jours_ferie FROM JourFerieAnnuelle jfa,JourFerie jf  where annee = ? and jfa.id_jourferie = jf.idJoursFerie";
			st = connection.prepareStatement(req);
			st.setInt(1, annee);
			ResultSet rs = st.executeQuery();
			
			while (rs.next())
			{
				Date date = new Date(rs.getDate(1).getTime());
				datesFeries.add(date);
				
				int nombreJoursDuFete = rs.getInt(3);
				if (nombreJoursDuFete == 2)
				{
					Date datePlus1 = date;
					Calendar ca = Calendar.getInstance();
					ca.setTime(datePlus1);
					int newjour = ca.get(ca.DATE) + 1;
					ca.set(ca.DATE, newjour);
					datePlus1 = new Date(ca.getTimeInMillis());
					datesFeries.add(datePlus1);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return datesFeries;
	}
	
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try
		{
//			if (action.getName().equals(btnenvoyerNS))
//			{
//				
//				String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
//				// float nbrCongeDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
//				float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
//				String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
//				String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
//				
//				if (typeconge.equals(typecongeSpe)&&natureconge.equals("Combiné"))
//				{
//					getResourceController().alert("Les types de congé doivent différés");
//					return false;
//				}
//				
//				// if(nbrCongeDispo<nbrJrCongeOuvrable && typeconge.equals("Normal payé")){
//				// getWorkflowInstance().setValue(msgErreurPeriodeCongeNS,"Le nombre de jours demandé dépasse le solde de votre congé.");
//				// getResourceController().alert("Le nombre de jours demandé dépasse le solde de votre congé.");
//				// return false;
//				// }
//				
//				float nbrCongePredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
//				if (nbrCongePredef != nbrJrCongeOuvrable && nbrCongePredef != 0)
//				{
//					getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
//					getResourceController().alert("Le nombre de jours demandé n'est pas authentique au solde séléctionnée");
//					return false;
//				}
//				
//				float nbrJrCongeOuvrableSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
//				float nbrCongePredefSpe = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
//				if (nbrCongePredefSpe != nbrJrCongeOuvrableSpe && nbrCongePredefSpe != 0 && natureconge.equals("Combiné") && !typecongeSpe.equals("Normal payé"))
//				{
//					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
//					getResourceController().alert("Le nombre de jours demandé n'est pas authentique au solde séléctionnée");
//					return false;
//				}
//				
//				if (nbrJrCongeOuvrable == 0 || (nbrJrCongeOuvrableSpe == 0 && natureconge.equals("Combiné")))
//				{
//					getResourceController().alert("Le nombre de jours de congé égal à 0. Veuillez modifier la période");
//					return false;
//				}
//				
//				Date dateFinNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
//				Date dateDebSpe = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
//				Date dateDebNor = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
//				Date dateFinSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
//				String congeannule = (String) getWorkflowInstance().getValue(codeCongeNS);
//				String login = getWorkflowModule().getLoggedOnUser().getLogin();
//				if(testDatesIftheyWasInConge(dateDebNor, login, congeannule)==false){
//					getResourceController().alert("La date début du premier congé désiré est en chevauchement avec un autre congé");
//					return false;
//				}
//				if(natureconge.equals("Non combiné")){
//					if(testDatesIftheyWasInCongeFin(dateFinNor, login, congeannule)==false){
//						getResourceController().alert("La date fin du premier congé désiré est en chevauchement avec un autre congé");
//						return false;
//					}
//				}
//				else{
//					if(testDatesIftheyWasInCongeFin(dateFinSpe, login, congeannule)==false){
//						getResourceController().alert("La date fin du deuxième congé désiré est en chevauchement avec un autre congé");
//						return false;
//					}
//				}
//				
//				
//				if (dateFinNor.before(dateDebNor))
//				{
//					getResourceController().alert("La date fin est antérieur à la date debut du congé");
//					return false;
//				}
//				
//				if (dateFinSpe != null && dateDebSpe != null)
//				{
//					if (dateFinSpe.before(dateDebSpe))
//					{
//						getResourceController().alert("La date fin est antérieur à la date debut du congé");
//						return false;
//					}
//				}
//				
//				float nbrJoursCongeApresModif = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
//				if (nbrJoursCongeApresModif > nbrJoursCongeAvantModif)
//				{
//					getResourceController().alert("le période doit être inférieur à " + nbrJoursCongeAvantModif);
//					return false;
//				}
//				
//				if (natureconge.equals("Combiné"))
//				{
//					if (dateDebSpe != null && dateFinNor != null)
//					{
//						GregorianCalendar cDateDebSpe = new GregorianCalendar();
//						cDateDebSpe.setTime(dateDebSpe);
//						GregorianCalendar cDateFinNor = new GregorianCalendar();
//						cDateFinNor.setTime(dateFinNor);
//						
//						if (!((cDateDebSpe.get(cDateDebSpe.YEAR) == cDateFinNor.get(cDateFinNor.YEAR)) && (cDateDebSpe.get(cDateDebSpe.MONTH) == cDateFinNor.get(cDateFinNor.MONTH)) && (cDateDebSpe
//								.get(cDateDebSpe.DATE) == cDateFinNor.get(cDateFinNor.DATE))))
//						{
//							getResourceController().alert("Il y a un chevauchement entre les périodes.");
//							return false;
//						}
//					}
//				}
//				
//				// float nbrJoursDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
//				// float nbrJoursSolde = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
//				// if(nbrJoursSolde>nbrJoursDispo){
//				// getResourceController().alert("Le nombre de jours soldés dépasse votre solde disponible");
//				// return false;
//				// }
//				
//				String mpEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
//				String mpSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
//				if (!mpEntreeNor.equals(mpSortieSpe) && natureconge.equals("Combiné"))
//				{
//					getResourceController().alert("il y a un chevauchement entre le moment d'entrée du premier congé et le moment de sortie du deuxième congé.");
//					return false;
//				}
//				
//				
//				
//				Date dateAujourdhui = new Date();
//				GregorianCalendar cdateAujourdhui = new GregorianCalendar();
//				cdateAujourdhui.setTime(dateAujourdhui);
//				GregorianCalendar cdateDebNor = new GregorianCalendar();
//				cdateDebNor.setTime(dateDebNor);
//				
//				
//				int yearNow = cdateAujourdhui.get(cdateAujourdhui.YEAR);
//				int monthNow = cdateAujourdhui.get(cdateAujourdhui.MONTH);
//				int dateNow = cdateAujourdhui.get(cdateAujourdhui.DATE);
//				
//				int yearDN = cdateDebNor.get(cdateDebNor.YEAR);
//				int monthDN = cdateDebNor.get(cdateDebNor.MONTH);
//				int dateDN = cdateDebNor.get(cdateDebNor.DATE);
//				
//				if(yearDN<=yearNow && monthDN <= monthNow && dateDN<=dateNow){
//					getResourceController().alert("Vous ne pouvez pas effectuer cette opération car vous avez commencer votre congé. Vous pouvez annuler via votre supérieur hiérarchique.");
//					return false;
//				}
//				
//			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return super.onBeforeSubmit(action);
	}
	
	public boolean testDatesIftheyWasInCongeFin(Date date,String user,String congeAnnuler){
		int i=0;
		try{
			
			String req = "select DateDeb,DateFin from Conge where ?  > DateDeb and  ? < DateFin and Personnelmatricule = ? and CodeVdocDemandeConge not like ?; ";
			st = connection.prepareStatement(req);
			java.sql.Date datesql = new java.sql.Date(date.getTime());
			st.setDate(1, datesql);
			st.setDate(2, datesql);
			st.setString(3, user);
			st.setString(4, congeAnnuler);
			ResultSet rs = st.executeQuery();
			
			while (rs.next())
			{
				i++;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		boolean test = i==0;
		return test;
	}
	
	public boolean testDatesIftheyWasInConge(Date date,String user,String congeAnnuler){
		int i=0;
		try{
			
			String req = "select DateDeb,DateFin from Conge where ?  >=DateDeb and  ? < DateFin and Personnelmatricule = ? and CodeVdocDemandeConge not like ?; ";
			st = connection.prepareStatement(req);
			java.sql.Date datesql = new java.sql.Date(date.getTime());
			st.setDate(1, datesql);
			st.setDate(2, datesql);
			st.setString(3, user);
			st.setString(4, congeAnnuler);
			ResultSet rs = st.executeQuery();
			
			while (rs.next())
			{
				i++;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		boolean test = i==0;
		return test;
	}
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try
		{
			if (action.getName().equals(btnenvoyerNS))
			{
				
				String codeVDOC = (String) getWorkflowInstance().getValue(codeCongeNS);
				String req = "update Conge set EtatConge = ? where CodeVdocDemandeConge = ?";
				st = connection.prepareStatement(req);
				String op = (String) getWorkflowInstance().getValue(operationNS);
				if(op.equals("Modification")){
					st.setString(1, "congé modifié");
				}
				else{
					st.setString(1, "congé annulé");
				}
				st.setString(2, codeVDOC);
				st.executeUpdate();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return super.onAfterSubmit(action);
	}
	
}
