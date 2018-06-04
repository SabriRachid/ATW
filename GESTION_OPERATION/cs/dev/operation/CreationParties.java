package cs.dev.operation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import dao.ConnexionBDD;
/**
 * Created on 20 juin 2017
 * @author R.SABRI 
 * Version  vdoc14
 * 
 */
public class CreationParties extends ConnexionBDD{
    private static final long serialVersionUID = 1L;
    /*
     * ===============================================================================================================================
     * @see com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension#onPropertyChanged(com.axemble.vdoc.sdk.interfaces.IProperty)
     * ===============================================================================================================================
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onPropertyChanged(IProperty property) {
        if(property.getName().equals("CP_ChefEquipe_Selector")) {
            try {
                if (getWorkflowInstance().getValue("CP_ChefEquipe_Selector")!= null)
                {
                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String UserFullName = (String) getWorkflowInstance().getValue("CP_ChefEquipe_Selector");
                    String UserID = null;
                    // RÉCUPÉRER ID D'UTILISATEUR SÉLÉCTINNÉ
                    String query = "SELECT [Login] as 'UserID' FROM Collaborateur WHERE [Fullname] like ?";
                    st = cnx.prepareStatement(query);
                    st.setString(1,UserFullName);
                    rs = st.executeQuery();
                    while(rs.next())
                    {
                        UserID = rs.getString("UserID");
                    }
                    // IDENTIFIANT UTILISATEUR
                    IUser CEQ = getWorkflowModule().getUserByLogin(UserID);  
                    List<IUser> utlisateur = new ArrayList<>();
                    utlisateur.add(CEQ);
                    getWorkflowInstance().setValue("GL_ChefEquipe", utlisateur);
                    getWorkflowInstance().save("GL_ChefEquipe");
                }else {
                    getWorkflowInstance().setValue("GL_ChefEquipe", null);
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }finally {
                // Fermer statement & Connection
                if(st!=null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if(cnx!=null) {
                    try {
                        cnx.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        super.onPropertyChanged(property);
    }
    /*
     * ======================================================================================================================================
     * @see com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension#isOnChangeSubscriptionOn(com.axemble.vdoc.sdk.interfaces.IProperty)
     * ======================================================================================================================================
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if(property.getName().equals("CP_ChefEquipe_Selector")) {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }
}
