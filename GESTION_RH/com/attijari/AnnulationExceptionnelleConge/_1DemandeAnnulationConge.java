package com.attijari.AnnulationExceptionnelleConge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import beans.Conge;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.serviceRH.ServiceRH;

import dao.SingletonConnexionBDD;

public class _1DemandeAnnulationConge extends BaseDocumentExtension
{
	int cpt = 0;
	private String rhNS;
	private String supHieNS;
	private String remplacantNS;
//	private String nbrJoursSoldesInitialNS;
//	private String codeVDOC;
//	private String demandeur;
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
//	private String periodecongenorNS;
	private String nbrjrouvrNS;
//	private String nbrjrcongdispoNS;
	private String justifabsNS;
	private String typecongespeNS;
	private String nbjrpredefspeNS;
//	private String periodecongespeNS;
	private String justifabsspeNS;
	private String frag_conge_combineNS;
//	private String frag_nbrjrpredefNS;
	private String matinApmidisortieNS;
	private String matinApmidientreeNS;
//	private String msgErreurPeriodeCongeNS;
//	private String msgErreurPeriodeCongeSpeNS;
	private String nbrjrouvrSpeNS;
//	private String matinApmidisortieSpeNS;
//	private String matinApmidientreeSpeNS;
	private String momentsortieSpeNS;
	private String momententreeSpeNS;
	private String nbrJrSoldeNS;
	private String nbrJrNonSoldeNS;
//	private String droitAnnCongNS;
	private String motifNorNS;
	private String motifSpeNS;
//	private String fragmotifNorNS;
//	private String fragmotifSpeNS;
	private String frag_remplacantNS;
//	private String resteJoursCongeNS;
//	private String commentaireNS;
//	private String alerteNS;
//	private String fragAlerteNS;
	private String fragCongeNS;
	private String codeCongeNS;
	private String fragIdCongeNorNS;
	private String fragIdCongeSpeNS;
	private String idCongeNorNS;
	private String idCongeSpeNS;
	private String lieuNS;
//	private String operationNS;
//	private float  nbrJoursCongeAvantModif;
	private String dateSortieExcepNS;
	private String momSortieExcepNS;
	private String dateEntreeExcepNS;
	private String momEntreeExcepNS;
	private String nbrJoursJoursTravailleNS;
	private String nbrJoursJoursTravailleSpecNS;
//	private String reliquatAnneEncours;
	
	private String droitAnnuelleNS;
//	private String reliquatDroitAnnuelleNS;
	private String soldeAnterieurNS;
	private String reliquatSoldeAnterieurNS;
	private String soldeanneEncoursNS;
	private String reliquatSoldeanneEncoursNS;
	
	
	private String droitAnnuelleInitNS;
	
	
	private static final long serialVersionUID = 4269810337595101111L;
//	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	
//	@SuppressWarnings("unchecked")
//	public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException
//	{
//		this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
//		return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
//	}
	
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
	
