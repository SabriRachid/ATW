package NoteDeFrais;
import java.io.File;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdp.ui.framework.foundation.Navigator;

/**
 * Created on 23 mars 2016
 * @author R.SABRI 
 * Version  vdoc14
 * 
 */
public  class GenererPDF extends BaseDocumentExtension {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static  GenererPDF instance;
    // IReport attributes
    private static  JasperDesign jasperDesign;
    private static JasperReport jasperReport;
    private static  JasperPrint jasperPrint;
    private static IWorkflowInstance instancew ;

    public static  GenererPDF getInstance() {
        if (instance == null) {

            instance = new GenererPDF();
        }
        return instance;
    }

    @Override
    public boolean onAfterLoad()
    {
        // TODO Auto-generated method stub
        instancew = getWorkflowInstance();
        return super.onAfterLoad();
    }

    @SuppressWarnings("deprecation")
    public static String generer(String jrxml, File path,String FileExportName,Map<String, Object> map,Connection connection,IWorkflowInstance instancew)
            throws Exception {
        jasperDesign = JRXmlLoader.load(path + "\\" + jrxml + ".jrxml");
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        jasperPrint = JasperFillManager.fillReport(jasperReport,map,connection);                           
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\OUT\\"+ FileExportName + ".pdf");
        String ip =null;
        ip = instancew.getCatalog().getConfiguration().getStringUserProperty("IPADRESSE");
        //Navigator.getNavigator().showPopup("http://"+ip+":8080/vdoc/RH/OUT/"+ FileExportName + ".pdf");
        return FileExportName + ".htm";

    }
}
