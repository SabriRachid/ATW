package com.attijari.processDemandeConge;

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

import beans.Conge;

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
import com.axemble.vdoc.sdk.modules.IPortalModule;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.structs.Period;
import com.axemble.vdp.ui.framework.foundation.Navigator;
import com.genererRapport.FileManager;
import com.genererRapport.GenererPDF;
import com.serviceRH.EncryptionFile;
import com.serviceRH.ServiceRH;

import dao.SingletonConnexionBDD;

public class _4ValidationRH extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4102309135372902802L;
	private IContext ctx;
	private String rhNS;
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
	private String commentaireNS;
	private String signatureNS;
	private String fragAlerteNS;
	private String maladieComptabiliseNS;
	private String AvecJustifNorNS;
	private String AvecJustifSpeNS;
	
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
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		//IPortalModule ipm = getPortalModule();
		rhNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DIRECTEUR_RESSOURCES_HUMAINES");
		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_ALERTRH");
		signatureNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALRH_SIGNATURE");
		dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEREPRISE");
		dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEENTREE");
		dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEREPRISESPE");
		dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEENTREESPE");
		pjEtatConge = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALRH_PJETATCONG");
		lieuNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_LIEUCONGE");
		btnEnvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALRH_BTNVALIDER");
		nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRSOLDCONG");
		nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRNSOLDCONG");
		// nbrJrTotalSoldeNS =
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");
		// nbrJrResteSoldeNS =
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
		momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORTSPE");
		momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTRSPE");
		matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORTSPE");
		matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTRSPE");
		nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJROUVSPE");
		msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGMSGSPE");
		supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
		remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
		btnenvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_BTNENVOYER");
		naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NATURECONGE");
		typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TYPECONGE");
		nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRDEF");
		momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORT");
		momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTR");
		nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJROUV");
		nbrjrcongdispoNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGDISPO");
		justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_JUSTABSC");
		typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TYPECONGESP");
		nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRDEFSPE");
		justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_JUSTABSCSPE");
		frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_DONNE_CONG_COMBINE");
		frag_nbrjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
		matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
		matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
		msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGMSG");
		commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALRH_COMM");
		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_ALERT");
		maladieComptabiliseNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALRH_MALADIECOMPTA");
		AvecJustifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_AVECJUSTIFNOR");
		AvecJustifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_AVECJUSTIFSPE");
		
		soldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
		reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPOMODIF");
		soldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURS");
		reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURSAPMODIF");
		droitAnnuelleInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGEINIT");
//		reliquatDroitAnnuelleInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGDISPOINIT");
//		reliquatSoldeAnterieurInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPOMODIFINIT");
//		reliquatSoldeanneEncoursInitNS  = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURSAPMODIFINIT");
		
		
		// resteDispoApresNS = (String)
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGDISPO");
		// String resteJoursCongeModifNS = (String)
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPOAPRESDEMCON_NBRJRCONGDISPO");
		
		float resteConge = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
		signatureNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SIGNATURE");
		getWorkflowInstance().setValue(signatureNS, null);
		if (resteConge < 0)
		{
			getResourceController().setMandatory(commentaireNS, true);
			getWorkflowInstance().setValue(alerteNS, "solde négatif");
			getResourceController().showBodyBlock(fragAlerteNS, true);
		}
		else
		{
			getResourceController().setMandatory(commentaireNS, false);
			getWorkflowInstance().setValue(alerteNS, "");
			getResourceController().showBodyBlock(fragAlerteNS, false);
		}
		getWorkflowInstance().setValue(pjEtatConge, null);
		getWorkflowInstance().setValue(maladieComptabiliseNS, "Non Comptabilisée");
		
		
