package com.attijari.processAnnulationConge;

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

public class _3ValidationRH extends BaseDocumentExtension
{
	/**
	 *com.attijari.processAnnulationConge._3ValidationRH 
	 *com.attijari.processAnnulationConge._3ValidationRH
	 */
	private static final long serialVersionUID = -4102309135372902802L;
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	private String btnEnvoyerNS;
	private String btnRefuserNS;
	private String supHieNS;
	private String remplacantNS;
	@SuppressWarnings("unused")
	private String btnenvoyerNS;
	private String naturecongeNS;
	private String typecongeNS;
	@SuppressWarnings("unused")
	private String nbjrpredefNS;
	private String momentsortieNS;
	private String momententreeNS;
	private String nbrjrouvrNS;
	@SuppressWarnings("unused")
	private String nbrjrcongdispoNS;
	@SuppressWarnings("unused")
	private String justifabsNS;
	private String typecongespeNS;
	@SuppressWarnings("unused")
	private String nbjrpredefspeNS;
	@SuppressWarnings("unused")
	private String justifabsspeNS;
	@SuppressWarnings("unused")
	private String frag_conge_combineNS;
	@SuppressWarnings("unused")
	private String frag_nbrjrpredefNS;
	@SuppressWarnings("unused")
	private String matinApmidisortieNS;
	@SuppressWarnings("unused")
	private String matinApmidientreeNS;
	@SuppressWarnings("unused")
	private String msgErreurPeriodeCongeNS;
	@SuppressWarnings("unused")
	private String msgErreurPeriodeCongeSpeNS;
	@SuppressWarnings("unused")
	private String nbrjrouvrSpeNS;
	@SuppressWarnings("unused")
	private String matinApmidisortieSpeNS;
	@SuppressWarnings("unused")
	private String matinApmidientreeSpeNS;
	private String momentsortieSpeNS;
	private String momententreeSpeNS;
	private String pjEtatConge;
	@SuppressWarnings("unused")
	private String nbrJrSoldeNS;
	private String lieuNS;
	@SuppressWarnings("unused")
	private String nbrJrNonSoldeNS;
	private String dateCongeDebNS;
	private String dateCongeFinNS;
	private String dateCongeDebSpeNS;
	private String dateCongeFinSpeNS;
	private String alerteNS;
	private String resteJoursCongeNS;
	private String commentaireNS;
	private String signatureNS;
	private String fragAlerteNS;
	private String codeCongeNS;
	private String nbrJoursSoldesInitialNS;
	private String operationNS;
	private String btnRejeterNS;
	private String cumulSoldeNS ;
	private String droitAnnCongNS;
	private String resteDispoApresNS;
	
	private String droitAnnuelleNS;
	private String reliquatDroitAnnuelleNS;
	private String soldeAnterieurNS;
	private String reliquatSoldeAnterieurNS;
	private String soldeanneEncoursNS;
	private String reliquatSoldeanneEncoursNS;
	
	
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
		btnRefuserNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_RefuserRH");
		btnRejeterNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_RejeterRH");
		operationNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_TypeOperation");
		nbrJoursSoldesInitialNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_nbrJoursSoldesInitial");
		codeCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DemandeConge");
		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_ALERTERH");
		signatureNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SIGNATURE");
		dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DateReprise");
		dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DateEntree");
		dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DateRepriseSpe");
		dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DateEntreeSpe");
		pjEtatConge = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_PJETATCONG");
		lieuNS  = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_LieuConge"); 
		btnEnvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ValiderRH");
		nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPrisConge");
		nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsSpeConge");
//		nbrJrTotalSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");  
//		nbrJrResteSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
		momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortieSpe");
		momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntreeSpe");
		matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortieSpe");  
		matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntreeSpe");  
		nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsOuvrablesSpe");  
		msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MsgErrPerSpe");
		supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
		remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
		btnenvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ValiderRH");
		naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_CongeNormalCombine");
		typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_TypeConge");
		nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPredefinis");
		momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortie");
		momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntree");
		nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsOuvrables");
		nbrjrcongdispoNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsDispoConge");
		justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_JustifAbscence");
		typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_TypeCongeSpecial");
		nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPredefinisSpe");
		justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_JustifAbscenceSpe");
		frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_DONNE_CONG_COMBINE");
		//frag_nbrjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
		
		msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_MsgErrPerConge");
		resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
		commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_CommentaireRH");
		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Alerte"); 
		signatureNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SIGNATURE");
		
		droitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DroitAnnuelConge");
		reliquatDroitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsDispoConge");
		soldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
		reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_RESTESOLDEDISPOAPRES");
		soldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SOLDEANNENCOURS");
		reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SOLDEANNENCOURSAPMODIF");
		
		
		matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
		matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
		
		
		
