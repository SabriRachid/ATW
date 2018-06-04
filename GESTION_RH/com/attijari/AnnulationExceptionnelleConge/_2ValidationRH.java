package com.attijari.AnnulationExceptionnelleConge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.turbine.services.servlet.TurbineServlet;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.genererRapport.FileManager;
import com.genererRapport.GenererPDF;
import com.serviceRH.EncryptionFile;
import com.serviceRH.ServiceRH;

import dao.SingletonConnexionBDD;

public class _2ValidationRH extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4102309135372902802L;
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	private String btnEnvoyerNS;
//	private String btnRefuserNS;
	private String supHieNS;
	private String remplacantNS;
	private String btnenvoyerNS;
	private String naturecongeNS;
	private String typecongeNS;
//	private String nbjrpredefNS;
	private String momentsortieNS;
	private String momententreeNS;
//	private String nbrjrouvrNS;
//	private String nbrjrcongdispoNS;
//	private String justifabsNS;
	private String typecongespeNS;
//	private String nbjrpredefspeNS;
//	private String justifabsspeNS;
//	private String frag_conge_combineNS;
//	private String frag_nbrjrpredefNS;
//	private String matinApmidisortieNS;
//	private String matinApmidientreeNS;
//	private String msgErreurPeriodeCongeNS;
//	private String msgErreurPeriodeCongeSpeNS;
//	private String nbrjrouvrSpeNS;
//	private String matinApmidisortieSpeNS;
//	private String matinApmidientreeSpeNS;
//	private String momentsortieSpeNS;
	private String momententreeSpeNS;
	private String pjEtatConge;
	private String nbrJrSoldeNS;
	private String lieuNS;
	private String nbrJrNonSoldeNS;
	private String dateCongeDebNS;
	private String dateCongeFinNS;
	private String dateCongeDebSpeNS;
	private String dateCongeFinSpeNS;
//	private String alerteNS;
//	private String resteJoursCongeNS;
//	private String commentaireNS;
	private String signatureNS;
//	private String fragAlerteNS;
	private String codeCongeNS;
//	private String nbrJoursSoldesInitialNS;
//	private String operationNS;
//	private String btnRejeterNS;
	private String nbrJoursJoursTravailleNS;
//	private String cumulSoldeNS ;
//	private String droitAnnCongNS;
//	private String reliquatAnneEncours;
	private String dateSortieExcepNS;
	private String momSortieExcepNS;
	private String dateEntreeExcepNS;
	private String momEntreeExcepNS;
	private String droitAnnuelleNS;
	private String reliquatDroitAnnuelleNS;
	private String soldeAnterieurNS;
	private String reliquatSoldeAnterieurNS;
	private String soldeanneEncoursNS;
	private String reliquatSoldeanneEncoursNS;
	private String nbrJoursJoursTravailleSpecNS;
	
	
//	@SuppressWarnings("unchecked")
//	public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException
//	{
//		this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
//		return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
//	}
	
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
//		btnRefuserNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_RefuserRH");
//		btnRejeterNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_RejeterRH");
//		operationNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeOperation");
//		nbrJoursSoldesInitialNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_nbrJoursSoldesInitial");
		codeCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DemandeConge");
//		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_ALERTERH");
		signatureNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Signature");
		dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateReprise");
		dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateEntree");
		dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseSpe");
		dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateEntreeSpe");
		pjEtatConge = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_EtatCongePJ");
		lieuNS  = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_LieuConge"); 
		btnEnvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ValiderRH");
		nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPrisConge");
		nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsSpeConge");
//		nbrJrTotalSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");  
//		nbrJrResteSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
//		momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieSpe");
		momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntreeSpe");
//		matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieSpe");  
//		matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntreeSpe");  
//		nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsOuvrablesSpe");  
//		msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MsgErrPerSpe");
		supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
		remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
		btnenvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ValiderRH");
		naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_CongeNormalCombine");
		typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeConge");
//		nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPredefinis");
		momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortie");
		momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntree");
