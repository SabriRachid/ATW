package cs.dev.operation;

import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.utils.Logger;

/**
 * @author r.sabri
 * @Creation_Date 15/01/2018 10:26 AM
 * @Plateform VDOC 14 2.1 
 */
public class ValidationSecretaireGeneral extends BaseDocumentExtension{

    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ValidationSecretaireGeneral.class);
    
    @Override
    public boolean onAfterLoad() {
        // Afficher/Cacher le fragment "Mandat(PJ)"
        List<IAttachment> MandatPJ = ((List<IAttachment>) getWorkflowInstance().getValue("GO_MandatPJ"));
        //Object MandatPJ = (Object) getWorkflowInstance().getValue("GO_MandatPJ");
        if (MandatPJ.size()==0)
        {
            getResourceController().showBodyBlock("marker_MandatPJ", false);
        }else {
            getResourceController().showBodyBlock("marker_MandatPJ", true);
        }
        return super.onAfterLoad();
    }
    
}
