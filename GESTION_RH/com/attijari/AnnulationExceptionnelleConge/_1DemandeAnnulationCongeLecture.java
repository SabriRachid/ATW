package com.attijari.AnnulationExceptionnelleConge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;





import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.serviceRH.ServiceRH;
public class _1DemandeAnnulationCongeLecture extends BaseDocumentExtension
{
	int cpt = 0;
//	private String nbrJoursSoldesInitialNS;
//	private String codeVDOC;
//	private String supHieNS;
//	private String demandeur;
//	private String btnenvoyerNS;
	private String naturecongeNS;
//	private String typecongeNS;
//	private String nbjrpredefNS;
//	private String momentsortieNS;
//	private String momententreeNS;
//	private String dateCongeDebNS;
//	private String dateCongeFinNS;
//	private String dateCongeDebSpeNS;
//	private String dateCongeFinSpeNS;
//	private String periodecongenorNS;
//	private String nbrjrouvrNS;
//	private String nbrjrcongdispoNS;
//	private String justifabsNS;
//	private String typecongespeNS;
//	private String nbjrpredefspeNS;
//	private String periodecongespeNS;
//	private String justifabsspeNS;
	private String frag_conge_combineNS;
//	private String frag_nbrjrpredefNS;
//	private String matinApmidisortieNS;
//	private String matinApmidientreeNS;
//	private String msgErreurPeriodeCongeNS;
//	private String msgErreurPeriodeCongeSpeNS;
//	private String nbrjrouvrSpeNS;
//	private String matinApmidisortieSpeNS;
//	private String matinApmidientreeSpeNS;
//	private String momentsortieSpeNS;
//	private String momententreeSpeNS;
//	private String nbrJrSoldeNS;
//	private String nbrJrNonSoldeNS;
//	private String droitAnnCongNS;
//	private String motifNorNS;
//	private String motifSpeNS;
//	private String fragmotifNorNS;
//	private String fragmotifSpeNS;
//	private String frag_remplacantNS;
//	private String resteJoursCongeNS;
//	private String commentaireNS;
//	private String alerteNS;
//	private String fragAlerteNS;
	private String fragCongeNS;
	private String codeCongeNS;
	private String fragIdCongeNorNS;
	private String fragIdCongeSpeNS;
//	private String idCongeNorNS;
//	private String idCongeSpeNS;
//	private String lieuNS;
//	private String operationNS;
//	private float  nbrJoursCongeAvantModif;
//	private String dateSortieExcepNS;
//	private String momSortieExcepNS;
//	private String dateEntreeExcepNS;
//	private String momEntreeExcepNS;
//	private String nbrJoursJoursTravailleNS;
//	private String reliquatAnneEncours;
	
	
	