//		nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsOuvrables");
//		nbrjrcongdispoNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
//		justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JustifAbscence");
		typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeCongeSpecial");
//		nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPredefinisSpe");
//		justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JustifAbscenceSpe");
//		frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_DONNE_CONG_COMBINE");
		//frag_nbrjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
		
//		msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MsgErrPerConge");
//		resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
//		commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_CommentaireRH");
//		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Alerte"); 
		
//		matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
//		matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
		signatureNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_Signature");
		nbrJoursJoursTravailleNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JoursTravailExcp");
		dateEntreeExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseExp");
		momEntreeExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentRepriseExp");
		dateSortieExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DateSortieExp");
		momSortieExcepNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieExp");
		
		droitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_DroitAnnuelConge");
		reliquatDroitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
		soldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
		reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_RESTESOLDEDISPOAPRES");
		soldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_SOLDEANNENCOURS");
		reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADEC_SOLDEANNENCOURSAPMODIF");
		
		nbrJoursJoursTravailleSpecNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AEDC_JoursTravailSpecExcp");
		
		getWorkflowInstance().setValue(signatureNS, null);
		return super.onAfterLoad();
	}
	
	
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<Connection> ConnectionDefinition(IWorkflowModule im) throws PortalModuleException
	{
		this.ctx = im.getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) Modules.getPortalModule().getConnectionDefinition(ctx,"RH_ATTIJARI");
	}
	
	@SuppressWarnings({
			"unchecked", "unused", "static-access"
	})
	public void generateBC(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource ilr)
	{
		try
		{
			ServiceRH srv = new ServiceRH();
			signatureNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_Signature");
			List<IAttachment> doc = (List<IAttachment>) iw.getValue(signatureNS);
			File newFile = null;
			String filename = "";
			if(doc!=null){
				if(!doc.isEmpty()){
					// Préparation du paramètre
					codeCongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DemandeConge");
//					fragAlerteNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_ALERTERH");
					signatureNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_Signature");
					dateCongeDebNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DateReprise");
					dateCongeFinNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DateEntree");
					dateCongeDebSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseSpe");
					dateCongeFinSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DateEntreeSpe");
					pjEtatConge = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_EtatCongePJ");
					lieuNS  = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_LieuConge"); 
					btnEnvoyerNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_ValiderRH");
					nbrJrSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPrisConge");
					nbrJrNonSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsSpeConge");
//					nbrJrTotalSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");  
//					nbrJrResteSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
//					momentsortieSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieSpe");
					momententreeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntreeSpe");
//					matinApmidisortieSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieSpe");  
//					matinApmidientreeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntreeSpe");  
//					nbrjrouvrSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsOuvrablesSpe");  
//					msgErreurPeriodeCongeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MsgErrPerSpe");
					supHieNS = iw.getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
					remplacantNS = iw.getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
					btnenvoyerNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_ValiderRH");
					naturecongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_CongeNormalCombine");
					typecongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeConge");
//					nbjrpredefNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPredefinis");
					momentsortieNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortie");
					momententreeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentEntree");
//					nbrjrouvrNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsOuvrables");
//					nbrjrcongdispoNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
//					justifabsNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_JustifAbscence");
					typecongespeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_TypeCongeSpecial");
//					nbjrpredefspeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsPredefinisSpe");
//					justifabsspeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_JustifAbscenceSpe");
//					frag_conge_combineNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_FRAG_DONNE_CONG_COMBINE");
					//frag_nbrjrpredefNS = iw.getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
					
//					msgErreurPeriodeCongeNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MsgErrPerConge");
//					resteJoursCongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
//					commentaireNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_CommentaireRH");
//					alerteNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_Alerte");
					nbrJoursJoursTravailleNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_JoursTravailExcp");
//					cumulSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
//					droitAnnCongNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DroitAnnuelConge");
//					reliquatAnneEncours = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
					dateEntreeExcepNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DateRepriseExp");
					momEntreeExcepNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentRepriseExp");
					dateSortieExcepNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DateSortieExp");
					momSortieExcepNS = iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_MomentSortieExp");
					
					droitAnnuelleNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_DroitAnnuelConge");
					reliquatDroitAnnuelleNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_NbreJrsDispoConge");
					soldeAnterieurNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_ResteJrsDispoConge");
					reliquatSoldeAnterieurNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADEC_RESTESOLDEDISPOAPRES");
					soldeanneEncoursNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADEC_SOLDEANNENCOURS");
					reliquatSoldeanneEncoursNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADEC_SOLDEANNENCOURSAPMODIF");
					nbrJoursJoursTravailleSpecNS =(String) iw.getCatalog().getConfiguration().getStringUserProperty("AEDC_JoursTravailSpecExcp");
					
					filename = doc.get(0).getShortName();
					newFile = new File(TurbineServlet.getRealPath("RH") +"\\"+ filename);
					newFile.createNewFile();
					InputStream is = doc.get(0).getInputStream();
					OutputStream os = new FileOutputStream(newFile);
					byte[] buffer = new byte[is.available()];
					int length;
					while ((length = is.read(buffer)) > 0)
					{
						os.write(buffer, 0, length);
					}
					is.close();os.close();
					Map<String, Object> Parametr = new HashMap<String, Object>();
					Parametr.put("signatureRH", filename);
					
					IUser demUser = (IUser) iw.getValue("sys_Creator");
					String demandeur = demUser.getFullName();
					String matriculeDem = demUser.getEmployeeNumber();
					ArrayList<IUser> supUser = (ArrayList<IUser>) iw.getValue(supHieNS);
					String superieur = supUser.get(0).getFullName();
					ArrayList<IUser> remUser = (ArrayList<IUser>) iw.getValue(remplacantNS);
					String remplacant = " --------- ";
					if (remUser != null)
					{
						if (remUser.size() > 0)
							remplacant = remUser.get(0).getFullName();
					}
					String natureConge = (String) iw.getValue(naturecongeNS);
					String typeConge = (String) iw.getValue(typecongeNS);
					String typeCongeSpe = (String) iw.getValue(typecongespeNS);
					
					String momentSortie = (String) iw.getValue(momentsortieNS);
					Parametr.put("matricule", matriculeDem);
					Parametr.put("demandeur", demandeur);
					Calendar startDate = Calendar.getInstance();
					Calendar endDate = Calendar.getInstance();
					Date dateDebNor = (Date) iw.getValue(dateCongeDebNS);
					Date dateFinNor = (Date) iw.getValue(dateCongeFinNS);
					endDate.setTime(dateFinNor);
					startDate.setTime(dateDebNor);
					String lieuConge = (String) iw.getValue(lieuNS);
					
					
					
					Date  dateDemande = (Date) iw.getValue("sys_CreationDate");
					Parametr.put("dateDemande", dateDemande);
					
					Parametr.put("momSortie", momentSortie);
					Parametr.put("lieuConge", lieuConge);
					Parametr.put("natureConge", natureConge);
					//nbrJrSoldeNS
					
					
					
					Date datEntreeExcep = (Date) iw.getValue(dateEntreeExcepNS);
					Calendar date = Calendar.getInstance();
					date.setTime(datEntreeExcep);
					Parametr.put("dateEntreeExcep", srv.afficheDate(date.getTime()));
					
					
					Date datSortieExcep = (Date) iw.getValue(dateSortieExcepNS);
					date.setTime(datSortieExcep);
					Parametr.put("dateSortieExcep", srv.afficheDate(date.getTime()));
					
					
					String momEntreeExcep = (String) iw.getValue(momEntreeExcepNS);
					Parametr.put("momEntreeExcep", momEntreeExcep);
					String momSortieExcep = (String) iw.getValue(momSortieExcepNS);
					Parametr.put("momSortieExcep", momSortieExcep);
					
					String typecongeNor = (String) iw.getValue(typecongeNS);
					String typecongeSpe = (String) iw.getValue(typecongespeNS);
					String maladieComptabilise = null;
					String avecJustif = null;
					boolean comptabilise = false;
					boolean justif = false;
					float nbrJourMaladie = 0;
//					if("Maladie".equals(typecongeNor)){
//						maladieComptabilise = (String) iw.getValue(maladieComptabiliseNS);
//						if("Comptabilisée".equals(maladieComptabilise)){
//							comptabilise = true;
//							nbrJourMaladie = (float) iw.getValue(nbrjrouvrNS);
//							Parametr.put("maladie", "Comptabilisé");
//						}else{
//							Parametr.put("maladie", "Non comptabilisé");
//						}
//						avecJustif= (String) getWorkflowInstance().getValue(AvecJustifNorNS); 
//						if("Oui".equals(avecJustif)){
//							Parametr.put("justifs", "Justifié");
//						}
//						else{
//							Parametr.put("justifs", "Non justifié");
//						}
//					}
//					else if("Maladie".equals(typecongeSpe) && "Combiné".equals(natureConge)){
//						maladieComptabilise = (String) iw.getValue(maladieComptabiliseNS);
//						if("Comptabilisée".equals(maladieComptabilise)){
//							comptabilise = true;
//							nbrJourMaladie = (float) iw.getValue(nbrjrouvrSpeNS);
//							Parametr.put("maladie", "Comptabilisé");
//						}else{
//							Parametr.put("maladie", "Non comptabilisé");
//						}
//						avecJustif= (String) getWorkflowInstance().getValue(AvecJustifSpeNS); 
//						if("Oui".equals(avecJustif)){
//							Parametr.put("justifs", "Justifié");
//						}
//						else{
//							Parametr.put("justifs", "Non justifié");
//						}
//					}
					
					float nbrJrOuvrConge = (float) iw.getValue(nbrJrSoldeNS)/*+(float) iw.getValue(nbrJrNonSoldeNS)*/;
					float nbrNnJrOuvrConge = (float) iw.getValue(nbrJrNonSoldeNS);
					float totalConge = nbrJrOuvrConge+nbrNnJrOuvrConge;
					if (testNumberIsFloatOrInt(totalConge))
					{
						Float xx = totalConge;
						int x = xx.intValue();
						Parametr.put("nbrJrOuvr", x + "");
					}
					else
					{
						Parametr.put("nbrJrOuvr", totalConge);
					}
					
					float nbrJrTravaille = (float) iw.getValue(nbrJoursJoursTravailleNS);
					float nbrJrTravailleSpec = (float) iw.getValue(nbrJoursJoursTravailleSpecNS);
//					String comptabiliseNor = (String) iw.getValue("AEDC_COMPTANOR");
//					String comptabiliseSpe = (String) iw.getValue("AEDC_COMPTASPE");
//					if("comptabilisé".equals(comptabiliseNor)||"comptabilisé".equals(comptabiliseSpe)){
//						nbrJrTravaille+=nbrJrTravailleSpec;
//						nbrJrTravailleSpec = 0;
//					}
						
					
					if (testNumberIsFloatOrInt(nbrJrTravailleSpec))
					{
						Float xx = nbrJrTravailleSpec;
						int x = xx.intValue();
						Parametr.put("congeSP", x + "");
					}
					else
					{
						Parametr.put("congeSP", nbrJrTravailleSpec);
					}
					
					
					if (testNumberIsFloatOrInt(nbrJrTravaille))
					{
						Float xx = nbrJrTravaille;
						int x = xx.intValue();
						Parametr.put("nbrJrTravaille", x + "");
					}
					else
					{
						Parametr.put("nbrJrTravaille", nbrJrTravaille);
					}
					
					if (testNumberIsFloatOrInt(nbrJrTravaille))
					{
						Float xx = nbrJrTravaille;
						int x = xx.intValue();
						Parametr.put("congeDemande", x + "");
					}
					else
					{
						Parametr.put("congeDemande", nbrJrTravaille);
					}
					
					
					float droitAnnuelle = (float) iw.getValue(droitAnnuelleNS);
					if (testNumberIsFloatOrInt(droitAnnuelle))
					{
						Float xx = droitAnnuelle;
						int x = xx.intValue();
						Parametr.put("DroitAnnuelle", x + "");
					}
					else
					{
						Parametr.put("DroitAnnuelle", droitAnnuelle);
					}
					
					float reliquatDroitAnnuelle = (float) iw.getValue(reliquatDroitAnnuelleNS);
					if (testNumberIsFloatOrInt(reliquatDroitAnnuelle))
					{
						Float xx = reliquatDroitAnnuelle;
						int x = xx.intValue();
						Parametr.put("ReliquatDA", x + "");
					}
					else
					{
						Parametr.put("ReliquatDA", reliquatDroitAnnuelle);
					}
					
					float soldeAnterieur = (float) iw.getValue(soldeAnterieurNS);
					if (testNumberIsFloatOrInt(soldeAnterieur))
					{
						Float xx = soldeAnterieur;
						int x = xx.intValue();
						Parametr.put("soldeAnterieur", x + "");
					}
					else
					{
						Parametr.put("soldeAnterieur", soldeAnterieur);
					}
					
					float reliquatSoldeAnterieur = (float) iw.getValue(reliquatSoldeAnterieurNS);
					if (testNumberIsFloatOrInt(reliquatSoldeAnterieur))
					{
						Float xx = reliquatSoldeAnterieur;
						int x = xx.intValue();
						Parametr.put("reliquatSA", x + "");
					}
					else
					{
						Parametr.put("reliquatSA", reliquatSoldeAnterieur);
					}
					
					float soldeAnneEncours = (float) iw.getValue(soldeanneEncoursNS);
					if (testNumberIsFloatOrInt(soldeAnneEncours))
					{
						Float xx = soldeAnneEncours;
						int x = xx.intValue();
						Parametr.put("soldeAnneeEnCours", x + "");
					}
					else
					{
						Parametr.put("soldeAnneeEnCours", soldeAnneEncours);
					}
					
					float reliquatSoldeAnneEncours = (float) iw.getValue(reliquatSoldeanneEncoursNS);
					if (testNumberIsFloatOrInt(reliquatSoldeAnneEncours))
					{
						Float xx = reliquatSoldeAnneEncours;
						int x = xx.intValue();
						Parametr.put("reliquatAnneeEncours", x + "");
					}
					else
					{
						Parametr.put("reliquatAnneeEncours", reliquatSoldeAnneEncours);
					}
					
					float total = reliquatSoldeAnneEncours + reliquatSoldeAnterieur;
					if (testNumberIsFloatOrInt(total))
					{
						Float xx = total;
						int x = xx.intValue();
						Parametr.put("total", x + "");
					}
					else
					{
						Parametr.put("total", total);
					}
					
					String momentEntree = "";
					if (natureConge.equals("Non combiné"))
					{
						Parametr.put("typeConge", typeConge);
						Parametr.put("periodeCongeDeb",srv.afficheDate(startDate.getTime()));
						Parametr.put("periodeCongeFin",srv.afficheDate(endDate.getTime()));
						momentEntree = (String) iw.getValue(momententreeNS);
					}
					else
					{
						Calendar startDateSpe = Calendar.getInstance();
						Calendar endDateSpe = Calendar.getInstance();
						Date dateDebSpe = (Date) iw.getValue(dateCongeDebSpeNS);
						Date dateFinSpe = (Date) iw.getValue(dateCongeFinSpeNS);
						endDateSpe.setTime(dateFinSpe);
						startDateSpe.setTime(dateDebSpe);
						
						Parametr.put("typeConge", typeConge + " / " + typeCongeSpe);
						Parametr.put("periodeCongeDeb", srv.afficheDate(startDate.getTime()));
						Parametr.put("periodeCongeFin", srv.afficheDate(endDateSpe.getTime()));
						
						momentEntree = (String) iw.getValue(momententreeSpeNS);
					}
					Parametr.put("momEntree", momentEntree);
					String nomFichier = ((String) iw.getValue("sys_Reference"));
					
					Parametr.put("superieur", superieur);
					Parametr.put("remplacant", remplacant);
					Parametr.put("numDemandeConge", nomFichier);
					
					// get nombre dispo du congé
					connection = ConnectionDefinition(im).getConnection();
					String loginVdocOfUser = demUser.getLogin();
					String req = "SELECT FilialeIdFiliale FROM Personnel where loginVdoc = ?";
					st = connection.prepareStatement(req);
					st.setString(1, loginVdocOfUser);
					ResultSet rs = st.executeQuery();
					String filiale = "";
					while (rs.next())
					{
						filiale = rs.getString(1);
					}
					
					if ("attijarifactoring".equals(filiale))
					{
						Parametr.put("logo", "Factoring Quad VF.png");
					}
					else if ("attijarifinancescorp".equals(filiale))
					{
						Parametr.put("logo", "AFC Quad.png");
					}
					else if ("attijariintermediation".equals(filiale))
					{
						Parametr.put("logo", "Attijari Intermediation.png");
					}
					else if ("attijarinvest".equals(filiale))
					{
						Parametr.put("logo", "AWB-Attijar Invest1.png");
					}
					else if ("wafaassurance".equals(filiale))
					{
						Parametr.put("logo", "Wafa Assurance Quad VA-VF.png");
					}
					else if ("wafabail".equals(filiale))
					{
						Parametr.put("logo", "Wafabail Quad.png");
					}
					else if ("wafacourtage".equals(filiale))
					{
						Parametr.put("logo", "Wafa Courtage VF.png");
					}
					else if ("wafacash".equals(filiale))
					{
						Parametr.put("logo", "wafacash.png");
					}
					else if ("wafagestion".equals(filiale))
					{
						Parametr.put("logo", "Wafagestion.png");
					}
					else if ("wafaimmobilier".equals(filiale))
					{
						Parametr.put("logo", "Wafa Immobilier Quad.png");
					}
					else if ("wafalld".equals(filiale))
					{
						Parametr.put("logo", "Wafa LLD.png");
					}
					else if ("wafasalaf".equals(filiale))
					{
						Parametr.put("logo", "Wafasalaf Quad.png");
					}
					else if ("attijariwb".equals(filiale))
					{
						Parametr.put("logo", "wafabourse.png");
					}
					
					
					req = "select CASE WHEN SUM(NbrJoursOuvrables) IS NULL THEN (SELECT convert(DOUBLE PRECISION,0)) "
							+ "ELSE (SELECT convert(DOUBLE PRECISION,SUM(NbrJoursOuvrables))) END"
							+ " from Conge where (TypeConge = 'Normal payé' or (TypeConge='Maladie' and maladieComptabilise = 1))"
							+ "and Personnelmatricule = ? and EtatConge =  'valide'";
					st = connection.prepareStatement(req);
					st.setString(1, loginVdocOfUser);
					rs = st.executeQuery();
					
					float congePris = new ServiceRH().calculateCongePris(loginVdocOfUser)/*-nbrJrTravaille*/;
					
					if(testNumberIsFloatOrInt(congePris)){
						Float xx = congePris;
						int x = xx.intValue();
						Parametr.put("congePris", x+"");
					}
					else{
						Parametr.put("congePris", congePris);
					}
					
					nomFichier = nomFichier.replace("/", "_");
					String nomFichierPDF = nomFichier + ".pdf";
					FileManager path = new FileManager();
					connection = ConnectionDefinition(im).getConnection();
					new GenererPDF().generer("ETATCONGE_ANNULATION_EXCEP", path.getOutDir(), nomFichier, Parametr, connection);
					
					EncryptionFile.crypter(TurbineServlet.getRealPath("RH") + "\\" + filename, TurbineServlet.getRealPath("RH") + "\\" + filename);
					EncryptionFile.deleteFile(TurbineServlet.getRealPath("RH") + "\\" + filename);
					// Création d'un objet pièce jointe à partir d'un fichier sur le serveur
					iw.setValue(pjEtatConge, null);
					IAttachment idisk = im.addAttachment(iw, pjEtatConge, nomFichierPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF);
					
					EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF);
					EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF);
				}
				
				else{
					ir.alert("La signature est obligatoire.");
				}
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public boolean testNumberIsFloatOrInt(float number){
		Float totalnbrjrouvr = number;
		String ch = totalnbrjrouvr.toString();
		char lastChar = ch.charAt(ch.length()-1);
		if(lastChar == '0'){
			return true;
		}
		else{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if(action.getName().equals(btnEnvoyerNS)){
			List<IAttachment> listeEtatsConge = (List<IAttachment>) getWorkflowInstance().getValue(pjEtatConge);
			if(listeEtatsConge==null){
				getResourceController().alert("Veuillez générer l'état du congé svp. c'est obligatoire");
				return false;
			}
			else{
				if(listeEtatsConge.isEmpty()){
					getResourceController().alert("Veuillez générer l'état du congé svp. c'est obligatoire");
					return false;
				}
			}
		}
		return super.onBeforeSubmit(action);
	}
	
	
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property)
	{
		// TODO Auto-generated method stub
		if(property.getName().equals(signatureNS)){
			return true;
		}
		return super.isOnChangeSubscriptionOn(property);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		if(property.getName().equals(signatureNS)){
			List<IAttachment> doc = (List<IAttachment>) getWorkflowInstance().getValue(signatureNS);
			if(doc!=null){
				if(doc.isEmpty()){
					getWorkflowInstance().setValue(pjEtatConge, null);
				}
			}
			
			if(doc==null){
				getWorkflowInstance().setValue(pjEtatConge, null);
			}
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
		ServiceRH rh = new ServiceRH();
		float nbJour = 0;
		while (rh.compareTwoDates(date1.getTime(), date2.getTime())<=0)
		{
			boolean test = false;// joursFeries.contains(date1);
			for (Date ligne : joursFeries)
			{
				dateToCompare.setTime(ligne);
				if (rh.compareTwoDates(date1.getTime(), dateToCompare.getTime())==0)
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
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try
		{
			if (action.getName().equals(btnenvoyerNS)){
				
				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				
				Date dateEntreeExcep = (Date) getWorkflowInstance().getValue(dateEntreeExcepNS);
				Date dateSortieExcep = (Date) getWorkflowInstance().getValue(dateSortieExcepNS);
				String momentSortieExcep = (String) getWorkflowInstance().getValue(momSortieExcepNS);
				String momentEntreeExcep = (String) getWorkflowInstance().getValue(momEntreeExcepNS);
				float nbrJoursTravaille = (float) getWorkflowInstance().getValue(nbrJoursJoursTravailleNS);
				
				String reqinsert = "insert into InfosCongeAnnule(idConge ,dateEntreeExcep,dateSortieExcep,momEntreeExcep,momSortieExcep,nbrJoursTravaille) Values (?,?,?,?,?,?) ";
				PreparedStatement st = connection.prepareStatement(reqinsert);
				String codeDemandeConge = (String) getWorkflowInstance().getValue(codeCongeNS);
				st.setString(1, codeDemandeConge);
				java.sql.Date datesql = new java.sql.Date(dateEntreeExcep.getTime());
				st.setDate(2, datesql);
				datesql = new java.sql.Date(dateSortieExcep.getTime());
				st.setDate(3, datesql);
				st.setString(4,momentEntreeExcep);
				st.setString(5,momentSortieExcep);
				st.setFloat(6,nbrJoursTravaille);
				st.executeUpdate();
				
//				float daysReprise = (float) getWorkflowInstance().getValue(nbrJoursJoursTravailleNS);
				
				IUser personnel = (IUser) getWorkflowInstance().getCreatedBy();
				String loginVdoc = personnel.getLogin();
				float nbrNvJoursPris = (float) getWorkflowInstance().getValue(nbrJoursJoursTravailleNS);
				new ServiceRH().updateSoldeCongePlus(loginVdoc, nbrNvJoursPris);
				
				String req = "update Conge set EtatConge='congé modifié' where CodeVdocDemandeConge = ?  ";
				st = connection.prepareStatement(req);
				st.setString(1, codeDemandeConge);
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