		float resteConge = (float) getWorkflowInstance().getValue(resteJoursCongeNS);
		//signatureNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SIGNATURE");
		getWorkflowInstance().setValue(signatureNS, null);
		if(resteConge<0){
			getResourceController().setMandatory(commentaireNS, true);
			getWorkflowInstance().setValue(alerteNS, "Le reste des jours de congé est inférieur à 0");
			getResourceController().showBodyBlock(fragAlerteNS, true);
		}
		else{
			getResourceController().setMandatory(commentaireNS, false);
			getWorkflowInstance().setValue(alerteNS, "");
			getResourceController().showBodyBlock(fragAlerteNS, false);
		}
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
			signatureNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_SIGNATURE");
			List<IAttachment> doc = (List<IAttachment>) iw.getValue(signatureNS);
			File newFile = null;
			String filename = "";
			if(doc!=null){
				if(!doc.isEmpty()){
					// Préparation du paramètre
					cumulSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
					droitAnnCongNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_DroitAnnuelConge");
					codeCongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_DemandeConge");
					fragAlerteNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_ALERTERH");
					signatureNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_SIGNATURE");
					dateCongeDebNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_DateReprise");
					dateCongeFinNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_DateEntree");
					dateCongeDebSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_DateRepriseSpe");
					dateCongeFinSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_DateEntreeSpe");
					pjEtatConge = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_PJETATCONG");
					lieuNS  = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_LieuConge"); 
					btnEnvoyerNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_ValiderRH");
					nbrJrSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPrisConge");
					nbrJrNonSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsSpeConge");
//					nbrJrTotalSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");  
//					nbrJrResteSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
					momentsortieSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortieSpe");
					momententreeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntreeSpe");
					matinApmidisortieSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortieSpe");  
					matinApmidientreeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntreeSpe");  
					nbrjrouvrSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsOuvrablesSpe");  
					msgErreurPeriodeCongeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_MsgErrPerSpe");
					supHieNS = iw.getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
					remplacantNS = iw.getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
					btnenvoyerNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_ValiderRH");
					naturecongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_CongeNormalCombine");
					typecongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_TypeConge");
					nbjrpredefNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPredefinis");
					momentsortieNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_MomentSortie");
					momententreeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_MomentEntree");
					nbrjrouvrNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsOuvrables");
					nbrjrcongdispoNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsDispoConge");
					justifabsNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_JustifAbscence");
					typecongespeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_TypeCongeSpecial");
					nbjrpredefspeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPredefinisSpe");
					justifabsspeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_JustifAbscenceSpe");
					frag_conge_combineNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_DONNE_CONG_COMBINE");
					//frag_nbrjrpredefNS = iw.getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
					
					msgErreurPeriodeCongeNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADC_MsgErrPerConge");
					resteJoursCongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
					commentaireNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_CommentaireRH");
					alerteNS = iw.getCatalog().getConfiguration().getStringUserProperty("ADC_Alerte");
					
					droitAnnuelleNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADC_DroitAnnuelConge");
					reliquatDroitAnnuelleNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsDispoConge");
					soldeAnterieurNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
					reliquatSoldeAnterieurNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADC_RESTESOLDEDISPOAPRES");
					soldeanneEncoursNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADC_SOLDEANNENCOURS");
					reliquatSoldeanneEncoursNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("ADC_SOLDEANNENCOURSAPMODIF");
					
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
					float nbrJrOuvrConge = (float) iw.getValue(nbrJrSoldeNS)/*+(float) iw.getValue(nbrJrNonSoldeNS)*/;
					float nbrNnJrOuvrConge = (float) iw.getValue(nbrJrNonSoldeNS);
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
					if(testNumberIsFloatOrInt((nbrJrOuvrConge+nbrNnJrOuvrConge))){
						Float xx = (nbrJrOuvrConge+nbrNnJrOuvrConge);
						int x = xx.intValue();
						Parametr.put("nbrJrOuvr", x+" ");
					}
					else{
						Parametr.put("nbrJrOuvr", (nbrJrOuvrConge+nbrNnJrOuvrConge));
					}
					Parametr.put("momSortie", momentSortie);
					Parametr.put("lieuConge", lieuConge);
					Parametr.put("natureConge", natureConge);
					//nbrJrSoldeNS
					
					
					
					
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
					
					if (testNumberIsFloatOrInt(nbrJrOuvrConge))
					{
						Float xx = nbrJrOuvrConge;
						int x = xx.intValue();
						Parametr.put("congeDemande", x + "");
					}
					else
					{
						Parametr.put("congeDemande", nbrJrOuvrConge);
					}
					
					if (testNumberIsFloatOrInt(nbrNnJrOuvrConge))
					{
						Float xx = nbrNnJrOuvrConge;
						int x = xx.intValue();
						Parametr.put("congeSP", x + "");
					}
					else
					{
						Parametr.put("congeSP", nbrNnJrOuvrConge);
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
						Parametr.put("periodeCongeFin", srv.afficheDate(endDate.getTime()));
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
						Parametr.put("periodeCongeDeb",srv.afficheDate(startDate.getTime()));
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
					
					
					float congePris = new ServiceRH().calculateCongePris(loginVdocOfUser)/*-nbrJrOuvrConge*/;
					
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
					new GenererPDF().generer("ETATCONGE_ANNULATION", path.getOutDir(), nomFichier, Parametr, connection);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try{
			
			if(action.getName().equals(btnEnvoyerNS)){
//				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
//				String codeVDOC = (String) getWorkflowInstance().getValue("sys_Reference");
//				String req = "update Conge set EtatConge = ?,rhMatricule=? where CodeVdocDemandeConge = ?";
//				st = connection.prepareStatement(req);
//				st.setString(1, "en etat conge valide");
//				st.setString(2, matrRH);
//				st.setString(3, codeVDOC);
//				st.executeUpdate();
				String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
				String codeDemCongVdoc = (String) getWorkflowInstance().getValue(codeCongeNS);
//				String typecongeNor = (String) getWorkflowInstance().getValue(typecongeNS);
//				String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
				
				Date dateDebNor = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date dateFinNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				
//				Date dateDemandeConge = getWorkflowInstance().getCreatedDate();
				
				float nbrJrOuvrableCongeNor = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
				
//				String etatCongeNor = "valide";
				String momentSortieNor = (String) getWorkflowInstance().getValue(momentsortieNS);
				String momentEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
				String lieu = (String) getWorkflowInstance().getValue(lieuNS);
				IUser personnel = (IUser) getWorkflowInstance().getCreatedBy();
//				String matriculePersonnel = personnel.getLogin();
				 
//				List<IUser> users = (List<IUser>) getWorkflowInstance().getValue(supHieNS);
//				IUser superieur = users.get(0);
//				String matriculeSup = superieur.getLogin();
//				users = (List<IUser>) getWorkflowInstance().getValue(remplacantNS);
//				IUser remplaçant = null;
//				String matriculeRemp = null;
//				if(users!=null){
//					if(!users.isEmpty()){
//						remplaçant = users.get(0);
//						matriculeRemp = remplaçant.getEmployeeNumber();
//					}
//					
//				}
//				
				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				
				String req = "update Conge set DateDeb=?, DateFin=?, NbrJoursOuvrables=?, MomSortie=?, MomEntre=?,Lieu=? where idConge = ?";
					st = connection.prepareStatement(req);
					st.setString(7, codeDemCongVdoc+"NOR");
					java.sql.Date dateDeb = new java.sql.Date(dateDebNor.getTime());
					st.setDate(1, dateDeb);
					java.sql.Date dateFin = new java.sql.Date(dateFinNor.getTime());
					st.setDate(2, dateFin);
					st.setFloat(3, nbrJrOuvrableCongeNor);
					st.setString(4, momentSortieNor);
					st.setString(5, momentEntreeNor);
					st.setString(6, lieu);
					st.executeUpdate();
					
					
				if("Combiné".equals(natureConge)){
					Date dateDebSpe = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
					Date dateFinSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
					
					float nbrJrOuvrableCongeSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
					
					
					String momentSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
					String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
					
					req = "update Conge set DateDeb=?, DateFin=?, NbrJoursOuvrables=?, MomSortie=?, MomEntre=?,Lieu=? where idConge = ?";
					st = connection.prepareStatement(req);
					st.setString(7, codeDemCongVdoc+"SPE");
					dateDeb = new java.sql.Date(dateDebSpe.getTime());
					st.setDate(1, dateDeb);
					dateFin = new java.sql.Date(dateFinSpe.getTime());
					st.setDate(2, dateFin);
					st.setFloat(3, nbrJrOuvrableCongeSpe);
					st.setString(4, momentSortieSpe);
					st.setString(5, momentEntreeSpe);
					st.setString(6, lieu);
					st.executeUpdate();
				}
				
				
				String loginVdoc = personnel.getLogin();
				float nbrNvJoursPris = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
				new ServiceRH().updateSoldeCongePlus(loginVdoc, nbrNvJoursPris);
			}
			
			if(action.getName().equals(btnRefuserNS)||action.getName().equals(btnRejeterNS)){
				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				String codeVDOC = (String) getWorkflowInstance().getValue(codeCongeNS);
				String req = "update Conge set EtatConge = ? where CodeVdocDemandeConge = ?";
				st = connection.prepareStatement(req);
				st.setString(1, "valide");
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
