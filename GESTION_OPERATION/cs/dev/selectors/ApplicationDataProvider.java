/**
 * 
 */
package cs.dev.selectors;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;

/**
 * @author SABRI Rachid
 * Modification 09/01/2017
 */
public class ApplicationDataProvider extends BaseDocumentExtension {
	private static final long serialVersionUID = 1L;
	public static IWorkflowInstance workflowInstance;
	public static IWorkflowModule workflowModule;
	public static IContext context;
	public static String RefDossier;
	public static String NomProjet;
	public static String RefFormater;
	public static String Dossier;
	public static String LogInUser;
	@Override
	public boolean onAfterLoad() {
		// WE GET THE WORKFLOWINSTANCE
		workflowInstance = getWorkflowInstance();
		// WE GET THE WORKFLOWMODULE
		workflowModule = getWorkflowModule();
		// RETURN THE REFERENCE SYSTEM OF PARENT INSTANCE
		 RefDossier = (String) getWorkflowInstance().getParentInstance().getValue("sys_Reference"); // RÉFÉRENCE VDOC
		 NomProjet = (String) getWorkflowInstance().getParentInstance().getValue("GO_NomProjet");
		 RefFormater = NomProjet+"-"+RefDossier;
		 
		 IUser Participant = getWorkflowModule().getLoggedOnUser();
		 LogInUser = Participant.getLogin();
		 
		getWorkflowInstance().setValue("GL_Dossier", RefFormater);
		// RETURN THE HIDDEN REFERENCE "DOSSIER" => PROCESS LIVRABLE
		Dossier = (String)workflowInstance.getParentInstance().getValue("GL_Dossier");
		try {
			// WE GET THE CONTEXT
			context = workflowModule.getContext(workflowModule.getUserByLogin("sysadmin"));
		} catch (WorkflowModuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
}