//		reliquatDroitAnnuelleINIT = (float) getWorkflowInstance().getValue(reliquatDroitAnnuelleNS);
//		reliquatSoldeanneEncoursINIT = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
//		reliquatSoldeAnterieurINIT = (float) getWorkflowInstance().getValue(reliquatSoldeAnterieurNS);
		return super.onAfterLoad();
	}
	
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<Connection> ConnectionDefinition(IWorkflowModule im) throws PortalModuleException
	{
		this.ctx = im.getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) Modules.getPortalModule().getConnectionDefinition(ctx, "RH_ATTIJARI");
	}
	
	public void setCalcule(){
//		float oldDroitAnnuelle = (float) getWorkflowInstance().getValue(droitAnnuelleNS);
		float oldSoldeAnterieur = (float) getWorkflowInstance().getValue(reliquatSoldeAnterieurNS);
		float oldSoldeEnCours = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
		float droitAnnuelleInit = (float) getWorkflowInstance().getValue(droitAnnuelleInitNS);
		
		String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
		float nbrJrCongeOuvr = 0 ;
		if(natureconge.equals("Combiné")){
			String typeConge = (String) getWorkflowInstance().getValue(typecongeNS);
			String typeCongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
			
			if(typeConge.equals("Divers")||typeConge.equals("Maladie")){
				nbrJrCongeOuvr = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
			}
			else if(typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie")){
				nbrJrCongeOuvr = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
			}
		}
		else{
			nbrJrCongeOuvr = (float) getWorkflowInstance().getValue(nbrJrNonSoldeNS);
		}
		
		
//		if(oldSoldeEnCours<droitAnnuelleInit){
			
			float nvSANC = 0;
			float nvSANT = 0;
			if(nbrJrCongeOuvr<=oldSoldeAnterieur){
				nvSANC = oldSoldeEnCours;
				nvSANT = -nbrJrCongeOuvr+oldSoldeAnterieur;
			}
			else{
				nvSANC = (oldSoldeAnterieur-nbrJrCongeOuvr)+oldSoldeEnCours;
				nvSANT = 0 ; 
			}
			
			getWorkflowInstance().setValue(reliquatSoldeAnterieurNS, nvSANT);
			getWorkflowInstance().setValue(reliquatSoldeanneEncoursNS, nvSANC);
			getWorkflowInstance().setValue("DEMCON_SOLDE", nvSANT+nvSANC);
			
//		}
//		else if(oldSoldeEnCours==droitAnnuelleInit){
//			float nvSANC = droitAnnuelleInit;
//			float nvSANT = 0;
//			nvSANT = oldSoldeAnterieur - nbrJrCongeOuvr;
//			if(nvSANT < 0){
//				nvSANC=(nvSANT+droitAnnuelleInit);
//				nvSANT = 0;
//			}
//			getWorkflowInstance().setValue(reliquatSoldeAnterieurNS, nvSANT);
//			getWorkflowInstance().setValue(reliquatSoldeanneEncoursNS, nvSANC);
//			getWorkflowInstance().setValue("DEMCON_SOLDE", nvSANT+nvSANC);
//		}
		
	}
	
	public void setCalculeBack(){
//		float oldDroitAnnuelle = (float) getWorkflowInstance().getValue(droitAnnuelleNS);
		float oldSoldeAnterieur = (float) getWorkflowInstance().getValue(reliquatSoldeAnterieurNS);
		float oldSoldeEnCours = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
		float droitAnnuelleInit = (float) getWorkflowInstance().getValue(droitAnnuelleInitNS);
		String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
		float nbrJrCongeOuvr = 0 ;
		if(natureconge.equals("Combiné")){
			String typeConge = (String) getWorkflowInstance().getValue(typecongeNS);
			String typeCongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
			
			if(typeConge.equals("Divers")||typeConge.equals("Maladie")){
				nbrJrCongeOuvr = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
			}
			else if(typeCongeSpe.equals("Divers")||typeCongeSpe.equals("Maladie")){
				nbrJrCongeOuvr = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
			}
		}
		else{
			nbrJrCongeOuvr = (float) getWorkflowInstance().getValue(nbrJrNonSoldeNS);
		}
		
		if(oldSoldeEnCours<droitAnnuelleInit){
			
			float nvSANC = 0;
			float nvSANT = 0;
			if(nbrJrCongeOuvr+oldSoldeEnCours>droitAnnuelleInit){
				nvSANC = droitAnnuelleInit;
				nvSANT = nvSANT+nbrJrCongeOuvr-droitAnnuelleInit+oldSoldeEnCours;
			}
			else{
				nvSANC = nbrJrCongeOuvr+oldSoldeEnCours;
				nvSANT = oldSoldeAnterieur;
			}
			
			getWorkflowInstance().setValue(reliquatSoldeAnterieurNS, nvSANT);
			getWorkflowInstance().setValue(reliquatSoldeanneEncoursNS, nvSANC);
			getWorkflowInstance().setValue("DEMCON_SOLDE", nvSANT+nvSANC);
			
		}
		else if(oldSoldeEnCours==droitAnnuelleInit){
			float nvSANT = oldSoldeAnterieur + nbrJrCongeOuvr;
			getWorkflowInstance().setValue(reliquatSoldeAnterieurNS, nvSANT);
			getWorkflowInstance().setValue(reliquatSoldeanneEncoursNS, droitAnnuelleInit);
			getWorkflowInstance().setValue("DEMCON_SOLDE", nvSANT+droitAnnuelleInit);
		}
		
	}
	
	@SuppressWarnings(
	{
			"unchecked", "unused", "static-access"
	})
	public void generateBC(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource ilr)
	{
		try
		{
			ServiceRH srv = new ServiceRH();
			signatureNS = iw.getCatalog().getConfiguration().getStringUserProperty("VALRH_SIGNATURE");
			List<IAttachment> doc = (List<IAttachment>) iw.getValue(signatureNS);
			File newFile = null;
			String filename = "";
			if (doc != null)
			{
				if (!doc.isEmpty())
				{
					// Préparation du paramètre
					dateCongeDebNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEREPRISE");
					dateCongeFinNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEENTREE");
					dateCongeDebSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEREPRISESPE");
					dateCongeFinSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEENTREESPE");
					lieuNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_LIEUCONGE");
					pjEtatConge = iw.getCatalog().getConfiguration().getStringUserProperty("VALRH_PJETATCONG");
					btnEnvoyerNS = iw.getCatalog().getConfiguration().getStringUserProperty("VALRH_BTNVALIDER");
					btnRefuserNS = iw.getCatalog().getConfiguration().getStringUserProperty("VALRH_BTNREFUSER");
					nbrJrSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRSOLDCONG");
					nbrJrNonSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRNSOLDCONG");
					// nbrJrTotalSoldeNS =
					// iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");
					// nbrJrResteSoldeNS =
					// iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
					momentsortieSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORTSPE");
					momententreeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTRSPE");
					matinApmidisortieSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORTSPE");
					matinApmidientreeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTRSPE");
					nbrjrouvrSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJROUVSPE");
					msgErreurPeriodeCongeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGMSGSPE");
					supHieNS = iw.getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
					remplacantNS = iw.getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
					btnenvoyerNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_BTNENVOYER");
					naturecongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_NATURECONGE");
					typecongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_TYPECONGE");
					nbjrpredefNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRDEF");
					momentsortieNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORT");
					momententreeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTR");
					nbrjrouvrNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJROUV");
					nbrjrcongdispoNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGDISPO");
					justifabsNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_JUSTABSC");
					typecongespeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_TYPECONGESP");
					nbjrpredefspeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRDEFSPE");
					justifabsspeNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_JUSTABSCSPE");
					frag_conge_combineNS = iw.getCatalog().getConfiguration().getStringUserProperty("FRAG_DONNE_CONG_COMBINE");
					frag_nbrjrpredefNS = iw.getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
					matinApmidisortieNS = (String) iw.getValue(momentsortieNS);
					matinApmidientreeNS = (String) iw.getValue(momententreeNS);
					msgErreurPeriodeCongeNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGMSG");
					maladieComptabiliseNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("VALRH_MALADIECOMPTA");
					AvecJustifNorNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_AVECJUSTIFNOR");
					AvecJustifSpeNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_AVECJUSTIFSPE");
					
					soldeAnterieurNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
					reliquatSoldeAnterieurNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPOMODIF");
					soldeanneEncoursNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURS");
					reliquatSoldeanneEncoursNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURSAPMODIF");
					
					filename = doc.get(0).getShortName();
					newFile = new File(TurbineServlet.getRealPath("RH") + "\\" + filename);
					newFile.createNewFile();
					
					
					InputStream is = doc.get(0).getInputStream();
					OutputStream os = new FileOutputStream(newFile);
					byte[] buffer = new byte[is.available()];
					int length;
					while ((length = is.read(buffer)) > 0)
					{
						os.write(buffer, 0, length);
					}
					is.close();
					os.close();
					Map<String, Object> Parametr = new HashMap<String, Object>();
					Parametr.put("signatureRH", filename);
					
					String typecongeNor = (String) iw.getValue(typecongeNS);
					String typecongeSpe = (String) iw.getValue(typecongespeNS);
					String natureConge = (String) iw.getValue(naturecongeNS);
					String maladieComptabilise = null;
					String avecJustif = null;
					boolean comptabilise = false;
					boolean justif = false;
					float nbrJourMaladie = 0;
					float nbrJrOuvrConge = (float) iw.getValue(nbrJrSoldeNS)/* +(float) iw.getValue(nbrJrNonSoldeNS) */;
					float nbrNnJrOuvrConge = (float) iw.getValue(nbrJrNonSoldeNS);
					if ("Maladie".equals(typecongeNor) || "Divers".equals(typecongeNor))
					{
						maladieComptabilise = (String) iw.getValue(maladieComptabiliseNS);
						if ("Comptabilisée".equals(maladieComptabilise))
						{
							comptabilise = true;
							
							
							
							nbrJourMaladie = (float) iw.getValue(nbrjrouvrNS);
							Parametr.put("maladie", "Comptabilisé");
							nbrJrOuvrConge += nbrJourMaladie;
							nbrNnJrOuvrConge -= nbrJourMaladie;
						}
						else
						{
							Parametr.put("maladie", "Non comptabilisé");
						}
						avecJustif = (String) iw.getValue(AvecJustifNorNS);
						if ("Oui".equals(avecJustif))
						{
							Parametr.put("justifs", "Justifié");
						}
						else
						{
							Parametr.put("justifs", "Non justifié");
						}
					}
					else if (("Maladie".equals(typecongeSpe) || "Divers".equals(typecongeSpe)) && "Combiné".equals(natureConge))
					{
						maladieComptabilise = (String) iw.getValue(maladieComptabiliseNS);
						if ("Comptabilisée".equals(maladieComptabilise))
						{
							comptabilise = true;
							nbrJourMaladie = (float) iw.getValue(nbrjrouvrSpeNS);
							Parametr.put("maladie", "Comptabilisé");
							nbrJrOuvrConge += nbrJourMaladie;
							nbrNnJrOuvrConge -= nbrJourMaladie;
						}
						else
						{
							Parametr.put("maladie", "Non comptabilisé");
						}
						avecJustif = (String) iw.getValue(AvecJustifSpeNS);
						if ("Oui".equals(avecJustif))
						{
							Parametr.put("justifs", "Justifié");
						}
						else
						{
							Parametr.put("justifs", "Non justifié");
						}
					}
					
					IUser demUser = (IUser) iw.getValue("sys_Creator");
					String demandeur = demUser.getFullName();
					String matriculeDem = demUser.getEmployeeNumber();
					String login = demUser.getLogin();
					List<IUser> supUser = getSuperieurOf(login,Modules.getPortalModule(),im);//(ArrayList<IUser>) iw.getValue(supHieNS);
					//supUser.addAll( getSuperieurOf(login,Modules.getPortalModule(),im));
					String superieur = "";
					int i=0;
					for(IUser user : supUser){
						if(supUser.size()==1)
							superieur=user.getFullName();
						else{
							if(i<supUser.size()-1)
								superieur+=user.getFullName()+" / ";
							else
								superieur+=user.getFullName();	
						}
								
						i++;
					}
					
					
					ArrayList<IUser> remUser = (ArrayList<IUser>) iw.getValue(remplacantNS);
					String remplacant = " --------- ";
					if (remUser != null)
					{
						if (remUser.size() > 0)
							remplacant = remUser.get(0).getFullName();
					}
					
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
					
					if (testNumberIsFloatOrInt(nbrJrOuvrConge + nbrNnJrOuvrConge))
					{
						Float xx = nbrJrOuvrConge + nbrNnJrOuvrConge;
						int x = xx.intValue();
						Parametr.put("nbrJrOuvr", x + "");
					}
					else
					{
						Parametr.put("nbrJrOuvr", (nbrJrOuvrConge + nbrNnJrOuvrConge));
					}
					
					Parametr.put("momSortie", momentSortie);
					Parametr.put("lieuConge", lieuConge);
					Parametr.put("natureConge", natureConge);
					
					Date dateDemande = (Date) iw.getValue("sys_CreationDate");
					Parametr.put("dateDemande", dateDemande);
					
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
					
				
					
					
					
					float soldeAnterieur = srv.getSoldeAnterieur(login);
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
					
					float reliquatSoldeAnterieur = 0;
					Object nombre = srv.getSoldeEnCours(login);
					if(nombre.getClass().getCanonicalName().equals("java.lang.Double")){
						double reliquatSoldeAnterieurD= (double) iw.getValue(reliquatSoldeAnterieurNS);
						reliquatSoldeAnterieur= Float.parseFloat(reliquatSoldeAnterieurD+"");
						
					}
					else{
						reliquatSoldeAnterieur= Float.parseFloat(iw.getValue(reliquatSoldeAnterieurNS)+"");
						
					}
					
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
					
					// float reliquat = (float) iw.getValue(reliquatDroitAnnuelleNS);
					//
					//
					// if(testNumberIsFloatOrInt(reliquat-(nbrJrOuvrConge+nbrJourMaladie))){
					// Float xx = reliquat-(nbrJrOuvrConge+nbrJourMaladie);
					// int x = xx.intValue();
					// Parametr.put("ReliquatDA", x+"");
					// }
					// else{
					// Parametr.put("ReliquatDA", reliquat);
					// }
					
					// if(testNumberIsFloatOrInt(reliquatSoldeAnterieur+reliquat-(nbrJrOuvrConge+nbrJourMaladie))){
					// Float xx = reliquatSoldeAnterieur+reliquat-(nbrJrOuvrConge+nbrJourMaladie);
					// int x = xx.intValue();
					// Parametr.put("total", x+"");
					// }
					// else{
					// Parametr.put("total", reliquatSoldeAnterieur+reliquat-(nbrJrOuvrConge+nbrJourMaladie));
					// }
					
					String momentEntree = "";
					if (natureConge.equals("Non combiné"))
					{
						Parametr.put("typeConge", typeConge);
						Parametr.put("periodeCongeDeb", srv.afficheDate(startDate.getTime()));
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
					
					
					float congePris = new ServiceRH().calculateCongePris(loginVdocOfUser);
					if (testNumberIsFloatOrInt(congePris))
					{
						Float xx = congePris;
						int x = xx.intValue();
						Parametr.put("congePris", x + "");
					}
					else
					{
						Parametr.put("congePris", congePris);
					}
					
					nomFichier = nomFichier.replace("/", "_");
					String nomFichierPDF = nomFichier + ".pdf";
					FileManager path = new FileManager();
					connection = ConnectionDefinition(im).getConnection();
					new GenererPDF().generer("ETATCONGE", path.getOutDir(), nomFichier, Parametr, connection);
					EncryptionFile.crypter(TurbineServlet.getRealPath("RH") + "\\" + filename, TurbineServlet.getRealPath("RH") + "\\" + filename);
					EncryptionFile.deleteFile(TurbineServlet.getRealPath("RH") + "\\" + filename);
					// Création d'un objet pièce jointe à partir d'un fichier sur le serveur
					iw.setValue(pjEtatConge, null);
					IAttachment idisk = im.addAttachment(iw, pjEtatConge, nomFichierPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF);
					newFile.delete();
					
					EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF);
					EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF);
				}
				
				else
				{
					ir.alert("La signature est obligatoire.");
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean testNumberIsFloatOrInt(float number)
	{
		Float totalnbrjrouvr = number;
		String ch = totalnbrjrouvr.toString();
		char lastChar = ch.charAt(ch.length() - 1);
		if (lastChar == '0')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if (action.getName().equals(btnEnvoyerNS))
		{
			List<IAttachment> listeEtatsConge = (List<IAttachment>) getWorkflowInstance().getValue(pjEtatConge);
			if (listeEtatsConge == null)
			{
				getResourceController().alert("Veuillez générer l'état du congé svp. c'est obligatoire");
				return false;
			}
			else
			{
				if (listeEtatsConge.isEmpty())
				{
					getResourceController().alert("Veuillez générer l'état du congé svp. c'est obligatoire");
					return false;
				}
			}
		}
		else {
			
			String decisionCompta = (String) getWorkflowInstance().getValue(maladieComptabiliseNS);
			if(decisionCompta.equals("Comptabilisée")){
				getWorkflowInstance().setValue(maladieComptabiliseNS, "Non Comptabilisée");
				getResourceController().alert("La décision est devenue non comptabilisé");
				return false;
			}
				
			
		}
		return super.onBeforeSubmit(action);
	}
	
	
	public List<IUser> getSuperieurOf(String matriculeUser,IPortalModule ip, IWorkflowModule im){
		List<IUser> superieurs = new ArrayList<IUser>();
		try{
			String loginVdocOfUser = matriculeUser;
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(im,ip).getConnection();
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
				IUser superieur = im.getUserByLogin(loginSuperieur);
				superieurs.add(superieur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return superieurs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		if (property.getName().equals(signatureNS))
		{
			List<IAttachment> doc = (List<IAttachment>) getWorkflowInstance().getValue(signatureNS);
			if (doc != null)
			{
				if (doc.isEmpty())
				{
					getWorkflowInstance().setValue(pjEtatConge, null);
				}
			}
			
			if (doc == null)
			{
				getWorkflowInstance().setValue(pjEtatConge, null);
			}
		}
		
		if (property.getName().equals(maladieComptabiliseNS))
		{
			getWorkflowInstance().setValue(pjEtatConge, null);
			String maladieComptabilise = (String) getWorkflowInstance().getValue(maladieComptabiliseNS);
//			float nbjrcongenor = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
//			float nbjrcongespe = (float) getWorkflowInstance().getValue(nbrJrNonSoldeNS);
//			float reliquatSoldeAnterieur = (float) getWorkflowInstance().getValue(reliquatSoldeAnterieurNS);
//			float reliquatSoldeEncours = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
//			float soldeAnterieur = (float) getWorkflowInstance().getValue(soldeAnterieurNS);
//			float soldeAnneeEnCours = (float) getWorkflowInstance().getValue(soldeanneEncoursNS);
			
			if ("Comptabilisée".equals(maladieComptabilise))
			{
				setCalcule();
			}
			else
			{
				setCalculeBack();
			}
				
			
		}
		
		super.onPropertyChanged(property);
	}
	
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if (action.getName().equals(btnEnvoyerNS))
		{
			try
			{
				String nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRSOLDCONG");
				if (action.getName().equals(btnEnvoyerNS))
				{
					
					float nbrJoursDemandee = 0;
					// connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
					// String codeVDOC = (String) getWorkflowInstance().getValue("sys_Reference");
					// String req = "update Conge set EtatConge = ?,rhMatricule=? where CodeVdocDemandeConge = ?";
					// st = connection.prepareStatement(req);
					// st.setString(1, "en etat conge valide");
					// st.setString(2, matrRH);
					// st.setString(3, codeVDOC);
					// st.executeUpdate();
					
					String natureConge = (String) getWorkflowInstance().getValue(naturecongeNS);
					
					String codeDemCongVdoc = (String) getWorkflowInstance().getValue("sys_Reference");
					String typecongeNor = (String) getWorkflowInstance().getValue(typecongeNS);
					String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
					String maladieComptabilise = null;
					String avecJustif = null;
					boolean comptabilise = false;
					boolean justif = false;
					float nbrJourMaladie = 0;
					if ("Maladie".equals(typecongeNor) || "Divers".equals(typecongeNor))
					{
						maladieComptabilise = (String) getWorkflowInstance().getValue(maladieComptabiliseNS);
						avecJustif = (String) getWorkflowInstance().getValue(AvecJustifNorNS);
						if ("Comptabilisée".equals(maladieComptabilise))
						{
							comptabilise = true;
							nbrJourMaladie = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
							nbrJoursDemandee+=nbrJourMaladie;
						}
						if ("Oui".equals(avecJustif))
						{
							justif = true;
						}
					}
					else if (("Maladie".equals(typecongeSpe) || "Divers".equals(typecongeSpe)) && "Combiné".equals(natureConge))
					{
						maladieComptabilise = (String) getWorkflowInstance().getValue(maladieComptabiliseNS);
						avecJustif = (String) getWorkflowInstance().getValue(AvecJustifSpeNS);
						if ("Comptabilisée".equals(maladieComptabilise))
						{
							comptabilise = true;
							nbrJourMaladie = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
							nbrJoursDemandee+=nbrJourMaladie;
						}
						if ("Oui".equals(avecJustif))
						{
							justif = true;
						}
					}
					
					Date dateDebNor = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
					Date dateFinNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
					
					Date dateDemandeConge = getWorkflowInstance().getCreatedDate();
					
					float nbrJrOuvrableCongeNor = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
					
					String etatCongeNor = "valide";
					String momentSortieNor = (String) getWorkflowInstance().getValue(momentsortieNS);
					String momentEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
					String lieu = (String) getWorkflowInstance().getValue(lieuNS);
					IUser personnel = (IUser) getWorkflowInstance().getCreatedBy();
					String matriculePersonnel = personnel.getLogin();
					
					List<IUser> users = (List<IUser>) getWorkflowInstance().getValue(supHieNS);
					IUser superieur = users.get(0);
					String matriculeSup = superieur.getLogin();
					users = (List<IUser>) getWorkflowInstance().getValue(remplacantNS);
					IUser remplaçant = null;
					String matriculeRemp = null;
					if (users != null)
					{
						if (!users.isEmpty())
						{
							remplaçant = users.get(0);
							matriculeRemp = remplaçant.getLogin();
						}
						
					}
					
					List<IUser> usersRH = (List<IUser>) getWorkflowInstance().getValue(rhNS);
					IUser rh = usersRH.get(0);
					String loginRH = rh.getLogin();
					connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
					
					String req = "insert into Conge(IdConge, supMatricule, remplacantMatricule, Personnelmatricule, DateDeb, DateFin, DateDemandeConge, NbrJoursOuvrables, TypeConge, EtatConge, MomSortie, MomEntre,CodeVdocDemandeConge,Lieu,natureConge,justifMaladie,maladieComptabilise,rhMatricule) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
					st = connection.prepareStatement(req);
					st.setString(1, codeDemCongVdoc + "NOR");
					st.setString(2, matriculeSup);
					st.setString(3, matriculeRemp);
					st.setString(4, matriculePersonnel);
					java.sql.Date dateDeb = new java.sql.Date(dateDebNor.getTime());
					st.setDate(5, dateDeb);
					java.sql.Date dateFin = new java.sql.Date(dateFinNor.getTime());
					st.setDate(6, dateFin);
					java.sql.Date dateDemande = new java.sql.Date(new Date().getTime());
					st.setDate(7, dateDemande);
					st.setFloat(8, nbrJrOuvrableCongeNor);
					st.setString(9, typecongeNor);
					st.setString(10, etatCongeNor);
					st.setString(11, momentSortieNor);
					st.setString(12, momentEntreeNor);
					st.setString(13, codeDemCongVdoc);
					st.setString(14, lieu);
					st.setString(15, natureConge);
					st.setBoolean(16, justif);
					st.setBoolean(17, comptabilise);
					st.setString(18, loginRH);
					st.executeUpdate();
					
					if ("Combiné".equals(natureConge))
					{
						Date dateDebSpe = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
						Date dateFinSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
						
						float nbrJrOuvrableCongeSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
						
						String etatCongeSpe = "valide";
						
						String momentSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
						String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
						
						req = "insert into  Conge(IdConge, supMatricule, remplacantMatricule, Personnelmatricule, DateDeb, DateFin, DateDemandeConge, NbrJoursOuvrables, TypeConge, EtatConge, MomSortie, MomEntre,CodeVdocDemandeConge,Lieu,natureConge,justifMaladie,maladieComptabilise,rhMatricule) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
						st = connection.prepareStatement(req);
						st.setString(1, codeDemCongVdoc + "SPE");
						st.setString(2, matriculeSup);
						st.setString(3, matriculeRemp);
						st.setString(4, matriculePersonnel);
						dateDeb = new java.sql.Date(dateDebSpe.getTime());
						st.setDate(5, dateDeb);
						dateFin = new java.sql.Date(dateFinSpe.getTime());
						st.setDate(6, dateFin);
						dateDemande = new java.sql.Date(dateDemandeConge.getTime());
						st.setDate(7, dateDemande);
						st.setFloat(8, nbrJrOuvrableCongeSpe);
						st.setString(9, typecongeSpe);
						st.setString(10, etatCongeSpe);
						st.setString(11, momentSortieSpe);
						st.setString(12, momentEntreeSpe);
						st.setString(13, codeDemCongVdoc);
						st.setString(14, lieu);
						st.setString(15, natureConge);
						st.setBoolean(16, justif);
						st.setBoolean(17, comptabilise);
						st.setString(18, loginRH);
						st.executeUpdate();
					}
					
					String loginVdoc = personnel.getLogin();
					float nbrNvJoursPris = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
					nbrNvJoursPris+=nbrJoursDemandee;
					new ServiceRH().updateSoldeCongeMoins(loginVdoc, nbrNvJoursPris );
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