	public void setCalcule(){
//		float oldDroitAnnuelle = (float) getWorkflowInstance().getValue(droitAnnuelleNS);
		float oldSoldeAnterieur = (float) getWorkflowInstance().getValue(soldeAnterieurNS);
		float oldSoldeEnCours = (float) getWorkflowInstance().getValue(soldeanneEncoursNS);
		float droitAnnuelleInit = (float) getWorkflowInstance().getValue(droitAnnuelleInitNS);
		
		String comptabilisespe = (String) getWorkflowInstance().getValue("AEDC_COMPTASPE");
		String comptabilisenor = (String) getWorkflowInstance().getValue("AEDC_COMPTANOR");
//		String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
		String typePremierConge = (String) getWorkflowInstance().getValue(typecongeNS);
		String typeDeuxiemeConge = (String) getWorkflowInstance().getValue(typecongespeNS);
		
		
		float nbrJrCongeOuvr = 0;
		
		if(("Divers".equals(typePremierConge)||"Maladie".equals(typePremierConge))||("Divers".equals(typeDeuxiemeConge)||"Maladie".equals(typeDeuxiemeConge))){
			if(comptabilisenor==null && ("Divers".equals(typePremierConge)||"Maladie".equals(typePremierConge))){
				nbrJrCongeOuvr += 0;
			}else if(comptabilisenor!=null && ("Divers".equals(typePremierConge)||"Maladie".equals(typePremierConge))){
				if(comptabilisenor.equals("comptabilisé")){
					nbrJrCongeOuvr += (float) getWorkflowInstance().getValue(nbrJoursJoursTravailleNS);	
				}else{
					nbrJrCongeOuvr += 0;
				}
			}
			
			
			
			
			
			if(comptabilisespe==null && ("Divers".equals(typeDeuxiemeConge)||"Maladie".equals(typeDeuxiemeConge))){
				nbrJrCongeOuvr += 0;
			}else if(comptabilisespe!=null && ("Divers".equals(typeDeuxiemeConge)||"Maladie".equals(typeDeuxiemeConge))){
				if(comptabilisespe.equals("comptabilisé")){
					nbrJrCongeOuvr += (float) getWorkflowInstance().getValue(nbrJoursJoursTravailleNS);
				}else{
					nbrJrCongeOuvr += 0;
				}
			}
		}
		else if("Normal payé".equals(typePremierConge)||"Normal payé".equals(typeDeuxiemeConge) ){
			nbrJrCongeOuvr += (float) getWorkflowInstance().getValue(nbrJoursJoursTravailleNS);
		}
		
		else{
			nbrJrCongeOuvr += 0;
		}
		
		
		
		if( ("Normal payé".equals(typePremierConge)&& comptabilisespe!=null && ("Divers".equals(typeDeuxiemeConge)||"Maladie".equals(typeDeuxiemeConge)))
				
			||	
				("Normal payé".equals(typeDeuxiemeConge))&&(comptabilisenor!=null && ("Divers".equals(typePremierConge)||"Maladie".equals(typePremierConge)))){
			{
				nbrJrCongeOuvr = (float) getWorkflowInstance().getValue(nbrJoursJoursTravailleNS);
			}
			
		}
		
//		getWorkflowInstance().setValue(nbrJoursJoursTravailleNS, nbrJrCongeOuvr);
		
		
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
//			nbrJoursSoldesInitialNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_nbrJoursSoldesInitial");
			idCongeNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_IdCongeNor");
			idCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_IdCongeSpe");
			fragIdCongeNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_IDCONGNOR");
			fragIdCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_IDCONGSPE");
			codeCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DemandeConge");
			fragCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_CONG");
//			demandeur = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Demandeur");
//			fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_ALERTE");
//			resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
//			commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Commentaire");
			frag_remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_REMPLACANT");
//			fragmotifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_MOTIFSPE");
//			fragmotifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_MOTIFNOR");
			motifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MotifsSpe");
			motifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Motifs");
			dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateReprise");
			dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateEntree");
			dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseSpe");
			dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateEntreeSpe");
//			alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Alerte");
//			droitAnnCongNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DroitAnnuelConge");
			nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPrisConge");
			nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsSpeConge");
			// nbrJrTotalSoldeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");
			// nbrJrResteSoldeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
			momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieSpe");
			momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntreeSpe");
//			matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieSpe");
//			matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntreeSpe");
			nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsOuvrablesSpe");
//			msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MsgErrPerSpe");
			supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
			// remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
			btnenvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Envoyer");
			naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_CongeNormalCombine");
			typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeConge");
			nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPredefinis");
			momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortie");
			momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntree");
			// periodecongenorNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONG");
			nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsOuvrables");
//			nbrjrcongdispoNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
			justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JustifAbscence");
			typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeCongeSpecial");
			nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPredefinisSpe");
			// periodecongespeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGSPE");
			justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JustifAbscenceSpe");
			frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_DONNE_CONG_COMBINE");
			// frag_nbrjrpredefNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
			matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
			matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
//			msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MsgErrPerConge");
//			operationNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeOperation");
			lieuNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_LieuConge");
			
			dateEntreeExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseExp");
			momEntreeExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentRepriseExp");
			dateSortieExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateSortieExp");
			momSortieExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieExp");
			nbrJoursJoursTravailleNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JoursTravailExcp");
			nbrJoursJoursTravailleSpecNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JoursTravailSpecExcp");
//			resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
			
//			reliquatAnneEncours = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
			
			droitAnnuelleInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_NBRJRCONGEINIT");
			droitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DroitAnnuelConge");
//			reliquatDroitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
			soldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
			reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_RESTESOLDEDISPOAPRES");
			soldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_SOLDEANNENCOURS");
			reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_SOLDEANNENCOURSAPMODIF");
			
			
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();//ConnectionDefinition("RH_ATTIJARI").getConnection();
//			codeVDOC = null;
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
	
	public void controleAffichageMotif(String typecongeNSM, String fragmotifNSM, String motifNSM, String justifNSM, String dateCongeFinNSM)
	{
		String typecongem = (String) getWorkflowInstance().getValue(typecongeNSM);
		
		if ("Maladie".equals(typecongem))
		{
			getResourceController().setMandatory(justifNSM, true);
		}
		else
		{
			getResourceController().setMandatory(justifNSM, false);
		}
		
		
		
		if ("Maladie".equals(typecongem)  || "Normal payé".equals(typecongem))
		{
			getResourceController().setEditable(dateCongeFinNSM, true);
		}
		else
		{
			getResourceController().setEditable(dateCongeFinNSM, false);
		}
	}
	
//	private void getValueFromOtherProcess(String refPro, String nsChampsDest, String nsChampsSource)
//	{
//		try
//		{
//			IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
//			IContext processContext = iDirectoryModule.getContextByLogin("sysadmin");
//			IOrganization organization = getDirectoryModule().getOrganization(processContext, "DefaultOrganization");
//			IProject project = getProjectModule().getProject(processContext, "APPATTIJARI", organization);
//			ICatalog catalog = getWorkflowModule().getCatalog(processContext, "GROUPE_DE_PROCESSUS_DE_GESTION_DE_RESSOURCES_HUMAINES", project);
//			IWorkflow workflow = getWorkflowModule().getWorkflow(processContext, catalog, "DEM_CON_1.0");
//			IViewController viewController = getWorkflowModule().getViewController(processContext);
//			Collection<IWorkflowInstance> cWorkflowInstance = viewController.evaluate(workflow);
//				for (IWorkflowInstance iWorkflowInstance : cWorkflowInstance)
//				{
//					// VERSION DE PROCESS 'GESTION DES OPERATIONS'
//					String workflowName = iWorkflowInstance.getWorkflow().getName();
//					if (workflowName.equalsIgnoreCase("DEM_CON_1.0"))
//					{
//						if (iWorkflowInstance.getValue(IProperty.System.REFERENCE) != null)
//						{
//							String ref = (String) iWorkflowInstance.getValue(IProperty.System.REFERENCE);
//							if (ref.trim().equalsIgnoreCase(refPro.trim()))
//							{
//								// String motif = (String) iWorkflowInstance.getValue(nsChampsSource);
//								getWorkflowInstance().setValue(nsChampsDest, iWorkflowInstance.getValue(nsChampsSource));
//							}
//						}
//					}
//				}
//			
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void setLectureEcriture(boolean lecture)
	{
		try
		{
			
			getResourceController().setEditable(naturecongeNS, false);
			getResourceController().setEditable(lieuNS, lecture);
			
			getResourceController().setEditable(typecongeNS, false);
			getResourceController().setEditable(typecongespeNS, false);
			
			getResourceController().setEditable(momententreeNS, lecture);
			getResourceController().setEditable(momententreeSpeNS, lecture);
			
			getResourceController().setEditable(momentsortieNS, lecture);
			getResourceController().setEditable(momentsortieSpeNS, lecture);
			
			getResourceController().setEditable(dateCongeDebNS, lecture);
			getResourceController().setEditable(dateCongeDebSpeNS, lecture);
			
			String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
			if(lecture==true)
			if ("Maladie".equals(typeconge)  || "Normal payé".equals(typeconge))
			{
				getResourceController().setEditable(dateCongeFinNS, true);
			}
			else
			{
				getResourceController().setEditable(dateCongeFinNS, false);
			}
			
			String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
			if(lecture==true)
				if ("Maladie".equals(typecongespe)  || "Normal payé".equals(typecongespe))
				{
					getResourceController().setEditable(dateCongeFinSpeNS, true);
				}
				else
				{
					getResourceController().setEditable(dateCongeFinSpeNS, false);
				}
			getResourceController().setEditable(dateCongeFinNS, lecture);
			getResourceController().setEditable(dateCongeFinSpeNS, lecture);
			
			getResourceController().setEditable(motifNorNS, lecture);
			getResourceController().setEditable(motifSpeNS, lecture);
			
			getResourceController().setEditable(justifabsNS, lecture);
			getResourceController().setEditable(justifabsspeNS, lecture);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public float getNombreJoursNormal( ){
		ServiceRH srv = new ServiceRH();
		String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
		Date dateEntreeExcep = (Date) getWorkflowInstance().getValue(dateEntreeExcepNS);
		Calendar cDateExcepDeb = Calendar.getInstance();
		cDateExcepDeb.setTime(dateEntreeExcep);
		
		Date dateSortieExcep = (Date) getWorkflowInstance().getValue(dateSortieExcepNS);
		Calendar cDateExcepFin = Calendar.getInstance();
		cDateExcepFin.setTime(dateSortieExcep);
		float nb_joursTravaille = 0;
		String momEntreeExcep = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
		String momSortieExcep = (String) getWorkflowInstance().getValue(momSortieExcepNS);
		String comptaMaladieNor = (String) getWorkflowInstance().getValue("AEDC_COMPTANOR");
		String comptaMaladieSpe = (String) getWorkflowInstance().getValue("AEDC_COMPTASPE");
		
		if("Combiné".equals(natureConge)){
			
			String typePremierConge = (String) getWorkflowInstance().getValue(typecongeNS);
			String typeDeuxiemeConge = (String) getWorkflowInstance().getValue(typecongespeNS);
			if(typePremierConge.equals("Normal payé") 
					|| (typePremierConge.equals("Maladie")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieNor)) 
					|| (typePremierConge.equals("Divers")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieNor))){
				
				Date dateDebConge = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date dateFinConge = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				String momEntree = (String) getWorkflowInstance().getValue(momententreeNS);
				String momSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
				
				Conge congeExcep = new Conge(momEntreeExcep, momSortieExcep,dateEntreeExcep, dateSortieExcep );
				Conge conge = new Conge(dateDebConge, dateFinConge, momSortie, momEntree);
				nb_joursTravaille = srv.getSpecificNbrJrOf1(congeExcep, conge);
			}
			if(typeDeuxiemeConge.equals("Normal payé") 
					|| (typeDeuxiemeConge.equals("Maladie")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieSpe)) 
					|| (typeDeuxiemeConge.equals("Divers")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieSpe))) {
				Date dateDebConge = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
				Date dateFinConge = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
				String momEntree = (String) getWorkflowInstance().getValue(momententreeSpeNS);
				String momSortie = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
				
				Conge congeExcep = new Conge(momEntreeExcep, momSortieExcep,dateEntreeExcep, dateSortieExcep );
				Conge conge = new Conge(dateDebConge, dateFinConge, momSortie, momEntree);
				nb_joursTravaille += srv.getSpecificNbrJrOf1(congeExcep, conge);
			}
		}
		else{
			String typePremierConge = (String) getWorkflowInstance().getValue(typecongeNS);
			if(typePremierConge.equals("Normal payé") 
					|| (typePremierConge.equals("Maladie")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieNor)) 
					|| (typePremierConge.equals("Divers")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieNor))){
				
				Date dateDebConge = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date dateFinConge = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				String momEntree = (String) getWorkflowInstance().getValue(momententreeNS);
				String momSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
				
				Conge congeExcep = new Conge(momEntreeExcep, momSortieExcep,dateEntreeExcep, dateSortieExcep );
				Conge conge = new Conge(dateDebConge, dateFinConge, momSortie, momEntree);
				nb_joursTravaille = srv.getSpecificNbrJrOf1(congeExcep, conge);
			}
		}

		
		getWorkflowInstance().setValue(nbrJoursJoursTravailleNS, (float)nb_joursTravaille);
		return 0 ;
	}
	
	
	public float getNombreJoursSpecial(){
		ServiceRH srv = new ServiceRH();
		String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
		Date dateEntreeExcep = (Date) getWorkflowInstance().getValue(dateEntreeExcepNS);
		Calendar cDateExcepDeb = Calendar.getInstance();
		cDateExcepDeb.setTime(dateEntreeExcep);
		
		Date dateSortieExcep = (Date) getWorkflowInstance().getValue(dateSortieExcepNS);
		Calendar cDateExcepFin = Calendar.getInstance();
		cDateExcepFin.setTime(dateSortieExcep);
		float nb_joursTravaille = 0;
		String momEntreeExcep = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
		String momSortieExcep = (String) getWorkflowInstance().getValue(momSortieExcepNS);
		String comptaMaladieNor = (String) getWorkflowInstance().getValue("AEDC_COMPTANOR");
		String comptaMaladieSpe = (String) getWorkflowInstance().getValue("AEDC_COMPTASPE");
		
		
		if("Combiné".equals(natureConge)){
			
			String typePremierConge = (String) getWorkflowInstance().getValue(typecongeNS);
			String typeDeuxiemeConge = (String) getWorkflowInstance().getValue(typecongespeNS);
			if(!(typePremierConge.equals("Normal payé") 
					|| (typePremierConge.equals("Maladie")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieNor)) 
					|| (typePremierConge.equals("Divers")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieNor)))){
				
				Date dateDebConge = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date dateFinConge = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				String momEntree = (String) getWorkflowInstance().getValue(momententreeNS);
				String momSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
				
				Conge congeExcep = new Conge(momEntreeExcep, momSortieExcep,dateEntreeExcep, dateSortieExcep );
				Conge conge = new Conge(dateDebConge, dateFinConge, momSortie, momEntree);
				nb_joursTravaille = srv.getSpecificNbrJrOf1(congeExcep, conge);
			}
			if(!(typeDeuxiemeConge.equals("Normal payé") 
					|| (typeDeuxiemeConge.equals("Maladie")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieSpe)) 
					|| (typeDeuxiemeConge.equals("Divers")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieSpe)))){
				Date dateDebConge = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
				Date dateFinConge = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
				String momEntree = (String) getWorkflowInstance().getValue(momententreeSpeNS);
				String momSortie = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
				
				Conge congeExcep = new Conge(momEntreeExcep, momSortieExcep,dateEntreeExcep, dateSortieExcep );
				Conge conge = new Conge(dateDebConge, dateFinConge, momSortie, momEntree);
				nb_joursTravaille += srv.getSpecificNbrJrOf1(congeExcep, conge);
			}
		}
		else{
			String typePremierConge = (String) getWorkflowInstance().getValue(typecongeNS);
			if(!(typePremierConge.equals("Normal payé") 
					|| (typePremierConge.equals("Maladie")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieNor)) 
					|| (typePremierConge.equals("Divers")&&"Comptabilisé".equalsIgnoreCase(comptaMaladieNor)))){
				
				Date dateDebConge = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date dateFinConge = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				String momEntree = (String) getWorkflowInstance().getValue(momententreeNS);
				String momSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
				
				Conge congeExcep = new Conge(momEntreeExcep, momSortieExcep,dateEntreeExcep, dateSortieExcep );
				Conge conge = new Conge(dateDebConge, dateFinConge, momSortie, momEntree);
				nb_joursTravaille = srv.getSpecificNbrJrOf1(congeExcep, conge);
			}
		}

		
		getWorkflowInstance().setValue(nbrJoursJoursTravailleSpecNS, (float)nb_joursTravaille);
		return 0 ;
	}
	
	public void setCongeData()
	{
		
		try
		{
			// get nombre dispo du congé
			
			IUser collab = getWorkflowInstance().getCreatedBy();
			String loginVdocOfUser = collab.getLogin();
			String req = "SELECT NombreJoursConge,NombrJoursDispo,reliquatAnneEnCours,FilialeIdFiliale,NombreJoursCongeAnnuel FROM Personnel where loginVdoc = ?";
			st = connection.prepareStatement(req);
			st.setString(1, loginVdocOfUser);
			ResultSet rsx = st.executeQuery();
			String filiale = "";
			
			float nbrJrNor = 0;
			float nbrJrSpe = 0;
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

				// get superieur hierarchique
//				IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
				List<IUser> users = new ArrayList<>();
				users.addAll(getSuperieurOf(loginVdocOfUser));
				getWorkflowInstance().setValue(supHieNS, users);
				getWorkflowInstance().save(supHieNS);
				
				// get rh
				IUser rh = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_ATI"));
				
//				getResourceController().showBodyBlock("FRAG_SUP", false);
//				getWorkflowInstance().setValue("AEDC_EmailNejjar", "");
				
				users = new ArrayList<>();
				users.add(rh);
				getWorkflowInstance().setValue(rhNS, users);
				getWorkflowInstance().save(rhNS);
				
			}
//			else if ("attijariwb".equals(filiale))
//			{
//				// get superieur hierarchique
////				IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
//				List<IUser> users = new ArrayList<>();
//				users.addAll(getSuperieurOf(loginVdocOfUser));
//
//				getWorkflowInstance().setValue(supHieNS, users);
//				getWorkflowInstance().save(supHieNS);
//				
//				// get rh
//				IUser rh = getWorkflowModule().getUserByLogin("m.oirrach");
//				
//				getResourceController().showBodyBlock("FRAG_SUP", false);
//				getWorkflowInstance().setValue("AEDC_EmailNejjar", "");
//				
//				users = new ArrayList<>();
//				users.add(rh);
//				getWorkflowInstance().setValue(rhNS, users);
//				getWorkflowInstance().save(rhNS);
//				
//			}
			else
			{
				getResourceController().showBodyBlock(frag_remplacantNS, false);
				getWorkflowInstance().setValue(remplacantNS, null);
				
				// get superieur hierarchique
//				IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
//				IUser sup1 = getWorkflowModule().getLoggedOnUser().getManager();
				List<IUser> users = new ArrayList<>();
				users.addAll(getSuperieurOf(loginVdocOfUser));

				getWorkflowInstance().setValue(supHieNS, users);
				getWorkflowInstance().save(supHieNS);
//				getResourceController().showBodyBlock("FRAG_SUP", true);
//				getWorkflowInstance().setValue("AEDC_EmailNejjar", getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("EMAIL_SNI"));
				
				IUser rh = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC"));
				users = new ArrayList<>();
				users.add(rh);
				getWorkflowInstance().setValue(rhNS, users);
				getWorkflowInstance().save(rhNS);
			}
			
			
			
			
			String codeDemandeConge = (String) getWorkflowInstance().getValue(codeCongeNS);
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			// Préparer la requette sql
			String query = "SELECT CodeVdocDemandeConge,DateDemandeConge,IdConge,c.TypeConge,DateDeb,DateFin,NbrJoursOuvrables,"
					+ "MomSortie,MomEntre,Lieu,t.nbrJrDef,c.Personnelmatricule,c.supMatricule,c.remplacantMatricule,c.natureConge,"
					+ "p.NombreJoursConge,p.NombrJoursDispo,reliquatAnneEnCours,p.NombreJoursConge+p.NombrJoursDispo-p.NombrJoursDispo"
					+ ",c.maladieComptabilise FROM Conge c, TypeConge t,Personnel p where "
					+ " CodeVdocDemandeConge = ? "
					+ " and c.TypeConge = t.TypeConge  "
					+ " and p.LoginVdoc = c.Personnelmatricule "
					+ " order by DateDeb";
			st = connection.prepareStatement(query);
			st.setString(1, codeDemandeConge);
			ResultSet rs = st.executeQuery();
			int i = 0;
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
				if (i == 0)
				{
					getWorkflowInstance().setValue(naturecongeNS, rs.getString(15));
					getWorkflowInstance().setValue(lieuNS, rs.getString(10));
					getWorkflowInstance().setValue(idCongeNorNS, rs.getString(3));
					getWorkflowInstance().setValue(typecongeNS, rs.getString(4));
					getWorkflowInstance().setValue(nbjrpredefNS, rs.getFloat(11));
					getWorkflowInstance().setValue(momentsortieNS, rs.getString(8));
					getWorkflowInstance().setValue(momEntreeExcepNS, rs.getString(8));
					getWorkflowInstance().setValue(momententreeNS, rs.getString(9));
					getWorkflowInstance().setValue(momSortieExcepNS, rs.getString(9));
					String ss = rs.getString(8)+" "+rs.getString(9);
					
					java.sql.Date  datesql = rs.getDate(5);
					getWorkflowInstance().setValue(dateCongeDebNS, new Date(datesql.getTime()));
					getWorkflowInstance().setValue(dateEntreeExcepNS, getWorkflowInstance().getValue(dateCongeDebNS));
					datesql = rs.getDate(6);
					getWorkflowInstance().setValue(dateCongeFinNS, new Date(datesql.getTime()));
					getWorkflowInstance().setValue(dateSortieExcepNS, getWorkflowInstance().getValue(dateCongeFinNS));
//					java.sql.Date datesqlDeb = rs.getDate(5);
//					GregorianCalendar cdatesqlDeb = new GregorianCalendar();
//					Date dateDeb = new Date(datesqlDeb.getTime());
//					cdatesqlDeb.setTime(dateDeb);
//					getWorkflowInstance().setValue(dateCongeDebNS, cdatesqlDeb.getTime() );
//					
//					java.sql.Date datesqlFin = rs.getDate(6);
//					GregorianCalendar cdatesqlFin = new GregorianCalendar();
//					Date dateFin = new Date(datesqlFin.getTime());
//					cdatesqlFin.setTime(dateFin);
//					getWorkflowInstance().setValue(dateCongeFinNS, cdatesqlFin.getTime());
					
//					getValueFromOtherProcess(rs.getString(1), dateCongeDebNS, "DEMCON_DATEREPRISE");
//					getWorkflowInstance().setValue(dateEntreeExcepNS, getWorkflowInstance().getValue(dateCongeDebNS));
//					getValueFromOtherProcess(rs.getString(1), dateCongeFinNS, "DEMCON_DATEENTREE");
//					getWorkflowInstance().setValue(dateSortieExcepNS, getWorkflowInstance().getValue(dateCongeFinNS));
					
					getWorkflowInstance().setValue(nbrjrouvrNS, rs.getFloat(7));
//					getValueFromOtherProcess(rs.getString(1), motifNorNS, "DEMCON_MOTIF");
					
					
//					getValueFromOtherProcess(rs.getString(1), nbrJrSoldeNS, "DEMCON_NBRJRSOLDCONG");
//					getValueFromOtherProcess(rs.getString(1), nbrJrNonSoldeNS, "DEMCON_NBRJRNSOLDCONG");
//					getValueFromOtherProcess(rs.getString(1), "ADEC_AVECJUSTIFNOR", "DEMCON_AVECJUSTIFNOR");
//					getValueFromOtherProcess(rs.getString(1), justifabsNS, "DEMCON_JUSTABSC");
					
					String typeCongeNor = rs.getString(4);
					boolean comptaNor = rs.getBoolean(20);
//					if((typeConge.equals("Divers")||typeConge.equals("Maladie"))&&comptaNor==true){
//						
//						getResourceController().showBodyBlock("FRAGCOMPTANOR", true);
//						getWorkflowInstance().setValue("AEDC_COMPTANOR", "comptabilisé");
//						nbrJrNor += rs.getFloat(7);
//					}
//					else{
//						getResourceController().showBodyBlock("FRAGCOMPTANOR", false);
//						getWorkflowInstance().setValue("AEDC_COMPTANOR", "non comptabilisé");
//						nbrJrSpe += rs.getFloat(7);
//					}
					
					
					if((typeCongeNor.equals("Divers")||typeCongeNor.equals("Maladie"))&&comptaNor==true){
						
						getResourceController().showBodyBlock("FRAGCOMPTANOR", true);
						getWorkflowInstance().setValue("AEDC_COMPTANOR", "comptabilisé");
						nbrJrNor += rs.getFloat(7);
					}
					else if((typeCongeNor.equals("Divers")||typeCongeNor.equals("Maladie"))&&comptaNor==false){
						getResourceController().showBodyBlock("FRAGCOMPTANOR", false);
						getWorkflowInstance().setValue("AEDC_COMPTANOR", "non comptabilisé");
						nbrJrSpe += rs.getFloat(7);
					}
					else if(typeCongeNor.equals("Normal payé")){
						getResourceController().showBodyBlock("FRAGCOMPTANOR", false);
						getWorkflowInstance().setValue("AEDC_COMPTANOR", null);
						nbrJrNor += rs.getFloat(7);
					}
					else if(!(typeCongeNor.equals("Divers")||typeCongeNor.equals("Maladie")||typeCongeNor.equals("Normal payé"))&&comptaNor==false){
						getResourceController().showBodyBlock("FRAGCOMPTANOR", false);
						getWorkflowInstance().setValue("AEDC_COMPTANOR", "non comptabilisé");
						nbrJrSpe += rs.getFloat(7);
					}
					
					
					
				
				}
				else if (i == 1 && natureConge.equals("Combiné"))
				{
					getWorkflowInstance().setValue(idCongeSpeNS, rs.getString(3));
					getWorkflowInstance().setValue(typecongespeNS, rs.getString(4));
					getWorkflowInstance().setValue(nbjrpredefspeNS, rs.getFloat(11));
					getWorkflowInstance().setValue(momentsortieSpeNS, rs.getString(8));
					getWorkflowInstance().setValue(momEntreeExcepNS, getWorkflowInstance().getValue(momentsortieNS));
					getWorkflowInstance().setValue(momententreeSpeNS, rs.getString(9));
					getWorkflowInstance().setValue(momSortieExcepNS, getWorkflowInstance().getValue(momententreeSpeNS));
					java.sql.Date datesql = rs.getDate(5);
					getWorkflowInstance().setValue(dateCongeDebSpeNS, new Date(datesql.getTime()));
					getWorkflowInstance().setValue(dateEntreeExcepNS, getWorkflowInstance().getValue(dateCongeDebNS));
					datesql = rs.getDate(6);
					getWorkflowInstance().setValue(dateCongeFinSpeNS, new Date(datesql.getTime()));

					getWorkflowInstance().setValue(dateSortieExcepNS, getWorkflowInstance().getValue(dateCongeFinSpeNS));
					
					
					getWorkflowInstance().setValue(nbrjrouvrSpeNS, rs.getFloat(7));
//					getValueFromOtherProcess(rs.getString(1), motifSpeNS, "DEMCON_MOTIFSPE");
//					getValueFromOtherProcess(rs.getString(1), "ADEC_AVECJUSTIFSPE", "DEMCON_AVECJUSTIFSPE");
//					getValueFromOtherProcess(rs.getString(1), justifabsspeNS, "DEMCON_JUSTABSCSPE");
					
					String typeCongeSpe = rs.getString(4);
					boolean comptaNor = rs.getBoolean(20);
//					if((typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie"))&&comptaNor==true){
//						
//						getResourceController().showBodyBlock("FRAGCOMPTASPE", true);
//						getWorkflowInstance().setValue("AEDC_COMPTASPE", "comptabilisé");
//						nbrJrNor += rs.getFloat(7);
//					}
//					else{
//						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
//						getWorkflowInstance().setValue("AEDC_COMPTASPE", "non comptabilisé");
//						nbrJrSpe += rs.getFloat(7);
//					}
					
					
					
					if((typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie"))&&comptaNor==true){
						
						getResourceController().showBodyBlock("FRAGCOMPTASPE", true);
						getWorkflowInstance().setValue("AEDC_COMPTASPE", "comptabilisé");
						nbrJrNor += rs.getFloat(7);
					}
					else if((typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie"))&&comptaNor==false){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("AEDC_COMPTASPE", "non comptabilisé");
						nbrJrSpe += rs.getFloat(7);
					}
					else if(typeCongeSpe.equals("Normal payé")){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("AEDC_COMPTASPE", null);
						nbrJrNor += rs.getFloat(7);
					}
					else if(!(typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie")||typeCongeSpe.equals("Normal payé"))&&comptaNor==false){
						getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						getWorkflowInstance().setValue("AEDC_COMPTASPE", "non comptabilisé");
						nbrJrSpe += rs.getFloat(7);
					}
					
				}
				
				i++;
			}
			
			getWorkflowInstance().setValue(nbrJrSoldeNS, nbrJrNor);
			getWorkflowInstance().setValue(nbrJrNonSoldeNS, nbrJrSpe);
			
//			matinApmidisortieNS = (String) getWorkflowInstance().getValue(momSortieExcepNS);
//			matinApmidientreeNS = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
//			getNombreJoursNormal(property);
//			getNombreJoursSpecial(property);
			getNombreJoursNormal();
			getNombreJoursSpecial();
			setCalcule();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		try
		{
			ServiceRH rh = new ServiceRH(); 
			if(property.getName().equals(codeCongeNS)){
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
			
			
			
			if(property.getName().equals(dateEntreeExcepNS)||property.getName().equals(dateSortieExcepNS)){
				
				getWorkflowInstance().setValue(momEntreeExcepNS,getWorkflowInstance().getValue(momEntreeExcepNS));
				getWorkflowInstance().setValue(momSortieExcepNS,getWorkflowInstance().getValue(momSortieExcepNS));
				
				
				Date dateEntreeExcep = (Date) getWorkflowInstance().getValue(dateEntreeExcepNS);
				Calendar dateRecuDebC = Calendar.getInstance();
				dateRecuDebC.setTime(dateEntreeExcep);
				
				Date dateSortieExcep = (Date) getWorkflowInstance().getValue(dateSortieExcepNS);
				Calendar dateRecuFinC = Calendar.getInstance();
				dateRecuFinC.setTime(dateSortieExcep);
				
				String momentSortieExcep = (String) getWorkflowInstance().getValue(momSortieExcepNS);
				String momentEntreeExcep = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
				
				Date dateDebConge = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Calendar dateDebCongeC = Calendar.getInstance();
				dateDebCongeC.setTime(dateDebConge);
				
				String momentSortieNor = (String) getWorkflowInstance().getValue(momentsortieNS);
				
				Calendar dateFinCongeC = Calendar.getInstance();
				String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
				
//				boolean test = true ;
				ServiceRH srv = new ServiceRH();
				if(srv.compareTwoDates(dateEntreeExcep, dateSortieExcep)>0){
					getResourceController().alert("Période invalide !!!");
				}
				
				
				if("Non combiné".equals(natureConge)){
					
					Date dateFinNorConge = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
					dateFinCongeC.setTime(dateFinNorConge);
					
					String momentEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
					
					if(!(rh.compareTwoDates(dateDebCongeC.getTime(),dateRecuDebC.getTime())<=0)
							||!(rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())<=0) ){
						
						if(!(rh.compareTwoDates(dateDebCongeC.getTime(),dateRecuDebC.getTime())<=0)){
							//getResourceController().alert("Vérifier les dates de récupération exceptionelles");
							getWorkflowInstance().setValue(dateEntreeExcepNS, dateDebConge);
//							test = false;
						}
						if(!(rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())<=0)){
							//getResourceController().alert("Vérifier les dates de récupération exceptionelles");
							getWorkflowInstance().setValue(dateSortieExcepNS, dateFinNorConge);
//							test = false;
						}
						
					}	
					else if(rh.compareTwoDates(dateDebCongeC.getTime(),dateRecuDebC.getTime())==0|| rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())==0){
						if(!momentSortieNor.equals(momentEntreeExcep) && momentSortieNor.equals("Après midi")&&rh.compareTwoDates(dateDebCongeC.getTime(),dateRecuDebC.getTime())==0){
							//getResourceController().alert("Vérifier les moments de récupération exceptionelles");
							getWorkflowInstance().setValue(momEntreeExcepNS, "Après midi");
//							test = false;
						}
						
						if(!momentEntreeNor.equals(momentSortieExcep) && momentEntreeNor.equals("Matin")&&rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())==0){
							getResourceController().alert("Vérifier les moments de récupération exceptionelles");
							getWorkflowInstance().setValue(momSortieExcepNS, "Matin");
//							test = false;
						}
						
					}
					if(rh.compareTwoDates(dateRecuDebC.getTime(),dateFinCongeC.getTime())>0 || rh.compareTwoDates(dateRecuFinC.getTime(),dateDebCongeC.getTime())<0){
						if(rh.compareTwoDates(dateRecuDebC.getTime(),dateFinCongeC.getTime())>0){
//							test = false;
							getWorkflowInstance().setValue(dateEntreeExcepNS, dateDebConge);
						}
						else if(rh.compareTwoDates(dateRecuFinC.getTime(),dateDebCongeC.getTime())<0){
//							test = false;
							getWorkflowInstance().setValue(dateSortieExcepNS, dateFinNorConge);
						}
					}
					
				}
				else{
					Date dateFinSpeConge = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
					dateFinCongeC.setTime(dateFinSpeConge);
					
					String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
					
					if(!(rh.compareTwoDates(dateDebCongeC.getTime(),dateRecuDebC.getTime())<=0 )
							||!(rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())<=0 )   ){
						
						if(!(rh.compareTwoDates(dateDebCongeC.getTime(),dateRecuDebC.getTime())<=0 )){
							//getResourceController().alert("Vérifier les dates de récupération exceptionelles");
							getWorkflowInstance().setValue(dateEntreeExcepNS, dateDebConge);
//							test = false;
						}
						if(!(rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())<=0 )){
							//getResourceController().alert("Vérifier les dates de récupération exceptionelles");
							getWorkflowInstance().setValue(dateSortieExcepNS, dateFinSpeConge);
//							test = false;
						}
						
					}	
					else if(rh.compareTwoDates(dateDebCongeC.getTime(),dateRecuDebC.getTime())==0|| rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())==0){
						if(!momentSortieNor.equals(momentEntreeExcep) && momentSortieNor.equals("Après midi")&&rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())==0){
							getResourceController().alert("Vérifier les moments de récupération exceptionelles");
							getWorkflowInstance().setValue(momEntreeExcepNS, "Après midi");
//							test = false;
						}
						
						if(!momentEntreeSpe.equals(momentSortieExcep) && momentEntreeSpe.equals("Matin")&&rh.compareTwoDates(dateDebCongeC.getTime(),dateRecuDebC.getTime())==0){
							getResourceController().alert("Vérifier les moments de récupération exceptionelles");
							getWorkflowInstance().setValue(momSortieExcepNS, "Matin");
//							test = false;
						}
						
						
						
					}
					
					else if(rh.compareTwoDates(dateRecuDebC.getTime(),dateFinCongeC.getTime())>0 
						|| rh.compareTwoDates(dateRecuFinC.getTime(),dateDebCongeC.getTime())<0){
						if(rh.compareTwoDates(dateRecuDebC.getTime(),dateFinCongeC.getTime())>0 ){
							getWorkflowInstance().setValue(dateEntreeExcepNS, dateDebConge);
						}
						else if(rh.compareTwoDates(dateRecuFinC.getTime(),dateDebCongeC.getTime())<0){
							getWorkflowInstance().setValue(dateSortieExcepNS, dateFinSpeConge);
						}
					}
				}
				
				
