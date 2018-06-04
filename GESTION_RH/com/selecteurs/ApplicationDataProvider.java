package com.selecteurs;


import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;


    public class ApplicationDataProvider extends BaseDocumentExtension {
    	
		private static final long serialVersionUID = 1L;
		
		public static IWorkflowInstance workflowInstance;
		public static IWorkflowModule workflowModule;
		public static IContext context;
		public static IResourceController resourceController;
		
		
		@Override
		public boolean onAfterLoad() {
			
			workflowInstance = getWorkflowInstance();
			workflowModule = getWorkflowModule();
			resourceController = getResourceController();
			try {
				context = workflowModule.getContext(workflowModule.getUserByLogin("sysadmin"));
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			return super.onAfterLoad();
		}


	
}
