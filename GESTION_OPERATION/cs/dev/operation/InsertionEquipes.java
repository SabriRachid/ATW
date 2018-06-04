package cs.dev.operation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;
public class InsertionEquipes extends ConnexionBDD
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6086267844912121083L;
	protected static final Logger log = Logger.getLogger(InsertionEquipes.class);
	// -----------------------------------------------------------------------------------------------------------
	// 
	// -----------------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	public boolean onAfterSubmit(IAction action) {
		try {
			if(action.getName().equals("GO_Affecter"))
			{
				cnx=getConnectionVDoc("Ref_Attijari").getConnection();
				String Customer =(String) getWorkflowInstance().getValue("GO_CodeClients");
				String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); // RÉFÉRENCE VDOC
				String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
				String RefFormater = NomProjet+"-"+RefDossier;
				String Query1 = "insert into Dossiers(RefDossier,Client) values(?,?)";
				st = cnx.prepareStatement(Query1);
				st.setString(1, RefFormater);
				st.setString(2,Customer);
				st.executeUpdate();
				Set<IUser> users = new HashSet<>() ;
				List<IUser> role1 = (List<IUser>) getWorkflowInstance().getValue("GO_Manager");
				for(IUser ligne : role1){
					users.add(ligne);
				}
				List<IUser> role2 = (List<IUser>) getWorkflowInstance().getValue("GO_SeniorAssociate");
				for(IUser ligne : role2){
					users.add(ligne);
				}
				List<IUser> role3 = (List<IUser>) getWorkflowInstance().getValue("GO_Associate");
				for(IUser ligne : role3){
					users.add(ligne);
				}
				for(IUser ligne : users){
					String Query2 = "insert into Collaborateur(login,Fullname,RefDossier) values(?,?,?)";
					st = cnx.prepareStatement(Query2);
					st.setString(1, ligne.getLogin()); // username
					st.setString(2, ligne.getFullName());
					st.setString(3, RefFormater);
					st.executeUpdate();
				}
			}
		} catch (Exception e) {
			log.info("CS Erreur : Problème d'insertion equipes: " + e.getClass() + " - " + e.getMessage() +" - "+ e.getLocalizedMessage());
		}
		return super.onAfterSubmit(action);
	}
	/*============================================================================================================
	 * @see com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension#onPropertyChanged(com.axemble.vdoc.sdk.interfaces.IProperty)
	 *============================================================================================================*/
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public void onPropertyChanged(IProperty property) {
		Set<IUser> users = new HashSet<>() ;
		if (property.getName().equals("GO_Manager")) {
			List<IUser> role1 = (List<IUser>) getWorkflowInstance().getValue("GO_Manager");
			getWorkflowInstance().setValue("GO_Liste_Manager",role1);
		}
		//---------------------------------------------------------------------------------
		if (property.getName().equals("GO_SeniorAssociate")) {
			List<IUser> role2 = (List<IUser>) getWorkflowInstance().getValue("GO_SeniorAssociate");
			getWorkflowInstance().setValue("GO_Liste_SiniorAssociate",role2);
		}
		//----------------------------------------------------------------------------------
		if (property.getName().equals("GO_Associate")) {
			List<IUser> role3 = (List<IUser>) getWorkflowInstance().getValue("GO_Associate");
			getWorkflowInstance().setValue("GO_Liste_Associate",role3);
		}
		super.onPropertyChanged(property);
	}
	/*=============================================================================================================
	 * @see com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension#isOnChangeSubscriptionOn(com.axemble.vdoc.sdk.interfaces.IProperty)
	 *=============================================================================================================*/
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property) {
		if (property.getName().equals("GO_Manager")) {
			return true;
		}
		if (property.getName().equals("GO_SeniorAssociate")) {
			return true;
		}
		if (property.getName().equals("GO_Associate")) {
			return true;
		}
		return false;
	}
}
