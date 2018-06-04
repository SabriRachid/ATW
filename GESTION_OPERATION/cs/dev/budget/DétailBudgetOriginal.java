package cs.dev.budget;

import java.sql.ResultSet;
import java.util.Collection;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;
/**
 * @author r.sabri
 * @Creation_Date 22/02/2017 16:29 PM
 * @Plateform VDOC15 
 */
public class DétailBudgetOriginal extends ConnexionBDD{

	private static final long serialVersionUID = 1L;
	protected static final Logger log =  Logger.getLogger(DétailBudgetOriginal.class);
	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 *Import via table budget 
	 *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	public void RemplireTableBudgetsOriginal_DB() {
		try {
			Collection table = (Collection) getWorkflowInstance().getLinkedResources("PB_Tab_OriginauxDetailBudget");
			if(table.size()==0)
			{
				FillGrid();
			}else {
				// Initialiser le tableau
				getWorkflowInstance().setValue("PB_Tab_OriginauxDetailBudget", null);
				FillGrid();
			}
		} catch (Exception e) {
		    e.printStackTrace();
			log.info("Error in RemplireTableBudgetsOriginal_DB() method : " + e.getClass() + " - " + e.getMessage() +" - "+ e.getLocalizedMessage());
		}
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 *Methode de chargement tableux détail budget
	 *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	private void FillGrid()
	{
	    cnx = null;
	    st = null;
	    rs = null;
		try {
			cnx=getConnectionVDoc("Ref_Attijari").getConnection();
			String cle =(String) getWorkflowInstance().getValue("PB_Dossier");
			String query = "select AnneeBudget,PS_FIX,DS_FIX,PS_VAR,DS_VAR from Details_budget_budget where Dossier =?";
			st = cnx.prepareStatement(query);
			st.setString(1, cle);
			ResultSet result = st.executeQuery();
			while (result.next()) {
				// création d'une ligne 
				ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "PB_Tab_OriginauxDetailBudget" );
				//positionnement de quelques valeurs 
				linkedResource.setValue("PB_Tab_Annee", result.getString("AnneeBudget"));
				linkedResource.setValue("PB_Tab_1erSemestreFixe",result.getFloat("PS_FIX"));  
				linkedResource.setValue("PB_Tab_2emeSemestreFixe",result.getFloat("DS_FIX"));
				linkedResource.setValue("PB_Tab_1erSemestreVariable",result.getFloat("PS_VAR")); 
				linkedResource.setValue("PB_Tab_2emeSemestreVariable",result.getFloat("DS_VAR"));
				// Total fix
				linkedResource.setValue("PB_Tab_TotalFixe",((result.getFloat("PS_FIX"))+(result.getFloat("DS_FIX"))));
				// Total varibale
				linkedResource.setValue("PB_Tab_TotalVariable",((result.getFloat("PS_VAR"))+(result.getFloat("DS_VAR"))));
				//  PB_Tab_TotalFixe  +  PB_Tab_TotalVariable 
				linkedResource.setValue("PB_Tab_TotalAnnee",((result.getFloat("PS_FIX"))+(result.getFloat("DS_FIX")))+(result.getFloat("PS_VAR"))+(result.getFloat("DS_VAR")));
				// ajout de la ligne au tableau
				getWorkflowInstance().addLinkedResource( linkedResource );
			}

		}catch(Exception e) {
		    e.printStackTrace();
			log.info("Error in FillGrid() method : " + e.getClass() + " - " + e.getMessage() +" - "+ e.getLocalizedMessage());
		}finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
	}
}
