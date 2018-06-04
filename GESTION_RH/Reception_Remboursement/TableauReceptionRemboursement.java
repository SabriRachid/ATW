package Reception_Remboursement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.attijari.DemandeRemboursement.ConnexionBDD;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IResource;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;

import dao.SingletonConnexionBDD;
import Impression_Dossier.FileManager;
import Impression_Dossier.GenererPDF;

public class TableauReceptionRemboursement extends ConnexionBDD {
	private static final DecimalFormat DFORMAT = new DecimalFormat("###0.00");
	private static final NumberFormat FORMATTER = new RuleBasedNumberFormat(RuleBasedNumberFormat.SPELLOUT);
	private Connection connection;

	@Override
	public boolean onAfterLoad() {
		// TODO Auto-generated method stub
		//int x = 1;
		String statut = (String) getWorkflowInstance().getValue("DMR_Statut");
		if(statut.equals("En cours")){
			getWorkflowInstance().setValue("DMR_DateRemboursement",null);
			getWorkflowInstance().setValue("RRM_MontantRemboursser",null);
			getWorkflowInstance().setValue("DRM_DR_motif",null);
		}
		if(statut.equals("Rejeté")){
			getWorkflowInstance().setValue("DMR_DateRemboursement",null);
			getWorkflowInstance().setValue("RRM_MontantRemboursser",null);
		}
		
		
		return super.onAfterLoad();
	}
	
	@Override
	public boolean onBeforeSave() {
		// TODO Auto-generated method stub
		try{
			
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String numDossier = (String) getWorkflowInstance().getValue("RRM_NumDossier");
			String statut = (String) getWorkflowInstance().getValue("DMR_Statut");
			
			if(statut.equals("En cours")){
				getWorkflowInstance().setValue("DMR_DateRemboursement",null);
				getWorkflowInstance().setValue("RRM_MontantRemboursser",null);
				getWorkflowInstance().setValue("DRM_DR_motif",null);
			}
			if(statut.equals("Rejeté")){
				getWorkflowInstance().setValue("DMR_DateRemboursement",null);
				getWorkflowInstance().setValue("RRM_MontantRemboursser",null);
			}
			if(statut.equals("Remboursé")){
				getWorkflowInstance().setValue("DRM_DR_motif",null);
			}
			
			
			
			
			
			float montant = 0;
			Date dateRemb = null;	
			java.sql.Date datesql = null;
			String var = null;
			
			if(statut.equals("Remboursé")){
				 montant = (float) getWorkflowInstance().getValue("RRM_MontantRemboursser");
				dateRemb = (Date) getWorkflowInstance().getValue("DMR_DateRemboursement");	
				datesql = new java.sql.Date(dateRemb.getTime());
				float MTNGlobal = getWorkflowInstance().getValue("RRM_MontantRemboursser")!=null ? (float) getWorkflowInstance().getValue("RRM_MontantRemboursser") : 0;
				 var = toLetter(new BigDecimal(MTNGlobal));
			}
			
			String motifRejet = (String) getWorkflowInstance().getValue("DRM_DR_motif");
			
			String req = "update DossierMutuelle set  statutRapprotValidation = ?,Montant = ?,dateRemboursement= ?, MntLettre= ? , motifRejet=? where NumDossier = ?";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, statut);
			st.setFloat(2, montant);
			st.setDate(3, datesql);
			st.setString(4, var);
			st.setString(5, motifRejet);
			st.setString(6, numDossier);
			st.executeUpdate();
			connection.close();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return super.onBeforeSave();
	}
	
	
	@Override
	public boolean onAfterSubmit(IAction action) {
		// TODO Auto-generated method stub
		//int x=1;
		return super.onAfterSubmit(action);
	}
	
	private static String toLetter(BigDecimal num) {
		String[] s = DFORMAT.format(num).split(
				Pattern.quote(String.valueOf(DFORMAT.getDecimalFormatSymbols()
						.getDecimalSeparator())));
		BigInteger intPart = new BigInteger(s[0]);
		if (s.length == 1) {
			return FORMATTER.format(intPart);
		} else {
			BigInteger decPart = new BigInteger(s[1]);
			return FORMATTER.format(intPart)
					// pour les parties fixes il faudrait faire un
					// resourcebundle
					+ " DIRHAM" + (intPart.intValue() > 1 ? "S" : "") + " et "
					+ FORMATTER.format(decPart) + " CENTIME"
					+ (decPart.intValue() > 1 ? "S" : "");
		}
	}

}
