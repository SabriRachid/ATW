package beans;

import java.util.Calendar;

public class ListeOfYears {

    public static void main(String[] args) {
            Calendar calendar = Calendar.getInstance();
            int ann=calendar.get( Calendar.YEAR );//==Annee actuel) 
            for (int i =0;i<6;i++)
            System.out.println(ann+i); 
        
        
    }
    
    
}