	private static final long serialVersionUID = 4269810337595101111L;
	private IContext ctx;
//	private Connection connection;
//	private PreparedStatement st;
	
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException
	{
		this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
	}
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub 
		try
		{
			// get nom systeme des champ ADC_FRAG_CONG
//			nbrJoursSoldesInitialNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_nbrJoursSoldesInitial");
//			idCongeNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_IdCongeNor");
//			idCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_IdCongeSpe");
			fragIdCongeNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_IDCONGNOR");
			fragIdCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_IDCONGSPE");
			codeCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DemandeConge");
			fragCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_CONG");
//			demandeur = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Demandeur");
//			fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_ALERTE");
//			resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
//			commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Commentaire");
//			frag_remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_REMPLACANT");
//			fragmotifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_MOTIFSPE");
//			fragmotifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_MOTIFNOR");
//			motifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MotifsSpe");
//			motifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Motifs");
//			dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateReprise");
//			dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateEntree");
//			dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseSpe");
//			dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateEntreeSpe");
//			alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Alerte");
//			droitAnnCongNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DroitAnnuelConge");
//			nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPrisConge");
//			nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsSpeConge");
			// nbrJrTotalSoldeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");
			// nbrJrResteSoldeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
//			momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieSpe");
//			momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntreeSpe");
//			matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieSpe");
//			matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntreeSpe");
//			nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsOuvrablesSpe");
//			msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MsgErrPerSpe");
//			supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
			// remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
//			btnenvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Envoyer");
			naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_CongeNormalCombine");
//			typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeConge");
//			nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPredefinis");
//			momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortie");
//			momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntree");
			// periodecongenorNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONG");
//			nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsOuvrables");
//			nbrjrcongdispoNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
//			justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JustifAbscence");
//			typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeCongeSpecial");
//			nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPredefinisSpe");
			// periodecongespeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGSPE");
//			justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JustifAbscenceSpe");
			frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_DONNE_CONG_COMBINE");
			// frag_nbrjrpredefNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
//			matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
//			matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
//			msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MsgErrPerConge");
//			operationNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeOperation");
//			lieuNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_LieuConge");
			
//			dateEntreeExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseExp");
//			momEntreeExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentRepriseExp");
//			dateSortieExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateSortieExp");
//			momSortieExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieExp");
//			nbrJoursJoursTravailleNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JoursTravailExcp");
//			resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
//			reliquatAnneEncours = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
			
			String fragIDconge = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_IDCONGNOR");
			getResourceController().showBodyBlock(fragIDconge, false);
			
//			codeVDOC = null;
			// cacher fragement
			
			// nature congé controle
			String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
			
			if ("Non combiné".equals(natureconge))
			{
				getResourceController().showBodyBlock(frag_conge_combineNS, false);
			}
			else
			{
				getResourceController().showBodyBlock(frag_conge_combineNS, true);
			}	
			
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
				//setCongeData();
				String loginVdocOfUser = getWorkflowInstance().getCreatedBy().getLogin();
				ServiceRH srv = new ServiceRH();
				String reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_RESTESOLDEDISPOAPRES");
				String reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_SOLDEANNENCOURSAPMODIF");
				
				Connection connection = ConnectionDefinition("RH_ATTIJARI").getConnection();
				String req = "SELECT FilialeIdFiliale,NombreJoursCongeAnnuel FROM Personnel where loginVdoc = ?";
				PreparedStatement st = connection.prepareStatement(req);
				st.setString(1, loginVdocOfUser);
				ResultSet rs = st.executeQuery();
				String filiale = "";
				float droitAquis = 0;
				while (rs.next())
				{
					filiale = rs.getString(1);
					droitAquis = rs.getFloat(2);
				}
				
				
//				String etatProcess = (String) getWorkflowInstance().getValue("ProcessState");
//				if(!etatProcess.equals("Terminé")){
//					droitAquis = srv.getDroitAcquis(loginVdocOfUser);
//					String nbrJoursOuvrableNS = "AEDC_JoursTravailExcp";
//					srv.setUpdatedSoldesPLUS(getWorkflowInstance(), reliquatSoldeanneEncoursNS, reliquatSoldeAnterieurNS, loginVdocOfUser, nbrJoursOuvrableNS, droitAquis);
//				
//				}
				
				
//				float soldeConsomme = (float) getWorkflowInstance().getValue("AEDC_JoursTravailExcp");
//				float oldSoldeAnterieur = srv.getSoldeAnterieur(loginVdocOfUser);
//				float oldReliquatEnCours = srv.getSoldeEnCours(loginVdocOfUser);;
//				
//				float nvSoldeEnCours = 0;
//				float nvSoldeAnterieur = 0;
//				float diff = 0;
//				
//				if(oldReliquatEnCours==droitAquis){
//					nvSoldeAnterieur= oldSoldeAnterieur+soldeConsomme;
//					nvSoldeEnCours = droitAquis;
//				}
//				else if(oldReliquatEnCours<droitAquis){
//					
//					if(oldReliquatEnCours+soldeConsomme>droitAquis){
//						diff = oldReliquatEnCours+soldeConsomme - droitAquis;
//						nvSoldeEnCours = droitAquis;
//						nvSoldeAnterieur = diff;
//						
//					}
//					if(oldReliquatEnCours+soldeConsomme==droitAquis){
//						nvSoldeEnCours = droitAquis;
//						nvSoldeAnterieur = diff;
//					}
//					if(oldReliquatEnCours+soldeConsomme<droitAquis){
//						nvSoldeEnCours = oldReliquatEnCours+soldeConsomme;
//						nvSoldeAnterieur = diff;
//					}
//				}
//				
//				getWorkflowInstance().setValue(reliquatSoldeanneEncoursNS, nvSoldeEnCours);
//				getWorkflowInstance().setValue(reliquatSoldeAnterieurNS, nvSoldeAnterieur);
//			
				
				
				if ("attijariintermediation".equals(filiale))
				{
//					getResourceController().showBodyBlock(frag_remplacantNS, true);
					getResourceController().showBodyBlock("FRAG_SUP", false);
				}
				else if ("attijariwb".equals(filiale))
				{
					getResourceController().showBodyBlock("FRAG_SUP", false);
				}
				else
				{
//					getResourceController().showBodyBlock(frag_remplacantNS, false);
					getResourceController().showBodyBlock("FRAG_SUP", true);
				}
			}
			
			
			
			String comptabiliseNor = getWorkflowInstance().getValue("AEDC_COMPTANOR")==null ? null : (String) getWorkflowInstance().getValue("AEDC_COMPTANOR");
			if(comptabiliseNor==null){
				getResourceController().showBodyBlock("FRAGCOMPTANOR", false);
			}
			else{
				if(comptabiliseNor.equals("comptabilisé")){
					getResourceController().showBodyBlock("FRAGCOMPTANOR", true);
				}else{
					getResourceController().showBodyBlock("FRAGCOMPTANOR", false);
				}
			}
			
			String comptabiliseSpe = getWorkflowInstance().getValue("AEDC_COMPTASPE")==null ? null : (String) getWorkflowInstance().getValue("AEDC_COMPTASPE");
			if(comptabiliseSpe==null){
				getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
			}
			else{
				if(comptabiliseSpe.equals("comptabilisé")){
					getResourceController().showBodyBlock("FRAGCOMPTASPE", true);
				}else{
					getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}

	
}
