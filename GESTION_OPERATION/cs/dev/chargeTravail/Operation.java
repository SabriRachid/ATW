package cs.dev.chargeTravail;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import beans.Collaborateur;
import beans.Event;
import beans.Semaine;

import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
//import com.google.gson.stream.JsonWriter;

import dao.ConnexionBDD;
/**
 * ==========================================================================================================
 * @author ADMIX
 * @Documentation by r.sabri le 06/07/2017 2:12 PM
 * ==========================================================================================================
 */
public class Operation extends ConnexionBDD {

    /**
     * ==========================================================================================================
     * @Created by k.kouiss
     * ==========================================================================================================
     */
    private static final long serialVersionUID = 1L;
    public static String noteTable1[][]= new String[25][7]; 
    public static String noteTable2[][]= new String[25][7];
    public static String noteTable3[][]= new String[25][7];
    int nbr_Semaine = 0;
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMIT DE RETOURNER LA LISTE DES DOSSIER DANS LA TABLE D'EQUIPE
     * ==========================================================================================================
     */
    public ArrayList<IOption> getDossier()
    {
        cnx=null;
        st=null;
        rs=null;
        ArrayList<IOption> lst = new ArrayList<IOption>();
        module=getWorkflowModule();
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            instance=getWorkflowInstance();
            String createur = getWorkflowInstance().getCreatedBy().getFullName();
            String req="SELECT distinct col.RefDossier from Collaborateur col inner join Dossiers d"+
                    " on col.refdossier = d.refdossier where d.Manager = '"+createur+"'";
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                lst.add(getWorkflowModule().createListOption(rs.getString(1),rs.getString(1))); // Modifie
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS Erreur in getDossier methode : "+e.getClass()+" _ "+e.getMessage()+" _ "+e.getStackTrace()[0].getLineNumber());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return lst;
    }
    /**
     * ==========================================================================================================
     * FULLFIRSTTABLE
     * ==========================================================================================================
     */
    @SuppressWarnings("rawtypes")
    public void fullFirstTable(String refDossier)
    {
        try {
            String nbr = "desc";
            String order = "asc";
            instance = getWorkflowInstance();
            instance.setValue("CT_NotesSemaineActuelle", null);

            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuelle");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()==0)
            {
                if(!areNotesSave(refDossier)){
                    for (Collaborateur coll : getCollaborateur(refDossier)) {
                        // création d'une ligne 
                        ILinkedResource linkedResource = instance.createLinkedResource("CT_NotesSemaineActuelle");
                        //positionnement de quelques valeurs 

                        linkedResource.setValue("CT_Tab_Collaborateur",coll.getNomPrenom() );
                        linkedResource.setValue("CT_Tab_NCumuleLundiAct",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                        linkedResource.setValue("CT_Tab_NCumuleMardiAct",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                        linkedResource.setValue("CT_Tab_NCumuleMercrediAct",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                        linkedResource.setValue("CT_Tab_NCumuleJeudiAct",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                        linkedResource.setValue("CT_Tab_NCumuleVendrediAct",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                        linkedResource.setValue("CT_Tab_NCumuleSamediAct",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                        linkedResource.setValue("CT_Tab_SommeLundiAct",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                        linkedResource.setValue("CT_Tab_SommeMardiAct",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                        linkedResource.setValue("CT_Tab_SommeMercrediAct",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                        linkedResource.setValue("CT_Tab_SommeJeudiAct",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                        linkedResource.setValue("CT_Tab_SommeVendrediAct",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                        linkedResource.setValue("CT_Tab_SommeSamediAct",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                        // ajout de la ligne au tableau
                        instance.addLinkedResource( linkedResource );
                    }
                }else{
                    int cpt=0;
                    for (Collaborateur coll : getCollaborateur(refDossier)) {

                        for (Semaine sem : getNotes(coll.getNum(),nbr,order)) {
                            // création d'une ligne 
                            ILinkedResource linkedResource = instance.createLinkedResource("CT_NotesSemaineActuelle");
                            //positionnement de quelques valeurs  
                            linkedResource.setValue("CT_Tab_Collaborateur",coll.getNomPrenom() );

                            linkedResource.setValue("CT_Tab_NCumuleLundiAct",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                            linkedResource.setValue("CT_Tab_NCumuleMardiAct",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                            linkedResource.setValue("CT_Tab_NCumuleMercrediAct",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                            linkedResource.setValue("CT_Tab_NCumuleJeudiAct",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                            linkedResource.setValue("CT_Tab_NCumuleVendrediAct",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                            linkedResource.setValue("CT_Tab_NCumuleSamediAct",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                            linkedResource.setValue("CT_Tab_SommeLundiAct",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                            linkedResource.setValue("CT_Tab_SommeMardiAct",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                            linkedResource.setValue("CT_Tab_SommeMercrediAct",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                            linkedResource.setValue("CT_Tab_SommeJeudiAct",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                            linkedResource.setValue("CT_Tab_SommeVendrediAct",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                            linkedResource.setValue("CT_Tab_SommeSamediAct",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                            linkedResource.setValue("CT_Tab_NoteLundiSemAct", sem.getLundi() );
                            linkedResource.setValue("CT_Tab_NoteMardiSemAct",sem.getMardi());  
                            linkedResource.setValue("CT_Tab_NoteMercrediSemAct", sem.getMercredi() );
                            linkedResource.setValue("CT_Tab_NoteJeudiSemAct",sem.getJeudi() );
                            linkedResource.setValue("CT_Tab_NoteVendrediSemAct",sem.getVendredi());
                            if(!sem.getSamedi().equals("null")){
                                linkedResource.setValue("CT_Tab_NoteSamediSemAct",sem.getSamedi());
                                noteTable1[cpt][6] = sem.getSamedi();
                            }else{
                                noteTable1[cpt][6] = "N";
                            }

                            noteTable1[cpt][0] = coll.getNomPrenom();
                            noteTable1[cpt][1] = sem.getLundi();
                            noteTable1[cpt][2] = sem.getMardi();
                            noteTable1[cpt][3] = sem.getMercredi();
                            noteTable1[cpt][4] = sem.getJeudi();
                            noteTable1[cpt][5] = sem.getVendredi();

                            // ajout de la ligne au tableau
                            instance.addLinkedResource( linkedResource );
                            cpt++;
                        }
                        getWorkflowInstance().setValue("CT_Semaine_1", nbr_Semaine);
                    }
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            log.info("Error in fullTable() method : " + e.getClass() + " - " + e.getMessage());
        }
    }
    /**
     * ==========================================================================================================
     * FULLSECONDTABLE
     * ==========================================================================================================
     */
    public void fullSecondTable(String refDossier)
    {
        try {
            String nbr = "asc";
            String order = "desc";
            instance = getWorkflowInstance();
            instance.setValue("CT_NotesSemaineActuellePlus1", null);
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus1");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()==0)
            {
                if(!areNotesSave(refDossier)){
                    for (Collaborateur coll : getCollaborateur(refDossier)) {
                        // création d'une ligne 
                        ILinkedResource linkedResource = instance.createLinkedResource("CT_NotesSemaineActuellePlus1");
                        //positionnement de quelques valeurs 
                        linkedResource.setValue("CT_Tab_CollaborateurSemActP1",coll.getNomPrenom() );

                        linkedResource.setValue("CT_Tab_NCumuleLundiAct1",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                        linkedResource.setValue("CT_Tab_NCumuleMardiAct1",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                        linkedResource.setValue("CT_Tab_NCumuleMercrediAct1",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                        linkedResource.setValue("CT_Tab_NCumuleJeudiAct1",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                        linkedResource.setValue("CT_Tab_NCumuleVendrediAct1",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                        linkedResource.setValue("CT_Tab_NCumuleSamediAct1",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                        //							 linkedResource.setValue("CT_Tab_SommeLundiAct1",getCumulative(coll.getNomPrenom())[0] );
                        //							 linkedResource.setValue("CT_Tab_SommeMardiAct1",getCumulative(coll.getNomPrenom())[1] );
                        //							 linkedResource.setValue("CT_Tab_SommeMercrediAct1",getCumulative(coll.getNomPrenom())[2] );
                        //							 linkedResource.setValue("CT_Tab_SommeJeudiAct1",getCumulative(coll.getNomPrenom())[3] );
                        //							 linkedResource.setValue("CT_Tab_SommeVendrediAct1",getCumulative(coll.getNomPrenom())[4] );
                        //							 linkedResource.setValue("CT_Tab_SommeSamediAct1",getCumulative(coll.getNomPrenom())[5] );

                        // ajout de la ligne au tableau
                        instance.addLinkedResource( linkedResource );
                    }

                }else{
                    int cpt=0;
                    for (Collaborateur coll : getCollaborateur(refDossier)) {
                        for (Semaine sem : getNotes(coll.getNum(),nbr,order)) {
                            // création d'une ligne 
                            ILinkedResource linkedResource = instance.createLinkedResource("CT_NotesSemaineActuellePlus1");
                            //positionnement de quelques valeurs 
                            linkedResource.setValue("CT_Tab_CollaborateurSemActP1",coll.getNomPrenom() );

                            linkedResource.setValue("CT_Tab_NCumuleLundiAct1",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                            linkedResource.setValue("CT_Tab_NCumuleMardiAct1",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                            linkedResource.setValue("CT_Tab_NCumuleMercrediAct1",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                            linkedResource.setValue("CT_Tab_NCumuleJeudiAct1",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                            linkedResource.setValue("CT_Tab_NCumuleVendrediAct1",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                            linkedResource.setValue("CT_Tab_NCumuleSamediAct1",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                            //						 linkedResource.setValue("CT_Tab_SommeLundiAct1",getCumulative(coll.getNomPrenom())[0] );
                            //						 linkedResource.setValue("CT_Tab_SommeMardiAct1",getCumulative(coll.getNomPrenom())[1] );
                            //						 linkedResource.setValue("CT_Tab_SommeMercrediAct1",getCumulative(coll.getNomPrenom())[2] );
                            //						 linkedResource.setValue("CT_Tab_SommeJeudiAct1",getCumulative(coll.getNomPrenom())[3] );
                            //						 linkedResource.setValue("CT_Tab_SommeVendrediAct1",getCumulative(coll.getNomPrenom())[4] );
                            //						 linkedResource.setValue("CT_Tab_SommeSamediAct1",getCumulative(coll.getNomPrenom())[5] );

                            linkedResource.setValue("CT_Tab_NoteLundiSemActP1",sem.getLundi() );
                            linkedResource.setValue("CT_Tab_NoteMardiSemActP1",sem.getMardi());  
                            linkedResource.setValue("CT_Tab_NoteMercrediSemActP1",sem.getMercredi() );
                            linkedResource.setValue("CT_Tab_NoteJeudiSemActP1",sem.getJeudi() );
                            linkedResource.setValue("CT_Tab_NoteVendrediSemActP1",sem.getVendredi());
                            if(!sem.getSamedi().equals("null")){
                                linkedResource.setValue("CT_Tab_NoteSamediSemActP1",sem.getSamedi());
                                noteTable2[cpt][6] = sem.getSamedi();
                            }else
                            {
                                noteTable2[cpt][6] = "N";
                            }
                            noteTable2[cpt][0] = coll.getNomPrenom();
                            noteTable2[cpt][1] = sem.getLundi();
                            noteTable2[cpt][2] = sem.getMardi();
                            noteTable2[cpt][3] = sem.getMercredi();
                            noteTable2[cpt][4] = sem.getJeudi();
                            noteTable2[cpt][5] = sem.getVendredi();

                            // ajout de la ligne au tableau
                            instance.addLinkedResource( linkedResource );
                            cpt++;
                        }	
                        getWorkflowInstance().setValue("CT_Semaine_2", nbr_Semaine);
                    }
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            log.info("Error in fullSecondTable() method : " + e.getClass() + " - " + e.getMessage());
        }
    }
    
    /**
     * ==========================================================================================================
     * FULLTHIRDTABLE
     * ==========================================================================================================
     */
    public void fullThirdTable(String refDossier)
    {
        try {
            String nbr = "desc";
            String order = "desc";
            instance = getWorkflowInstance();
            instance.setValue("CT_NotesSemaineActuellePlus2", null);
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus2");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()==0)
            {
                if(!areNotesSave(refDossier)){
                    for (Collaborateur coll : getCollaborateur(refDossier)) {
                        // création d'une ligne 
                        ILinkedResource linkedResource = instance.createLinkedResource("CT_NotesSemaineActuellePlus2");
                        //positionnement de quelques valeurs 
                        linkedResource.setValue("CT_Tab_CollaborateurSemActP2",coll.getNomPrenom() );

                        linkedResource.setValue("CT_Tab_NCumuleLundiAct2",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                        linkedResource.setValue("CT_Tab_NCumuleMardiAct2",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                        linkedResource.setValue("CT_Tab_NCumuleMercrediAct2",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                        linkedResource.setValue("CT_Tab_NCumuleJeudiAct2",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                        linkedResource.setValue("CT_Tab_NCumuleVendrediAct2",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                        linkedResource.setValue("CT_Tab_NCumuleSamediAct2",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                        // ajout de la ligne au tableau
                        instance.addLinkedResource( linkedResource );
                    }

                }else{
                    int cpt=0;
                    for (Collaborateur coll : getCollaborateur(refDossier)) {
                        for (Semaine sem : getNotes(coll.getNum(),nbr,order)) {
                            // création d'une ligne 
                            ILinkedResource linkedResource = instance.createLinkedResource("CT_NotesSemaineActuellePlus2");
                            //positionnement de quelques valeurs 
                            linkedResource.setValue("CT_Tab_CollaborateurSemActP2",coll.getNomPrenom() );

                            linkedResource.setValue("CT_Tab_NCumuleLundiAct2",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                            linkedResource.setValue("CT_Tab_NCumuleMardiAct2",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                            linkedResource.setValue("CT_Tab_NCumuleMercrediAct2",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                            linkedResource.setValue("CT_Tab_NCumuleJeudiAct2",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                            linkedResource.setValue("CT_Tab_NCumuleVendrediAct2",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                            linkedResource.setValue("CT_Tab_NCumuleSamediAct2",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                            linkedResource.setValue("CT_Tab_NoteLundiSemActP2",sem.getLundi() );
                            linkedResource.setValue("CT_Tab_NoteMardiSemActP2",sem.getMardi());  
                            linkedResource.setValue("CT_Tab_NoteMercrediSemActP2",sem.getMercredi() );
                            linkedResource.setValue("CT_Tab_NoteJeudiSemActP2",sem.getJeudi() );
                            linkedResource.setValue("CT_Tab_NoteVendrediSemActP2",sem.getVendredi());
                            if(!sem.getSamedi().equals("null")){
                                linkedResource.setValue("CT_Tab_NoteSamediSemActP2",sem.getSamedi());
                                noteTable3[cpt][6] = sem.getSamedi();
                            }else
                            {
                                noteTable3[cpt][6] = "N";
                            }
                            noteTable3[cpt][0] = coll.getNomPrenom();
                            noteTable3[cpt][1] = sem.getLundi();
                            noteTable3[cpt][2] = sem.getMardi();
                            noteTable3[cpt][3] = sem.getMercredi();
                            noteTable3[cpt][4] = sem.getJeudi();
                            noteTable3[cpt][5] = sem.getVendredi();

                            // ajout de la ligne au tableau
                            instance.addLinkedResource( linkedResource );
                            cpt++;
                        }   
                        getWorkflowInstance().setValue("CT_Semaine_3", nbr_Semaine);
                    }
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            log.info("Error in fullThirdTable() method : " + e.getClass() + " - " + e.getMessage());
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE RECEVOIR LE REFERENCE DE DOSSIER ET QUI PERMET DE REMPLIR LE QUATRIÈME TABLE
     * ==========================================================================================================
     */
    public void fullFourthTable(String refDossier)
    {
        try {
            getWorkflowInstance().setValue("CT_NotesSemaineActuellePlus3", null);
            for (Collaborateur coll : getCollaborateur(refDossier)) {
                // création d'une ligne 
                ILinkedResource linkedResource = instance.createLinkedResource("CT_NotesSemaineActuellePlus3");
                //positionnement de quelques valeurs 
                linkedResource.setValue("CT_Tab_CollaborateurSemActP3",coll.getNomPrenom() );

                linkedResource.setValue("CT_Tab_NCumuleLundiAct3",getCumulative(coll.getNomPrenom(),refDossier)[0] );
                linkedResource.setValue("CT_Tab_NCumuleMardiAct3",getCumulative(coll.getNomPrenom(),refDossier)[1] );
                linkedResource.setValue("CT_Tab_NCumuleMercrediAct3",getCumulative(coll.getNomPrenom(),refDossier)[2] );
                linkedResource.setValue("CT_Tab_NCumuleJeudiAct3",getCumulative(coll.getNomPrenom(),refDossier)[3] );
                linkedResource.setValue("CT_Tab_NCumuleVendrediAct3",getCumulative(coll.getNomPrenom(),refDossier)[4] );
                linkedResource.setValue("CT_Tab_NCumuleSamediAct3",getCumulative(coll.getNomPrenom(),refDossier)[5] );

                // ajout de la ligne au tableau
                instance.addLinkedResource( linkedResource );
            }
        } catch (Exception e) {
            log.error("Erreor in fullFourthTable method :"+e.getMessage()+" _ "+e.getClass());
        }
    }
    /**
     * ==========================================================================================================
     * CETTE PERMET DE RECUPERER UNE LIST DE COLLABORATEUR QU'ONT LIE AU REFERECE DE DSSIER SELECTIONNER
     * @param REFDOSSIER : VALUE OF LIST DOSSIER
     * ==========================================================================================================
     */
    public List<Collaborateur> getCollaborateur(String RefDossier)
    {
        cnx=null;
        st=null;
        rs=null;
        ArrayList<Collaborateur> lst = new ArrayList<Collaborateur>() ;
        module=getWorkflowModule();
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            instance=getWorkflowInstance();
            String req="SELECT login,Fullname,N_Collab from collaborateur where RefDossier='"+RefDossier+"'";
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                Collaborateur col = new Collaborateur();
                col.setLogin(rs.getString("login"));
                col.setNomPrenom(rs.getString("Fullname"));
                col.setNum(rs.getInt("N_Collab"));
                lst.add(col);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS Erreur in getDossier methode : "+e.getClass()+" _ "+e.getMessage()+" _ "+e.getStackTrace()[0].getLineNumber());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return  lst;
    }
    /**
     * ==========================================================================================================
     * LIST<SEMAINE> GETNOTES
     * ==========================================================================================================
     */
    public List<Semaine> getNotes(int num,String sem,String indx)
    {
        cnx=null;
        st=null;
        rs=null;
        ArrayList<Semaine> lst = new ArrayList<Semaine>() ;
        module=getWorkflowModule();
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            instance=getWorkflowInstance();
            String req="select top 1 * from (SELECT top 2 [Semaine],Col.Fullname,[Lundi],[Mardi],[Mercredi] ,[Jeudi],[Vendredi],[Samedi],[DateCreation]"+
                    "FROM Notes N inner join Collaborateur Col on Col.N_Collab = N.Num_coll where Num_coll="+num+" order by Semaine*1 "+indx+") col order by Semaine*1 "+sem;
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                Semaine col = new Semaine();
                col.setLundi(rs.getString("Lundi"));
                col.setMardi(rs.getString("Mardi"));
                col.setMercredi(rs.getString("Mercredi"));
                col.setJeudi(rs.getString("Jeudi"));
                col.setVendredi(rs.getString("Vendredi"));
                col.setSamedi(rs.getString("Samedi"));
                nbr_Semaine = rs.getInt("Semaine");
                lst.add(col);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS Erreur in getNotes methode : "+e.getClass()+" _ "+e.getMessage()+" _ "+e.getStackTrace()[0].getLineNumber());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return  lst;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE QUI REاOIE LE REFERENCE DE DOSSIER ET QUI RETURNE TRUE SI LA RESUTLAT DE LA REQUETE EST 
     * SUPERIEUR ہ 0 SINON RECUPERER FALSE
     * ==========================================================================================================
     */
    public boolean areNotesSave(String RefDossier)
    {
        cnx=null;
        st=null;
        rs=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String req = "select COUNT(*) as Nbr from Collaborateur c inner join Notes n on c.N_Collab=n.Num_coll where c.RefDossier='"+RefDossier+"'";
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt("Nbr")>0)
                {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS Erreur in getDossier methode : "+e.getClass()+" _ "+e.getMessage()+" _ "+e.getStackTrace()[0].getLineNumber());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return false;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET D'ENREGISTRER LES NOTES QUI SONT DANS LE TABLEAU DE LA PREMIER SEMAINE DANS 
     * LA TABLE NOTES
     * ==========================================================================================================
     */
    public void saveFirstNotesTable(String refDossier)
    {
        cnx=null;
        st=null;
        try {
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("CT_NotesSemaineActuelle");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()!=0)
            {
                int lastWeek = getLastWeek();
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    int Semaine = lastWeek;
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemAct");
                    String mardi = (String)asso.getValue("CT_Tab_NoteMardiSemAct");
                    String mercredi = (String)asso.getValue("CT_Tab_NoteMercrediSemAct");
                    String jeudi = (String)asso.getValue("CT_Tab_NoteJeudiSemAct");
                    String vendredi = (String)asso.getValue("CT_Tab_NoteVendrediSemAct");
                    String samedi = (String)asso.getValue("CT_Tab_NoteSamediSemAct")==null ? "0/4" : (String)asso.getValue("CT_Tab_NoteSamediSemAct");
                    int num = getNumCollByName((String)asso.getValue("CT_Tab_Collaborateur"), refDossier);
                    String dateCreation = getWorkflowInstance().getCreatedDate().toString();
                    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,new Locale("EN","en"));
                    //String startDate = shortDateFormat.format(getWorkflowInstance().getValue("CT_Date"));
                    
                    Date startDateUtil = (Date) getWorkflowInstance().getValue("CT_Date");
                    java.sql.Date startDate = new java.sql.Date(startDateUtil.getTime());

                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String req = "INSERT INTO [Attijari].[dbo].[Notes]([Semaine],[Lundi],[Mardi],[Mercredi],[Jeudi],[Vendredi],[Samedi],[DateCreation],[Num_coll],[StartDate],[EndDate])"+
                            "VALUES("+Semaine+",'"+lundi+"','"+mardi+"','"+mercredi+"','"+jeudi+"','"+vendredi+"','"+samedi+"','"+dateCreation+"',"+num+",?,DATEADD(day, 5, ?))";
                    st = cnx.prepareStatement(req);
                    st.setDate(1, startDate);
                    st.setDate(2, startDate);
                    st.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error in the saveFirstNotesTable method :"+e.getMessage()+" _ "+ e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET D'ENREGISTRER LES NOTES QUI SONT DANS LE TABLEAU DE LA DEUXIEME SEMAINE DANS 
     * LA TABLE NOTES
     * ==========================================================================================================
     */
    public void saveSecondNotesTable(String refDossier)
    {
        cnx=null;
        st=null;
        try {
            instance = getWorkflowInstance();
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus1");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()!=0)
            {
                int lastWeek = getLastWeek();
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    int Semaine = lastWeek;
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemActP1");
                    String mardi = (String)asso.getValue("CT_Tab_NoteMardiSemActP1");
                    String mercredi = (String)asso.getValue("CT_Tab_NoteMercrediSemActP1");
                    String jeudi = (String)asso.getValue("CT_Tab_NoteJeudiSemActP1");
                    String vendredi = (String)asso.getValue("CT_Tab_NoteVendrediSemActP1");
                    String samedi = (String)asso.getValue("CT_Tab_NoteSamediSemActP1")==null ? "0/4" : (String)asso.getValue("CT_Tab_NoteSamediSemActP1") ;
                    int num = getNumCollByName((String)asso.getValue("CT_Tab_CollaborateurSemActP1"), refDossier);
                    String dateCreation = getWorkflowInstance().getCreatedDate().toString();

                    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,new Locale("EN","en"));
                    //String startDate = shortDateFormat.format(getWorkflowInstance().getValue("CT_Date"));


                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String req = "INSERT INTO [Attijari].[dbo].[Notes]([Semaine],[Lundi],[Mardi],[Mercredi],[Jeudi],[Vendredi],[Samedi],[DateCreation],[Num_coll],[StartDate],[EndDate])"+
                            "VALUES("+Semaine+",'"+lundi+"','"+mardi+"','"+mercredi+"','"+jeudi+"','"+vendredi+"','"+samedi+"','"+dateCreation+"',"+num+",DATEADD(day, 7,?),DATEADD(day, 12, ?))";
                    st = cnx.prepareStatement(req);
                    Date startDateUtil = (Date) getWorkflowInstance().getValue("CT_Date");
                    java.sql.Date startDate = new java.sql.Date(startDateUtil.getTime());
                    st.setDate(1, startDate);
                    st.setDate(2, startDate);
                    st.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error in the saveSecondNotesTable method :"+e.getMessage()+" _ "+ e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /**
     * ==========================================================================================================
     *  CETTE METHODE PERMET D'ENREGISTRER LES NOTES QUI SONT DANS LE TABLEAU DE LA TROISIEME SEMAINE DANS 
     *  LA TABLE NOTES
     * ==========================================================================================================
     */
    public void saveThirdNotesTable(String refDossier)
    {
        cnx=null;
        st=null;
        try {
            instance = getWorkflowInstance();
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus2");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()!=0)
            {
                int lastWeek = getLastWeek();
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    int Semaine = lastWeek;
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemActP2");
                    String mardi = (String)asso.getValue("CT_Tab_NoteMardiSemActP2");
                    String mercredi = (String)asso.getValue("CT_Tab_NoteMercrediSemActP2");
                    String jeudi = (String)asso.getValue("CT_Tab_NoteJeudiSemActP2");
                    String vendredi = (String)asso.getValue("CT_Tab_NoteVendrediSemActP2");
                    String samedi = (String)asso.getValue("CT_Tab_NoteSamediSemActP2")==null ? "0/4" : (String)asso.getValue("CT_Tab_NoteSamediSemActP2");
                    int num = getNumCollByName((String)asso.getValue("CT_Tab_CollaborateurSemActP2"), refDossier);
                    String dateCreation = getWorkflowInstance().getCreatedDate().toString();

                    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,new Locale("EN","en"));
                    String startDate = shortDateFormat.format(getWorkflowInstance().getValue("CT_Date"));

                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String req = "INSERT INTO [Attijari].[dbo].[Notes]([Semaine],[Lundi],[Mardi],[Mercredi],[Jeudi],[Vendredi],[Samedi],[DateCreation],[Num_coll],[StartDate],[EndDate])"+
                            "VALUES("+Semaine+",'"+lundi+"','"+mardi+"','"+mercredi+"','"+jeudi+"','"+vendredi+"','"+samedi+"','"+dateCreation+"',"+num+",DATEADD(day, 14,?),DATEADD(day, 19, ?))";
                    st = cnx.prepareStatement(req);
                    Date date = (Date) getWorkflowInstance().getValue("CT_Date");
                    java.sql.Date datesql = new java.sql.Date(date.getTime());
                    st.setDate(1, datesql);
                    st.setDate(2, datesql);
                    st.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error in the saveThirdNotesTable method :"+e.getMessage()+" _ "+ e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /**
     * ==========================================================================================================
     *  CETTE METHODE PERMET D'ENREGISTRER LES NOTES QUI SONT DANS LE TABLEAU DE LA QUATRIÈME SEMAINE DANS 
     *  LA TABLE NOTES
     * ==========================================================================================================
     */
    public void saveFourthNotesTable(String refDossier)
    {
        cnx=null;
        st=null;
        try {
            instance = getWorkflowInstance();
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus3");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()!=0)
            {
                int lastWeek = getLastWeek();
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    int Semaine = lastWeek;
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemActP3");
                    String mardi = (String)asso.getValue("CT_Tab_NoteMardiSemActP3");
                    String mercredi = (String)asso.getValue("CT_Tab_NoteMercrediSemActP3");
                    String jeudi = (String)asso.getValue("CT_Tab_NoteJeudiSemActP3");
                    String vendredi = (String)asso.getValue("CT_Tab_NoteVendrediSemActP3");
                    String samedi = (String)asso.getValue("CT_Tab_NoteSamediSemActP3")==null ? "0/4" : (String)asso.getValue("CT_Tab_NoteSamediSemActP3");
                    int num = getNumCollByName((String)asso.getValue("CT_Tab_CollaborateurSemActP3"), refDossier);
                    String dateCreation = getWorkflowInstance().getCreatedDate().toString();

                    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,new Locale("EN","en"));
                    String startDate = shortDateFormat.format(getWorkflowInstance().getValue("CT_Date"));

                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String req = "INSERT INTO [Attijari].[dbo].[Notes]([Semaine],[Lundi],[Mardi],[Mercredi],[Jeudi],[Vendredi],[Samedi],[DateCreation],[Num_coll],[StartDate],[EndDate])"+
                            "VALUES("+Semaine+",'"+lundi+"','"+mardi+"','"+mercredi+"','"+jeudi+"','"+vendredi+"','"+samedi+"','"+dateCreation+"',"+num+",DATEADD(day, 21,?),DATEADD(day, 26, ?))";
                    st = cnx.prepareStatement(req);
                    Date date = (Date) getWorkflowInstance().getValue("CT_Date");
                    java.sql.Date datesql = new java.sql.Date(date.getTime());
                    st.setDate(1, datesql);
                    st.setDate(2, datesql);
                    st.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error in the saveFourthNotesTable method :"+e.getMessage()+" _ "+ e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE MODIFIER LES NOTES DE LE PREMIER TABLEAU APRES LA VALIDATION
     * ==========================================================================================================
     */
    public void updateFirstTable(String refDossier,int week1)
    {
        cnx=null;
        st=null;
        try {
            instance = getWorkflowInstance();
            //int FirstWeek = (int)instance.getValue("CT_Semaine_1");
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuelle");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()!=0)
            {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemAct");
                    String mardi = (String)asso.getValue("CT_Tab_NoteMardiSemAct");
                    String mercredi = (String)asso.getValue("CT_Tab_NoteMercrediSemAct");
                    String jeudi = (String)asso.getValue("CT_Tab_NoteJeudiSemAct");
                    String vendredi = (String)asso.getValue("CT_Tab_NoteVendrediSemAct");
                    String samedi = (String)asso.getValue("CT_Tab_NoteSamediSemAct");
                    int num = getNumCollByName((String)asso.getValue("CT_Tab_Collaborateur"), refDossier);
                    String dateCreation =  getWorkflowInstance().getCreatedDate().toString();
                    
                    Date dateUtil = getWorkflowInstance().getCreatedDate();
                    java.sql.Date dateSql = new java.sql.Date(dateUtil.getTime());

                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String req = "UPDATE [Attijari].[dbo].[Notes] SET [Lundi] = '"+lundi+"', [Mardi] = '"+mardi+"'"+
                            " ,[Mercredi] = '"+mercredi+"',[Jeudi] = '"+jeudi+"'"+
                            " ,[Vendredi] = '"+vendredi+"',[Samedi] = '"+samedi+"'"+
                            " ,[DateCreation] = ? "+
                            " WHERE num_coll ="+num+" and Semaine ="+week1;
                    st = cnx.prepareStatement(req);
                    st.setDate(1, dateSql);
                    st.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error in the saveFirstNotesTable method :"+e.getMessage()+" _ "+ e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE MODIFIER LES NOTES DE LE DEUXIEME TABLEAU APRES LA VALIDATION
     * ==========================================================================================================
     */
    public void updateSecondTable(String refDossier,int week2)
    {
        cnx=null;
        st=null;
        try {
            instance = getWorkflowInstance();
            //int SecondWeek = (int)instance.getValue("CT_Semaine_2");
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus1");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()!=0)
            {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemActP1");
                    String mardi = (String)asso.getValue("CT_Tab_NoteMardiSemActP1");
                    String mercredi = (String)asso.getValue("CT_Tab_NoteMercrediSemActP1");
                    String jeudi = (String)asso.getValue("CT_Tab_NoteJeudiSemActP1");
                    String vendredi = (String)asso.getValue("CT_Tab_NoteVendrediSemActP1");
                    String samedi = (String)asso.getValue("CT_Tab_NoteSamediSemActP1");
                    int num = getNumCollByName((String)asso.getValue("CT_Tab_CollaborateurSemActP1"), refDossier);
                    String dateCreation =  getWorkflowInstance().getCreatedDate().toString();

                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String req = "UPDATE [Attijari].[dbo].[Notes] SET [Lundi] = '"+lundi+"', [Mardi] = '"+mardi+"'"+
                            " ,[Mercredi] = '"+mercredi+"',[Jeudi] = '"+jeudi+"'"+
                            " ,[Vendredi] = '"+vendredi+"',[Samedi] = '"+samedi+"'"+
                            " ,[DateCreation] = '"+dateCreation+"'"+
                            " WHERE num_coll ="+num+" and Semaine ="+week2;
                    st = cnx.prepareStatement(req);
                    st.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error in the saveSecondNotesTable method :"+e.getMessage()+" _ "+ e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA ééMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE MODIFIER LES NOTES DE LA TROISIÈME TABLEAU APRES LA VALIDATION
     * ==========================================================================================================
     */
    public void updateThirdTable(String refDossier,int week2)
    {
        cnx=null;
        st=null;
        try {
            instance = getWorkflowInstance();
            //int SecondWeek = (int)instance.getValue("CT_Semaine_2");
            // recuperer les lignes de le tableau article
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus2");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ.size()!=0)
            {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemActP2");
                    String mardi = (String)asso.getValue("CT_Tab_NoteMardiSemActP2");
                    String mercredi = (String)asso.getValue("CT_Tab_NoteMercrediSemActP2");
                    String jeudi = (String)asso.getValue("CT_Tab_NoteJeudiSemActP2");
                    String vendredi = (String)asso.getValue("CT_Tab_NoteVendrediSemActP2");
                    String samedi = (String)asso.getValue("CT_Tab_NoteSamediSemActP2");
                    int num = getNumCollByName((String)asso.getValue("CT_Tab_CollaborateurSemActP2"), refDossier);
                    String dateCreation =  getWorkflowInstance().getCreatedDate().toString();

                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String req = "UPDATE [Attijari].[dbo].[Notes] SET [Lundi] = '"+lundi+"', [Mardi] = '"+mardi+"'"+
                            " ,[Mercredi] = '"+mercredi+"',[Jeudi] = '"+jeudi+"'"+
                            " ,[Vendredi] = '"+vendredi+"',[Samedi] = '"+samedi+"'"+
                            " ,[DateCreation] = '"+dateCreation+"'"+
                            " WHERE num_coll ="+num+" and Semaine ="+week2;
                    st = cnx.prepareStatement(req);
                    st.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error in the saveThirdNotesTable method :"+e.getMessage()+" _ "+ e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE QUI RETURNE LA DERNIER SEMAINE 
     * ========================================================================================================== 
     */
    public int getLastWeek()
    {
        cnx=null;
        st=null;
        rs=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String req = "select top(1) Semaine from Notes order by Semaine*1 desc";
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt("Semaine")+1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in getLastWeek method :"+e.getMessage()+" _ "+e.getCause()+" _ "+e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return 1;
    }
    /**
     * ==========================================================================================================
     * CETTE METHOD QUI REاOIT LE NOM DE COLLABORATEUR ET LE REFERENCE DE DOSSIER ET QUI RETURNE 
     * LE NUMER CE COLLABORATEUR
     * ==========================================================================================================
     */
    public int getNumCollByName(String CollName,String refDossier)
    {
        cnx=null;
        st=null;
        rs=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String req = "select N_Collab from Collaborateur where Fullname = '"+CollName+"' and RefDossier= '"+refDossier+"'";
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt("N_Collab");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in getLastWeek method :"+e.getMessage()+" _ "+e.getCause()+" _ "+e.getStackTrace());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return 0;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE QUI REاOIT LA SEMAINE ET QUI RETORNE TRUE SI LA SEMAIN EXISTE
     * ==========================================================================================================
     */
    public boolean ifWeekExist(int semaine)
    {
        cnx=null;
        st=null;
        rs=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String req = "select count(*) as nbr  from Notes where Semaine="+semaine;
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt("nbr")>0)
                    return true ;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error in ifWeekExist method :"+e.getMessage()+" _ "+e.getCause());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return false;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET D'ENREGISTRER LES NOTES QUI SONT DANS LE TABLEAU DE LA PREMIER SEMAINE DANS 
     * LA TABLE NOTES
     * ==========================================================================================================
     */
    public boolean IsEmpty()
    {
        try {
            // recuperer les lignes de le tableau article
            Collection associ1 = (Collection) getWorkflowInstance().getLinkedResources("CT_NotesSemaineActuelle");
            Collection associ2 = (Collection) getWorkflowInstance().getLinkedResources("CT_NotesSemaineActuellePlus1");
            Collection associ3 = (Collection) getWorkflowInstance().getLinkedResources("CT_NotesSemaineActuellePlus2");
            Collection associ4 = (Collection) getWorkflowInstance().getLinkedResources("CT_NotesSemaineActuellePlus3");
            // mettre les lignes recuperent de le tableau article dans le tableau article demandee
            if(associ1.size()!=0 || associ2.size()!=0 || associ3.size()!=0 || associ4.size()!=0)
            {
                for (Iterator iter1 = associ1.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemAct");
                    if(lundi==null || lundi=="")
                    {
                        return true;
                    }
                }

                for (Iterator iter1 = associ2.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemActP1");
                    if(lundi==null || lundi=="")
                    {
                        return true;
                    }
                }

                for (Iterator iter1 = associ3.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemActP2");
                    if(lundi==null || lundi=="")
                    {
                        return true;
                    }
                }
                
                for (Iterator iter1 = associ4.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String lundi = (String)asso.getValue("CT_Tab_NoteLundiSemActP3");
                    if(lundi==null || lundi=="")
                    {
                        return true;
                    }
                }
            }
        }catch(Exception e){
            log.error("Error in the IsEmpty method :"+e.getMessage()+" _ "+ e.getStackTrace());
        }

        return false;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE RETOURNER LA DATE DE LA PREMIER JOUR DE LA SEMAIN (LUNDI)
     * ==========================================================================================================
     */
    public Date getFirstDayOfWeek()
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_WEEK,getDayNumber()); 

        Date threedayslater = cal.getTime();
        return threedayslater;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE RETOURNER LE NOMBRE DE JOUR DE LA SEMAIN
     * ==========================================================================================================
     */
    public int getDayNumber()
    {
        String toDay = new SimpleDateFormat("EEE",Locale.FRENCH).format(new Date());
        if( toDay.equals("lun."))
        {
            return 0;
        }else if(toDay.equals("mar."))
        {
            return -1;
        }else if(toDay.equals("mer."))
        {
            return -2;
        }else if(toDay.equals("jeu."))
        {
            return -3;
        }else if(toDay.equals("ven."))
        {
            return -4;
        }else if(toDay.equals("sam."))
        {
            return 2;
        }else{
            return 1;
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE RECUPERE LES COLLABORATEUR SANS DOUBLANT
     * ==========================================================================================================
     */
    public List<String> getResource()
    {
        cnx=null;
        st=null;
        rs=null;
        List<String> Col = new ArrayList<>();
        try {
            String req = "select distinct fullname from Collaborateur";
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                Col.add(rs.getString("fullname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error in getResource method :"+e.getMessage()+" _ "+e.getCause());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }

        return Col;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE CREER LE FICHIER EVENT.JSON
     * ==========================================================================================================
     */
    public void createJsonFileEvent(String path)
    {
//        JsonWriter jsonWriter = null;
//        try {
//            jsonWriter = new JsonWriter(new FileWriter(path+"/events.json"));
//
//            jsonWriter.beginArray();
//            int resourceId = 10;
//            for(String fullname : getResource()) {
//                for(Event ev : getEvent(fullname, resourceId)){
//                    jsonWriter.beginObject();
//                    jsonWriter.name("id");
//                    jsonWriter.value(ev.getId());
//                    jsonWriter.name("resourceId");
//                    jsonWriter.value(ev.getResourceId());
//                    jsonWriter.name("start");
//                    jsonWriter.value(ev.getStartDate());
//                    jsonWriter.name("end");
//                    jsonWriter.value(ev.getEndDate());
//                    jsonWriter.name("title");
//                    jsonWriter.value(ev.getTitle());
//                    jsonWriter.endObject();
//                }
//                resourceId+=10;
//            }
//
//            jsonWriter.endArray();
//
//        } catch (IOException e) {
//            log.error("error in createJsonFileEvent method 1 :"+e.getMessage()+" _ "+e.getCause());
//        }finally{
//            try {
//                jsonWriter.close();
//                System.out.println("Fin Writing");
//            } catch (IOException e) {
//                log.error("error in createJsonFileEvent method 2 :"+e.getMessage()+" _ "+e.getCause());
//            }
//        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE CREER LE FICHIER RESOURCES.JSON
     * ==========================================================================================================
     */
    public void createJsonFileResources(String path)
    {
//        JsonWriter jsonWriter = null;
//        try {
//            jsonWriter = new JsonWriter(new FileWriter(path+"/resources.json"));
//            jsonWriter.beginArray();
//            int i=0,j=10;
//            for(String Fullname :getResource()) {
//
//                jsonWriter.beginObject();
//                jsonWriter.name("id");
//                jsonWriter.value(j);
//                jsonWriter.name("title");
//                jsonWriter.value(Fullname);
//                jsonWriter.name("eventColor");
//                jsonWriter.value(getColor()[i]);
//                jsonWriter.endObject();
//                i++;
//                j+=10;
//            }
//
//            jsonWriter.endArray();
//
//        } catch (IOException e) {
//            log.error("error in createJsonFileResources method 1:"+e.getMessage()+" _ "+e.getCause());
//        }finally{
//            try {
//                jsonWriter.close();
//                System.out.println("Fin Writing");
//            } catch (IOException e) {
//                log.error("error in createJsonFileResources method 2:"+e.getMessage()+" _ "+e.getCause());
//            }
//        }
    }
    /**
     * ==========================================================================================================
     * LIST<EVENT> GETEVENT
     * ==========================================================================================================
     */
    public List<Event> getEvent(String fullName,int resourceId)
    {
        cnx=null;
        st=null;
        rs=null;
        List<Event> Events = new ArrayList<Event>();
        try {
            String req = "select id,StartDate,EndDate,RefDossier from Notes n inner join Collaborateur c on n.Num_coll=c.N_Collab "+
                    " where c.Fullname='"+fullName+"'";
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            while(rs.next())
            {
                Event e = new Event();
                e.setId(rs.getString("id"));
                e.setResourceId(resourceId+""); // convert int to String
                e.setStartDate(rs.getString("StartDate"));
                e.setEndDate(rs.getString("EndDate"));
                e.setTitle(rs.getString("RefDossier"));
                Events.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error in getEvent method :"+e.getMessage()+" _ "+e.getClass());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return Events;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE RETOURNER UNE LISTE STRING DES COLOURS
     * ==========================================================================================================
     */
    public String[] getColor()
    {
        String[] lst = new String[]{"green","blue","red","tan","yellow","orange","grey","gold","beaver","salmon","copper","denim","canary","cerulean"};
        return lst;
    }
    /**
     * ==========================================================================================================
     * IFEXISTE
     * ==========================================================================================================
     */
    public boolean ifExiste()
    {
        cnx=null;
        st=null;
        rs=null;
        try {
            //			DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,new Locale("FR","fr"));
            //			String startDate = shortDateFormat.format(getWorkflowInstance().getValue("CT_Date"));
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String startDate = simpleDateFormat.format(getWorkflowInstance().getValue("CT_Date"));
           // String req = "select count(*) as nbr from notes where StartDate=DATEADD(day,14,'"+startDate+"')";
            String req = "select count(*) as nbr from notes where StartDate=DATEADD(day,14,?)";
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            st = cnx.prepareStatement(req);
            //----------------------
            Date date = (Date) getWorkflowInstance().getValue("CT_Date");
            java.sql.Date datesql = new java.sql.Date(date.getTime());
            st.setDate(1, datesql);
            rs=st.executeQuery();
            int nbr= 0;
            while(rs.next())
            {
                nbr = rs.getInt("nbr");
            }
            if(nbr>0)
                return true;
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("error in ifExiste method :"+e.getMessage()+" _ "+e.getCause());
        }
        finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }
        return false;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE REاOIT LE NOM COMPLET DE COLLABORATEUR ET DE RETURNER UNE CHAINE DE CARACTERES
     * ==========================================================================================================
     */
    public String[] getCumulative(String fullName,String ref)
    {
        cnx=null;
        st=null;
        rs=null;
        String tab[] = new String[6];
        try {
            // Etat de payement : en cours
            //String req = "select c.Fullname,d.RefDossier,lundi,mardi,Mercredi,jeudi,vendredi,samedi from notes n inner join Collaborateur c "+
            //        "on n.Num_coll=c.N_Collab inner join Dossiers d on c.RefDossier=d.RefDossier where c.Fullname='"+fullName+"' and d.EtatPaiement='En cours' and d.RefDossier='"+ref+"'";
            // Pas de clause "Etat de payement"
            String req = "select c.Fullname,d.RefDossier,lundi,mardi,Mercredi,jeudi,vendredi,samedi from notes n inner join Collaborateur c "+
                    "on n.Num_coll=c.N_Collab inner join Dossiers d on c.RefDossier=d.RefDossier where c.Fullname='"+fullName+"' and d.RefDossier='"+ref+"'";
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            st = cnx.prepareStatement(req);
            rs=st.executeQuery();
            int intLun = 0,intMar = 0,intMer = 0,intJeu = 0,intVen = 0,intSam = 0;
            String lundi,mardi,mercredi,jeudi,vendredi,samedi="";
            while(rs.next())
            {
                lundi=(rs.getString("lundi")).substring(0,1);
                intLun+=Integer.parseInt(lundi);
                mardi=(rs.getString("mardi")).substring(0,1);
                intMar+=Integer.parseInt(mardi);
                mercredi=(rs.getString("Mercredi")).substring(0,1);
                intMer+=Integer.parseInt(mercredi);
                jeudi=(rs.getString("jeudi")).substring(0,1);
                intJeu+=Integer.parseInt(jeudi);
                vendredi=(rs.getString("vendredi")).substring(0,1);
                intVen+=Integer.parseInt(vendredi);
                if(samedi!="null" || samedi!="")
                    samedi=(rs.getString("samedi")).substring(0,1);
                intSam+=Integer.parseInt(samedi);
            }
            tab[0]=intLun+"/4";
            tab[1]=intMar+"/4";
            tab[2]=intMer+"/4";
            tab[3]=intJeu+"/4";
            tab[4]=intVen+"/4";
            tab[5]=intSam+"/4";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error in getCumulative method :"+e.getMessage()+" _ "+e.getClass());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st,rs);
        }

        return tab;
    }
    /**
     * ==========================================================================================================
     * @param FULLNAME
     * ==========================================================================================================
     */
    public String CalculerSommeTable1(String fullname,String newNote,String Cumule,int indexDay)
    {
        String note="";
        try {
            int newNoteInt=0,oldNoteInt=0,cumuleNote=0;
            //Converted string to int (new note)
            if(newNote!=null){
                newNote=newNote.substring(0,1);
                newNoteInt+=Integer.parseInt(newNote);
            }
            if(Cumule!=null){
                Cumule=Cumule.substring(0,1);
                cumuleNote+=Integer.parseInt(Cumule);
            }
            for(int i=0;i<noteTable1.length;i++)
            {
                if(noteTable1[i][0]==fullname){
                    noteTable1[i][indexDay]=noteTable1[i][indexDay].substring(0,1);
                    oldNoteInt+=Integer.parseInt(noteTable1[i][indexDay]);
                }
            }
            note= (cumuleNote+(newNoteInt-oldNoteInt))+"/4";
        } catch (Exception e) {
            log.error("error in CalculerSommeTable1 method :"+e.getMessage()+" _ "+e.getClass());
        }
        return note;
    }
    /**
     * ==========================================================================================================
     * @param FULLNAME
     * ==========================================================================================================
     */
    public String CalculerSommeTable2(String fullname,String newNote,String Cumule,int indexDay)
    {
        String note="";
        try {
            int newNoteInt=0,oldNoteInt=0,cumuleNote=0;
            //Converted string to int (new note)
            if(newNote!=null){
                newNote=newNote.substring(0,1);
                newNoteInt+=Integer.parseInt(newNote);
            }
            if(Cumule!=null){
                Cumule=Cumule.substring(0,1);
                cumuleNote+=Integer.parseInt(Cumule);
            }
            for(int i=0;i<noteTable2.length;i++)
            {
                if(noteTable2[i][0]==fullname){
                    noteTable2[i][indexDay]=noteTable2[i][indexDay].substring(0,1);
                    oldNoteInt+=Integer.parseInt(noteTable2[i][indexDay]);
                }
            }
            note= (cumuleNote+(newNoteInt-oldNoteInt))+"/4";
        } catch (Exception e) {
            log.error("error in CalculerSommeTable2 method :"+e.getMessage()+" _ "+e.getClass());
        }
        return note;
    }
    /**
     * ==========================================================================================================
     * @param FULLNAME
     * ==========================================================================================================
     */
    public String CalculerSommeTable3(String fullname,String newNote,String Cumule,int indexDay)
    {
        String note="";
        try {
            int newNoteInt=0,oldNoteInt=0,cumuleNote=0;
            //Converted string to int (new note)
            if(newNote!=null){
                newNote=newNote.substring(0,1);
                newNoteInt+=Integer.parseInt(newNote);
            }
            if(Cumule!=null){
                Cumule=Cumule.substring(0,1);
                cumuleNote+=Integer.parseInt(Cumule);
            }
            for(int i=0;i<noteTable3.length;i++)
            {
                if(noteTable3[i][0]==fullname){
                    noteTable3[i][indexDay]=noteTable3[i][indexDay].substring(0,1);
                    oldNoteInt+=Integer.parseInt(noteTable3[i][indexDay]);
                }
            }
            note= (cumuleNote+(newNoteInt-oldNoteInt))+"/4";
        } catch (Exception e) {
            log.error("error in CalculerSommeTable3 method :"+e.getMessage()+" _ "+e.getClass());
        }
        return note;
    }
    /**
     * ==========================================================================================================
     * @param FULLNAME
     * ==========================================================================================================
     */
    public String CalculerSommeTable4(String fullname,String newNote,String Cumule,int indexDay)
    {
        String note="";
        try {
            int newNoteInt=0,cumuleNote=0;
            //Converted string to int (new note)
            if(newNote!=null){
                newNote=newNote.substring(0,newNote.length()-2);
                newNoteInt+=Integer.parseInt(newNote);
            }
            if(Cumule!=null){
                Cumule=Cumule.substring(0,Cumule.length()-2);
                cumuleNote+=Integer.parseInt(Cumule);
            }
            note= (cumuleNote+newNoteInt)+"/4";
        } catch (Exception e) {
            log.error("error in CalculerSommeTable4 method :"+e.getMessage()+" _ "+e.getClass());
        }
        return note;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE RETOURNER LA SOMME DES JOURS DE LA SEMEIN 1
     * ==========================================================================================================
     */
    public String[][] getSommeTable1()
    {
        String tab[][] = new String[25][7]; //25 lignes discrétionnaire
        try {
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuelle");
            int i = 0;
            for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                ILinkedResource asso = (ILinkedResource) iter1.next();
                tab[i][0]=(String)asso.getValue("CT_Tab_Collaborateur");
                tab[i][1]=(String)asso.getValue("CT_Tab_SommeLundiAct");
                tab[i][2]=(String)asso.getValue("CT_Tab_SommeMardiAct");
                tab[i][3]=(String)asso.getValue("CT_Tab_SommeMercrediAct");
                tab[i][4]=(String)asso.getValue("CT_Tab_SommeJeudiAct");
                tab[i][5]=(String)asso.getValue("CT_Tab_SommeVendrediAct");
                if((String)asso.getValue("CT_Tab_SommeSamediAct")!=null)
                    tab[i][6]=(String)asso.getValue("CT_Tab_SommeSamediAct");
                i++;
            }
        } catch (Exception e) {
            log.error("Error in getSommeTable1 method "+e.getMessage()+" _ "+e.getClass());
        }
        return tab;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE RETOURNER LA SOMME DES JOURS DE LA SEMEIN 2
     * ==========================================================================================================
     */
    public String[][] getSommeTable2()
    {
        String tab[][] = new String[25][7]; //25 lignes discrétionnaire
        try {
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus1");
            int i = 0;
            for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                ILinkedResource asso = (ILinkedResource) iter1.next();
                tab[i][0]=(String)asso.getValue("CT_Tab_CollaborateurSemActP1");
                tab[i][1]=(String)asso.getValue("CT_Tab_SommeLundiAct1");
                tab[i][2]=(String)asso.getValue("CT_Tab_SommeMardiAct1");
                tab[i][3]=(String)asso.getValue("CT_Tab_SommeMercrediAct1");
                tab[i][4]=(String)asso.getValue("CT_Tab_SommeJeudiAct1");
                tab[i][5]=(String)asso.getValue("CT_Tab_SommeVendrediAct1");
                if((String)asso.getValue("CT_Tab_SommeSamediAct1")!=null)
                    tab[i][6]=(String)asso.getValue("CT_Tab_SommeSamediAct1");
                i++;
            }
        } catch (Exception e) {
            log.error("Error in getSommeTable2 method "+e.getMessage()+" _ "+e.getClass());
        }
        return tab;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE RETOURNER LA SOMME DES JOURS DE LA SEMEIN 3
     * ==========================================================================================================
     */
    public String[][] getSommeTable3()
    {
        String tab[][] = new String[25][7]; //25 lignes discrétionnaire
        try {
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus2");
            int i = 0;
            for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                ILinkedResource asso = (ILinkedResource) iter1.next();
                tab[i][0]=(String)asso.getValue("CT_Tab_CollaborateurSemActP2");
                tab[i][1]=(String)asso.getValue("CT_Tab_SommeLundiAct2");
                tab[i][2]=(String)asso.getValue("CT_Tab_SommeMardiAct2");
                tab[i][3]=(String)asso.getValue("CT_Tab_SommeMercrediAct2");
                tab[i][4]=(String)asso.getValue("CT_Tab_SommeJeudiAct2");
                tab[i][5]=(String)asso.getValue("CT_Tab_SommeVendrediAct2");
                if((String)asso.getValue("CT_Tab_SommeSamediAct2")!=null)
                    tab[i][6]=(String)asso.getValue("CT_Tab_SommeSamediAct2");
                i++;
            }
        } catch (Exception e) {
            log.error("Error in getSommeTable3 method "+e.getMessage()+" _ "+e.getClass());
        }
        return tab;
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE MODIFIER LE CUMULE DU TABLEAU DE LA SEMAINE 2
     * ==========================================================================================================
     */
    public void modiferCumuleTable2()
    {
        try {
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus1");
            int i = 0;
            for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                ILinkedResource asso = (ILinkedResource) iter1.next();
                asso.setValue("CT_Tab_NCumuleLundiAct1",getSommeTable1()[i][1]);
                asso.setValue("CT_Tab_NCumuleMardiAct1",getSommeTable1()[i][2]);
                asso.setValue("CT_Tab_NCumuleMercrediAct1",getSommeTable1()[i][3]);
                asso.setValue("CT_Tab_NCumuleJeudiAct1",getSommeTable1()[i][4]);
                asso.setValue("CT_Tab_NCumuleVendrediAct1",getSommeTable1()[i][5]);
                if(getSommeTable1()[i][6]!=null)
                    asso.setValue("CT_Tab_NCumuleSamediAct1",getSommeTable1()[i][6]);
                asso.setValue("CT_Tab_SommeLundiAct1",getSommeTable1()[i][1]);
                asso.setValue("CT_Tab_SommeMardiAct1",getSommeTable1()[i][2]);
                asso.setValue("CT_Tab_SommeMercrediAct1",getSommeTable1()[i][3]);
                asso.setValue("CT_Tab_SommeJeudiAct1",getSommeTable1()[i][4]);
                asso.setValue("CT_Tab_SommeVendrediAct1",getSommeTable1()[i][5]);
                if(getSommeTable1()[i][6]!=null)
                    asso.setValue("CT_Tab_SommeSamediAct1",getSommeTable1()[i][6]);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in modiferCumuleTable2 method "+e.getMessage()+" _ "+e.getClass());
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE MODIFIER LE CUMULE DU TABLEAU DE LA SEMAINE 3
     * ==========================================================================================================
     */
    public void modiferCumuleTable3()
    {
        try {
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus2");
            int i = 0;
            for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                ILinkedResource asso = (ILinkedResource) iter1.next();
                asso.setValue("CT_Tab_NCumuleLundiAct2",getSommeTable2()[i][1]);
                asso.setValue("CT_Tab_NCumuleMardiAct2",getSommeTable2()[i][2]);
                asso.setValue("CT_Tab_NCumuleMercrediAct2",getSommeTable2()[i][3]);
                asso.setValue("CT_Tab_NCumuleJeudiAct2",getSommeTable2()[i][4]);
                asso.setValue("CT_Tab_NCumuleVendrediAct2",getSommeTable2()[i][5]);
                if(getSommeTable2()[i][6]!=null)
                    asso.setValue("CT_Tab_NCumuleSamediAct2",getSommeTable2()[i][6]);
                asso.setValue("CT_Tab_SommeLundiAct2",getSommeTable2()[i][1]);
                asso.setValue("CT_Tab_SommeMardiAct2",getSommeTable2()[i][2]);
                asso.setValue("CT_Tab_SommeMercrediAct2",getSommeTable2()[i][3]);
                asso.setValue("CT_Tab_SommeJeudiAct2",getSommeTable2()[i][4]);
                asso.setValue("CT_Tab_SommeVendrediAct2",getSommeTable2()[i][5]);
                if(getSommeTable2()[i][6]!=null)
                    asso.setValue("CT_Tab_SommeSamediAct2",getSommeTable2()[i][6]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in modiferCumuleTable3 method "+e.getMessage()+" _ "+e.getClass());
        }
    }
    /**
     * ==========================================================================================================
     * CETTE METHODE PERMET DE MODIFIER LE CUMULE DU TABLEAU DE LA SEMAINE 4
     * ==========================================================================================================
     */
    public void modiferCumuleTable4()
    {
        try {
            Collection associ = (Collection) instance.getLinkedResources("CT_NotesSemaineActuellePlus3");
            int i = 0;
            for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                ILinkedResource asso = (ILinkedResource) iter1.next();
                asso.setValue("CT_Tab_NCumuleLundiAct3",getSommeTable3()[i][1]);
                asso.setValue("CT_Tab_NCumuleMardiAct3",getSommeTable3()[i][2]);
                asso.setValue("CT_Tab_NCumuleMercrediAct3",getSommeTable3()[i][3]);
                asso.setValue("CT_Tab_NCumuleJeudiAct3",getSommeTable3()[i][4]);
                asso.setValue("CT_Tab_NCumuleVendrediAct3",getSommeTable3()[i][5]);
                if(getSommeTable3()[i][6]!=null)
                    asso.setValue("CT_Tab_NCumuleSamediAct3",getSommeTable3()[i][6]);
                asso.setValue("CT_Tab_SommeLundiAct3",getSommeTable3()[i][1]);
                asso.setValue("CT_Tab_SommeMardiAct3",getSommeTable3()[i][2]);
                asso.setValue("CT_Tab_SommeMercrediAct3",getSommeTable3()[i][3]);
                asso.setValue("CT_Tab_SommeJeudiAct3",getSommeTable3()[i][4]);
                asso.setValue("CT_Tab_SommeVendrediAct3",getSommeTable3()[i][5]);
                if(getSommeTable3()[i][6]!=null)
                    asso.setValue("CT_Tab_SommeSamediAct3",getSommeTable3()[i][6]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in modiferCumuleTable4 method "+e.getMessage()+" _ "+e.getClass());
        }
    }
}