//				if(test == true||test==false){
////					getWorkflowInstance().setValue(momEntreeExcepNS, "Matin");
////					getWorkflowInstance().setValue(momentSortieExcep, "Matin");
//					float nb_joursTravaille = nbJours(dateEntreeExcep, dateSortieExcep, false, true, true, true, true, true, false, false);
//					String momEntreeExcep = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
//					String momSortieExcep = (String) getWorkflowInstance().getValue(momSortieExcepNS);
//					
//					if ("Après midi".equals(momEntreeExcep))
//						nb_joursTravaille -= 0.5;
//					// }
//					
//					// if(!matinApmidientree.equals(momentEntree)){
//					if ("Après midi".equals(momSortieExcep))
//						nb_joursTravaille += 0.5;
//					// }
//					
//					
//					if(dateEntreeExcep!=null&&dateSortieExcep!=null){
//						Calendar dateDebC = Calendar.getInstance();
//						dateDebC.setTime(dateEntreeExcep);
//						Calendar dateFinC = Calendar.getInstance();
//						dateFinC.setTime(dateSortieExcep);
//						if(dateDebC.get(dateDebC.YEAR) == dateFinC.get(dateFinC.YEAR) && dateDebC.get(dateDebC.MONTH) == dateFinC.get(dateFinC.MONTH) && dateDebC.get(dateDebC.DATE) == dateFinC.get(dateFinC.DATE)){
//							if (!matinApmidientreeNS.equals(momentEntreeExcep) )
//							{
//								if ("Après midi".equals(momentEntreeExcep))
//									nb_joursTravaille -= 0.5f;
//								else
//									nb_joursTravaille += 0.5f;
//							}
//							
//							if (!matinApmidisortieNS.equals(momentSortieExcep) )
//							{
//								if ("Après midi".equals(momentSortieExcep))
//									nb_joursTravaille += 0.5f;
//								else
//									nb_joursTravaille -= 0.5f;
//							}
//							
//							if ("Après midi".equals(momentEntreeExcep)&&"Matin".equals(momentSortieExcep) && property.getName().equals(momEntreeExcepNS)){
//								nb_joursTravaille += 0.5f;
//							}
//							else if ("Matin".equals(momentEntreeExcep)&&"Matin".equals(momentSortieExcep) && property.getName().equals(momEntreeExcepNS)){
//								nb_joursTravaille -= 0.5f;
//							}	
//							
//							if(("Après midi".equals(momentEntreeExcep)&&"Matin".equals(momentSortieExcep))||
//							   ("Après midi".equals(momentEntreeExcep)&&"Après midi".equals(momentSortieExcep)) ||
//							   ("Matin".equals(momentEntreeExcep)     &&"Matin".equals(momentSortieExcep))){
//								nb_joursTravaille=0;
//							}
//							else if(("Matin".equals(momentEntreeExcep)&&"Après midi".equals(momentSortieExcep))){
//								nb_joursTravaille=0.5f;
//							}
//							
//	//						if ("Après midi".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
//	//							days += 0.5f;
//	//						}
//	//						else if ("Matin".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
//	//							days -= 0.5f;
//	//						}	
//						}
//					}
//					
//					getWorkflowInstance().setValue(nbrJoursJoursTravailleNS, (float)nb_joursTravaille);
//					matinApmidisortieNS = (String) getWorkflowInstance().getValue(momSortieExcepNS);
//					matinApmidientreeNS = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
//				}
				
				getNombreJoursNormal();
				getNombreJoursSpecial();
				setCalcule();
				
			}
			
			if (property.getName().equals(momEntreeExcepNS) || property.getName().equals(momSortieExcepNS))
			{
				
				
				boolean test = true;
				String momentSortieExcep = (String) getWorkflowInstance().getValue(momSortieExcepNS);
				String momentEntreeExcep = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
				Date dateEntreeExcep = (Date) getWorkflowInstance().getValue(dateEntreeExcepNS);
				Date dateSortieExcep = (Date) getWorkflowInstance().getValue(dateSortieExcepNS);
				
				//Date dateEntreeExcep = (Date) getWorkflowInstance().getValue(dateEntreeExcepNS);
				Calendar dateRecuDebC = Calendar.getInstance();
				dateRecuDebC.setTime(dateEntreeExcep);
				
				//Date dateSortieExcep = (Date) getWorkflowInstance().getValue(dateSortieExcepNS);
				Calendar dateRecuFinC = Calendar.getInstance();
				dateRecuFinC.setTime(dateSortieExcep);
				
//				String momentSortieExcep = (String) getWorkflowInstance().getValue(momSortieExcepNS);
//				String momentEntreeExcep = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
				
				Date dateDebConge = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Calendar dateDebCongeC = Calendar.getInstance();
				dateDebCongeC.setTime(dateDebConge);
				
				String momentSortieNor = (String) getWorkflowInstance().getValue(momentsortieNS);
				
				Calendar dateFinCongeC = Calendar.getInstance();
				String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
				
				Calendar searchDate = Calendar.getInstance();
				Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(new Date());
				List<Date> dates = getJourFeries(currentDate.get(Calendar.YEAR));
				boolean trouve = false;
				for(Date d : dates){
					searchDate.setTime(d);
					if(	rh.compareTwoDates(searchDate.getTime(), dateRecuDebC.getTime())==0	){
						trouve = true;
					}
					
					
					if(	rh.compareTwoDates(searchDate.getTime(), dateRecuFinC.getTime())==0 ){
						trouve = true;
					}
				}
				
				
				
				if("Non combiné".equals(natureConge)){
					Date dateFinNorConge = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
					dateFinCongeC.setTime(dateFinNorConge);
					String momentEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);

					if(!(rh.compareTwoDates(dateDebCongeC.getTime(), dateRecuDebC.getTime())<=0)
						|| !(rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())<=0)  ){
						
						
						if(!(rh.compareTwoDates(dateDebCongeC.getTime(), dateRecuDebC.getTime())<=0)){
							//getResourceController().alert("Vérifier les dates de récupération exceptionelles");
							getWorkflowInstance().setValue(dateEntreeExcepNS, dateDebConge);
							test = false;
						}
						if(!(rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())<=0)   ){
							//getResourceController().alert("Vérifier les dates de récupération exceptionelles");
							getWorkflowInstance().setValue(dateSortieExcepNS, dateFinNorConge);
							test = false;
						}
						
					}	
					else if(rh.compareTwoDates(dateDebCongeC.getTime(), dateRecuDebC.getTime())==0|| rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())==0){
						if(!momentSortieNor.equals(momentEntreeExcep) && momentSortieNor.equals("Après midi")&&rh.compareTwoDates(dateDebCongeC.getTime(), dateRecuDebC.getTime())==0){
							getResourceController().alert("Vérifier les moments de récupération exceptionelles");
							getWorkflowInstance().setValue(momEntreeExcepNS, "Après midi");
							test = false;
						}
						
						if(!momentEntreeNor.equals(momentSortieExcep) && momentEntreeNor.equals("Matin")&& rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())==0){
							getResourceController().alert("Vérifier les moments de récupération exceptionelles");
							getWorkflowInstance().setValue(momSortieExcepNS, "Matin");
							test = false;
						}
						
					}
					
				}
				else{
					Date dateFinSpeConge = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
					dateFinCongeC.setTime(dateFinSpeConge);
					
					String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
					
					if(!(rh.compareTwoDates(dateDebCongeC.getTime(), dateRecuDebC.getTime())<=0)
							|| !(rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())<=0)    ){
						if(!(rh.compareTwoDates(dateDebCongeC.getTime(), dateRecuDebC.getTime())<=0)){
							//getResourceController().alert("Vérifier les dates de récupération exceptionelles");
							getWorkflowInstance().setValue(dateEntreeExcepNS, dateDebConge);
							test = false;
						}
						if(!(rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())<=0)){
							//getResourceController().alert("Vérifier les dates de récupération exceptionelles");
							getWorkflowInstance().setValue(dateSortieExcepNS, dateFinSpeConge);
							test = false;
						}
						
					}	
					else if(rh.compareTwoDates(dateDebCongeC.getTime(), dateRecuDebC.getTime())==0|| rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())==0){
						if(!momentSortieNor.equals(momentEntreeExcep) && momentSortieNor.equals("Après midi")&&rh.compareTwoDates(dateDebCongeC.getTime(), dateRecuDebC.getTime())==0){
							
							getResourceController().alert("Vérifier les moments de récupération exceptionelles");
							getWorkflowInstance().setValue(momEntreeExcepNS, "Après midi");
							test = false;
						}
						
						if(!momentEntreeSpe.equals(momentSortieExcep) && momentEntreeSpe.equals("Matin")&&rh.compareTwoDates(dateRecuFinC.getTime(),dateFinCongeC.getTime())==0){
							getResourceController().alert("Vérifier les moments de récupération exceptionelles");
							getWorkflowInstance().setValue(momSortieExcepNS, "Matin");
							test = false;
						}
						
					}
				}
				
				if(test == true){
					
//					//getWorkflowInstance().setValue(momEntreeExcepNS, "Matin");
//					//getWorkflowInstance().setValue(momSortieExcepNS, "Matin");
//					float days = (float) getWorkflowInstance().getValue(nbrJoursJoursTravailleNS);
//					if (!matinApmidientreeNS.equals(momentEntreeExcep) && days != 0 && !dateEntreeExcep.equals(dateSortieExcep))
//					{
//						if ("Après midi".equals(momentEntreeExcep))
//							days -= 0.5f;
//						else
//							days += 0.5f;
//					}
//					
//					if (!matinApmidisortieNS.equals(momentSortieExcep) && days != 0 && !dateEntreeExcep.equals(dateSortieExcep))
//					{
//						if ("Après midi".equals(momentSortieExcep))
//							days += 0.5f;
//						else
//							days -= 0.5f;
//					}
//					
//					
//					
//					if(dateEntreeExcep!=null&&dateSortieExcep!=null){
//						Calendar dateDebC = Calendar.getInstance();
//						dateDebC.setTime(dateEntreeExcep);
//						Calendar dateFinC = Calendar.getInstance();
//						dateFinC.setTime(dateSortieExcep);
//						if(dateDebC.get(dateDebC.YEAR) == dateFinC.get(dateFinC.YEAR) && dateDebC.get(dateDebC.MONTH) == dateFinC.get(dateFinC.MONTH) && dateDebC.get(dateDebC.DATE) == dateFinC.get(dateFinC.DATE)){
//							if (!matinApmidientreeNS.equals(momentEntreeExcep) )
//							{
//								if ("Après midi".equals(momentEntreeExcep))
//									days -= 0.5f;
//								else
//									days += 0.5f;
//							}
//							
//							if (!matinApmidisortieNS.equals(momentSortieExcep) )
//							{
//								if ("Après midi".equals(momentSortieExcep))
//									days += 0.5f;
//								else
//									days -= 0.5f;
//							}
//							
//							if ("Après midi".equals(momentEntreeExcep)&&"Matin".equals(momentSortieExcep) && property.getName().equals(momEntreeExcepNS)){
//								days += 0.5f;
//							}
//							else if ("Matin".equals(momentEntreeExcep)&&"Matin".equals(momentSortieExcep) && property.getName().equals(momEntreeExcepNS)){
//								days -= 0.5f;
//							}	
//							
//							if(("Après midi".equals(momentEntreeExcep)&&"Matin".equals(momentSortieExcep))||
//							   ("Après midi".equals(momentEntreeExcep)&&"Après midi".equals(momentSortieExcep)) ||
//							   ("Matin".equals(momentEntreeExcep)     &&"Matin".equals(momentSortieExcep))){
//										days=0;
//							}
//							else if(("Matin".equals(momentEntreeExcep)&&"Après midi".equals(momentSortieExcep))){
//								days=0.5f;
//							}
//							
//	//						if ("Après midi".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
//	//							days += 0.5f;
//	//						}
//	//						else if ("Matin".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
//	//							days -= 0.5f;
//	//						}	
//						}
//					}
//					
//					getWorkflowInstance().setValue(nbrJoursJoursTravailleNS,(float) days);
//					matinApmidisortieNS = (String) getWorkflowInstance().getValue(momSortieExcepNS);
//					matinApmidientreeNS = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
					if(!trouve){
						getNombreJoursNormal();
						getNombreJoursSpecial();
						setCalcule();
					}
					
					
				}
				
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
		
		ServiceRH rh = new ServiceRH();
		
		if ( rh.compareTwoDates(d2, d1)<=0  )
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
		while (rh.compareTwoDates(date1.getTime(), date2.getTime())<=0)
		{
			boolean test = false;// joursFeries.contains(date1);
			for (Date ligne : joursFeries)
			{
				dateToCompare.setTime(ligne);
				if (rh.compareTwoDates(dateToCompare.getTime(), date1.getTime())==0)
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
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
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
			if (action.getName().equals(btnenvoyerNS)){
				
//				String dateRepriseExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseExp");
//				String momRepriseExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentRepriseExp");
				
				String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
				
				
//				Date dateRepriseEx = (Date) getWorkflowInstance().getValue(dateRepriseExcepNS);
				
				if(natureconge.equals("Non combiné")){
//					Date departNor  = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
//					Date repriseNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
//					String momdepartNor  = (String) getWorkflowInstance().getValue(momentsortieNS);
//					String momRepriseNor = (String) getWorkflowInstance().getValue(momententreeNS);
//					if(dateRepriseEx.before(departNor)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
//					if(dateRepriseEx.after(repriseNor)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
//					if(dateRepriseEx.equals(departNor)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
//					if(momdepartNor.equals(momRepriseExcepNS)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
//					if(dateRepriseEx.equals(repriseNor)&&momRepriseNor.equals(momRepriseExcepNS)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
				}		
				else{
//					Date departNor  = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
//					Date repriseSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
//					if(dateRepriseEx.before(departNor)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
//					if(dateRepriseEx.after(repriseSpe)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
//					
//					String momdepartSpe  = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
//					String momRepriseSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
//					if(dateRepriseEx.equals(departNor)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
//					if(momdepartSpe.equals(momRepriseExcepNS)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
//					if(dateRepriseEx.equals(repriseSpe)&&momRepriseSpe.equals(momRepriseExcepNS)){
//						getResourceController().alert("la date de reprise doit être inclus dans la période de congé");
//						return false;
//					}
				}
				
				
				
				float nbrJrInter =  (float)getWorkflowInstance().getValue(nbrJoursJoursTravailleNS)+ (float)getWorkflowInstance().getValue(nbrJoursJoursTravailleSpecNS);
				if(nbrJrInter==0){
					getResourceController().alert("Pensez à vérifier le nombre de jours d'intervention !!!");
					return false;
				}
				
			}
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
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
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
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
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
//		try
//		{
//			if (action.getName().equals(btnenvoyerNS)){
//				String dateRepriseExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseExp");
//				String momRepriseExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentRepriseExp");
//				
//				String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
//				
//				float daysReprise = 0;
//				Date dateRepriseEx = (Date) getWorkflowInstance().getValue(dateRepriseExcepNS);
//				String momRepriseExcep = (String) getWorkflowInstance().getValue(momRepriseExcepNS);
//				
//				if(natureconge.equals("Normal")){
//					Date repriseNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
//					String momRepriseNor = (String) getWorkflowInstance().getValue(momententreeNS);
//					daysReprise = nbJours(dateRepriseEx,repriseNor , true, false, false, false, false, false, true, true);
//					if(momRepriseNor.equals("Matin")){
//						if(!momRepriseExcep.equals(momRepriseNor)){
//							daysReprise+=0.5;
//						}
//					}
//					else{
//						if(!momRepriseExcep.equals(momRepriseNor)){
//							daysReprise-=0.5;
//						}
//					}
//				}	
//				
//				else{
//					Date repriseSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
//					String momRepriseSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
//					daysReprise = nbJours(dateRepriseEx,repriseSpe , true, false, false, false, false, false, true, true)+1;
//					if(momRepriseSpe.equals("Matin")){
//						if(!momRepriseExcep.equals(momRepriseSpe)){
//							daysReprise-=0.5;
//						}
//					}
//					else{
//						if(!momRepriseExcep.equals(momRepriseSpe)){
//							daysReprise+=0.5;
//						}
//					}
//				}
//				
//				connection = ConnectionDefinition("RH_ATTIJARI").getConnection();
//				String req = "update Personnel set NombrJoursDispo = NombrJoursDispo + ? where loginVdoc = (select distinct Personnelmatricule from Conge where CodeVdocDemandeConge = ?)";
//				PreparedStatement st = connection.prepareStatement(req);
//				st.setFloat(1, daysReprise);
//				String codeDemandeConge = (String) getWorkflowInstance().getValue(codeCongeNS);
//				st.setString(2, codeDemandeConge);
//				st.executeUpdate();
//		
//			}
//				
//				
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
		
		return super.onAfterSubmit(action);
	}
	
}
