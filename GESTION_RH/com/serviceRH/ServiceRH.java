package com.serviceRH;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;

import dao.SingletonConnexionBDD;
import beans.Conge;



public  class ServiceRH
{
	private static Connection connection;
	private PreparedStatement st ;
	
	
	public Connection getConnection(){
		return connection;
	}
	
	public ServiceRH()
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(Modules.getWorkflowModule(),Modules.getPortalModule()).getConnection();
		}
		catch (PortalModuleException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  float getSpecificNbrJrOf1(Conge congeExcep, Conge conge ){
		float nbrJours = 0;
		
		boolean trouveDFCE = false;
		boolean trouveDDCE = false;
		boolean trouveSELF = false;
		
		if(// deb congé normal < fin congé excep
		   // congé égaux et mom deb CN et le matin
		   // congé égaux et mom deb CN = AP et mom fin CE = AP  
				(  compareTwoDates(conge.getDateDeb(),congeExcep.getDateFin() )<0 
				|| compareTwoDates(conge.getDateDeb(),congeExcep.getDateFin() )==0 && conge.getMomDeb().equals("Matin") 
				|| compareTwoDates(conge.getDateDeb(),congeExcep.getDateFin() )==0 && conge.getMomDeb().equals("Après midi") && congeExcep.getMomFinEx().equals("Après midi") 
				)
				&& 
				(  compareTwoDates(congeExcep.getDateFin(),conge.getDateFin() )<0
				|| compareTwoDates(congeExcep.getDateFin(),conge.getDateFin() )==0 && conge.getMomFin().equals("Après midi") 
				|| compareTwoDates(congeExcep.getDateFin(),conge.getDateFin() )==0 && conge.getMomFin().equals("Matin") && congeExcep.getMomFinEx().equals("Matin") 
				)
			)
		{
			trouveDFCE = true;
		}
		
		if(// deb congé normal < fin congé excep
				   // congé égaux et mom deb CN et le matin
				   // congé égaux et mom deb CN = AP et mom fin CE = AP  
						(  compareTwoDates(conge.getDateDeb(),congeExcep.getDateDeb() )<0 
						|| compareTwoDates(conge.getDateDeb(),congeExcep.getDateDeb() )==0 && conge.getMomDeb().equals("Matin") 
						|| compareTwoDates(conge.getDateDeb(),congeExcep.getDateDeb() )==0 && conge.getMomDeb().equals("Après midi") && congeExcep.getMomFinEx().equals("Après midi") 
						)
						&& 
						(  compareTwoDates(congeExcep.getDateDeb(),conge.getDateFin() )<0
						|| compareTwoDates(congeExcep.getDateDeb(),conge.getDateFin() )==0 && conge.getMomFin().equals("Après midi") 
						|| compareTwoDates(congeExcep.getDateDeb(),conge.getDateFin() )==0 && conge.getMomFin().equals("Matin") && congeExcep.getMomFinEx().equals("Matin") 
						)
					)
		{
			trouveDDCE =  true;
		}
		
		
		
		if(// deb congé excep > fin congé excep
				   // congé égaux et mom deb CE = mom deb || mom fin = mom fin le matin
				   // congé égaux et mom deb CE = AP et mom fin CE = Matin  
					!	(  compareTwoDates(congeExcep.getDateDeb(),congeExcep.getDateFin() )>0 
						|| compareTwoDates(congeExcep.getDateDeb(),congeExcep.getDateFin() )==0 && congeExcep.getMomDebEx().equals(congeExcep.getMomFinEx()) 
						|| compareTwoDates(congeExcep.getDateDeb(),congeExcep.getDateFin() )==0 && congeExcep.getMomDebEx().equals("Après midi") && congeExcep.getMomFinEx().equals("matin") 
						)
		)
			
		{
			trouveSELF =  true;
		}
		
		
		
		
		
		
		if(trouveDDCE&&trouveDFCE&&trouveSELF)
			nbrJours = getNombreJoursOfTravaille(congeExcep.getDateDeb(), congeExcep.getDateFin(), congeExcep.getMomDebEx(), congeExcep.getMomFinEx());
		
		if(!trouveDDCE&&trouveDFCE&&trouveSELF)
			nbrJours = getNombreJoursOfTravaille(conge.getDateDeb(), congeExcep.getDateFin(), conge.getMomDeb(), congeExcep.getMomFinEx());
		
		if(trouveDDCE&&!trouveDFCE&&trouveSELF)
			nbrJours = getNombreJoursOfTravaille(congeExcep.getDateDeb(), conge.getDateFin(), congeExcep.getMomDebEx(), conge.getMomFin());
		
		if((!trouveDDCE&&!trouveDFCE)||!trouveSELF)//faire retourner les dates excep egaux au dates initiales
			nbrJours = 0;
		System.out.println("Nombre jours =  "+nbrJours);
		return nbrJours;
	}
	
	public void setUpdatedSoldesPLUS(IWorkflowInstance iw,String reliquatSoldeanneEncoursNS,String reliquatSoldeAnterieurNS,String loginVdocOfUser,String nbrJoursSoldeNS,float droitAquis ){
		
		
		droitAquis = getDroitAcquis(loginVdocOfUser);
		
		float soldeConsomme = (float) iw.getValue(nbrJoursSoldeNS);
		float oldSoldeAnterieur = getSoldeAnterieur(loginVdocOfUser);
		float oldReliquatEnCours = getSoldeEnCours(loginVdocOfUser);;
		
		float nvSoldeEnCours = 0;
		float nvSoldeAnterieur = 0;
		float diff = 0;
		
		if(oldReliquatEnCours==droitAquis){
			nvSoldeAnterieur= oldSoldeAnterieur+soldeConsomme;
			nvSoldeEnCours = droitAquis;
		}
		else if(oldReliquatEnCours<droitAquis){
			
			if(oldReliquatEnCours+soldeConsomme>droitAquis){
				diff = oldReliquatEnCours+soldeConsomme - droitAquis;
				nvSoldeEnCours = droitAquis;
				nvSoldeAnterieur = diff;
				
			}
			if(oldReliquatEnCours+soldeConsomme==droitAquis){
				nvSoldeEnCours = droitAquis;
				nvSoldeAnterieur = diff;
			}
			if(oldReliquatEnCours+soldeConsomme<droitAquis){
				nvSoldeEnCours = oldReliquatEnCours+soldeConsomme;
				nvSoldeAnterieur = diff;
			}
		}
		
		iw.setValue(reliquatSoldeanneEncoursNS, nvSoldeEnCours);
		iw.setValue(reliquatSoldeAnterieurNS, nvSoldeAnterieur);
	}
	
	public void setUpdatedSoldesMOINS(IWorkflowInstance iw,String reliquatSoldeanneEncoursNS,String reliquatSoldeAnterieurNS,String loginVdocOfUser,String nbrJoursSoldeNS,float droitAquis ){
		
		droitAquis = getDroitAcquis(loginVdocOfUser);
		
		float soldeConsomme = (float) iw.getValue(nbrJoursSoldeNS);
		float oldSoldeAnterieur = getSoldeAnterieur(loginVdocOfUser);
		float oldReliquatEnCours = getSoldeEnCours(loginVdocOfUser);;
		
		float nvSoldeAnterieur = oldSoldeAnterieur-soldeConsomme;
		float nvSoldeEnCours = droitAquis;
		
		if(nvSoldeAnterieur<0){
			nvSoldeEnCours = oldReliquatEnCours - (-nvSoldeAnterieur);
			nvSoldeAnterieur = 0;
		}
		
		iw.setValue(reliquatSoldeanneEncoursNS, nvSoldeEnCours);
		iw.setValue(reliquatSoldeAnterieurNS, nvSoldeAnterieur);
	}
	
	public int compareTwoDates(Date d1 , Date d2){
		 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date date1=d1,date2=d2;
			try
			{
				Calendar c = Calendar.getInstance();
				c.setTime(date1);
				
				Calendar c1 = Calendar.getInstance();
				c1.setTime(date2);
				
				date1 = sdf.parse(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE));
				date2 = sdf.parse(c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.MONTH)+1)+"-"+c1.get(Calendar.DATE));
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	        if (date1.compareTo(date2) > 0) {
	            System.out.println("Date1 is after Date2");
	        } else if (date1.compareTo(date2) < 0) {
	            System.out.println("Date1 is before Date2");
	        } else if (date1.compareTo(date2) == 0) {
	            System.out.println("Date1 is equal to Date2");
	        } 
			return date1.compareTo(date2);

	}
	
	public List<IUser> getSuperieurOf(String loginUser){
		List<IUser> superieurs = new ArrayList<IUser>();
		try{
			String loginVdocOfUser = loginUser;
			String req = "select p.loginVdoc "
					+ "from Superieur s , Personnel p , Personnel sup  "
					+ "where sup.loginVdoc = ? "
					+ "and s.PersonnelMatricule=sup.matricule "
					+ "and s.SupMatricule=p.matricule";
			st = connection.prepareStatement(req);
			st.setString(1, loginVdocOfUser);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				String loginSuperieur = rs.getString(1);
				IUser superieur = Modules.getWorkflowModule().getUserByLogin(loginSuperieur);
				superieurs.add(superieur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return superieurs;
	}
	
	public float getSoldeAnterieur(String loginVdoc){
		 float oldSoldeAnterieur = 0;
		 try
			{
			 	String req = "select NombrJoursDispo from Personnel  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				ResultSet rs = st.executeQuery();
				while(rs.next()){
					oldSoldeAnterieur=rs.getFloat(1);
				}
			}catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
			}
			return oldSoldeAnterieur;
	}
	
	public float setSoldeAnterieur(String loginVdoc, float nvSoldeAnterieur){
		 float reliquatAnneEnCours = 0;
		 try
			{
			 	String req = "update Personnel set NombrJoursDispo = ?  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setFloat(1, nvSoldeAnterieur);
				st.setString(2, loginVdoc);
				st.execute();
				
			}catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
			}
			return reliquatAnneEnCours;
	}
	
	public float getSoldeEnCours(String loginVdoc){
		 float reliquatAnneEnCours = 0;
		 try
			{
			 	String req = "select reliquatAnneEnCours from Personnel  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				ResultSet rs = st.executeQuery();
				while(rs.next()){
					reliquatAnneEnCours=rs.getFloat(1);
				}
			}catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
			}
			return reliquatAnneEnCours;
	}
	
	public float setSoldeEnCours(String loginVdoc, float nvSoldeEnCours){
		 float reliquatAnneEnCours = 0;
		 try
			{
			 	String req = "update Personnel set reliquatAnneEnCours = ?  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setFloat(1, nvSoldeEnCours);
				st.setString(2, loginVdoc);
				st.execute();
				
			}catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
			}
			return reliquatAnneEnCours;
	}
	
	public String afficheDate(Date date){
		String stringDate ="";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			stringDate = sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return stringDate;
	}
	
	public float getDroitAcquis(String loginVdoc){
		 float droitsAcquis = 0;
		 try
			{
			 	String req = "select NombreJoursCongeAnnuel from Personnel  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				ResultSet rs = st.executeQuery();
				while(rs.next()){
					droitsAcquis=rs.getFloat(1);
				}
			}catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
			}
			return droitsAcquis;
	}

	public float setDroitAcquis(String loginVdoc, float nvDroitAcquis){
		 float reliquatAnneEnCours = 0;
		 try
			{
			 	String req = "update Personnel set NombreJoursCongeAnnuel = ?  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setFloat(1, nvDroitAcquis);
				st.setString(2, loginVdoc);
				st.execute();
				
			}catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
			}
			return reliquatAnneEnCours;
	}
	
	public String getFilialeOfUser(String loginVdoc){
		String filiale = "";
		try{
			String req = "SELECT FilialeIdFiliale FROM Personnel where loginVdoc = ?";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, loginVdoc);
			ResultSet rs = st.executeQuery();
			
			while (rs.next())
			{
				filiale = rs.getString(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return filiale;
		
	}
	
	public void updateSoldeCongeMoins(String loginVdoc,float soldeConsomme){
		
		 try
			{
			 	String req = "select NombrJoursDispo,reliquatAnneEnCours,NombreJoursCongeAnnuel from Personnel  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				ResultSet rs = st.executeQuery();
				float oldSoldeAnterieur = 0;
				float oldReliquatEnCours = 0;
				float droitAquis = 0;
				while(rs.next()){
					oldSoldeAnterieur=rs.getFloat(1);
					oldReliquatEnCours=rs.getFloat(2);
					droitAquis=rs.getFloat(3);
				}
				
				float diffSoldeAnterieurEtSoldeConsome = oldSoldeAnterieur-soldeConsomme;
				float diffSoldeEnCoursEtSoldeConsome = droitAquis;
				
				if(diffSoldeAnterieurEtSoldeConsome<0){
					diffSoldeEnCoursEtSoldeConsome = oldReliquatEnCours - (-diffSoldeAnterieurEtSoldeConsome);
					diffSoldeAnterieurEtSoldeConsome = 0;
				}
				
				
				
				
				
				req = "update Personnel set NombrJoursDispo = ?, reliquatAnneEnCours = ?  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setFloat(1, diffSoldeAnterieurEtSoldeConsome);
				st.setFloat(2, diffSoldeEnCoursEtSoldeConsome);
				st.setString(3, loginVdoc);
				st.executeUpdate();
				
				
				
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void updateSoldeCongePlus(String loginVdoc,float soldeConsomme){
		
		 try
			{
				String req = "select NombrJoursDispo,reliquatAnneEnCours,NombreJoursCongeAnnuel from Personnel  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				ResultSet rs = st.executeQuery();
				float oldSoldeAnterieur = 0;
				float oldReliquatEnCours = 0;
				float droitAquis = 0;
				while(rs.next()){
					oldSoldeAnterieur=rs.getFloat(1);
					oldReliquatEnCours=rs.getFloat(2);
					droitAquis=rs.getFloat(3);
				}
				
				float nvSoldeEnCours = 0;
				float nvSoldeAnterieur = 0;
				float diff = 0;
				
				if(oldReliquatEnCours==droitAquis){
					nvSoldeAnterieur= oldSoldeAnterieur+soldeConsomme;
					nvSoldeEnCours = droitAquis;
				}
				else if(oldReliquatEnCours<droitAquis){
					
					if(oldReliquatEnCours+soldeConsomme>droitAquis){
						diff = oldReliquatEnCours+soldeConsomme - droitAquis;
						nvSoldeEnCours = droitAquis;
						nvSoldeAnterieur = diff;
						
					}
					if(oldReliquatEnCours+soldeConsomme==droitAquis){
						nvSoldeEnCours = droitAquis;
						nvSoldeAnterieur = diff;
					}
					if(oldReliquatEnCours+soldeConsomme<droitAquis){
						nvSoldeEnCours = oldReliquatEnCours+soldeConsomme;
						nvSoldeAnterieur = diff;
					}
				}
				
				
				
				req = "update Personnel set NombrJoursDispo = ?, reliquatAnneEnCours = ?  where loginVdoc = ?";
				st = connection.prepareStatement(req);
				st.setFloat(1, nvSoldeAnterieur);
				st.setFloat(2, nvSoldeEnCours);
				st.setString(3, loginVdoc);
				st.executeUpdate();
				
				
				
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public  float getNombreJoursOfConge(Date d1, Date d2, String momDeb, String momFin){
		return nbJoursConge(d1, d2, false, true, true, true, true, true, false, false, momDeb, momFin);
	}
	
	public  float getNombreJoursOfTravaille(Date d1, Date d2, String momDeb, String momFin){
		return nbJoursTravaille(d1, d2, false, true, true, true, true, true, false, false, momDeb, momFin);
	}
	
	public  float calculateCongePris(String Login){
		float CongePrisAnneEnCours = 0;
		try{
			
				String loginVdoc = Login;
				String req = " select DateDeb,DateFin,MomSortie,MomEntre from Conge where Personnelmatricule = ? "
						+ " and( DATEDIFF(YEAR,DateDeb,?)>=0 and"
						+ " DATEDIFF(YEAR,DateFin,?)<=0)"
						+ " and(  ( TypeConge = 'Normal payé') or ( TypeConge = 'Divers' and maladieComptabilise = 'True')"
						+ " or ( TypeConge = 'Maladie' and maladieComptabilise = 'True'))and EtatConge = 'valide'";
				
				
				
				PreparedStatement st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				st.setDate(2, new java.sql.Date(new Date().getTime()));
				st.setDate(3, new java.sql.Date(new Date().getTime()));
				ResultSet rs = st.executeQuery();
				List<Conge> conges = new ArrayList<Conge>();
				while(rs.next()){
					Conge c = new Conge(rs.getDate(1), rs.getDate(2), rs.getString(3), rs.getString(4));
					conges.add(c);
					//System.out.println(c.getDateDeb()+" "+c.getDateFin());
				}
				
				
				
				
				Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(new Date());
				
				
				Calendar dateDebc = Calendar.getInstance();
				Calendar dateFinc = Calendar.getInstance();
				
				Calendar dateDebAnne = Calendar.getInstance();
				Date debanne = new Date();
				dateDebAnne.setTime(debanne);
				dateDebAnne.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
				dateDebAnne.set(Calendar.MONTH, Calendar.JANUARY);
				dateDebAnne.set(Calendar.DATE, 02);
				
				
				
				Calendar dateFinAnne = Calendar.getInstance();
				Date finanne = new Date();
				dateFinAnne.setTime(finanne);
				dateFinAnne.set(Calendar.YEAR, currentDate.get(Calendar.YEAR)+1);
				dateFinAnne.set(Calendar.MONTH, Calendar.JANUARY);
				dateFinAnne.set(Calendar.DATE, 02);
				
				for(Conge conge : conges){
					dateDebc.setTime(conge.getDateDeb());
					dateFinc.setTime(conge.getDateFin());
					
					System.out.println("congé :  "+conge.getDateDeb()+" "+conge.getDateFin()+" "+conge.getMomDeb()+" "+conge.getMomFin());
					
					if(dateDebc.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)  && dateFinc.get(Calendar.YEAR)>currentDate.get(Calendar.YEAR)){
						
						CongePrisAnneEnCours+=getNombreJoursOfConge(conge.getDateDeb(), dateFinAnne.getTime(), conge.getMomDeb(), "Matin");
						System.out.println("nb jours déduit  :  "+getNombreJoursOfConge(conge.getDateDeb(), dateFinAnne.getTime(), conge.getMomDeb(), "Matin"));
					}
					
					else if(dateDebc.get(Calendar.YEAR)<currentDate.get(Calendar.YEAR)  && dateFinc.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)){
						
						CongePrisAnneEnCours+=getNombreJoursOfConge(dateDebAnne.getTime(), conge.getDateFin(), "Matin", conge.getMomFin());
						System.out.println("nb jours déduit  :  "+getNombreJoursOfConge(dateDebAnne.getTime(), conge.getDateFin(), "Matin", conge.getMomFin()));
					}
					
					else if(dateDebc.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)  && dateFinc.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)){
						
						CongePrisAnneEnCours+=getNombreJoursOfConge(conge.getDateDeb(), conge.getDateFin(), conge.getMomDeb(),conge.getMomFin() );
						System.out.println("nb jours déduit  :  "+getNombreJoursOfConge(conge.getDateDeb(), conge.getDateFin(), conge.getMomDeb(),conge.getMomFin() ));
					}
					
					
					
				}
				
				System.out.println("Nombre des jours de congé pris : "+CongePrisAnneEnCours);
				
				req = " select DateDeb,DateFin,MomSortie,MomEntre,dateEntreeExcep,dateSortieExcep,MomEntreeExcep,MomSortieExcep,Conge.IdConge"
						+ " from Conge,InfosCongeAnnule "
						+ " where Conge.Personnelmatricule = ? "
						+ " and Conge.EtatConge = 'congé modifié' "
						+ " and Conge.CodeVdocDemandeConge = InfosCongeAnnule.idConge"
						+ " and ( DATEDIFF(YEAR,InfosCongeAnnule.dateEntreeExcep,?)>=0"
						+ " and DATEDIFF(YEAR,InfosCongeAnnule.dateSortieExcep,?)<=0)";
				st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				st.setDate(2, new java.sql.Date(new Date().getTime()));
				st.setDate(3, new java.sql.Date(new Date().getTime()));
				rs = st.executeQuery();
				
				while(rs.next()){
					Conge congeModifé = new Conge(rs.getDate(1), rs.getDate(2), rs.getString(3), rs.getString(4));
					Conge congeExcep = new Conge(rs.getString(7), rs.getString(8), rs.getDate(5), rs.getDate(6));
					
					System.out.println("congé Excep :  "+congeExcep.getDateDeb()+" "+congeExcep.getDateFin()+" "+congeExcep.getMomDebEx()+" "+congeExcep.getMomFinEx());
					
					dateDebc = Calendar.getInstance();
					dateDebc.setTime(congeModifé.getDateDeb());
					
					dateFinc = Calendar.getInstance();
					dateFinc.setTime(congeModifé.getDateFin());
					
					Calendar dateDebCEAnne = Calendar.getInstance();
					dateDebCEAnne.setTime(congeExcep.getDateDeb());
					
					Calendar dateFinCEAnne = Calendar.getInstance();
					dateFinCEAnne.setTime(congeExcep.getDateFin());
					
					if(dateDebCEAnne.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)  && dateFinCEAnne.get(Calendar.YEAR)>currentDate.get(Calendar.YEAR)){
						CongePrisAnneEnCours+=
								(getNombreJoursOfConge(congeModifé.getDateDeb(), dateFinAnne.getTime(), congeModifé.getMomDeb(), "Matin")-
								getNombreJoursOfTravaille(congeExcep.getDateDeb(), dateFinAnne.getTime(), congeExcep.getMomDebEx(), "Matin"));
					}
					else if(dateDebCEAnne.get(Calendar.YEAR)<currentDate.get(Calendar.YEAR)  && dateFinCEAnne.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)){
						CongePrisAnneEnCours+=
								(getNombreJoursOfConge(dateDebAnne.getTime(), congeModifé.getDateFin(), "Matin", congeModifé.getMomFin())-
								getNombreJoursOfTravaille(dateDebAnne.getTime(), congeExcep.getDateFin(), "Matin", congeExcep.getMomFinEx()));

					}
					else if(dateDebCEAnne.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)  && dateFinCEAnne.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)){
						CongePrisAnneEnCours+=
								(getNombreJoursOfConge(congeModifé.getDateDeb(), congeModifé.getDateFin(), congeModifé.getMomDeb(), congeModifé.getMomFin())-
								getNombreJoursOfTravaille(congeExcep.getDateDeb(), congeExcep.getDateFin(), congeExcep.getMomDebEx(), congeExcep.getMomFinEx()));

					}
						
						
						
					
				}
				System.out.println("Nombre des jours de congé pris : "+CongePrisAnneEnCours);
		
			
			
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return CongePrisAnneEnCours;
		
	}

	public  float nbJoursConge(Date d1, Date d2, boolean notionJourFerie, boolean priseCompteLundi, boolean priseCompteMardi, boolean priseCompteMercredi, boolean priseCompteJeudi,
			boolean priseCompteVendredi, boolean priseCompteSamedi, boolean priseCompteDimanche,String momDeb, String momFin)
	{
		Calendar date1 = Calendar.getInstance();
		date1.setTime(d1);
		Calendar date2 = Calendar.getInstance();
		date2.setTime(d2);
		
		boolean trouveSamdiOuDimanche = false;
		
		if (date2.before(date1) 
				|| (date2.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY && date2.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) 
				|| (date1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY && date1.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY))
			return 0;
		
		// Tableau des jours a prendre en compte
		Boolean[] joursPrisEncompte = new Boolean[]
		{
				priseCompteDimanche, priseCompteLundi, priseCompteMardi, priseCompteMercredi, priseCompteJeudi, priseCompteVendredi, priseCompteSamedi
		};
		
		
		
		// Récupération des jours fériés
		List<Date> joursFeries = new ArrayList<Date>();
		int yeardeb = date1.get(Calendar.YEAR);
		int yearfin = date2.get(Calendar.YEAR);
		for (int i = yeardeb - 1; i <= yearfin; i++)
		{
			joursFeries.addAll(getJourFeries(i));
		}
		
		// Calcul du nombre de jour
		Calendar dateToCompare = Calendar.getInstance();
		
		float nbJour = 0;
//		while (date1.before(date2) || (date1.get(date1.YEAR) == date2.get(date2.YEAR) && date1.get(date1.MONTH) == date2.get(date2.MONTH) && date1.get(date1.DATE) == date2.get(date2.DATE)))
		while(compareTwoDates(date1.getTime(), date2.getTime())<=0)
		{
			boolean test = false;// joursFeries.contains(date1);
			boolean test1 = false;
			for (Date ligne : joursFeries)
			{
				dateToCompare.setTime(ligne);
				boolean testyear = date1.get(date1.YEAR) == dateToCompare.get(dateToCompare.YEAR);
				boolean testmonth = date1.get(date1.MONTH) == dateToCompare.get(dateToCompare.MONTH);
				boolean testday = date1.get(date1.DATE) == dateToCompare.get(dateToCompare.DATE);
				if (testyear == true && testmonth == true && testday == true)
				{
					test = true;
				}
				
				testyear = date2.get(date2.YEAR) == dateToCompare.get(dateToCompare.YEAR);
				testmonth = date2.get(date2.MONTH) == dateToCompare.get(dateToCompare.MONTH);
				testday = date2.get(date2.DATE) == dateToCompare.get(dateToCompare.DATE);
				if (testyear == true && testmonth == true && testday == true)
				{
					test1 = true;
				}
			}
			
			if (test == false)
			{
				if (joursPrisEncompte[date1.get(Calendar.DAY_OF_WEEK) - 1]){
					nbJour++;
					trouveSamdiOuDimanche = false;
				}
					
				else{
					trouveSamdiOuDimanche = true;
				}
			}
			
			if (test == true && test1 == true)
			{
				return 0;
			}
			
			date1.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		
		
		
		if(momDeb.equals("Après midi")&&!trouveSamdiOuDimanche)
			nbJour-=0.5;
		if(momFin.equals("Après midi")&&!trouveSamdiOuDimanche)
			nbJour+=0.5;
		
		date1.setTime(d1);
		date2.setTime(d2);
		
		if(	date1.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || date1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY 
				|| date2.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || date2.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
			nbJour=1;
		
		if(compareTwoDates(d1, d2)==0&&momFin.equals("Matin")&&momFin.equals("Après midi"))
			nbJour=1;
		
		return nbJour-1;
	}
	
	public  float nbJoursTravaille(Date d1, Date d2, boolean notionJourFerie, boolean priseCompteLundi, boolean priseCompteMardi, boolean priseCompteMercredi, boolean priseCompteJeudi,
			boolean priseCompteVendredi, boolean priseCompteSamedi, boolean priseCompteDimanche,String momDeb, String momFin)
	{
		Calendar date1 = Calendar.getInstance();
		date1.setTime(d1);
		Calendar date2 = Calendar.getInstance();
		date2.setTime(d2);
		
		
		
		if (date2.before(date1) 
				|| (date2.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY && date2.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) 
				|| (date1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY && date1.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY))
			return 0;
		
		// Tableau des jours a prendre en compte
		Boolean[] joursPrisEncompte = new Boolean[]
		{
				priseCompteDimanche, priseCompteLundi, priseCompteMardi, priseCompteMercredi, priseCompteJeudi, priseCompteVendredi, priseCompteSamedi
		};
		
		boolean trouveSamdiOuDimanche = false;
		
		// Récupération des jours fériés
		List<Date> joursFeries = new ArrayList<Date>();
		int yeardeb = date1.get(Calendar.YEAR);
		int yearfin = date2.get(Calendar.YEAR);
		for (int i = yeardeb - 1; i <= yearfin; i++)
		{
			joursFeries.addAll(getJourFeries(i));
		}
		
		// Calcul du nombre de jour
		Calendar dateToCompare = Calendar.getInstance();
		
		float nbJour = 0;
//		while (date1.before(date2) || (date1.get(date1.YEAR) == date2.get(date2.YEAR) && date1.get(date1.MONTH) == date2.get(date2.MONTH) && date1.get(date1.DATE) == date2.get(date2.DATE)))
		while(compareTwoDates(date1.getTime(), date2.getTime())<=0)
		{
			boolean test = false;// joursFeries.contains(date1);
			boolean test1 = false;
			for (Date ligne : joursFeries)
			{
				dateToCompare.setTime(ligne);
				boolean testyear = date1.get(date1.YEAR) == dateToCompare.get(dateToCompare.YEAR);
				boolean testmonth = date1.get(date1.MONTH) == dateToCompare.get(dateToCompare.MONTH);
				boolean testday = date1.get(date1.DATE) == dateToCompare.get(dateToCompare.DATE);
				if (testyear == true && testmonth == true && testday == true)
				{
					test = true;
				}
				
				testyear = date2.get(date2.YEAR) == dateToCompare.get(dateToCompare.YEAR);
				testmonth = date2.get(date2.MONTH) == dateToCompare.get(dateToCompare.MONTH);
				testday = date2.get(date2.DATE) == dateToCompare.get(dateToCompare.DATE);
				if (testyear == true && testmonth == true && testday == true)
				{
					test1 = true;
				}
			}
			
			if (test == false)
			{
				if (joursPrisEncompte[date1.get(Calendar.DAY_OF_WEEK) - 1]){
					nbJour++;
					trouveSamdiOuDimanche = false;
				}
					
				else{
					trouveSamdiOuDimanche = true;
				}
			}
			
			if (test == true && test1 == true)
			{
				return 0;
			}
			
			date1.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		
		
		
		if(momDeb.equals("Après midi")&&!trouveSamdiOuDimanche)
			nbJour-=0.5;
		if(momFin.equals("Après midi")&&!trouveSamdiOuDimanche)
			nbJour+=0.5;
		
		date1.setTime(d1);
		date2.setTime(d2);
		
		if(	date1.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || date1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY 
				|| date2.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || date2.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
			nbJour=1;
		
		if(compareTwoDates(d1, d2)==0&&momFin.equals("Matin")&&momDeb.equals("Après midi"))
			nbJour=1;
//		
		
		return nbJour-1;
	}
	
	public  List<Date> getJourFeries(int annee)
	{
		
		List<Date> datesFeries = new ArrayList<Date>();
		try
		{
			String req = "SELECT jfa.date_jourferie,jf.libelleJoursFerie,jf.nbr_jours_ferie FROM JourFerieAnnuelle jfa,JourFerie jf  where annee = ? and jfa.id_jourferie = jf.idJoursFerie";
			PreparedStatement st = connection.prepareStatement(req);
			st.setInt(1, annee);
			ResultSet rs = st.executeQuery();
			
			while (rs.next())
			{
				Date date = new Date(rs.getDate(1).getTime());
				datesFeries.add(date);
				
				int nombreJoursDuFete = rs.getInt(3);
				if (nombreJoursDuFete == 2)
				{
					Date datePlus1 = date;
					Calendar ca = Calendar.getInstance();
					ca.setTime(datePlus1);
					int newjour = ca.get(Calendar.DATE) + 1;
					ca.set(Calendar.DATE, newjour);
					datePlus1 = new Date(ca.getTimeInMillis());
					datesFeries.add(datePlus1);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return datesFeries;
	}
}
