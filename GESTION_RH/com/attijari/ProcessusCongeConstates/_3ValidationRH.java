package com.attijari.ProcessusCongeConstates;

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
import com.axemble.vdoc.sdk.interfaces.IResource;
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
	 * 
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
	private String demandeur;
	private String frag_remplacantNS;
	private String fragmotifSpeNS;
	private String fragmotifNorNS;
	private String motifSpeNS;
	private String motifNorNS;
	private String droitAnnCongNS;
	private String cumulSoldeNS;
	
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
	
		remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
		lieuNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_LieuConge");
		demandeur = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("PERSONNEL");
		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_ALERTERH");
		resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
		commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_CommentaireConRH");
		frag_remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_REMPLACANT");
		fragmotifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_MOTIFSPE");
		fragmotifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_MOTIFNOR");
		motifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MotifsSpe");
		motifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Motifs");
		dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DateReprise");
		dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DateEntree");
		dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DateRepriseSpe");
		dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DateEntreeSpe");
		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Alerte");
		droitAnnCongNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DroitAnnuelConge");
		nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsSoldesConge");
		nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsNonSoldesConge");
		// nbrJrTotalSoldeNS =
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");
		// nbrJrResteSoldeNS =
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
		momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortieSpe");
		momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntreeSpe");
		matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortieSpe");
		matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntreeSpe");
		nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsOuvrablesSpe");
		msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MsgErrPerSpe");
		supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
		// remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
		btnEnvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Confirmer");
		naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_CongeNormalCombine");
		typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_TypeConge");
		nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsPredefinis");
		momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortie");
		momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntree");
		// periodecongenorNS =
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONG");
		nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsOuvrables");
		nbrjrcongdispoNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsDispoConge");
		justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_JustifAbscence");
		typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_TypeCongeSpecial");
		nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsPredefinisSpe");
		// periodecongespeNS =
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGSPE");
		justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_JustifAbscenceSpe");
		frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_DONNE_CONG_COMBINE");
		// frag_nbrjrpredefNS =
		// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
		msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MsgErrPerConge");
		signatureNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_SignatureRH");
		pjEtatConge = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_PjEtatCongeRH");
		matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
		matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
		
		droitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DroitAnnuelConge");
		reliquatDroitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsDispoConge");
		soldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
		reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoCongeApresModif");
		soldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_SoldeAnneeEnCours");
		reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ReliquatSoldeAnneeEnCours");
		
		signatureNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_SignatureRH");
		getWorkflowInstance().setValue(signatureNS, null);
		float resteConge = (float) getWorkflowInstance().getValue(resteJoursCongeNS);
		
		if (resteConge < 0)
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
		return super.onAfterLoad();
	}
	
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<Connection> ConnectionDefinition(IWorkflowModule im) throws PortalModuleException
	{
		this.ctx = im.getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) Modules.getPortalModule().getConnectionDefinition(ctx, "RH_ATTIJARI");
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
			signatureNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_SignatureRH");
			List<IAttachment> doc = (List<IAttachment>) iw.getValue(signatureNS);
			File newFile = null;
			String filename = "";
			if (doc != null)
			{
				if (!doc.isEmpty())
				{
					// Préparation du paramètre
					cumulSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
					remplacantNS = iw.getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
					demandeur = iw.getCatalog().getConfiguration().getStringUserProperty("PERSONNEL");
					fragAlerteNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_ALERTRH");
					resteJoursCongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
					commentaireNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_CommentaireConRH");
					frag_remplacantNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_REMPLACANT");
					fragmotifSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_MOTIFSPE");
					fragmotifNorNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_MOTIFNOR");
					motifSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MotifsSpe");
					motifNorNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_Motifs");
					dateCongeDebNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_DateReprise");
					dateCongeFinNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_DateEntree");
					dateCongeDebSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_DateRepriseSpe");
					dateCongeFinSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_DateEntreeSpe");
					alerteNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_Alerte");
					droitAnnCongNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_DroitAnnuelConge");
					nbrJrSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsSoldesConge");
					nbrJrNonSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsNonSoldesConge");
					// nbrJrTotalSoldeNS =
					// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");
					// nbrJrResteSoldeNS =
					// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
					momentsortieSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortieSpe");
					momententreeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntreeSpe");
					matinApmidisortieSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortieSpe");
					matinApmidientreeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntreeSpe");
					nbrjrouvrSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsOuvrablesSpe");
					msgErreurPeriodeCongeSpeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MsgErrPerSpe");
					supHieNS = iw.getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
					// remplacantNS = iw.getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
					btnenvoyerNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_Envoyer");
					naturecongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_CongeNormalCombine");
					typecongeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_TypeConge");
					nbjrpredefNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsPredefinis");
					momentsortieNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortie");
					momententreeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntree");
					// periodecongenorNS = iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONG");
					nbrjrouvrNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsOuvrables");
					nbrjrcongdispoNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsDispoConge");
					justifabsNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_JustifAbscence");
					typecongespeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_TypeCongeSpecial");
					nbjrpredefspeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsPredefinisSpe");
					// periodecongespeNS =
					// iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGSPE");
					justifabsspeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_JustifAbscenceSpe");
					frag_conge_combineNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_DONNE_CONG_COMBINE");
					pjEtatConge = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_PjEtatCongeRH");
					// frag_nbrjrpredefNS =
					// iw.getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
					msgErreurPeriodeCongeNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("GCC_MsgErrPerConge");
					cumulSoldeNS = iw.getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
					
					
					droitAnnuelleNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("GCC_DroitAnnuelConge");
					reliquatDroitAnnuelleNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsDispoConge");
					soldeAnterieurNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
					reliquatSoldeAnterieurNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoCongeApresModif");
					soldeanneEncoursNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("GCC_SoldeAnneeEnCours");
					reliquatSoldeanneEncoursNS = (String) iw.getCatalog().getConfiguration().getStringUserProperty("GCC_ReliquatSoldeAnneeEnCours");
					// maladieComptabiliseNS = (String)
					// iw.getCatalog().getConfiguration().getStringUserProperty("VALRH_MALADIECOMPTA");
					// AvecJustifNorNS = (String)
					// iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_AVECJUSTIFNOR");
					// AvecJustifSpeNS = (String)
					// iw.getCatalog().getConfiguration().getStringUserProperty("DEMCON_AVECJUSTIFSPE");
					
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
					ArrayList<IUser> collaborateurs = (ArrayList<IUser>) iw.getValue(demandeur);
					IUser demUser = collaborateurs.get(0);
					String demandeur = demUser.getFullName();
					String matriculeDem = demUser.getEmployeeNumber();
					ArrayList<IUser> supUser = (ArrayList<IUser>) iw.getValue(supHieNS);
					
					String superieur = "";
					int i=0;
					for(IUser sup : supUser){
						superieur += sup.getFullName();
						
						if(i<supUser.size()-1)
							superieur += " / ";
						else if(i==supUser.size()-1)
							superieur +=" ";
						
						i++;
					}
					
					ArrayList<IUser> remUser = (ArrayList<IUser>) iw.getValue(remplacantNS);
					String remplacant = " --------- ";
					if (remUser != null)
					{
						if (remUser.size() > 0)
							remplacant = remUser.get(0).getFullName();
					}
					String natureConge = (String) iw.getValue(naturecongeNS);
					//String lieuConge = (String) iw.getValue(lieuNS);
					String typeConge = (String) iw.getValue(typecongeNS);
					String typeCongeSpe = (String) iw.getValue(typecongespeNS);
					float nbrJrOuvrConge = (float) iw.getValue(nbrJrSoldeNS)/* +(float) iw.getValue(nbrJrNonSoldeNS) */;
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
					// String lieuConge = (String) iw.getValue(lieuNS);
					
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
					//Parametr.put("lieuConge", lieuConge);
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
					
					float reliquatSoldeAnterieur = 0;
					Object nombre = iw.getValue(reliquatSoldeAnterieurNS);;
					if(nombre.getClass().getCanonicalName().equals("java.lang.Double")){
						double reliquatSoldeAnterieurD= (double) iw.getValue(reliquatSoldeAnterieurNS);
						reliquatSoldeAnterieur= Float.parseFloat(reliquatSoldeAnterieurD+"");
						
					}
					else{
						reliquatSoldeAnterieur= (float) iw.getValue(reliquatSoldeAnterieurNS);
						
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
					
					
					
					
					String momentEntree = "";
					// if (natureConge.equals("Non combiné"))
					// {
					Parametr.put("typeConge", typeConge);
					Parametr.put("periodeCongeDeb", srv.afficheDate(startDate.getTime()));
					Parametr.put("periodeCongeFin", srv.afficheDate(endDate.getTime()));
					momentEntree = (String) iw.getValue(momententreeNS);
					// }
					// else
					// {
					// Calendar startDateSpe = Calendar.getInstance();
					// Calendar endDateSpe = Calendar.getInstance();
					// Date dateDebSpe = (Date) iw.getValue(dateCongeDebSpeNS);
					// Date dateFinSpe = (Date) iw.getValue(dateCongeFinSpeNS);
					// endDateSpe.setTime(dateFinSpe);
					// startDateSpe.setTime(dateDebSpe);
					//
					// Parametr.put("typeConge", typeConge + " / " + typeCongeSpe);
					// Parametr.put("periodeCongeDeb",(startDate.get(startDate.DATE)) + "-" +
					// (startDate.get(startDate.MONTH) + 1) + "-" + startDate.get(startDate.YEAR));
					// Parametr.put("periodeCongeFin", (endDateSpe.get(endDateSpe.DATE)) + "-" +
					// (endDateSpe.get(endDateSpe.MONTH) + 1) + "-"+ endDateSpe.get(endDateSpe.YEAR));
					//
					// momentEntree = (String) iw.getValue(momententreeSpeNS);
					// }
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
					
					
					
					
					if(testNumberIsFloatOrInt(nbrNnJrOuvrConge)){
						Float xx = nbrNnJrOuvrConge;
						int x = xx.intValue();
						Parametr.put("congeSP", x+"");
					}
					else{
						Parametr.put("congeSP", nbrNnJrOuvrConge);
					}
					
					
					float congePris = new ServiceRH().calculateCongePris(loginVdocOfUser);
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
					new GenererPDF().generer("ETATCONGE_CONSTATES", path.getOutDir(), nomFichier, Parametr, connection);
					EncryptionFile.crypter(TurbineServlet.getRealPath("RH") + "\\" + filename, TurbineServlet.getRealPath("RH") + "\\" + filename);
					EncryptionFile.deleteFile(TurbineServlet.getRealPath("RH") + "\\" + filename);
					// Création d'un objet pièce jointe à partir d'un fichier sur le serveur
					iw.setValue(pjEtatConge, null);
					IAttachment idisk = im.addAttachment(iw, pjEtatConge, nomFichierPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF);
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
		return super.onBeforeSubmit(action);
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
		super.onPropertyChanged(property);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try
		{
			// String nbrJrSoldeNS="";
			// nbrJrSoldeNS =
			// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRSOLDCONG");
			if (action.getName().equals(btnEnvoyerNS))
			{
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
				
				Date dateDebNor = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date dateFinNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				
				Date dateDemandeConge = getWorkflowInstance().getCreatedDate();
				
				float nbrJrOuvrableCongeNor = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
				
				String etatCongeNor = "valide";
				String momentSortieNor = (String) getWorkflowInstance().getValue(momentsortieNS);
				String momentEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
				String lieu = (String) getWorkflowInstance().getValue(lieuNS);
				List<IUser> users = (List<IUser>) getWorkflowInstance().getValue(demandeur);
				IUser personnel = users.get(0);
				String matriculePersonnel = personnel.getLogin();
				List<IUser> superieur = new ServiceRH().getSuperieurOf(personnel.getLogin());
				String matriculeSup = superieur.get(0).getLogin();
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
				
				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				
				String req = "insert into Conge(IdConge, supMatricule, remplacantMatricule, Personnelmatricule, DateDeb, DateFin, DateDemandeConge, NbrJoursOuvrables, TypeConge, EtatConge, MomSortie, MomEntre,CodeVdocDemandeConge,Lieu) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				st = connection.prepareStatement(req);
				st.setString(1, codeDemCongVdoc + "NOR");
				st.setString(2, matriculeSup);
				st.setString(3, matriculeRemp);
				st.setString(4, matriculePersonnel);
				java.sql.Date dateDeb = new java.sql.Date(dateDebNor.getTime());
				st.setDate(5, dateDeb);
				java.sql.Date dateFin = new java.sql.Date(dateFinNor.getTime());
				st.setDate(6, dateFin);
				java.sql.Date dateDemande = new java.sql.Date(dateFinNor.getTime());
				st.setDate(7, dateDemande);
				st.setFloat(8, nbrJrOuvrableCongeNor);
				st.setString(9, typecongeNor);
				st.setString(10, etatCongeNor);
				st.setString(11, momentSortieNor);
				st.setString(12, momentEntreeNor);
				st.setString(13, codeDemCongVdoc);
				st.setString(14, lieu);
				st.executeUpdate();
				
				if ("Combiné".equals(natureConge))
				{
					Date dateDebSpe = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
					Date dateFinSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
					
					float nbrJrOuvrableCongeSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
					
					String etatCongeSpe = "valide";
					
					String momentSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
					String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
					
					req = " insert into Conge(IdConge, supMatricule, remplacantMatricule, Personnelmatricule, DateDeb, DateFin, DateDemandeConge, NbrJoursOuvrables, TypeConge, EtatConge, MomSortie, MomEntre,CodeVdocDemandeConge,Lieu) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
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
					st.executeUpdate();
				}
				
				String loginVdoc = personnel.getLogin();
//				float nvSoldeAnterieur = 0;
//				Object nombre = getWorkflowInstance().getValue(reliquatSoldeAnterieurNS);;
//				if(nombre.getClass().getCanonicalName().equals("java.lang.Double")){
//					double reliquatSoldeAnterieurD= (double) getWorkflowInstance().getValue(reliquatSoldeAnterieurNS);
//					nvSoldeAnterieur= Float.parseFloat(reliquatSoldeAnterieurD+"");
//					
//				}
//				else{
//					nvSoldeAnterieur= (float) getWorkflowInstance().getValue(reliquatSoldeAnterieurNS);
//					
//				}
				//float nvSoldeEnCours = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
				
				float nbrNvJoursPris = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
				new ServiceRH().updateSoldeCongeMoins(loginVdoc, nbrNvJoursPris );
			}
			
			if (action.getName().equals(btnRefuserNS))
			{
				// connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				// String codeVDOC = (String) getWorkflowInstance().getValue("sys_Reference");
				// String req = "update Conge set EtatConge = ?,rhMatricule=? where CodeVdocDemandeConge = ?";
				// st = connection.prepareStatement(req);
				// st.setString(1, "en etat refus rh");
				// st.setString(2, matrRH);
				// st.setString(3, codeVDOC);
				// st.executeUpdate();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterSubmit(action);
	}
}
